# Functional tests for Grails Core

[![Java CI](https://github.com/grails/grails-functional-tests/actions/workflows/gradle.yml/badge.svg)](https://github.com/grails/grails-functional-tests/actions/workflows/gradle.yml)


A Suite of functional tests for Grails

To change the Geb driver used to run the functional tests supply the geb.env system property.

    ./gradlew -Dgeb.env=chromeHeadless iT   

### Usage
To test a PR in [grails/grails-core](https://github.com/grails/grails-core) with grails-functional-tests before merging:
1. Publish the PR branch to Maven Local:
   Run this in `grails/grails-core`:
```
./gradlew pTML
```
2. Checkout the relevant branch (e.g. 7.0.x) in grails/grails-functional-tests
```
git clone --branch 7.0.x --single-branch https://github.com/grails/grails-functional-tests.git
```
3.
   In grails/grails-functional-tests, run:
```
cd grails-functional-tests
./gradlew build -PmavenLocal
```
4. Check the Results:
   Verify that the build completes successfully.
```console
BUILD SUCCESSFUL in 4m 20s
139 actionable tasks: 124 executed, 15 up-to-date
```
