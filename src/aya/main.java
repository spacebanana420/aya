package aya;

import aya.cli.parser;
import aya.cli.help;

public class main {
  public static void main(String[] args) {
    if (parser.hasArgument(args, "-h")) {
      System.out.println(help.getHelp());
      return;
    }
    new screenshotter().snap(args);
  }
}
