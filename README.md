
# Code Check #

Do you have a problem with duplicated functions in your java project? Here is the solution on how to find all the duplicate functions, specify the location of your repository in the local-config.properties file (just make a copy of config.properties) and run the code! Then you will get a file in "results" with all the places where functions match.

### There are even some extra features such as ###

* query an LLM on complicated situations where 2 functions could be similar
* lots of configuration
* it even has testing for it!

## Testing ##

### Manually ###

To run the tests run this command:

```bash
./gradlew test
```

### Setting up pre-commit testing ###

If you want the tests to run when you commit code (to be sure it's working), you can install this hook locally.

To skip using it once, use `-n` or `--no-verify` when committing.
E.g. `git commit -nm "[commit message]"`

#### Install Hook ####

```bash
cp pre-commit.tests .git/hooks/pre-commit
chmod u+x .git/hooks/pre-commit
```

#### Uninstall Hook ####

```bash
rm .git/hooks/pre-commit
```

## Getting started with the large language model (LLM) ##

### Download the language model ###
* Either download the bin-file directly from [Hugging Face](https://huggingface.co/):
[ggml-model-gpt4all-falcon-q4_0.bin](https://huggingface.co/nomic-ai/gpt4all-falcon-ggml/resolve/main/ggml-model-gpt4all-falcon-q4_0.bin?download=true) (4.06 GB)

* Or choose downloads from the website:
[https://huggingface.co/nomic-ai/gpt4all-falcon-ggml/tree/main](https://huggingface.co/nomic-ai/gpt4all-falcon-ggml/tree/main)

* Or you may also download any [other model](https://huggingface.co/models) of your taste and use that one instead.

### Setting up the configuration ###

#### Add model and set the configuration to use LLM ####

1. Move the downloaded LLM into a new folder called `model` in the main repository.
2. In `config.properties`, set `LLM_FILE` to the name of your downloaded model (e.g. model/ggml-model-gpt4all-falcon-q4_0.bin).
3. In `config.properties`, set `RUN_WITH_LLM` to `true`.

#### Usage ####
Run `Main.java` in the project. <!-- TODO: Add run option/script that works "on the go". -->

#### Results ####
The results will be shown in the `/results/` folder. By default, the result naming is `result_{nr}` where `{nr}` is represented by a number that increases for each execution.

## Configuration file ##
You can edit some configuration in `config.properties`.

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

#### LOGGING_LEVEL
The different options are the following:

|           | ERROR | WARNING | INFO | DEBUG | TRACE |
|-----------|:-----:|:-------:|:----:|:-----:|:-----:|
| OFF/NONE  |       |         |      |       |       |
| ERROR     |   X   |         |      |       |       |
| WARNING   |   X   |    X    |      |       |       |
| INFO      |   X   |    X    |  X   |       |       |
| DEBUG     |   X   |    X    |  X   |   X   |       |
| TRACE     |   X   |    X    |  X   |   X   |   X   |
