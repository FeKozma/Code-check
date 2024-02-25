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

    public OneFunction() {
        content = new ArrayList<>();
    }

    public int equals(OneFunction obj) {
        if (obj.content.size() > 2 && obj.name.equals(name) && obj.removeExtraInformationAndGetString().equals(removeExtraInformationAndGetString()))
            return 1;
        if (obj.name.equals(name) && obj.content.size() > 4 && content.size() > 4) return 2;
        if (obj.name.equals(name)) return 3;

        return 10;
    }

    public Comparison compare(OneFunction other, LLM llm) {
        switch (equals(other)) {
            case 1: return new Comparison(true, "Functions are identical").setFunctions(this, other);
            case 2: return similar(other, llm);
            case 3: return new Comparison(false, false, "Empty or closetherby");
            default:
                return new Comparison();
        }
    }

    private Comparison similar(OneFunction obj, LLM llm) {
        List<String> thisCont = removeExtraInformation();
        List<String> objCont = obj.removeExtraInformation();

        int matchingLines = objCont.stream().filter(objLine -> thisCont.contains(objLine)).toList().size();

        if ((matchingLines + 0.0)/objCont.size() > 0.5 || matchingLines > 10)
            return new Comparison(false, true, "Matching lines " + matchingLines + " out of " + objCont.size()).setFunctions(this, obj);
        else
            return new Comparison(false, true, llmComparison(obj, llm)).setFunctions(this, obj);
    }

    @Override
    public String toString() {
        return file + ":" + line + " (" + name + ")";
    }

    public String llmComparison(OneFunction other, LLM llm) {

        Log.debug(this.content.toString());
        Log.debug(other.content.toString());

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
