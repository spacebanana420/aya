package aya;

import aya.cli.parser;
import aya.cli.help;

public class main {
  public static byte verbosity_level;
  
  public static void main(String[] args) {
    if (parser.hasArgument(args, "-h")) {
      System.out.println(help.getHelp());
      return;
    }
    verbosity_level = getVerbosityLevel(args);
    if (!systemSupported()) {
      System.out.println("Aya does not support this operating system! Aya must run under an operating system that can run an X11-based environment!");
      return;
    }
    screenshotter.takeScreenshot(args);
  }

  private static boolean systemSupported() {
    String os = System.getProperty("os.name").toLowerCase();
    return (!os.contains("Windows") && !os.contains("mac") && !os.equals("haiku"));
  }

  private static byte getVerbosityLevel(String[] args) {
    if (parser.hasArgument(args, "-quiet")) {return 0;}
    else if (parser.hasArgument(args, "-verbose")) {return 2;}
    else if (parser.hasArgument(args, "-debug")) {return 3;}
    else {return 1;}
  }
}
