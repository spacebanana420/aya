package aya.config;

import aya.ui.stdout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class confio {
  public static Config openConfig() {
    createConfig();
    String config_path = System.getProperty("user.home") + "/.config/aya/aya.conf";
    String conf = null;
    try {
      var is = new FileInputStream(config_path);
      conf = new String(is.readAllBytes());
      is.close();
    }
    catch (IOException e) {
      stdout.error("Failed to read Aya config in path " + config_path);
      return new Config();
    }    
      
    var line = new StringBuilder();
    ArrayList<String> conflist = new ArrayList<String>();
    for (int i = 0; i < conf.length(); i++)
    {
      char c = conf.charAt(i);
      if (c == '\n') {
        addLine(conflist, line);
        line = new StringBuilder();
      }
      else {line.append(conf.charAt(i));}
    }
    addLine(conflist, line);
    Config c = new Config();
    for (String confline : conflist) {c.addSetting(confline);}
    return c;
  }

  private static void addLine(ArrayList<String> lines, StringBuilder line) {
    if (line.length() == 0) {return;}
    String line_str = line.toString().trim();
    if (line_str.length() == 0 || line_str.charAt(0) == '#') {return;}
    lines.add(line_str);
  }

  private static void createConfig() {
    String home = System.getProperty("user.home");
    var dir_f = new File(home + "/.config/aya");
    var conf_f = new File(home + "/.config/aya/aya.conf");
    
    try {
      if (!dir_f.isDirectory()) {dir_f.mkdir();}
      if (conf_f.isFile()) {return;}
      var os = new FileOutputStream(conf_f);
      os.write(getDefaultConfig());
      os.close();
    }
    catch (IOException e) {
      stdout.error("Error creating Aya config! Unable to create config directory or file!");}
  }

  private static byte[] getDefaultConfig() {
    return
      (
        "==Aya Config=="
        + "\n# To enable a setting, uncomment it by removing the initial \"#\" character"
        + "\n# CLI arguments override their respective settings here"

        + "\n\n==Settings=="
        + "\n# Set path to where the screenshots are saved"
        + "\n#screenshot_directory=."
        
        + "\n\n# Set a default delay in seconds for taking screenshots"
        + "\n#screenshot_delay=0"

        + "\n\n# Supported formats: \"png\" \"jpg\" \"avif\" \"bmp\""
        + "\n#screenshot_format=png"

        + "\n\n# PNG: quality ranges from 0 to 5 (FFmpeg). Higher is better"
        + "\n# JPG: quality ranges from 1 to 100. Higher is better"
        + "\n# AVIF: quality ranges from 0 to 63. Lower is better. 0 implies lossless compression"
        + "\n#screenshot_quality=5"
        
        + "\n\n# Includes the cursor in the screenshot (FFmpeg only)"
        + "\n#capture_cursor=false"
        
        + "\n\n# AVIF speed defines the tradeoff between compression speed and compresion efficiency"
        + "\n# Value ranges from 0 to 8, default value is 8. Higher values represent faster encoding at the cost of lower compression efficiency"
        + "\n#avif_speed=8"

        + "\n\n# Turn this on to increase AVIF encoding speed at the cost of lower encoding efficiency. Overrides avif_speed"
        + "\n#avif_fast_mode=false"
        
        + "\n\n# The command to use for opening screenshot images"
        + "\n# Use the special keyword %F to place the filename"
        + "\n# If unspecified, the filename is added at the end of the command"
        + "\n#image_viewer_command=xdg-open %F"
        
        + "\n\n# If another image with the same filename exists, Aya will override it"
        + "\n#override_file=false"

        + "\n\n# You can specify a custom path to the FFmpeg binary if you don't want to use the one installed in the system"
        + "\n#ffmpeg_path=ffmpeg"

        + "\n\n# Configure Aya to work on Wayland instead of X11"
        + "\n#wayland_mode=false"

        + "\n\n# Enable GUI mode to open graphical windows when an error happens"
        + "\n#use_gui=false"
        
        + "\n\n# Set the scale of GUI elements, supports any value between 0.5 and 3 (inclusive) (e.g. 1.75)"
        + "\n#gui_scale=1"
      ).getBytes();
  }
}
