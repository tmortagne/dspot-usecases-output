# Maven

* Reported https://github.com/STAMP-project/dspot/issues/573
* Command line: mvn clean eu.stamp-project:dspot-maven:1.1.1-SNAPSHOT:amplify-unit-tests -e -Dverbose=true
* DSpot version: built from sources 32fc184deec936f02c72b316b1e9998aa8708d2f
* Session test time: 1 hour

# JAR

* Commit: not committed before it was decreasing the mutation score
* Command line: `java -jar ~/.m2/repository/eu/stamp-project/dspot/1.1.1-SNAPSHOT/dspot-1.1.1-SNAPSHOT-jar-with-dependencies.jar --path-to-properties dspot.properties`
* DSpot version: built from sources caba4a708b387f8dca2211f44242a7fa90026a08
* Code coverage: from 62% to %
* Mutation score (descartes): unchanged from 70% to %
* DSpot execution: 
* Session test time: 1 hour

* Reported https://github.com/STAMP-project/dspot/issues/575