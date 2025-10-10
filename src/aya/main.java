package aya;

import aya.cli.cli;
import aya.cli.help;
import aya.config.*;
import aya.ui.*;

public class main {
  public static void main(String[] args) {
    setVerbosityLevel(args);    
    
    if (cli.hasArgument(args, "-h")) {
      stdout.print(help.getHelp());
      return;
    }
    if (cli.hasArgument(args, "-qh")) {
      stdout.print(help.getQualityHelp());
      return;
    }
    if (cli.hasArgument(args, "-v")) {
      stdout.print("Aya version " + help.VERSION);
      return;
    }
    if (unsupportedSystem()) {
      stdout.error("Aya does not support this operating system! Aya must run on a UNIX-like system!");
      return;
    }
    Config conf = confio.openConfig();
    gui.setupGUI(args, conf);
    
    int result = capture.takeScreenshot(args, conf);
    System.exit(result);
  }

  private static boolean unsupportedSystem() {
    String os = System.getProperty("os.name").toLowerCase();
    return (os.contains("windows") || os.contains("mac") || os.contains("darwin") || os.equals("haiku"));
  }

  private static void setVerbosityLevel(String[] args) {
    if (cli.hasArgument(args, "-quiet")) {stdout.setQuiet();}
    else if (cli.hasArgument(args, "-verbose")) {stdout.setVerbose();}
    else if (cli.hasArgument(args, "-debug")) {stdout.setDebug();}
  }
}
