package aya.cli;

import aya.misc;
import aya.ui.stdout;

//CLI parsing functions
public class cli {
  public static boolean hasArgument(String[] args, String arg) {return argIndex(args, arg) != -1;}
  
  public static String getArgValue(String[] args, String arg) {
    int i = argIndex(args, arg);
    if (i == -1) {return null;}
    if (i == args.length-1 || invalidValue(args[i+1])) {
      stdout.error("The argument " + arg + " must be followed by a value!");
      return null;
    }
    return args[i+1];
  }

  public static String getFilename(String[] args, String extension) {
    for (String arg : args) {
      if (arg.charAt(0) != '-' && misc.hasExtension(arg, extension)) {
        return arg;
      }
    }
    return null;
  }

  public static int getArgInt(String[] args, String arg) {
    String value = getArgValue(args, arg);
    if (value == null) {return -1;}
    if (value.length() > 9) {
      stdout.error("The value provided " + value + " for the argument " + arg + " is too big! Ignoring");
      return -1;
    }
    try {
      int num = Integer.parseInt(value);
      if (num < 0) {
        stdout.error("Incorrect value provided with CLI argument " + arg + "\nThe value must not be negative, ignoring");
        return -1;
      }
      return num;
    }
    catch (NumberFormatException e) {return -1;}
  }
  
  public static float getArgFloat(String[] args, String arg) {
    String value = getArgValue(args, arg);
    if (value == null) {return -1;}
    try {
      float num = Float.parseFloat(value);
      if (num < 0) {
        stdout.error("Incorrect value provided with CLI argument " + arg + "\nThe value must not be negative, ignoring");
        return -1;
      }
      return num;
    }
    catch (NumberFormatException e) {return -1;}
  }

  //Byte value with an upper limit of 100
  public static byte getArgQuality(String[] args, String arg) {return getArgByte(args, arg, 100);}

  //For GUI display scale
  public static byte getArgScale(String[] args, String arg) {return getArgByte(args, arg, 3);}

  private static byte getArgByte(String[] args, String arg, int upper_bound) {
    int num = getArgInt(args, arg);
    if (num == -1) {return -1;}
    if (num > upper_bound) {
      stdout.error("Incorrect value provided with CLI argument " + arg + "\nThe value must not be above " + upper_bound + ", ignoring");
      return -1;
    }
    return (byte)num;
  }

  private static int argIndex(String[] args, String arg) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals(arg)) {return i;}
    }
    return -1;
  }

  //CLI arguments that start with "-" are meant to be options and not values of an option
  private static boolean invalidValue(String value) {return value.isEmpty() || value.charAt(0) == '-';}
}
