package CodeCheck;

import com.hexadevlabs.gpt4all.LLModel;

import java.nio.file.Path;

import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class LLM {
    // GPT4All facade classes

    private LLModel model;
    private LLModel.GenerationConfig config;
    private long startTime;

    public void initModel() {
        startTime = System.currentTimeMillis();

        runIfConfig(() -> {
            Path modelPath = ConfigInterface.conf.getBoolean("RUN_WITH_LLM")
                            .orElse(false) ? Path.of(Util.checkIfHomePath(ConfigInterface.conf.getProperty("LLM_FILE"))) : null;

            if (Files.isDirectory(modelPath)) {
                Log.error("LLM_FILE not configured correctly - it is a directory, but was expecting a model file. Please change it into a correct model filepath.");
                throw new RuntimeException("'LLM_FILE' is a directory, model file not found. [Model path: %s]".formatted(modelPath));
            } else if (!Files.isRegularFile(modelPath) ) {
                Log.error("LLM_FILE expected to be configured.");
                throw new RuntimeException("LLM_FILE not found. [Model path: %s]".formatted(modelPath));
            }

            model = new LLModel(modelPath);
            model.setThreadCount(ConfigInterface.conf.getInteger("LLM_THREADS")
                    .orElse(4));

            Log.debug("Thread Count: " + model.threadCount());

            config = LLModel.config()
                    .withNPredict(64)
                    .withNBatch(128)
                    .build();

            return true;
        });
    }

    public void cleanModel() {
        runIfConfig(() -> {
            try {
                model.close();
            } catch (Exception e) {
                throw new RuntimeException("LLM is not closing...", e);
            }
            return true;
        });

        int[] formattedTime = Util.getPreparedDurationTime(startTime);
        Log.log(String.format("Run completed in %02d hours, %02d minutes, %02d seconds and %02d milliseconds.",
                formattedTime[0], formattedTime[1], formattedTime[2], formattedTime[3]));
    }

    public String getAnswer(String question) {

        Log.trace("Question: " + question);

        return runIfConfig(() -> {
            String answer = model.chatCompletion(createMessage(question), config).choices.toString();

            Log.trace("Answer: " + answer);

            return answer;
        }).orElse("LLM has not been configured.");
    }

    private List<Map<String, String>> createMessage(String content) {
        return List.of(Map.of("role", "user", "content", content));
    }

    private <T> Optional<T> runIfConfig(Supplier<T> supplier) {
        if (ConfigInterface.conf.getBoolean("RUN_WITH_LLM")
                .orElse(false)) {
            return Optional.of(supplier.get());
        }
        return Optional.empty();
    }
}
