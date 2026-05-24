package aya.wrapper;

import aya.ui.stdout;
import java.util.ArrayList;

public class wayland {
  public static byte[] captureScreen(boolean make_selection, boolean capture_cursor, boolean better_compression) {
    var cmd_grim = new ArrayList<String>();
    cmd_grim.add("grim");
    if (make_selection) {
      String coordinates = runSlurp();
      if (coordinates == null) return null;
      cmd_grim.add("-g");
      cmd_grim.add(coordinates);
    }
    if (capture_cursor) cmd_grim.add("-c");
    cmd_grim.add("-l"); cmd_grim.add(better_compression ? "2" : "0");
    cmd_grim.add("-");
    
    byte[] image = process.readStdout(process.runProcess(cmd_grim));
    
    if (image == null) {
      stdout.error("Failed to capture screen! Make sure you have Grim installed!\nIf you are not running a Wayland environment, use Aya in x11 mode instead!");
      return null;
    }
    stdout.print_debug("Grim-captured screenshot size: " + image.length + " bytes");
    return image;
  }

  public static boolean copyToClipboard(byte[] data) {
    Process p = process.runProcess(new String[]{"wl-copy"});
    process.writeToStdin(p, data);
    process.awaitCompletion(p, "wl-copy");
    if (!process.succeeded(p)) {
      stdout.error("Failed to copy image to clipboard!\nMake sure you have wl-clipboard installed!");
      return false;
    }
    return true;
  }
  
  //Capture a region of the screen
  private static String runSlurp() {
    var cmd = new ProcessBuilder(new String[]{"slurp", "-c", "#00000000", "-b", "#FFFFFF25"}).redirectInput(ProcessBuilder.Redirect.INHERIT);
    Process p = process.runProcess(cmd);
    byte[] data = process.readStdout(p);
    if (!process.succeeded(p)) {
      stdout.print_verbose("Region selection was cancelled");
      return null;
    } 
    if (data == null) {
      stdout.error("Slurp region selection failed! Make sure you have Slurp installed");
      return null;
    }
    return new String(data).trim();
  }
}
