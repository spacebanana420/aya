package aya.wrapper;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import aya.ui.stdout;

//Process execution and handling
public class process {
  public static Process runProcess(ArrayList<String> args) {return runProcess(new ProcessBuilder(args));}
  public static Process runProcess(String[] args) {return runProcess(new ProcessBuilder(args));}
  public static Process runProcess(ProcessBuilder pb) {
    List<String> command = pb.command();
    stdout.print_debug("Running command:", command);
    try {
      Process p = pb.start();
      return p;
    }
    catch (IOException e) {error_missingProcess(command.get(0)); return null;}
  }

  public static void awaitCompletion(Process p, String programName) {
    try {p.waitFor();}
    catch (InterruptedException e) {error_interruptedProcess(programName);}
  }

  public static void writeToStdin(Process process, byte[] data) {
    try {
      var stdin = process.getOutputStream();
      stdin.write(data);
      stdin.close();
    }
    catch (IOException e) {e.printStackTrace();}
  }

  public static byte[] readStdout(Process process) {    
    try {
      byte[] data = process.getInputStream().readAllBytes();
      process.waitFor();
      return data;
    }
    catch (IOException e) {e.printStackTrace(); return null;}
    catch (InterruptedException e) {stdout.error("Process execution was interrupted!"); return null;}
  }

  public static boolean succeeded(Process process) {return process.exitValue() == 0;}

  //Legacy function, to be replaced by the latest refactor
  public static String runAndGet(String[] args) {
    stdout.print_debug("Running command:", args);
    try {
      Process p = new ProcessBuilder(args).start();
      var stdout = p.getInputStream();
      p.waitFor();
      if (p.exitValue() != 0) {return null;}
      return new String(stdout.readAllBytes());
    }
    catch (IOException e) {error_missingProcess(args[0]); return null;}
    catch (InterruptedException e) {error_interruptedProcess(args[0]); return null;}
  }

  //Convenient for creating an ArrayList out of individual Strings
  static ArrayList<String> mkList(String... args) {
    var list = new ArrayList<String>();
    for (String a : args) {list.add(a);}
    return list;
  }

  private static void error_missingProcess(String process_name) {
    stdout.error("Failed to execute subprocess with name \""+process_name+"\"! Process does not seem to be present in system!");
  }
  
  private static void error_interruptedProcess(String process_name) {
    stdout.error("Failed to execute subprocess with name \""+process_name+"\"! Process execution was interrupted!");
  }
}
