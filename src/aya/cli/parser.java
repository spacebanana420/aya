package aya.config;

public class parser {
  public static int argIndex(String[] args, String arg) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals(arg)) {return i;}
    }
    return -1;
  }
  
  public static boolean hasArgument(String[] args, String arg) {
    return argIndex(args, arg) != -1;
  }
  
  public static String getArgValue(String[] args, String arg) {
    int i = argIndex(args, arg);
    if (i == -1 || i == args.length-1) {return null;}
    return args[i+1];
  }
}
