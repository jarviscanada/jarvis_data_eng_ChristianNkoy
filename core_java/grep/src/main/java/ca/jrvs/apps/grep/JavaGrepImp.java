package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep{

  static final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

  private String regex;
  private String rootPath;
  private String outFile;

  @Override
  public String getRegex() {
    return regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getRootPath() {
    return this.rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getOutFile() {
    return outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  @Override
  public List<File> listFiles(String rootDir) {
    File directory = new File(rootDir);
    List<File> files = Arrays.asList(directory.listFiles());
    return files;
  }

  @Override
  public List<String> readLines(File inputFile) {
    List<String> lines = new ArrayList<String>();
    try{
      FileReader fileReader = new FileReader(inputFile);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      String line = "";

      while((line = bufferedReader.readLine()) != null){
        lines.add(line);
      }
      bufferedReader.close();

    } catch(FileNotFoundException e){
      logger.error("File not found.");

    } catch(IOException e){
      logger.error("Could not read file.");
    }

    return lines;
  }

  @Override
  public boolean containsPattern(String line) {
    return line.matches(".*" + getRegex() + ".*");
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    try{
      FileWriter fileWriter = new FileWriter(getOutFile());

      for(String line : lines){
        fileWriter.write(line + "\n");
      }

      fileWriter.flush();
      fileWriter.close();

    }catch (IOException e){

      logger.error("Could not write to file.");
    }
  }

  @Override
  public void process() throws IOException {
    List <String> matchedLines = new ArrayList<>();
    List<File> listOfFiles = listFiles(getRootPath());

    for(File file : listOfFiles){
      for(String line : readLines(file)){
        if(containsPattern(line)){
          matchedLines.add(line);
        }
      }
    }
    writeToFile(matchedLines);
  }





  public static void main(String[] args){
    if(args.length != 3){
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    //Use default logger config
    BasicConfigurator.configure();

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

//    javaGrepImp.setRootPath("./grep/src/main/java/ca/jrvs/apps/grep/practice/");
//    javaGrepImp.listFiles(javaGrepImp.getRootPath());
//    File file = new File("./grep/src/main/java/ca/jrvs/apps/grep/practice/test.txt");
//    javaGrepImp.readLines(file);

    try{
      javaGrepImp.process();
    }catch (Exception ex){
      javaGrepImp.logger.error("Error: Unable to process", ex);
    }
  }
}
