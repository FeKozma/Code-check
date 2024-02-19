# Code Check #

Do you have a problem with duplicated functions in your java project? Here is the solution on how to find all the duplicate functions, specify the location of your repository in the local-config.properties file (just make a copy of config.properties) and run the code! Then you will get a file in "results" with all the places where functions match.

### There are even some extra features such as ###
* query an LLM on complicated situations where 2 functions could be similar
* lots of configuration
* it even has testing for it!

----------------
### *Please set up before committing* ###
This will run tests when committing and pushing.

To skip the testing you can use the `-n` or `--no-verify` when committing.

``` bash
cp pre-commit.tests .git/hooks/pre-commit
cp pre-commit.tests .git/hooks/pre-push
chmod u+x gradlew \
          .git/hooks/pre-commit \
          .git/hooks/pre-push
```