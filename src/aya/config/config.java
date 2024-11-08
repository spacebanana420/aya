package aya.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import aya.stdout;

public class config {
  public static String[] openConfig() {
    confwriter.createConfig();
    String home = System.getProperty("user.home");
    try {
      var is = new FileInputStream(home + "/.config/aya/aya.conf");
      String conf = new String(is.readAllBytes());
      is.close();
      
      String buffer = "";
      ArrayList<String> conflist = new ArrayList<String>();
      for (int i = 0; i < conf.length(); i++) {
        char c = conf.charAt(i);
        if (c == '\n') {
          if (!buffer.equals("") && buffer.charAt(0) != '#') {conflist.add(buffer);}
          buffer = "";
          continue;
        }
        buffer += conf.charAt(i);
      }
      return conflist.toArray(new String[0]);
    }
    catch (IOException e) {return new String[0];}    
  }

  public static String getDirectory(String[] config) {
    String dir = readSetting(config, "screenshot_directory");
    if (dir == null) {return "";}
    var f = new File(dir);
    if (!f.isDirectory() || !f.isAbsolute()) {return "";}
    return dir;
  }

  public static int getDelay(String[] config) {
    int delay = readSetting_int(config, "screenshot_delay");
    return (delay < 0) ? 0 : delay;
  }

  public static String getFormat(String[] config, boolean use_magick) {
    String fmt = readSetting(config, "screenshot_format");
    if (fmt == null) {return "png";}
    fmt = fmt.toLowerCase();
    if (!fmt.equals("png") && !fmt.equals("jpg") && (!fmt.equals("avif") || use_magick)) {return "png";}  
    return fmt;
  }

  public static byte getQuality(String[] config) {
    byte value = readSetting_byte(config, "screenshot_quality");
    return (value >= 0 && value < 100) ? value : -1;
  }

  public static boolean useMagick(String[] config) {
    String use_magick = readSetting(config, "use_magick");
    if (use_magick == null) {return false;}
    use_magick = use_magick.toLowerCase();
    return (use_magick.equals("true") || use_magick.equals("yes")) ? true : false;
  }

  private static int readSetting_int(String[] config, String setting) {
    String svalue = readSetting(config, setting);
    if (svalue == null) {return -1;}
    try {
      return Integer.parseInt(svalue);
    }
    catch (NumberFormatException e) {return -1;}
  }

  private static byte readSetting_byte(String[] config, String setting) {
    String svalue = readSetting(config, setting);
    if (svalue == null) {return -1;}
    try {
      return Byte.parseByte(svalue);
    }
    catch (NumberFormatException e) {return -1;}
  }
  
  private static String readSetting(String[] config, String setting) {
    String full_setting = setting + "=";
    for (String line : config) {
      if (isSetting(line, full_setting)) {
        String value = "";
        for (int i = full_setting.length(); i < line.length(); i++)  {
          value += line.charAt(i);
        }
        return value;
      }
    }
    return null;
  }

  private static boolean isSetting(String line, String setting) {
    if (line.length() <= setting.length()) {return false;}
    for (int i = 0; i < setting.length(); i++) {
      if (line.charAt(i) != setting.charAt(i)) {return false;}
    }
    return true;
  }
}

class confwriter {
  public static void createConfig() {
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
    catch (IOException e) {stdout.print("Error creating Aya config! Unable to create config directory or file!");}
  }

  public static byte[] getDefaultConfig() {
    return
      (
        "==Aya Config=="
        + "\nTo enable a setting, uncomment it by removing the initial \"#\" character"
        + "\nCLI arguments override their respective settings here"

        + "\n\n==Settings=="
        + "\nSet an absolute path as the directory to where the screenshots are saved"
        + "\n#screenshot_directory=."
        
        + "\n\nSet a default delay in milliseconds for taking screenshots"
        + "\n#screenshot_delay=0"

        + "\n\nSupported formats (FFmpeg): \"png\" \"jpg\" \"avif\""
        + "\nSupported formats (ImageMagick): \"png\" \"jpg\""
        + "\n#screenshot_format=png"

        + "\n\nPNG: quality ranges from 0 to 5 (FFmpeg), or 0 to 100 (ImageMagick). Higher is better"
        + "\nJPG: quality ranges from 0 to 100. Higher is better"
        + "\nAVIF: quality ranges from 0 to 63. Lower is better. 0 implies lossless compression"
        + "\n#screenshot_quality=5"
        
        + "\n\nSet to \"true\" to use ImageMagick as a screenshotting backend rather than FFmpeg"
        + "\n#use_magick=false"
      ).getBytes();
  }
}
