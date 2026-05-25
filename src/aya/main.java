package aya;

import aya.cli.*;
import aya.config.*;
import aya.ui.*;

public class main {
  public static void main(String[] args) {
    setVerbosityLevel(args);
    if (args.length == 0) {
      stdout.print(help.getSmallHelp());
      return;
    }    
    if (cli.hasArgument(args, "-h") || cli.hasArgument(args, "--help")) {
      stdout.print(help.getHelp());
      return;
    }
    if (cli.hasArgument(args, "-qh")) {
      stdout.print(help.getQualityHelp());
      return;
    }
    if (cli.hasArgument(args, "-v") || cli.hasArgument(args, "--version")) {
      stdout.print("Aya version " + help.VERSION);
      return;
    }
    
    Config conf = confio.openConfig();
    CaptureOpts opts = new CaptureOpts(args, conf);
    String OS = System.getProperty("os.name").toLowerCase();
    if (!opts.save_file && !opts.copy_to_clipboard) {
      stdout.print(help.getSmallHelp());
      return;
    }
    if (unsupportedSystem(OS)) {
      stdout.error("Aya does not support this operating system! Aya must run on a UNIX-like system!");
      return;
    }
    gui.setupGUI(args, conf);
    
    boolean result = capture.takeScreenshot(opts, OS.equals("linux"));
    System.exit(result ? 0 : 1);
  }

  private static boolean unsupportedSystem(String os) {
    return (os.contains("windows") || os.contains("mac") || os.contains("darwin") || os.equals("haiku"));
  }

  private static void setVerbosityLevel(String[] args) {
    if (cli.hasArgument(args, "-quiet")) {stdout.setQuiet();}
    else if (cli.hasArgument(args, "-verbose")) {stdout.setVerbose();}
    else if (cli.hasArgument(args, "-debug")) {stdout.setDebug();}
  }
}
