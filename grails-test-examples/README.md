# Functional tests for Grails Core

A Suite of functional tests for Grails.  By default, tests in this directory will not run from the root if doing a complete grails build locally. 

They will run if the gradle command is executed under the `grails-test-examples` directory or if the `onlyFunctionalTests` gradle property is set like so:

    ./gradlew -PonlyFunctionalTests build
