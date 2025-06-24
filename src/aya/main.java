package aya;

import aya.cli.parser;
import aya.cli.help;

public class main {
  public static void main(String[] args) {
    stdio.VERBOSITY_LEVEL = getVerbosityLevel(args);
    
    if (parser.hasArgument(args, "-h")) {
      stdio.print(help.getHelp());
      return;
    }
    else if (parser.hasArgument(args, "-v")) {
      stdio.print("Aya version " + help.VERSION);
      return;
    }
    if (!systemSupported()) {
      stdio.print_error("Aya does not support this operating system! Aya must run on a UNIX-like system!");
      return;
    }
    int result = screenshotter.takeScreenshot(args);
    System.exit(result);
  }

  public static boolean systemSupported() {
    String os = System.getProperty("os.name").toLowerCase();
    return (!os.contains("windows") && !os.contains("mac") && !os.contains("darwin") && !os.equals("haiku"));
  }

  private static byte getVerbosityLevel(String[] args) {
    if (parser.hasArgument(args, "-quiet")) {return 0;}
    else if (parser.hasArgument(args, "-verbose")) {return 2;}
    else if (parser.hasArgument(args, "-debug")) {return 3;}
    else {return 1;}
  }
}
