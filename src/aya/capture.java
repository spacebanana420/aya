package aya;

import aya.wrapper.ffmpeg;
import aya.wrapper.process;
import aya.wrapper.xwininfo;
import aya.wrapper.wayland;

import aya.cli.cli;
import aya.ui.stdout;
import aya.config.config;
import aya.config.Setting;

import java.util.ArrayList;
import java.io.File;
import java.time.LocalDate;

public class capture {
  public static int takeScreenshot(String[] args, Setting[] conf) {
    CaptureOpts opts = new CaptureOpts(args, conf);
    
    if (!opts.override_file && new File(opts.filename).isFile()) {
      String message = "The file in path " + opts.filename + " already exists!\nOverride file? (y/N)";
      String answer = stdout.readInput(message).trim();
      if (!answer.equals("y") && !answer.equals("yes")) {return 0;}
    }
    
    if (opts.delay > 0) {
       stdout.print("Taking a screenshot in " + opts.delay + " seconds");
       misc.sleep(opts.delay * 1000);
    }

    //Take the screenshot and save it, x11 or Wayland
    stdout.print_verbose("Taking screenshot as file \"" + opts.filename + "\"");
    int result = opts.wayland_mode ? wayland_takeScreenshot(opts) : x11_takeScreenshot(opts);

    switch (result) {
      case 0:
        stdout.print("Screenshot saved at path " + opts.filename);
        break;
      case -1:
        stdout.error("Aya failed to take a screenshot! FFmpeg was not found in your system! You need to install FFmpeg to use Aya");
        break;
      case -2:
        stdout.error("Aya's process was interrupted while taking a screenshot!");
        break;
      default:
        stdout.error("Unknown error happened when taking a screenshot!\nMake sure you are running a graphical environment appropriate to your configuration (X11/Wayland)!");
    }
    if (result != 0) {return 1;}
    
    if (opts.open_image) {
      if (opts.image_viewer_cmd == null) {
        stdout.error("Error opening screenshot, image viewer command is missing!");
        return 2;
      }
      result = process.run(opts.image_viewer_cmd, true);
      if (result != 0) {
        stdout.error("Error opening screenshot, command is invalid or program is not present in system!");
        return 3;
      }
    }
    return 0;
  }

  //For x11, FFmpeg both takes the screenshot and encodes it
  private static int x11_takeScreenshot(CaptureOpts opts) {
    var cmd = new ArrayList<String>();
    cmd.add(opts.ffmpeg_path);
    cmd.addAll(ffmpeg.getCaptureArgs(opts.region_select, opts.capture_cursor));
    cmd.addAll(ffmpeg_extraArgs(opts));
    String arg_crop = ffmpeg.cropArgs(opts.crop[0], opts.crop[1], opts.crop[2], opts.crop[3]);
    String arg_scale = ffmpeg.scaleArgs(opts.scale);
    cmd.addAll(ffmpeg.assembleFilters(arg_crop, arg_scale));
    
    cmd.add(opts.filename);
    return process.run(cmd, false);
  }
  
  //For Wayland, Grim takes the screenshot and FFmpeg only encodes it for feature parity
  private static int wayland_takeScreenshot(CaptureOpts opts) {
    byte[] picture = wayland.captureScreen(opts.region_select, opts.capture_cursor);
    if (picture == null) {return 1;}
    
    var cmd = new ArrayList<String>();
    cmd.add(opts.ffmpeg_path);
    cmd.addAll(ffmpeg.getWaylandArgs());
    cmd.addAll(ffmpeg_extraArgs(opts));
    String arg_crop = ffmpeg.cropArgs(opts.crop[0], opts.crop[1], opts.crop[2], opts.crop[3]);
    String arg_scale = ffmpeg.scaleArgs(opts.scale);
    cmd.addAll(ffmpeg.assembleFilters(arg_crop, arg_scale));
    
    cmd.add(opts.filename);
    return process.run_stdin(cmd, picture);
  }

   private static ArrayList<String> ffmpeg_extraArgs(CaptureOpts opts) {
    switch(opts.format) {
      case "png":
        return ffmpeg.encodeArgs_png(opts.quality);
      case "avif":
        return ffmpeg.encodeArgs_avif(opts.quality, opts.avif_speed);
      case "bmp":
        return ffmpeg.encodeArgs_bmp();
      default:
        return ffmpeg.encodeArgs_jpg(opts.quality);
    }
  }
}

//Stores screenshot settings according to the provided CLI arguments and config file
class CaptureOpts {
  int[] crop = new int[4];
  String format = "png";
  byte quality = -1;
  float scale = 0f;
  byte avif_speed = 8;

  boolean override_file = false;
  String filename = "";
  int delay = 0;
  boolean open_image = false;
  ArrayList<String> image_viewer_cmd = null;
  
