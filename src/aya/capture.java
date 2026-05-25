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
  
  public static boolean takeScreenshot(CaptureOpts opts, boolean supportsTTY) {
    if (opts.tty_mode) stdout.print_debug("Running in TTY mode");
    else if (opts.wayland_mode) stdout.print_debug("Running in Wayland mode");
    else stdout.print_debug("Running in X11 mode");
     
    if (opts.save_file && !opts.override_file && new File(opts.file_path).isFile()) {
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
    if (opts.wayland_mode) result = wayland_takeScreenshot(opts);
    //TTY mode (Linux)
    else if (opts.tty_mode) {
      if (!supportsTTY) {
        stdout.error("TTY screenshot is only supported on Linux!");
        return false;
      }
      if (!opts.save_file) {
        stdout.error("TTY screenshot mode does not support saving to clipboard!");
        return false;
      }
      result = tty_takeScreenshot(opts);
    }
    //X11 mode
    else result = opts.save_file ? x11_takeScreenshot_file(opts) : x11_takeScreenshot_clip(opts);
    if (!result) return false;
    if (!opts.save_file || !opts.open_image) return true;
    if (opts.tty_mode) {
      stdout.print("Image view is not supported in TTY mode, skipping.");
      return true;
    }

    //Optionally open the image only if a file was successfully saved
    if (opts.image_viewer_cmd == null) {
      stdout.error("Error opening screenshot, image viewer command is missing!");
      return false;
    }
    Process p = process.runProcess(opts.image_viewer_cmd);
    process.awaitCompletion(p, opts.image_viewer_cmd.get(0));
    if (!process.succeeded(p)) {
      stdout.error("Error opening screenshot, command is invalid or program is not present in system!");
      return false;
    }
    return true;
  }

  //x11, FFmpeg both takes the screenshot and encodes it
  private static boolean x11_takeScreenshot_file(CaptureOpts opts) {
    var cmd = new ArrayList<String>();
    cmd.add(opts.ffmpeg_path);
    cmd.addAll(ffmpeg.getCaptureArgs(opts.region_select, opts.capture_cursor));
    cmd.addAll(ffmpeg_extraArgs(opts));
    cmd.addAll(ffmpeg_filterArgs(opts));
    cmd.add(opts.file_path);

    Process p = process.runProcess(cmd);
    process.awaitCompletion(p, "FFmpeg");
    boolean result = process.succeeded(p);
    if (!result) {
      stdout.print(fileFailed);
      return false;
    }
    stdout.print(fileSuccess);
    if (opts.copy_to_clipboard) {
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

    byte[] image_data = process.readStdout(process.runProcess(cmd));
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
  private static boolean wayland_takeScreenshot(CaptureOpts opts) {
    byte[] picture = wayland.captureScreen(opts.region_select, opts.capture_cursor, opts.copy_to_clipboard);
    if (picture == null) return false;

    if (opts.copy_to_clipboard) {
      boolean result = wayland.copyToClipboard(picture);
      if (result) stdout.print(clipSuccess);
      else stdout.print(clipFailed);
    }

    if (!opts.save_file) return true;
    var cmd = new ArrayList<String>();
    cmd.add(opts.ffmpeg_path);
    cmd.addAll(ffmpeg.getWaylandArgs());
    cmd.addAll(ffmpeg_extraArgs(opts));
    cmd.addAll(ffmpeg_filterArgs(opts));    
    cmd.add(opts.file_path);

    Process p = process.runProcess(cmd);
    process.writeToStdin(p, picture);
    process.awaitCompletion(p, "FFmpeg");
    boolean result = process.succeeded(p);

    if (result) stdout.print(fileSuccess);
    else stdout.print(fileFailed);
    return result;
  }

  //Captures a screenshot of the TTY framebuffer, Linux-only
  private static boolean tty_takeScreenshot(CaptureOpts opts) {
    var cmd = new ArrayList<String>();
    cmd.add(opts.ffmpeg_path);
    cmd.addAll(ffmpeg.getFramebufferArgs());
    cmd.addAll(ffmpeg_extraArgs(opts));
    cmd.addAll(ffmpeg_filterArgs(opts));    
    cmd.add(opts.file_path);

    Process p = process.runProcess(cmd);
    process.awaitCompletion(p, "FFmpeg");
    boolean result = process.succeeded(p);

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
