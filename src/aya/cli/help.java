package aya.cli;

public class help {
  public static String getHelp() {
    return
      "Aya (version 0.1)"
      + "\nUsage: Aya [options]"
      + "\n\nAvailable Options:"
      + "\n  * -h - opens this menu"
      + "\n  * -t [delay] - sets the delay for screenshotting in milliseconds"
      + "\n  * -d [directory] - sets the directory to save the screenshot at"
      + "\n  * -width [width] - sets the screenshot's width for cropping"
      + "\n  * -height [height] - sets the screenshot's height for cropping"
      + "\n  * -x [pixels] - sets the starting horizontal point for cropping"
      + "\n  * -y [pixels] - sets the starting vertical point for cropping"
      + "\n  * -s [factor] - scales the screenshot by a factor (>0 to +inf)"
      + "\n  * -f [format] - sets the image file format (supported: \"png\", \"jpg\")"
      + "\n  * -q [number] - sets the quality level (0-5 for PNG and 1-100 for JPG)"
      + "\n  * -magick - uses ImageMagick instead of FFmpeg for taking screenshots"
      + "\n  * -quiet - disables all output messages"
      + "\n  * -verbose - displays more status messages"
      + "\n  * -debug - displays all messages for debugging"
  ;
  }

  public static String getSmallHelp() {
    return
      "Aya (version 0.1)"
      + "\nUsage: Aya [options] [filename]"
      + "\nRun \"aya -h\" to view the full help screen"
    ;
  }
}
