package aya.config;

import java.io.File;
import java.util.ArrayList;
import aya.ui.stdout;

public class config {
  public static String getDirectory(Config c) {
    String dir = c.readSetting("screenshot_directory");
    if (dir == null) {return "";}
    var f = new File(dir);
    if (!f.isDirectory() || !f.isAbsolute()) {return "";}
    return dir;
  }

  public static int getDelay(Config c) {
    int delay = c.readSetting_int("screenshot_delay");
    return (delay < 0) ? 0 : delay;
  }

  public static String getFormat(Config c) {
    String fmt = c.readSetting("screenshot_format");
    return fmt == null ? null : fmt.toLowerCase();
  }

  public static byte getQuality(Config c) {
    byte value = c.readSetting_byte("screenshot_quality");
    return (value >= 0 && value < 100) ? value : -1;
  }
  
  public static byte getAvifSpeed(Config c) {
    byte value = c.readSetting_byte("avif_speed");
    return (value >= 0 && value <= 8) ? value : 8;
  }

  public static String getFFmpegPath(Config c) {
    String path = c.readSetting("ffmpeg_path");
    if (path == null) {return "ffmpeg";}
    var f = new File(path);
    if (f.isFile() && f.isAbsolute() && f.canExecute()) {return path;} else {return "ffmpeg";}
  }
  
  public static ArrayList<String> getImageViewer(Config c, String filename) {
    return c.readCommand("image_viewer_command", filename);
  }
  
  public static boolean overrideFile(Config c) {
    return c.readSetting_bool("override_file");
  }

  public static boolean captureCursor(Config c) {
    return c.readSetting_bool("capture_cursor");
  }

  public static boolean waylandModeEnabled(Config c) {return c.readSetting_bool("wayland_Mode");}

  public static byte getGUIScale(Config c) {
    byte scale = c.readSetting_byte("gui_scale");
    return scale < 1 || scale > 3 ? -1 : scale;
  }
}
