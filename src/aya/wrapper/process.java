package aya.wrapper;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import aya.ui.stdout;

public class process {
  public static boolean run(ArrayList<String> args, boolean silent) {
    stdout.print_debug("Running command:", args);
    try {
      var pb = new ProcessBuilder(args);
      if (silent) {
        pb.redirectOutput(Redirect.DISCARD);
        pb.redirectError(Redirect.DISCARD);
      }
      else {pb.inheritIO();}
      Process p = pb.start();
      p.waitFor();
      return p.exitValue() == 0;
    }
    catch (IOException e) {error_missingProcess(args.get(0)); return false;}
    catch (InterruptedException e) {error_interruptedProcess(args.get(0)); return false;}
  }

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
  
  //Used for grim and slurp commands for Wayland screen capture, they need lower-level control of the data
  public static byte[] run_stdout(ProcessBuilder cmd) {
    stdout.print_debug("Running command:", cmd.command());
    try {
      Process p = cmd.start();
      byte[] stdout = p.getInputStream().readAllBytes();
      p.waitFor();
      return stdout;
    }
    catch (IOException e) {error_missingProcess(cmd.command().get(0)); return null;}
    catch (InterruptedException e) {error_interruptedProcess(cmd.command().get(0)); return null;}
  }
  
  //Used in Wayland capture for passing an image into FFmpeg's standard input
  public static boolean run_stdin(ArrayList<String> args, byte[] screenshot_image) {
    stdout.print_debug("Running command:", args);
    try {
      Process p = new ProcessBuilder(args).start();
      var stdin = p.getOutputStream();
      stdin.write(screenshot_image);
      stdin.flush(); //is this necessary?
      stdin.close();
      p.waitFor();
      return p.exitValue() == 0;
    }
    catch (IOException e) {error_missingProcess(args.get(0)); return false;}
    catch (InterruptedException e) {error_interruptedProcess(args.get(0)); return false;}
  }

  //Used by xclip when copying an image's bytes into clipboard
   public static boolean run_stdin(String[] args, byte[] image) {
    stdout.print_debug("Running command:", args);
    try {
      Process p = new ProcessBuilder(args).start();
      var stdin = p.getOutputStream();
      stdin.write(image);
      stdin.close();
      p.waitFor();
      return p.exitValue() == 0;
    }
    catch (IOException e) {error_missingProcess(args[0]); return false;}
    catch (InterruptedException e) {error_interruptedProcess(args[0]); return false;}
  }

  public static ArrayList<String> mkList(String[] args) {
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
