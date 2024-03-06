package CodeCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class OneFunction {
    public String name, file;
    public int nrParams, line;
    public List<String> content;

    private static final Pattern patternRemoveComments = Pattern.compile("(.*)(//.*)+");
    private static final Pattern patternRemoveSpacesTabsNewLines = Pattern.compile("\\s");

    protected OneFunction() {
        content = new ArrayList<>();
    }

    private enum Compare {
        Default,
        Identical,
        Similar,
        CloseThereby,
    }

    public Compare equals(OneFunction obj) {
        if (obj.content.size() > 2 && obj.name.equals(name) && obj.removeExtraInformationAndGetString().equals(removeExtraInformationAndGetString()))
            return Compare.Identical;
        if (obj.name.equals(name) && obj.content.size() > 4 && content.size() > 4)
            return Compare.Similar;
        if (obj.name.equals(name))
            return Compare.CloseThereby;

        return Compare.Default;
    }

    public Comparison compare(OneFunction laterOneFunction, LLM llm) {
        return switch (equals(laterOneFunction)) {
            case Identical -> new Comparison(true, "Functions are identical.").setFunctions(this, laterOneFunction);
            case Similar -> similar(laterOneFunction, llm);
            case CloseThereby -> new Comparison(false, false, "Empty or closetherby.");
            case Default -> new Comparison();
        };
    }

    private Comparison similar(OneFunction obj, LLM llm) {
        List<String> thisCont = removeExtraInformation();
        List<String> objCont = obj.removeExtraInformation();

        int matchingLines = objCont
                .stream()
                .filter(thisCont::contains)
                .toList()
                .size();

        if ((matchingLines + 0.0) / objCont.size() > 0.5 || matchingLines > 10)
            return new Comparison(false, true, "Matching lines " + matchingLines + " out of " + objCont.size()).setFunctions(this, obj);
        else
            return new Comparison(false, true, llmComparison(obj, llm)).setFunctions(this, obj);
    }

    @Override
    public String toString() {
        return file + ":" + line + " (" + name + ")";
    }

    public String llmComparison(OneFunction other, LLM llm) {

        Log.trace("llmComparison->this:  " + this.content.toString());
        Log.trace("llmComparison->other:  " + other.content.toString());

        String functionThis = getFunctionMsg(1) + this.content.toString(); // getFunctionMsg(1)
        String functionOther = getFunctionMsg(2) + other.content.toString(); // other.getFunctionMsg(2)

        String msg = "There are two Java 8 functions presented to you. If these functions are interchangeable, you must answer YES or NO followed by an explanation. If they are partially similar and could use common functions for part of their execution, you might start your answer with PARTIAL." +
               functionThis + functionOther;

        return llm.getAnswer(msg);
    }

    private String getFunctionMsg(int nr) {
        return "\n\n------function " + nr + "------\n"; // + removeLogAndComments();
    }

    private String removeLogAndComments() {
        return this.content.stream().filter(s -> !s.matches("^\\s*\\/\\/")).filter(s -> s.startsWith("Log")).reduce("", (s, s1) -> s + "\n" + s);
    }

    private String removeExtraInformationAndGetString() {
        return String.join("\n", removeExtraInformation());
    }

    private List<String> removeExtraInformation() {
        return this.content.stream()
                .map(String::strip)
                .map(s -> patternRemoveComments.matcher(s).replaceAll("$1"))
                .map(s -> patternRemoveSpacesTabsNewLines.matcher(s).replaceAll(""))
                .filter(s -> !s.startsWith(Log.class.getSimpleName()))
                .filter(Predicate.not(String::isEmpty))
                .toList();
    }
}
