package aya.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import aya.stdio;

public class config {
  public static Setting[] openConfig() {
    confwriter.createConfig();
    String home = System.getProperty("user.home");
    try {
      var is = new FileInputStream(home + "/.config/aya/aya.conf");
      String conf = new String(is.readAllBytes());
      is.close();
      
      String buffer = "";
      ArrayList<String> conflist = new ArrayList<String>();
      for (int i = 0; i < conf.length(); i++)
      {
        char c = conf.charAt(i);
        if (c == '\n') {
          if (buffer.length() > 0 && buffer.charAt(0) != '#') {conflist.add(buffer);}
          buffer = "";
          continue;
        }
        buffer += conf.charAt(i);
      }
      if (buffer.length() > 0 && buffer.charAt(0) != '#') {conflist.add(buffer);}
      return getSettings(conflist);
    }
    catch (IOException e) {return new Setting[0];}    
  }

  private static Setting[] getSettings(ArrayList<String> lines) {
    var settings = new ArrayList<Setting>();
    
    for (String line : lines) 
    {
      Setting s = new Setting(line);
      if (s.key == null || s.value == null) {continue;}
      settings.add(s);
    }
    return settings.toArray(new Setting[0]);
  }

  public static String getDirectory(Setting[] config) {
    String dir = confio.readSetting(config, "screenshot_directory");
    if (dir == null) {return "";}
    var f = new File(dir);
    if (!f.isDirectory() || !f.isAbsolute()) {return "";}
    return dir;
  }

  public static int getDelay(Setting[] config) {
    int delay = confio.readSetting_int(config, "screenshot_delay");
    return (delay < 0) ? 0 : delay;
  }

  public static String getFormat(Setting[] config, boolean use_magick) {
    String fmt = confio.readSetting(config, "screenshot_format");
    if (fmt == null) {return "png";}
    fmt = fmt.toLowerCase();
    if (!unsupportedFormat(fmt, use_magick)) {return "png";}  
    return fmt;
  }
  
  public static boolean unsupportedFormat(String format, boolean use_magick) { //also used in screenshotter.java
    return
      !format.equals("png")
      && !format.equals("jpg")
      && ((!format.equals("avif") && !format.equals("bmp")) || use_magick)
    ;
  }

  public static byte getQuality(Setting[] config) {
    byte value = confio.readSetting_byte(config, "screenshot_quality");
    return (value >= 0 && value < 100) ? value : -1;
  }
  
  public static byte getAvifSpeed(Setting[] config) {
    byte value = confio.readSetting_byte(config, "avif_speed");
    return (value >= 0 && value <= 8) ? value : 8;
  }

  public static boolean useMagick(Setting[] config) {
    return confio.readSetting_bool(config, "use_magick");
  }

  public static String getFFmpegPath(Setting[] config) {
    String path = confio.readSetting(config, "ffmpeg_path");
    if (path == null) {return "ffmpeg";}
    var f = new File(path);
    if (f.isFile() && f.isAbsolute() && f.canExecute()) {return path;} else {return "ffmpeg";}
  }
  
  public static String getMagickPath(Setting[] config) {
    String path = confio.readSetting(config, "magick_path");
    if (path == null) {return "magick";}
    var f = new File(path);
    if (f.isFile() && f.isAbsolute() && f.canExecute()) {return path;} else {return "magick";}
  }
  
  public static ArrayList<String> getImageViewer(Setting[] config, String filename) {
    return confio.readCommand(config, "image_viewer_command", filename);
  }
  
  public static boolean overrideFile(Setting[] config) {
    return confio.readSetting_bool(config, "override_file");
  }
}

class confwriter {
  static void createConfig() {
    String home = System.getProperty("user.home");
    var dir_f = new File(home + "/.config/aya");
    var conf_f = new File(home + "/.config/aya/aya.conf");
    
    try {
      if (!dir_f.isDirectory()) {dir_f.mkdir();}
      if (!conf_f.isFile()) {
        var os = new FileOutputStream(conf_f);
        os.write(getDefaultConfig());
        os.close();
      }
    }
    catch (IOException e) {stdio.print("Error creating Aya config! Unable to create config directory or file!");}
  }

  static byte[] getDefaultConfig() {
    return
      (
        "==Aya Config=="
        + "\n# To enable a setting, uncomment it by removing the initial \"#\" character"
        + "\n# CLI arguments override their respective settings here"

        + "\n\n==Settings=="
        + "\n# Set an absolute path as the directory to where the screenshots are saved"
        + "\n#screenshot_directory=."
        
        + "\n\n# Set a default delay in milliseconds for taking screenshots"
        + "\n#screenshot_delay=0"

        + "\n\n# Supported formats (FFmpeg): \"png\" \"jpg\" \"avif\" \"bmp\""
        + "\n# Supported formats (ImageMagick): \"png\" \"jpg\""
        + "\n#screenshot_format=png"

        + "\n\n# PNG: quality ranges from 0 to 5 (FFmpeg), or 1 to 100 (ImageMagick). Higher is better"
        + "\n# JPG: quality ranges from 1 to 100. Higher is better"
        + "\n# AVIF: quality ranges from 0 to 63. Lower is better. 0 implies lossless compression"
        + "\n#screenshot_quality=5"
        
        + "\n\n# AVIF speed defines the tradeoff between compression speed and compresion efficiency"
        + "\n# Value ranges from 0 to 8, default value is 8. Higher values represent faster encoding at the cost of lower compression efficiency"
        + "\n#avif_speed=8"
        
        + "\n\n# The command to use for opening screenshot images"
        + "\n# Use the special keyword %F to place the filename"
        + "\n# If unspecified, the filename is added at the end of the command"
        + "\n#image_viewer_command=xdg-open %F"
        
        + "\n\n# If another image with the same filename exists, Aya will override it"
        + "\n#override_file=false"
        
        + "\n\n# Set to \"true\" to use ImageMagick as a screenshotting backend rather than FFmpeg"
        + "\n#use_magick=false"

        + "\n\n# Set the absolute path to the FFmpeg and ImageMagick binaries for custom ones"
        + "\n#ffmpeg_path=ffmpeg"
        + "\n#magick_path=magick"
      ).getBytes();
  }
}
