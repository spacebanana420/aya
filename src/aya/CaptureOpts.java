package aya;

import aya.config.config;
import aya.config.Config;
import aya.cli.cli;
import aya.ui.stdout;
import aya.wrapper.x11;

import java.util.ArrayList;
import java.io.File;
import java.time.LocalDate;

//Stores all kinds of program settings
//These settings are determined by the provided CLI arguments and the Aya config file
//CLI arguments take priority over the configuration file
public class CaptureOpts {
  public String format = "png";
  public int[] crop = new int[4];
  public byte quality = -1;
  public float scale = 0f;
  
  public byte avif_speed = 8;
  public boolean avif_fast = false;
  
  int delay = 0;
  boolean override_file = false;
  boolean open_image = false;
  ArrayList<String> image_viewer_cmd = null;

  String file_path = null;
  boolean save_file = false;
  boolean copy_to_clipboard = false;
  
  boolean window_select = false;
  boolean region_select = false;
  boolean capture_cursor = false;
  
  boolean wayland_mode = false;
  boolean tty_mode = false;

  //Initialize all variables from CLI arguments and config file
  //Cancel if Aya wasn't instructed to save the screenshot file or copy to clipboard
  CaptureOpts(String[] args, Config conf) {
    this.copy_to_clipboard = cli.hasArgument(args, "-clip");
    this.save_file = cli.hasArgument(args, "-file");
    if (!this.save_file && !this.copy_to_clipboard) return;
    
    Thread[] threads = new Thread[3];
    threads[0] = new Thread(() -> {
      this.override_file = cli.hasArgument(args, "-y") || config.overrideFile(conf);
      this.capture_cursor = cli.hasArgument(args, "-c") || config.captureCursor(conf);
      this.open_image = cli.hasArgument(args, "-open");
    });

    threads[1] = new Thread(() -> {
      String waylandEnv = System.getenv("XDG_BACKEND");
      this.tty_mode = cli.hasArgument(args, "-tty");
      this.wayland_mode = (waylandEnv != null && waylandEnv.equals("wayland")) || cli.hasArgument(args, "-wayland") || config.waylandModeEnabled(conf);
      this.wayland_mode = this.wayland_mode && !this.tty_mode; //TTY mode takes priority if selected
      this.window_select = cli.hasArgument(args, "-window");
      this.region_select = !this.window_select && cli.hasArgument(args, "-region");
      this.crop = getCrop(args, this.window_select, this.wayland_mode);
    });

    threads[2] = new Thread(() -> {
      this.scale = getScale(args);
      this.format = getFormat(args, conf);
      this.quality = getQuality(args, conf);
      this.delay = getDelay(args, conf);
      this.file_path = generateFilename(cli.getFilePath(args), this.format);
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
      if (value >= 0) crop_coords[i] = value;
    }

    if (window_select) {
      if (wayland_enabled) {
        stdout.error("Window capture (-window) is not supported in Wayland mode!");
        return crop_coords;
      }
      int[] window_coords = x11.getWindowCoordinates();
      if (window_coords != null) crop_coords = window_coords;
    }
    return crop_coords;
  }

  private static float getScale(String[] args) {
    float value = cli.getScreenshotScale(args);
    if (value < 0) value = 0;
    return value;
  }
  
  private static String getFormat(String[] args, Config conf) {
    String value = getFormat_cli(args);
    if (value != null) return value;

    value = getFormat_config(conf);
    if (value != null) return value;
    return "png";
  }

  private static String getFormat_cli(String[] args) {
    String value = cli.getArgValue(args, "-f");
    if (value == null) return null;
    value = value.toLowerCase();

    if (supportedFormat(value)) return value;
    stdout.error("Ignored specified image format " + value + " found in CLI arguments for being invalid");
    return null;
  }
  private static String getFormat_config(Config conf) {
    String value = config.getFormat(conf);
    if (value == null) return null;
    if (supportedFormat(value)) return value;
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
    if (value == -1) value = config.getQuality(conf);
    return value;
  }

  private static int getDelay(String[] args, Config conf) {
    int value = cli.getScreenshotDelay(args);
    if (value == -1) value = config.getDelay(conf);
    return value;
  }
  
  private static byte getAvifSpeed(String[] args, Config conf) {
    byte cli_speed = cli.getAvifSpeed(args);
    if (cli_speed == -1) cli_speed = config.getAvifSpeed(conf);
    return cli_speed;
  }

  //Get the screenshot file name and path, either user-specified or generated, containing a path optionally
  private static String generateFilename(String fileName, String imageFormat) {
    String directory = ""; //By default it's the working directory
    String currentTime = LocalDate.now().toString();
    
    if (fileName != null && fileName.length() > 0) {
      File f = new File(fileName);
      if (f.isDirectory()) directory = convertDirectory(fileName); //User provided a directory but no filename
      else if (misc.hasExtension(fileName, imageFormat)) return fileName; //User provided a filename, with or without path
      else stdout.error("The given filename "+fileName+" does not have the file extension for the format "+imageFormat+", ignoring");
    }
    if (directory.length() == 0) stdout.print_verbose("No custom screenshot directory was specified, defaulting to working directory");
    stdout.print_verbose("No valid filename was found, generating screenshot filename");

    //Automatic filename generation
    String name = directory + "AyaScreenshot-" + currentTime;
    int num = 0;
    String full = name + "-" + num + "." + imageFormat;

    //If a screenshot with the generated name already exists, then increment the number
    while (new File(full).isFile()) {
      num++;
      full = name + "-" + num + "." + imageFormat;
    }
    return full;    
  }

  //Converts a path to a full path and makes sure it ends with a slash
  //If the path is null (not specified by user), then just return an empty string
  private static String convertDirectory(String dir) {
    if (dir.length() == 0) return "";
    if (dir.equals("..")) return ""; //Too ambiguous, better not accept this as a valid relative path
    if (dir.length() == 1 && dir.charAt(0) == '/') return dir;
    
    String home = System.getProperty("user.home");
    if (dir.equals("~")) { //Home directory
      stdout.print_verbose("Interpreting the provided path " + dir + " as " + home);
      return home;
    }

    //Paths can start with ~ character to represent home as their starting point
    if (dir.length() > 2 && dir.charAt(0) == '~' && dir.charAt(1) == '/') {
      String new_dir = dir.replaceFirst("~/", home);
      File new_dir_f = new File(new_dir);
      if (new_dir_f.isFile() && new_dir_f.canWrite()) {
        stdout.print_verbose("Interpreting the provided path " + dir + " as " + new_dir);
        return new_dir;
      }
    }

    File f = new File(dir);
    String error_base = "\nDefaulting to current working directory";
    if (!f.isDirectory()) {
      stdout.error("The specified directory located at " + dir + " is not a real directory" + error_base);
      return null;
    }
    if (!f.canWrite()) {
      stdout.error("You lack the permission to write at the specified directory " + dir + error_base);
      return null;
    }
    
    String fullPath = f.getAbsolutePath();
    if (fullPath.charAt(fullPath.length()-1) != '/') return fullPath + '/'; //Directory will always end in a slash
    return fullPath;
  }

  private static void runThreads(Thread[] threads) {
    for (Thread t : threads) {t.start();}
    try {for (Thread t : threads) {t.join();}}
    catch (InterruptedException e) {e.printStackTrace(); stdout.error("An error happened during thread execution while initializing Aya settings!");}
  }
}
