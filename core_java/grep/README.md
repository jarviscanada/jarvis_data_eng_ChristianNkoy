# Java Grep App
## Introduction
This is a Java application that mimics the functionality of Linux's `grep` command.
The app takes in three arguments: a regex, an input file, and an output file; then reads the input file line by line, searching for phrases that match the regex.
It prints the lines with matches into an output file. I implemented the app using Java features and libraries such as Stream API, Lambda expressions, and Apache Log4j.
I also used technologies such as Git for version control, Maven for package management, Intellij IDE for organizing source code and debugging, and Docker for containerization and deployment of the app.
## Quick Start
### Using `java` command
Make sure that both Java 8 or later and Maven are installed on your computer. Then on the CLI, run the following commands:
```
mvn clean compile package 
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp .*Romeo.*Juliet.* ./data ./out/grep.txt
```

### Using Docker
Make sure that Docker is installed on your machine and that the Dockerfile is in the current folder. On the CLI, run:
```
docker build .
docker run chrisnkoy/grep .*Romeo.*Juliet.* ./data ./out/grep.txt
```

## Implemenation
### Pseudocode
```
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

### Performance Issue
For really large input files, JVM may run out of heap memory when storing massive `List` objects. A way to avoid this is by using `Stream` or `Buffer`. These two classes minimize memory usage.
`Stream` does not store the entirety of the data, it processes it on-the-fly and only stores necessary intermediate results. `Buffer` processes the data in smaller chunks.

## Test
I tested the app manually by using multiple text files as input with different regexes. The app yielded results as expected. I also used `Intellij`'s debugging feature to test each method implementation.

## Deployment
I created a Dockerfile for the app, then saved it both locally and on Docker Hub. The Dockerfile can be used to build a Docker image and run the app on any platform without needing any additional setup.

## Improvement
* Enable the app to be able to search through other file types, e.g., html, xml, binary etc.
* Give the user the ability to specify if the search should be case-sensitive or case-insensitive.
* Add a UI so the user does not have to use a CLI to run the application.