package aya.cli;

public class help {
  public static final String VERSION = "0.5.1";
  
  public static String getHelp() {
    return
      "Aya (version "+VERSION+")"
      + "\nUsage: Aya [options] [optional filename]"
      + "\n\nAvailable Options:"
      + "\n  * -h - opens this menu"
      + "\n  * -v - displays Aya's version"
      + "\n  * -t [delay] - sets the delay for screenshotting in milliseconds"
      + "\n  * -d [directory] - sets the directory to save the screenshot at"
      + "\n  * -y - overwrites the image file if it already exists"
      + "\n"
      + "\n  * -width [width] - sets the screenshot's width for cropping"
      + "\n  * -height [height] - sets the screenshot's height for cropping"
      + "\n  * -x [pixels] - sets the starting horizontal point for cropping"
      + "\n  * -y [pixels] - sets the starting vertical point for cropping"
      + "\n  * -window - crops the screenshot to a window the user selects (overrides -x and -y)"
      + "\n  * -region - screenshots the region the user selects (FFmpeg only)"
      + "\n  * -open - opens the screenshot after saving it (requires configuration in dotfile)"
      + "\n"
      + "\n  * -s [factor] - scales the screenshot by a factor (>0 to +inf)"
      + "\n  * -f [format] - sets the image file format"
      + "\n    (supported formats: \"png\", \"jpg\", \"avif\" (FFmpeg only), \"bmp\" (FFmpeg only))"
      + "\n  * -q [number] - sets the quality level (0-5 for PNG (or 1-100 with ImageMagick), 1-100 for JPG, 0-63 for AVIF)"
      + "\n    (0-5 for PNG with FFmpeg and 1-100 for JPG or PNG with ImageMagick)"
      + "\n"
      + "\n  * -magick - uses ImageMagick instead of FFmpeg for taking screenshots"
      + "\n  * -quiet - disables all output messages"
      + "\n  * -verbose - displays more status messages"
      + "\n  * -debug - displays all messages for debugging"
    ;
  }

  public static String getSmallHelp() {
    return
      "Aya (version "+VERSION+")"
      + "\nUsage: Aya [options] [filename]"
      + "\nRun \"aya -h\" to view the full help screen"
    ;
  }
}
