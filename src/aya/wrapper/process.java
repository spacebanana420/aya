package aya.wrapper;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import aya.stdio;

public class process {
  public static int run(ArrayList<String> args, boolean silent) {
    stdio.print_debug("Running command:", args);
    try {
      var pb = new ProcessBuilder(args);
      if (silent) {
        pb.redirectOutput(Redirect.DISCARD);
        pb.redirectError(Redirect.DISCARD);
      }
      else {pb.inheritIO();}
      Process p = pb.start();
      p.waitFor();
      return p.exitValue();
    }
    catch (IOException e) {return -1;}
    catch (InterruptedException e) {return -2;}
  }

  public static String runAndGet(String[] args) {
    stdio.print_debug("Running command:", args);
    try {
      Process p = new ProcessBuilder(args).start();
      var stdout = p.getInputStream();
      p.waitFor();
      if (p.exitValue() != 0) {return null;}
      return new String(stdout.readAllBytes());
    }
    catch (IOException | InterruptedException e) {return null;}
  }
  
  //Used for grim and slurp commands for Wayland screen capture, they need lower-level control of the data
  public static byte[] run_stdout(ProcessBuilder cmd) {
    try {
      Process p = cmd.start();
      byte[] stdout = p.getInputStream().readAllBytes();
      p.waitFor();
      if (p.exitValue() != 0) {return null;}
      return stdout;
    }
    catch (IOException | InterruptedException e) {return null;}
  }
  
  //Used in Wayland capture for passing an image into FFmpeg's standard input
  public static int run_stdin(ArrayList<String> args, byte[] screenshot_image) {
    try {
      Process p = new ProcessBuilder(args).start();
      var stdout = p.getOutputStream();
      stdout.write(screenshot_image);
      stdout.flush(); //is this necessary?
      p.waitFor();
      return p.exitValue();
    }
    catch (IOException e) {return -1;}
    catch (InterruptedException e) {return -2;}
  }

  public static ArrayList<String> mkList(String[] args) {
    var list = new ArrayList<String>();
    for (String a : args) {list.add(a);}
    return list;
  }
}
