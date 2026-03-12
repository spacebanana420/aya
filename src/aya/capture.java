package aya;

import aya.wrapper.*;
import aya.ui.stdout;
import aya.config.Config;

import java.util.ArrayList;
import java.io.File;

//Handles the high-level logic for taking a screenshot and saving it
//Implements the lower-level wrappers, CLI/config parsing, etc
public class capture {

  //Common status/error messages that are used in multiple places here
  private static final String fileSuccess = "Screenshot saved successfully";
  private static final String fileFailed = "Failed to take screenshot";
  private static final String clipSuccess = "Screenshot copied to clipboard";
  private static final String clipFailed = "Failed to copy screenshot to clipboard";
  
  public static boolean takeScreenshot(String[] args, Config conf, boolean clipboard_copy, boolean file_save) {
    CaptureOpts opts = new CaptureOpts(args, conf);
    stdout.print_debug("Running for the graphical backend " + (opts.wayland_mode ? "\"Wayland\"" : "\"X11\""));
    
    if (file_save && !opts.override_file && new File(opts.file_path).isFile()) {
      boolean answer = stdout.promptQuestion("The file in path " + opts.file_path + " already exists!\nOverride file? (y/N)");
      if (!answer) return true;
    }
    
    if (opts.delay > 0) {
       stdout.print("Taking a screenshot in " + opts.delay + " seconds");
       misc.sleep(opts.delay * 1000);
    }
    stdout.print_verbose("Taking screenshot as file \"" + opts.file_path + "\"");
    boolean result;
    //Wayland mode
    if (opts.wayland_mode) result = wayland_takeScreenshot(opts, clipboard_copy, file_save);
    //X11 mode
    else {
      if (file_save) result = x11_takeScreenshot_file(opts, clipboard_copy);
      else result = x11_takeScreenshot_clip(opts);
    }
    if (!result) return false;
    if (!file_save) return true;

    //Optionally open the image only if a file was successfully saved
    if (opts.open_image) {
      if (opts.image_viewer_cmd == null) {
        stdout.error("Error opening screenshot, image viewer command is missing!");
        return false;
      }
      result = process.run(opts.image_viewer_cmd, true);
      if (!result) {
        stdout.error("Error opening screenshot, command is invalid or program is not present in system!");
        return false;
      }
    }
    return true;
  }

  //x11, FFmpeg both takes the screenshot and encodes it
  private static boolean x11_takeScreenshot_file(CaptureOpts opts, boolean clipboard) {
    var cmd = new ArrayList<String>();
    cmd.add(opts.ffmpeg_path);
    cmd.addAll(ffmpeg.getCaptureArgs(opts.region_select, opts.capture_cursor));
    cmd.addAll(ffmpeg_extraArgs(opts));
    cmd.addAll(ffmpeg_filterArgs(opts));
    cmd.add(opts.file_path);
    
    boolean result = process.run(cmd, false);
    if (!result) {
      stdout.print(fileFailed);
      return false;
    }
    stdout.print(fileSuccess);
    if (clipboard) {
      result = x11.xclip_copyToClipboard(new File(opts.file_path).getAbsolutePath());
      if (result) stdout.print(clipSuccess);
      else stdout.print(clipFailed);
    }
    return result;
  }

  //x11, FFmpeg takes the screenshot and writes it to stdout, xclip is used to copy to clipboard
  //In clipboard-only mode, the screenshot is encoded as PNG regardless of aya's settings
  private static boolean x11_takeScreenshot_clip(CaptureOpts opts) {
    var cmd = new ArrayList<String>();
    cmd.add(opts.ffmpeg_path);
    cmd.addAll(ffmpeg.getCaptureArgs(opts.region_select, opts.capture_cursor));
    cmd.addAll(ffmpeg.encodeArgs_png((byte)5));
    cmd.addAll(ffmpeg_filterArgs(opts));
    cmd.add("-f"); cmd.add("image2");
    cmd.add("-");
    
    byte[] image_data = process.run_stdout(new ProcessBuilder(cmd));
    if (image_data == null) {
      stdout.error("No screenshot data was retrieved, cannot copy to clipboard!");
      return false;
    }
    boolean result = x11.xclip_copyToClipboard(image_data);
    if (result) stdout.print(clipSuccess);
    else stdout.print(clipFailed);
    return result;
  }  
  
  //For Wayland, Grim takes the screenshot and FFmpeg only encodes it for feature parity
  //Clipboard support uses wl-copy, in clipboard mode the image is PNG and slightly compressed
  private static boolean wayland_takeScreenshot(CaptureOpts opts, boolean clipboard, boolean savefile) {
    byte[] picture = wayland.captureScreen(opts.region_select, opts.capture_cursor, clipboard);
    if (picture == null) return false;

    if (clipboard) {
      boolean result = wayland.copyToClipboard(picture);
      if (result) stdout.print(clipSuccess);
      else stdout.print(clipFailed);
    }

    if (!savefile) return true;
    var cmd = new ArrayList<String>();
    cmd.add(opts.ffmpeg_path);
    cmd.addAll(ffmpeg.getWaylandArgs());
    cmd.addAll(ffmpeg_extraArgs(opts));
    cmd.addAll(ffmpeg_filterArgs(opts));    
    cmd.add(opts.file_path);
    boolean result = process.run_stdin(cmd, picture);
    if (result) stdout.print(fileSuccess);
    else stdout.print(fileFailed);
    return result;
  }

  private static ArrayList<String> ffmpeg_filterArgs(CaptureOpts opts) {
    String arg_crop = ffmpeg.cropArgs(opts.crop[0], opts.crop[1], opts.crop[2], opts.crop[3]);
    String arg_scale = ffmpeg.scaleArgs(opts.scale);
    return ffmpeg.assembleFilters(arg_crop, arg_scale);
  }

  private static ArrayList<String> ffmpeg_extraArgs(CaptureOpts opts) {
    switch(opts.format) {
      case "png":
        return ffmpeg.encodeArgs_png(opts.quality);
      case "avif":
        return opts.avif_fast
          ? ffmpeg.encodeArgs_avif(opts.quality)
          : ffmpeg.encodeArgs_avif(opts.quality, opts.avif_speed);
      case "bmp":
        return ffmpeg.encodeArgs_bmp();
      default:
        return ffmpeg.encodeArgs_jpg(opts.quality);
    }
  }
}
