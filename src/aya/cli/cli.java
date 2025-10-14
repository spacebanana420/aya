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
      printError(arg, "The argument must be followed by a value!");
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
      printError(arg, "The value provided " + value + " is too big! Ignoring");
      return -1;
    }
    try {
      int num = Integer.parseInt(value);
      if (num < 0) {
        printError(arg, "The value must not be negative, ignoring");
        return -1;
      }
      return num;
    }
    catch (NumberFormatException e) {
      printError(arg, "Failed to convert value " + value + " into a signed int number (32bit)");
      return -1;
    }
  }

  public static String getScreenshotDirectory(String[] args) {return getArgValue(args, "-d");}
  public static float getScreenshotScale(String[] args) {return getArgFloat(args, "-s");}
  public static byte getScreenshotQuality(String[] args) {return getArgByte(args, "-q", 100);}
  public static int getScreenshotDelay(String[] args) {return getArgInt(args, "-t");}
  public static byte getAvifSpeed(String[] args) {return getArgByte(args, "-avif-speed", 8);}
  public static boolean fastAvif(String[] args) {return hasArgument(args, "-avif-fast");}
  public static float getGUIScale(String[] args) {return getArgFloat(args, "-gui-scale");}

  private static byte getArgByte(String[] args, String arg, int upper_bound) {
    int num = getArgInt(args, arg);
    if (num == -1) {return -1;}
    if (num > upper_bound) {
      printError(arg, "The value must not be above " + upper_bound + ", ignoring");
      return -1;
    }
    return (byte)num;
  }

  private static float getArgFloat(String[] args, String arg) {
    String value = getArgValue(args, arg);
    if (value == null) {return -1;}
    try {
      float num = Float.parseFloat(value);
      if (num < 0) {
        printError(arg, "The value must not be negative, ignoring");
        return -1;
      }
      return num;
    }
    catch (NumberFormatException e) {
      printError(arg, "Failed to convert value " + value + " into a float number");
      return -1;
    }
  }

  private static int argIndex(String[] args, String arg) {
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals(arg)) {return i;}
    }
    return -1;
  }

  //CLI arguments that start with "-" are meant to be options and not values of an option
  private static boolean invalidValue(String value) {return value.isEmpty() || value.charAt(0) == '-';}

  private static void printError(String arg, String message) {
    stdout.error("[Aya CLI] Incorrect value provided with CLI argument " + arg + "\n" + message);
  }
}
