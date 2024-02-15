package org.example;

import java.util.ArrayList;
import java.util.List;

public class ManyFunctions {
    public List<OneFunction> oneFunctions;

    public LLM llm;
    public OneFunction commit;
    public ManyFunctions(LLM llm) {
        this.oneFunctions = new ArrayList<>();
    }

    public ManyFunctions(ManyFunctions manyFunctions, ManyFunctions manyFunctions2) {
        this.oneFunctions = manyFunctions.oneFunctions;
        this.oneFunctions.addAll(manyFunctions2.oneFunctions);
    }

    public void commitName(String name) {
        defineCommit();
        commit.name = name;
    }

    public void commitFile(String file) {
        defineCommit();
        commit.file = file;
    }
    public void commitLine(int line) {
        defineCommit();
        commit.line = line;
    }
    public void commitNrParameters(int nr) {
        defineCommit();
        commit.nrParams = nr;
    }
    public void commitContentLine(String content) {
        defineCommit();
        commit.content.add(content);
    }

    public void push() {
        oneFunctions.add(commit);
        commit = null;
    }

    private void defineCommit() {
        if (commit == null) commit = new OneFunction();
    }
}
