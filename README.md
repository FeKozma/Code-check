# Code Check #

Do you have a problem with duplicated functions in your java project? Here is the solution on how to find all the duplicate functions, specify the location of your repository in the local-config.properties file (just make a copy of config.properties) and run the code! Then you will get a file in "results" with all the places where functions match.

### There are even some extra features such as ###

* query an LLM on complicated situations where 2 functions could be similar
* lots of configuration
* it even has testing for it!

## Testing ##

To run the tests run this command:

### Manually ###

```bash
./gradlew test
```

### Setting up pre-committed testing ###

If you want the tests to run when you commit code (to be sure it's working), you install this hook locally.

To skip using it once, use `-n` or `--no-verify` when committing.
E.g. `git commit -n -m "[commit message]"`

#### Installation ####

```bash
cp pre-commit.tests .git/hooks/pre-commit
chmod u+x .git/hooks/pre-commit
```

#### Uninstallation ####

```bash
rm .git/hooks/pre-commit
```
