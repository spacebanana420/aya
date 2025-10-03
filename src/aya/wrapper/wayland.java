package aya.wrapper;

import aya.ui.stdout;
import java.util.ArrayList;

public class wayland {
  public static byte[] captureScreen(boolean make_selection, boolean capture_cursor) {
    var cmd_grim = new ArrayList<String>();
    cmd_grim.add("grim");
    if (make_selection) {
      String coordinates = runSlurp();
      if (coordinates != null) {cmd_grim.add("-g"); cmd_grim.add(coordinates);}
      else {
        stdout.error("Failed to grab selection! Make sure you have Slurp installed");}
    }
    if (capture_cursor) {cmd_grim.add("-c");}
    cmd_grim.add("-l"); cmd_grim.add("0");
    cmd_grim.add("-");
    
    stdout.print_debug("Running Grim", cmd_grim);
    byte[] image = process.run_stdout(new ProcessBuilder(cmd_grim));
    
    if (image == null) {
      stdout.error("Failed to capture screen! Make sure you have Grim installed!\nIf you are not running a Wayland environment, use Aya in x11 mode instead!");
      return null;
    }
    stdout.print_debug("Grim-captured screenshot size: " + image.length + " bytes");
    return image;
  }
  
  //Capture a region of the screen
  private static String runSlurp() {
    stdout.print_debug("Running Slurp");
    var cmd = new ProcessBuilder(new String[]{"slurp", "-c", "#00000000", "-b", "#FFFFFF25"}).redirectInput(ProcessBuilder.Redirect.INHERIT);
    byte[] data = process.run_stdout(cmd);
    if (data == null) {return null;}
    return new String(data).trim();
  }
}
