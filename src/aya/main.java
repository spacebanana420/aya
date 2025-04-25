package aya;

import aya.cli.parser;
import aya.cli.help;

public class main {
  public static void main(String[] args) {
    if (parser.hasArgument(args, "-h")) {
      stdout.print(help.getHelp());
      return;
    }
    else if(parser.hasArgument(args, "-v")) {
      stdout.print("Aya version " + help.VERSION);
      return;
    }
    global.verbosity_level = getVerbosityLevel(args);
    if (!systemSupported()) {
      stdout.print("Aya does not support this operating system! Aya must run under an operating system that can run an X11-based environment!");
      return;
    }
    screenshotter.takeScreenshot(args);
  }

  public static boolean systemSupported() {
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
