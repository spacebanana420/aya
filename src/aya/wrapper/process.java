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

  //duplicate code, rewrite later
  public static String runAndGet(String[] args) {
    stdio.print_debug("Running command:", args);
    try {
      var p = new ProcessBuilder(args).start();
      var stdout = p.getInputStream();
      p.waitFor();
      if (p.exitValue() != 0) {return null;}
      return new String(stdout.readAllBytes());
    }
    catch (IOException e) {return null;}
    catch (InterruptedException e) {return null;}
  }

  public static ArrayList<String> mkList(String[] args) {
    var list = new ArrayList<String>();
    for (String a : args) {list.add(a);}
    return list;
  }
}
