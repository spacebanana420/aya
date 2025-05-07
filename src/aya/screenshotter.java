package aya;

import aya.wrapper.ffmpeg;
import aya.wrapper.magick;
import aya.wrapper.process;
import aya.wrapper.xwininfo;

import aya.cli.parser;
import aya.config.config;
import aya.config.Setting;

import java.util.ArrayList;
import java.io.File;
import java.time.LocalDate;

public class screenshotter {
  public static int takeScreenshot(String[] args) {
    CaptureOpts opts = new CaptureOpts(args);
    
    if (!opts.override_file && new File(opts.filename).isFile()) {
      String message = "The file in path " + opts.filename + " already exists!\nOverride file? (y/N)";
      String answer = stdio.readInput(message).trim();
      if (!answer.equals("y") && !answer.equals("yes")) {return 0;}
    }
    
    if (opts.delay > 0) {
       stdio.print_verbose("Taking a screenshot in " + opts.delay + " seconds");
       misc.sleep(opts.delay * 1000);
    }
    
    stdio.print_verbose("Taking screenshot as file \"" + opts.filename + "\"");
    ArrayList<String> cmd = opts.mkCommand();
    int result = process.run(cmd, false);
    switch (result) {
      case 0:
        stdio.print("Screenshot saved successfully!");
        break;
      case -1:
        String process_name = (opts.use_magick) ? "ImageMagick" : "FFmpeg";
        stdio.print("Aya failed to take a screenshot! " + process_name + " not found in your system!");
        break;
      case -2:
        stdio.print("Aya's process was interrupted while taking a screenshot!");
        break;
      default:
        stdio.print("Unknown error happened when taking a screenshot!\nMake sure you are running an x11-based graphical environment!");
    }
    if (result != 0) {return 1;}
    
    if (opts.open_image) {
      if (opts.image_viewer_cmd == null) {
        stdio.print("Error opening screenshot, image viewer command is missing!");
        return 2;
      }
      result = process.run(opts.image_viewer_cmd, true);
      if (result != 0) {
        stdio.print("Error opening screenshot, command is invalid or program is not present in system!");
        return 3;
      }
    }
    return 0;
  }
}

class CaptureOpts {
  int[] crop = new int[4];
  String format = "png";
  byte quality = -1;
  float scale = 0f;
  byte avif_speed = 8;

  boolean use_magick = false;
  boolean override_file = false;
  String filename = "";
  int delay = 0;
  boolean open_image = false;
  ArrayList<String> image_viewer_cmd = null;
  
  private String ffmpeg_path = "ffmpeg";
  private String magick_path = "magick";
  
  private boolean window_select = false;
  private boolean region_select = false;
  
  CaptureOpts(String[] args) {
    //Config reading comes first, so CLI arguments override respective settings
    Setting[] conf = config.openConfig();
    
    use_magick = config.useMagick(conf) || parser.hasArgument(args, "-magick");
    ffmpeg_path = config.getFFmpegPath(conf);
    magick_path = config.getMagickPath(conf);
       
    setCrop(args);
    setScale(args);
    setFormat(args, conf, use_magick);
    setQuality(args, conf);
    setDelay(args, conf);
    setOverrideFile(args, conf);
    setAvifSpeed(args, conf);
    setRegionSelect(args);
    
    filename = generateFilename(args, conf);
    open_image = parser.hasArgument(args, "-open");
    image_viewer_cmd = config.getImageViewer(conf, filename);
  }

  ArrayList<String> mkCommand() {
    var args = new ArrayList<String>();
    if (use_magick) {
      args.add(magick_path);
      args.addAll(magick.getCaptureArgs());
      if (format.equals("png")) {
        args.addAll(magick.encodeArgs_png(quality));
      }
      else {args.addAll(ffmpeg.encodeArgs_jpg(quality));}
      args.addAll(magick.cropArgs(crop[0], crop[1], crop[2], crop[3]));
      args.addAll(magick.scaleArgs(scale));
    }
    else {
      args.add(ffmpeg_path);
      args.addAll(ffmpeg.getCaptureArgs(region_select));
      if (format.equals("png")) {
        args.addAll(ffmpeg.encodeArgs_png(quality));
      }
      else if (format.equals("avif")) {
        args.addAll(ffmpeg.encodeArgs_avif(quality, avif_speed));
      }
      else if (format.equals("bmp")) {
        args.addAll(ffmpeg.encodeArgs_bmp());
      }
      else {args.addAll(ffmpeg.encodeArgs_jpg(quality));}
      String arg_crop = ffmpeg.cropArgs(crop[0], crop[1], crop[2], crop[3]);
      String arg_scale = ffmpeg.scaleArgs(scale);
      args.addAll(ffmpeg.assembleFilters(arg_crop, arg_scale));
    }
    
    args.add(filename);
    return args;
  }

  private void setCrop(String[] args) {
    String[] opts = new String[]{"-width", "-height", "-x", "-y"};
    for (int i = 0; i < opts.length; i++) {
      int value = parser.getArgInt(args, opts[i]);
      if (value >= 0) {crop[i] = value;}
    }

    if (parser.hasArgument(args, "-window")) {
      window_select = true;
      int[] window_coords = xwininfo.getWindowCoordinates();
      if (window_coords != null) {crop = window_coords;}
    }
  }

  private void setScale(String[] args) {
    float value = parser.getArgFloat(args, "-s");
    if (value > 0.0) {scale = value;}
  }
  
  private void setFormat(String[] args, Setting[] conf, boolean image_magick) {
    String value = parser.getArgValue(args, "-f");
    if (value == null) {return;}
    
    value = value.toLowerCase();
    if (!config.unsupportedFormat(value, image_magick)) {format = value;}
    else {
      stdio.print("Ignored specified image format " + value + " for being invalid\nDefaulting to " + format);
      format = config.getFormat(conf, image_magick);
    }
  }

  private void setQuality(String[] args, Setting[] conf) {
    byte value = parser.getArgByte(args, "-q");
    if (value >= 0) {quality = value;}
    else {quality = config.getQuality(conf);}
  }

  private void setDelay(String[] args, Setting[] conf) {
    int value = parser.getArgInt(args, "-t");
    if (value > 0) {delay = value;}
    else {delay = config.getDelay(conf);}
  }

  private void setRegionSelect(String[] args) {
    if (parser.hasArgument(args, "-region") && !window_select) {region_select = true;}
  }
  
  private void setOverrideFile(String[] args, Setting[] conf) {
    boolean result = parser.hasArgument(args, "-y");
    if (result) {override_file = result;}
    else {override_file = config.overrideFile(conf);}
  }
  
  private void setAvifSpeed(String[] args, Setting[] conf) {
    byte conf_speed = config.getAvifSpeed(conf);
    byte cli_speed = parser.getArgByte(args, "-avif-speed");
    if (cli_speed >= 0 && cli_speed <= 8) {avif_speed = cli_speed;}
    else {avif_speed = conf_speed;}
  }

  private String generateFilename(String[] args, Setting[] conf) {
    String argname = parser.getFilename(args, format);
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
    
    String value = parser.getArgValue(args, "-d");
    if (value == null || value.length() == 0) {return config_directory;}
    if (value.equals("~")) {return System.getProperty("user.home");}
    
    File f = new File(value);
    if (!f.isDirectory()) {
      stdio.print("The specified directory located at " + value + " is not a real directory\nDefaulting to current working directory or config-specified directory");
      return config_directory;
    }
    if (!f.canWrite()) {
      stdio.print("You lack the permission to write at the specified directory " + value + "\nDefaulting to current working directory or config-specified directory");
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
