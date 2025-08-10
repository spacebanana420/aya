package aya.cli;

public class help {
  public static final String VERSION = "0.9";
  
  public static String getHelp() {
    return
      "Aya (version "+VERSION+")"
      + "\nUsage: Aya [options] [optional filename]"
      + "\n\nAvailable Options:"
      + "\n  * -h                     opens this menu"
      + "\n  * -v                     displays Aya's version"
      + "\n  * -t [delay]             sets the delay for screenshotting (in seconds)"
      + "\n  * -d [directory]         sets the directory to save the screenshot at"
      + "\n  * -y                     overwrites the image file if it already exists"
      + "\n  * -c                     includes the cursor in the screenshot (FFmpeg only)"
      + "\n"
      + "\n  * -width [width]         sets the screenshot's width for cropping"
      + "\n  * -height [height]       sets the screenshot's height for cropping"
      + "\n  * -x [pixels]            sets the starting horizontal point for cropping"
      + "\n  * -y [pixels]            sets the starting vertical point for cropping"
      + "\n  * -window                crops the screenshot to a window the user selects (overrides -x and -y)"
      + "\n  * -region                screenshots the region the user selects (FFmpeg only)"
      + "\n  * -open                  opens the screenshot after saving it (requires configuration in dotfile)"
      + "\n"
      + "\n  * -s [factor]            scales the screenshot by a factor (>0 to +inf)"
      + "\n  * -f [format]            sets the image file format (supported formats: png, jpg, avif, bmp"
      + "\n  * -q [number]            sets the quality level (0-5 for PNG, 1-100 for JPG, 0-63 for AVIF)"
      + "\n  * -avif-speed [number]   sets the AVIF encoding speed/efficiency tradeoff (0 to 8, higher is faster) (default 8)"
      + "\n"
      + "\n  * -wayland               enables experimental Wayland mode"
      + "\n  * -quiet                 disables all output messages"
      + "\n  * -verbose               displays more status messages"
      + "\n  * -debug                 displays all messages for debugging"
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
