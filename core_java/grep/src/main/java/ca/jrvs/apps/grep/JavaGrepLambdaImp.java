package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JavaGrepLambdaImp extends JavaGrepImp{

  public static void main(String[] args) {
    if(args.length != 3){
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    JavaGrepLambdaImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try{
      javaGrepLambdaImp.process();
    }catch(Exception ex){
      javaGrepLambdaImp.logger.error("Could not process.", ex);
    }
  }

  @Override
  public List<File> listFiles(String rootDir) {
    File directory = new File(rootDir);
    List<File> files = Arrays.stream(directory.listFiles())
                              .collect(Collectors.toList());
    return files;
  }

  @Override
  public List<String> readLines(File inputFile) {
    try(BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFile))){
      List<String> lines = bufferedReader.lines()  //line() method returns a stream object
                                          .collect(Collectors.toList());
      return lines;
    }catch(IOException e){
      logger.error("Could not read file.", e);
      return Collections.emptyList();
    }
  }

  @Override
  public boolean containsPattern(String line) {
    return line.matches(".*" + getRegex() + ".*");
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    try(FileWriter fileWriter = new FileWriter(getOutFile())){
      lines.forEach(line -> {
        try {
          fileWriter.write(line + "\n");
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    }catch (IOException e){
      logger.error("Could not write to file.");
    }
  }

  @Override
  public void process() throws IOException {
    List<File> listOfFiles = listFiles(getRootPath());
    List <String> matchedLines = listOfFiles.stream()
                                            .flatMap(file -> readLines(file).stream())
                                            .filter(line -> containsPattern(line))
                                            .collect(Collectors.toList());

    writeToFile(matchedLines);
  }
}
