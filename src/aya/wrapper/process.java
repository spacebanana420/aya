package aya.wrapper;

import java.io.IOException;
import java.util.ArrayList;

public class process {
  public static int run(String[] args) {
    try {
      var p =
        new ProcessBuilder(args)
        .inheritIO()
        .start();
      p.waitFor();
      return p.exitValue();
    }
    catch (IOException e) {return -1;}
    catch (InterruptedException e) {return -2;}
  }

  public static int run(ArrayList<String> args) {
    return run(args.toArray(new String[0]));
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
