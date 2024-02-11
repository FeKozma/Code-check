package org.example;
import com.hexadevlabs.gpt4all.LLModel;

import java.util.List;
import java.util.Map;


public class LLM {
    // GPT4All facade classes

    LLModel model;
    LLModel.GenerationConfig config;
    public void initModel() {

        java.nio.file.Path modelPath = java.nio.file.Path.of("C:/Users/Felix/AppData/Local/nomic.ai/GPT4All", "gpt4all-falcon-newbpe-q4_0.gguf");

        model = new LLModel(modelPath);

        config = LLModel.config()
                .withNPredict(4096).build();
    }

    public void cleanModel() throws Exception {
        model.close();
    }

    public String getAnswer(String question) {
        return model.chatCompletion(createMessage(question), config).choices.toString();
    }

    private List<Map<String, String>> createMessage(String content) {
        return List.of(Map.of("role", "user", "content", content));
    }

}
