package aya.ui;

import java.util.ArrayList;
import java.util.Scanner;

public class stdout {
  public static byte VERBOSITY_LEVEL = 1;
  
  public static void print(String message) {
    if (VERBOSITY_LEVEL > 0) {System.out.println(message);}
  }

  public static void print(String title, String[] contents) {
    if (VERBOSITY_LEVEL > 0) {printSeq(title, contents);}
  }

  public static void print(String title, ArrayList<String> contents) {
    if (VERBOSITY_LEVEL > 0) {printSeq(title, contents);}
  }

  public static void print_verbose(String message) {
    if (VERBOSITY_LEVEL > 1) {System.out.println(message);}
  }

  public static void print_verbose(String title, String[] contents) {
    if (VERBOSITY_LEVEL > 1) {printSeq(title, contents);}
  }

  public static void print_verbose(String title, ArrayList<String> contents) {
    if (VERBOSITY_LEVEL > 1) {printSeq(title, contents);}
  }
  
  public static void print_debug(String message) {
    if (VERBOSITY_LEVEL > 2) {System.out.println(message);}
  }

  public static void print_debug(String title, String[] contents) {
    if (VERBOSITY_LEVEL > 2) {printSeq(title, contents);}
  }

  public static void print_debug(String title, ArrayList<String> contents) {
    if (VERBOSITY_LEVEL > 2) {printSeq(title, contents);}
  }
  
  public static void error(String message) {
    if (VERBOSITY_LEVEL > 0) {System.err.println(message);}
    if (gui.GUI_ENABLED) {gui.displayMessage("Aya Error", message);}
  }
  
  public static String readInput(String message) {
    if (message != null && !message.isEmpty()) {
      System.out.println(message);
    }
    return new Scanner(System.in).nextLine();
  }
  
  public static void warnInvalidQuality(String format, int q_min, int q_max, int q_default) {
    print(
      "Invalid quality level chosen for format " + format + "!"
      + "\nSupported quality range: " + q_min + " to " + q_max
      + "\nDefaulting to " + q_default
    );
  }

  
  private static void printSeq(String title, String[] contents) {
    String txt = title;
    for (String c : contents) {txt += "\n  * " + c;}
    
    System.out.println(txt);
  }

  private static void printSeq(String title, ArrayList<String> contents) {
    String txt = title;
    for (int i = 0; i < contents.size(); i++) {txt += "\n  * " + contents.get(i);}

    System.out.println(txt);
  }
}
