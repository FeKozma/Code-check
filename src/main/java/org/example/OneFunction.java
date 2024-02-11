package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class OneFunction extends LLM {
    public String name;
    public int nrParams;
    public String file;
    public int line;
    public List<String> content = new ArrayList<>();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OneFunction) {
            OneFunction comp = (OneFunction) obj;
            return comp.content.size() > 1 && comp.name.equals(name) && comp.content.equals(content);
        }
        return false;
    }

    public String comparison(OneFunction other) {
        String msg = "There are two Java 17 functions presented to you. If these functions are interchangeable, you must answer YES or NO followed by an explanation. If they are partly similar and could use common functions for part of their execution, you might start your answer with PARTIAL."
                + getFunctionMsg(this, 1) + getFunctionMsg(other, 2);

        return getAnswer(msg);
    }

    private String getFunctionMsg(OneFunction oneFunction, int nr) {
        return "\n\n------function " + nr + "------\n"+ removeLogAndComments(oneFunction.content);
    }

    private String removeLogAndComments(List<String> str) {
        return str.stream().filter(s -> !s.matches("^\\s*\\/\\/")).filter(s -> s.startsWith("Log")).reduce( "", (s, s1) -> s + "\n" + s);
    }

    private String removeLogAndCommentsAndMore(List<String> str) {
        return str.stream().filter(s -> !s.matches("^\\s*\\/\\/")).filter(s -> s.replaceAll(" ", "").length() < 2).reduce( "", (s, s1) -> s + "\n" + s);
    }


}
