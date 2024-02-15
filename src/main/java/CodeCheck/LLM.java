package CodeCheck;

import com.hexadevlabs.gpt4all.LLModel;

import java.util.List;
import java.util.Map;


public class LLM {
    // GPT4All facade classes

    LLModel model;
    LLModel.GenerationConfig config;

    public void initModel() {

        java.nio.file.Path modelPath = java.nio.file.Path.of("ggml-model-gpt4all-falcon-q4_0.bin");

        model = new LLModel(modelPath);

        config = LLModel.config()
                .withNPredict(4096).build();
    }

    public void cleanModel() throws Exception {
        model.close();
    }

    public String getAnswer(String question) {
        String answer = model.chatCompletion(createMessage(question), config).choices.toString();
        System.out.println(answer);
        return answer;
    }

    private List<Map<String, String>> createMessage(String content) {
        return List.of(Map.of("role", "user", "content", content));
    }

}
