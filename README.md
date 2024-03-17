
# Code Check #

Do you have a problem with duplicated functions in your coding project? This might be the solution on finding them.

### Features ###

* Query an LLM (large language model) on complicated situations where 2 or more functions could be similar.
* Configuration - it's easy to change the configurations.
* Comparison - matching through names of lines and functions. 
* Tested on both UNIX and Windows.

## Testing ##

To run the test files you can run this command with Gradle:

```sh
./gradlew test
```

To run tests directly in IntelliJ or Eclipse (or a program of your choice). Just run the testing files - and it should be working.

## Getting started with large language model (LLM) ##

### Download the language model ###

These are only suggestions, you may use any source and model as you want.

* Either download the bin-file directly from [Hugging Face](https://huggingface.co/) which we used for testing:
[ggml-model-gpt4all-falcon-q4_0.bin](https://huggingface.co/nomic-ai/gpt4all-falcon-ggml/resolve/main/ggml-model-gpt4all-falcon-q4_0.bin?download=true) (4.06 GB)

* Or choose downloads from their website for gpt4all-falcon-ggml:
[https://huggingface.co/nomic-ai/gpt4all-falcon-ggml/tree/main](https://huggingface.co/nomic-ai/gpt4all-falcon-ggml/tree/main)

* Or you may also download any [other model](https://huggingface.co/models) of your taste and use that one instead.

## Setting up the configuration ##

Create and update the local file `local-config.properties` (make a copy of `config.properties` and edit it.), then after running you will get a file in a "results" folder with places where functions/methods of different code are being similar or matching.

### Add model and set the configuration to use LLM ###

1. Move the downloaded LLM into a new folder called `models` in the main repository.
2. In `local-config.properties`, set `LLM_FILE` to the name of your downloaded model, for example `model/ggml-model-gpt4all-falcon-q4_0.bin`.
3. In `local-config.properties`, set `RUN_WITH_LLM` to `true`.

## Results ##

By default results will be shown in the `./results/` folder. Also, by default, the naming would be `result_{nr}` where `{nr}` is represented by a number that is incremental (until RESULT_FILE_LIMIT).

## Configurations ##

You can edit configurations in `local-config.properties`. However, the main configuration will still be loaded from [Properties](`config.properties`) first. After that [Local Properties](local-config.properties) and [Testing Properties](test-config.properties) (the testing properties is reset every run).

### Properties

| Property             | Default values if not set (if not required) | Type         | Description                                                  |
|----------------------|---------------------------------------------|--------------|--------------------------------------------------------------|
| LOGGING_LEVEL        | INFO                                        | String       | Which level to show the logging in.                          |
| PATH_TO_CODE         | src/test/resources/                         | String       | Required attribute. Where the code is located.               |
| PATH_TO_RESULTS      | results                                     | String       | Results directory.                                           |
| RESULT_NAME_PREFIX   | result_{nr}                                 | String       | Result name. {nr} will be replaced by an incremental number. |
| RESULTS_FILE_LIMIT   | 100                                         | Integer      | Since it's creating a new file each run, there is a limit.   |
| EXCLUDED_PATHS       | []                                          | List<String> | Separated by commas. Files/Paths to exclude during a run.    |
| RUN_WITH_LLM         | false                                       | String       | Whether to use LLM during the run or not.                    |
| LLM_FILE             | models/ggml-model-gpt4all-falcon-q4_0.bin   | String       | If running with LLM, this has to be set linking to a model.  |
| LLM_THREADS          | 4                                           | Integer      | How many threads the LLM will be using.                      |
| TEMP_FILE_ENABLED    | false                                       | Boolean      | Whether to use a temporary file.                             |
| TEMP_FILE            | debugFile.txt                               | String       | Where the temporary file is located.                         |

### LOGGING_LEVEL
The different options are the following:

|         | ERROR | WARNING | INFO | DEBUG | TRACE |
|---------|:-----:|:-------:|:----:|:-----:|:-----:|
| NONE    |       |         |      |       |       |
| ERROR   |   X   |         |      |       |       |
| WARNING |   X   |    X    |      |       |       |
| INFO    |   X   |    X    |  X   |       |       |
| DEBUG   |   X   |    X    |  X   |   X   |       |
| TRACE   |   X   |    X    |  X   |   X   |   X   |
