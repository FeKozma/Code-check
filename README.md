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

<details>
  <summary>
    Default configuration (click to expand)
  </summary>

###### *config.properties*
```shell
LOGGING_LEVEL=INFO
  # This is related to TEMP_FILE below.
TEMP_FILE_ENABLED=false
  # If enabled, this file will be deleted and replaced every run instead of using the results files.
TEMP_FILE=debugFile.txt
  # Directory of where the result files are saved. They range from 0 to 99.
PATH_TO_RESULTS=results
  # Set this property to a path that should be excluded. It's regex based, so writing [file1, file2] would exclude any file containing file1 and file2.
EXCLUDED_PATHS=[]
  # Prefix for the result files.
RESULT_NAME_PREFIX=result_{nr}
  # The LLM located in project root.
LLM_FILE=models/ggml-model-gpt4all-falcon-q4_0.bin
  # Run with LLM - needs a model defined and will take more computing power and time to run.
RUN_WITH_LLM=false
  # Directory path to where the code is to be scanned.
PATH_TO_CODE=src/test/resources/
```
</details>

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
