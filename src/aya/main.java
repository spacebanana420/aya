package aya;

import aya.cli.cli;
import aya.cli.help;

public class main {
  public static void main(String[] args) {
    stdout.VERBOSITY_LEVEL = getVerbosityLevel(args);
    
    if (cli.hasArgument(args, "-h")) {
      stdout.print(help.getHelp());
      return;
    }
    else if (cli.hasArgument(args, "-qh")) {
      stdout.print(help.getQualityHelp());
      return;
    }
    else if (cli.hasArgument(args, "-v")) {
      stdout.print("Aya version " + help.VERSION);
      return;
    }
    if (unsupportedSystem()) {
      stdout.error("Aya does not support this operating system! Aya must run on a UNIX-like system!");
      return;
    }
    int result = capture.takeScreenshot(args);
    System.exit(result);
  }

  private static boolean unsupportedSystem() {
    String os = System.getProperty("os.name").toLowerCase();
    return (os.contains("windows") || os.contains("mac") || os.contains("darwin") || os.equals("haiku"));
  }

  private static byte getVerbosityLevel(String[] args) {
    if (cli.hasArgument(args, "-quiet")) {return 0;}
    else if (cli.hasArgument(args, "-verbose")) {return 2;}
    else if (cli.hasArgument(args, "-debug")) {return 3;}
    else {return 1;}
  }
}
