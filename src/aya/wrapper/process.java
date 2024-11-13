package aya.wrapper;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import aya.stdout;

public class process {
  public static int run(String[] args, boolean silent) {
    stdout.print_debug("Running command:", args);
    try {
      var pb = new ProcessBuilder(args);
      pb.inheritIO();
      if (silent) {
        pb.redirectOutput(Redirect.DISCARD);
        pb.redirectError(Redirect.DISCARD);
      }
      var p = pb.start();
      p.waitFor();
      return p.exitValue();
    }
    catch (IOException e) {return -1;}
    catch (InterruptedException e) {return -2;}
  }

  //duplicate code, rewrite later
  public static String runAndGet(String[] args, boolean silent) {
    stdout.print_debug("Running command:", args);
    try {
      var pb = new ProcessBuilder(args);
      pb.inheritIO();
      if (silent) {
        pb.redirectOutput(Redirect.DISCARD);
        pb.redirectError(Redirect.DISCARD);
      }
      var p = pb.start();
      p.waitFor();
      if (p.exitValue() != 0) {return null;}
      return new String(p.getInputStream().readAllBytes());
    }
    catch (IOException e) {return null;}
    catch (InterruptedException e) {return null;}
  }

  public static int run(ArrayList<String> args, boolean silent) {
    return run(args.toArray(new String[0]), silent);
  }

  public static String[] concatArgs(String[]... args) {
    if (args.length == 0 || args[0].length == 0) {return new String[0];}
    int len = 0;
    int i = 0;
    for (String[] a : args) {len += a.length;}
    String[] final_args = new String[len];
    for (String[] a : args) {
      for (String arg : a) {
        final_args[i] = arg;
        i++;
      }
    }
    return final_args;
  }

  public static String[] appendArg(String[] args, String newarg) {
    String[] newArgs = new String[args.length+1];
    for (int i = 0; i < args.length; i++) {
      newArgs[i] = args[i];
    }
    newArgs[args.length] = newarg;
    return newArgs;
  }

  public static ArrayList<String> mkList(String[] args) {
    var list = new ArrayList<String>();
    for (String a : args) {list.add(a);}
    return list;
  }

  public static void concatList(ArrayList<String> list, String[] args) {
    for (String a : args) {list.add(a);}
  }
}