  String ffmpeg_path = null;
  
  boolean window_select = false;
  boolean region_select = false;
  boolean capture_cursor = false;
  boolean wayland_mode = false;

  CaptureOpts(String[] args, Setting[] conf) {
    this.wayland_mode = cli.hasArgument(args, "-wayland") || config.waylandModeEnabled(conf);
    this.override_file = cli.hasArgument(args, "-y") || config.overrideFile(conf);
    this.ffmpeg_path = config.getFFmpegPath(conf);
    this.capture_cursor = cli.hasArgument(args, "-c") || config.captureCursor(conf);
    
    setCrop(args);
    this.scale = getScale(args);
    this.format = getFormat(args, conf);
    this.quality = getQuality(args, conf);
    this.delay = getDelay(args, conf);
    this.region_select = !this.window_select && cli.hasArgument(args, "-region"); //window_select is defined earlier in setCrop()
    
    this.filename = generateFilename(args, conf);
    this.open_image = cli.hasArgument(args, "-open");
    if (this.open_image) {this.image_viewer_cmd = config.getImageViewer(conf, filename);}
    if (this.format.equals("avif")) {this.avif_speed = getAvifSpeed(args, conf);}
  }

  private void setCrop(String[] args) {
    String[] opts = new String[]{"-width", "-height", "-x", "-y"};
    for (int i = 0; i < opts.length; i++) {
      int value = cli.getArgInt(args, opts[i]);
      if (value >= 0) {this.crop[i] = value;}
    }

    if (cli.hasArgument(args, "-window") && !this.wayland_mode) {
      this.window_select = true;
      int[] window_coords = xwininfo.getWindowCoordinates();
      if (window_coords != null) {this.crop = window_coords;}
    }
  }

  private float getScale(String[] args) {
    float value = cli.getArgFloat(args, "-s");
    if (value < 0) {value = 0;}
    return value;
  }
  
  private String getFormat(String[] args, Setting[] conf) {
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
    stdout.print("Ignored specified image format " + value + " found in CLI arguments for being invalid");
    return null;
  }
  private static String getFormat_config(Setting[] conf) {
    String value = config.getFormat(conf);
    if (value == null) {return null;}
    if (supportedFormat(value)) {return value;}
    stdout.print("Ignored specified image format " + value + " found in aya configuration for being invalid");
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

  private byte getQuality(String[] args, Setting[] conf) {
    byte value = cli.getArgQuality(args, "-q");
    if (value >= 0) {return value;}
    else {return config.getQuality(conf);}
  }

  private int getDelay(String[] args, Setting[] conf) {
    int value = cli.getArgInt(args, "-t");
    if (value > 0) {return value;}
    else {return config.getDelay(conf);}
  }
  
  private byte getAvifSpeed(String[] args, Setting[] conf) {
    byte cli_speed = cli.getArgQuality(args, "-avif-speed");
    if (cli_speed >= 0 && cli_speed <= 8) {return cli_speed;}
    else {return config.getAvifSpeed(conf);}
  }

  //Get the screenshot filename, either user-specified or generated
  private String generateFilename(String[] args, Setting[] conf) {
    String argname = cli.getFilename(args, format);
    if (argname != null) {return argname;}

    String currentTime = LocalDate.now().toString();
    String directory = getDirectory(args, conf);
    String name =
      (misc.isWorkingDirectory(directory)) ? "AyaScreenshot-"+currentTime
      : directory + "AyaScreenshot-"+currentTime;
    int num = 0;
    String full = name + "-" + num + "." + format;

    while (new File(full).isFile()) {
      num++;
      full = name + "-" + num + "." + format;
    }
    return full;    
  }
  
  private static String getDirectory(String[] args, Setting[] conf) {
    String config_directory = config.getDirectory(conf);
    config_directory = addDirSlash(config_directory);
    
    String value = cli.getArgValue(args, "-d");
    if (value == null || value.isEmpty()) {return config_directory;}
    if (value.equals("~")) {return System.getProperty("user.home");}
    
    File f = new File(value);
    String error_base = "\nDefaulting to current working directory or config-specified directory";
    if (!f.isDirectory()) {
      stdout.error("The specified directory located at " + value + " is not a real directory" + error_base);
      return config_directory;
    }
    if (!f.canWrite()) {
      stdout.error("You lack the permission to write at the specified directory " + value + error_base);
      return config_directory;
    }
    
    value = addDirSlash(value);
    return value;
  }
  
  private static String addDirSlash(String dir) {
    if (dir.length() <= 1) {return dir;}
    char final_char = dir.charAt(dir.length()-1);
    if (final_char != '/' && final_char != '\\') {return dir + System.getProperty("file.separator");}
    return dir;
  }
}
