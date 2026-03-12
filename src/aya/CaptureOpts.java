package aya;

import aya.config.config;
import aya.config.Config;
import aya.cli.cli;
import aya.ui.stdout;
import aya.wrapper.x11;

import java.util.ArrayList;
import java.io.File;
import java.time.LocalDate;

//Stores screenshot settings to be used by capture.java
//These settings are determined by the provided CLI arguments and Aya config file
//CLI arguments take priority over the configuration file
class CaptureOpts {
  String ffmpeg_path = "ffmpeg";
  String file_path = "";
  int delay = 0;
  boolean override_file = false;
  boolean open_image = false;
  ArrayList<String> image_viewer_cmd = null;
  
  String format = "png";
  int[] crop = new int[4];
  byte quality = -1;
  float scale = 0f;
  
  byte avif_speed = 8;
  boolean avif_fast = false;
  
  boolean window_select = false;
  boolean region_select = false;
  boolean capture_cursor = false;
  boolean wayland_mode = false;

  CaptureOpts(String[] args, Config conf) {
    Thread[] threads = new Thread[3];
    threads[0] = new Thread(() -> {
      this.override_file = cli.hasArgument(args, "-y") || config.overrideFile(conf);
      this.ffmpeg_path = config.getFFmpegPath(conf);
      this.capture_cursor = cli.hasArgument(args, "-c") || config.captureCursor(conf);
      this.open_image = cli.hasArgument(args, "-open");
    });

    threads[1] = new Thread(() -> {
      String waylandEnv = System.getenv("XDG_BACKEND");
      this.wayland_mode = (waylandEnv != null && waylandEnv.equals("wayland")) || cli.hasArgument(args, "-wayland") || config.waylandModeEnabled(conf);
      this.window_select = cli.hasArgument(args, "-window");
      this.region_select = !this.window_select && cli.hasArgument(args, "-region");
      this.crop = getCrop(args, this.window_select, this.wayland_mode);
    });

    threads[2] = new Thread(() -> {
      this.scale = getScale(args);
      this.format = getFormat(args, conf);
      this.quality = getQuality(args, conf);
      this.delay = getDelay(args, conf);
      this.file_path = generateFilename(args, conf, this.format);
    });
    runThreads(threads);
    
    if (this.open_image) {
      this.image_viewer_cmd = config.getImageViewer(conf, file_path);
    }
    if (this.format.equals("avif")) {
      this.avif_fast = cli.fastAvif(args) || config.getAvifMode(conf);
      if (!this.avif_fast) {this.avif_speed = getAvifSpeed(args, conf);}
    }
  }

  private static int[] getCrop(String[] args, boolean window_select, boolean wayland_enabled) {
    String[] opts = new String[]{"-width", "-height", "-x", "-y"};
    int[] crop_coords = new int[4];
    
    for (int i = 0; i < opts.length; i++) {
      int value = cli.getArgInt(args, opts[i]);
      if (value >= 0) {crop_coords[i] = value;}
    }

    if (window_select) {
      if (wayland_enabled) {
        stdout.error("Window capture (-window) is not supported in Wayland mode!");
        return crop_coords;
      }
      int[] window_coords = x11.getWindowCoordinates();
      if (window_coords != null) {crop_coords = window_coords;}
    }
    return crop_coords;
  }

  private static float getScale(String[] args) {
    float value = cli.getScreenshotScale(args);
    if (value < 0) {value = 0;}
    return value;
  }
  
  private static String getFormat(String[] args, Config conf) {
    String value = getFormat_cli(args);
    if (value != null) {return value;}

    value = getFormat_config(conf);
    if (value != null) {return value;}
    return "png";
  }

  private static String getFormat_cli(String[] args) {
    String value = cli.getArgValue(args, "-f");
    if (value == null) {return null;}
    value = value.toLowerCase();

    if (supportedFormat(value)) {return value;}
    stdout.error("Ignored specified image format " + value + " found in CLI arguments for being invalid");
    return null;
  }
  private static String getFormat_config(Config conf) {
    String value = config.getFormat(conf);
    if (value == null) {return null;}
    if (supportedFormat(value)) {return value;}
    stdout.error("Ignored specified image format " + value + " found in aya configuration for being invalid");
    return null;
  }

  private static boolean supportedFormat(String format) {
    return
      format.equals("png")
      || format.equals("jpg")
      || format.equals("avif")
      || format.equals("bmp")
    ;
  }

  private static byte getQuality(String[] args, Config conf) {
    byte value = cli.getScreenshotQuality(args);
    if (value == -1) {value = config.getQuality(conf);}
    return value;
  }

  private static int getDelay(String[] args, Config conf) {
    int value = cli.getScreenshotDelay(args);
    if (value == -1) {value = config.getDelay(conf);}
    return value;
  }
  
  private static byte getAvifSpeed(String[] args, Config conf) {
    byte cli_speed = cli.getAvifSpeed(args);
    if (cli_speed == -1) {cli_speed = config.getAvifSpeed(conf);}
    return cli_speed;
  }

  //Get the screenshot filename, either user-specified or generated
  private static String generateFilename(String[] args, Config conf, String image_format) {
    String argname = cli.getFilename(args, image_format);
    if (argname != null) {return argname;}

    String currentTime = LocalDate.now().toString();
    String directory = getDirectory(args, conf);
    String name =
      (misc.isWorkingDirectory(directory)) ? "AyaScreenshot-"+currentTime
      : directory + "AyaScreenshot-"+currentTime;
    int num = 0;
    String full = name + "-" + num + "." + image_format;

    while (new File(full).isFile()) {
      num++;
      full = name + "-" + num + "." + image_format;
    }
    return full;    
  }
  
  private static String getDirectory(String[] args, Config conf) {
    String dir = cli.getScreenshotDirectory(args);
    if (dir == null) {dir = config.getDirectory(conf);}
    if (dir == null) {
      stdout.print_verbose("No custom screenshot directory was specified, defaulting to working directory");
      return "";
    }
    String home = System.getProperty("user.home");
    if (dir.equals("~")) {
      stdout.print_verbose("Interpreting the provided path " + dir + " as " + home);
      return home;
    }
    
    if (dir.length() > 2 && dir.charAt(0) == '~' && dir.charAt(1) == '/') {
      String new_dir = dir.replaceFirst("~/", home);
      File new_dir_f = new File(new_dir);
      if (new_dir_f.isFile() && new_dir_f.canWrite()) {
        stdout.print_verbose("Interpreting the provided path " + dir + " as " + new_dir);
        return new_dir;
      }
    }

    dir = addDirSlash(dir);
    File f = new File(dir);
    String error_base = "\nDefaulting to current working directory";
    if (!f.isDirectory()) {
      stdout.error("The specified directory located at " + dir + " is not a real directory" + error_base);
      return "";
    }
    if (!f.canWrite()) {
      stdout.error("You lack the permission to write at the specified directory " + dir + error_base);
      return "";
    }
    return dir;
  }
  
  private static String addDirSlash(String dir) {
    if (dir.length() <= 1) {return dir;}
    char final_char = dir.charAt(dir.length()-1);
    if (final_char != '/' && final_char != '\\') {return dir + System.getProperty("file.separator");}
    return dir;
  }

  private static void runThreads(Thread[] threads) {
    for (Thread t : threads) {t.start();}
    try {
      for (Thread t : threads) {t.join();}
    }
    catch (InterruptedException e) {e.printStackTrace(); stdout.error("An error happened during thread execution while initializing Aya settings!");}
  }
}
