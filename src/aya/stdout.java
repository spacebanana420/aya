package aya;

import java.util.ArrayList;

public class stdout {
  public static void print(String message) {
    if (global.verbosity_level > 0) {System.out.println(message);}
  }

  public static void print(String title, String[] contents) {
    if (global.verbosity_level > 0) {printSeq(title, contents);}
  }

  public static void print(String title, ArrayList<String> contents) {
    if (global.verbosity_level > 0) {printSeq(title, contents);}
  }

  public static void print_verbose(String message) {
    if (global.verbosity_level > 1) {System.out.println(message);}
  }

  public static void print_verbose(String title, String[] contents) {
    if (global.verbosity_level > 1) {printSeq(title, contents);}
  }

  public static void print_verbose(String title, ArrayList<String> contents) {
    if (global.verbosity_level > 1) {printSeq(title, contents);}
  }
  
  public static void print_debug(String message) {
    if (global.verbosity_level > 2) {System.out.println(message);}
  }

  public static void print_debug(String title, String[] contents) {
    if (global.verbosity_level > 2) {printSeq(title, contents);}
  }

  public static void print_debug(String title, ArrayList<String> contents) {
    if (global.verbosity_level > 2) {printSeq(title, contents);}
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
