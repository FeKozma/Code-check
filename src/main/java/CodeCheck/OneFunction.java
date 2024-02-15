package CodeCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OneFunction {
    public String name;
    public int nrParams;
    public String file;
    public int line;
    public List<String> content = new ArrayList<>();

    public int equals(OneFunction obj) {
        if (obj.content.size() > 2 && obj.name.equals(name) && obj.removeLogAndCommentsAndMore().equals(removeLogAndCommentsAndMore()))
            return 1;
        if (obj.name.equals(name) && obj.content.size() > 4 && content.size() > 4) return 2;
        if (obj.name.equals(name)) return 3;

        return 10;
    }

    public Comparison compare(OneFunction other, LLM llm) {
        switch (equals(other)) {
            case 1: return new Comparison(true, "Functions are identical").setFunctions(this, other);
            case 2: return new Comparison(false, true, llmComparison(other, llm)).setFunctions(this, other);
            case 3: return new Comparison(false, false, "Empty or closetherby");
            default:
                return new Comparison();
        }
    }

    @Override
    public String toString() {
        return file + ":" + name + ":" + line;
    }

    public String llmComparison(OneFunction other, LLM llm) {
        String msg = "There are two Java 8 functions presented to you. If these functions are interchangeable, you must answer YES or NO followed by an explanation. If they are partly similar and could use common functions for part of their execution, you might start your answer with PARTIAL."
                + getFunctionMsg(1) + other.getFunctionMsg(2);

        return llm.getAnswer(msg);
    }

    private String getFunctionMsg(int nr) {
        return "\n\n------function " + nr + "------\n" + removeLogAndComments();
    }

    private String removeLogAndComments() {
        return this.content.stream().filter(s -> !s.matches("^\\s*\\/\\/")).filter(s -> s.startsWith("Log")).reduce("", (s, s1) -> s + "\n" + s);
    }

    private String removeLogAndCommentsAndMore() {

        return this.content.stream().filter(s -> !s.matches("^\\s*\\/\\/")).filter(s -> s.replaceAll(" ", "").length() < 2).collect(Collectors.joining("\n"));
    }


}
