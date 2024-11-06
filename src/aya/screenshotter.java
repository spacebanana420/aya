package aya;

import aya.wrapper.ffmpeg;
import aya.wrapper.magick;
import aya.wrapper.process;
import aya.cli.parser;
import java.util.ArrayList;
import java.io.File;

public class screenshotter {
  public static int takeScreenshot(String[] args) {
    var opts = new ssoptions();
    opts.setOpts(args);
    var cmd = opts.mkCommand();
    
    if (opts.delay > 0) {misc.sleep(opts.delay);}
    return process.run(cmd);
  }
}

class ssoptions {
  public int[] crop = new int[4];
  public String format = "png";
  public byte quality = -1;
  public float scale = 0f;

  public boolean use_magick = false;
  public String directory = "";
  public String filename = "";
  public int delay = 0;
  
  public void setOpts(String[] args) {
    use_magick = parser.hasArgument(args, "-magick");
    setCrop(args);
    setScale(args);
    setFormat(args);
    setQuality(args);
    setDelay(args);
    setDirectory(args);
    filename = generateFilename();
  }

  public ArrayList<String> mkCommand() {
    var args = new ArrayList<String>();
    if (use_magick) {
      args.addAll(magick.getCaptureArgs());
      if (format.equals("png")) {
        args.addAll(magick.encodeArgs_png(quality));
      }
      args.addAll(magick.cropArgs(crop[0], crop[1], crop[2], crop[3]));
      args.addAll(magick.scaleArgs(scale));
    }
    else {
      args.add("ffmpeg"); args.addAll(ffmpeg.getCaptureArgs());
      if (format.equals("png")) {
        args.addAll(ffmpeg.encodeArgs_png(quality));
      }
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
  }

  private void setScale(String[] args) {
    float value = parser.getArgFloat(args, "-s");
    if (value > 0.0) {scale = value;}
  }
  
  private void setFormat(String[] args) {
    String value = parser.getArgValue(args, "-f");
    if (value == null) {return;}
    value = value.toLowerCase();
    if (value.equals("png") || value.equals("jpg")) {
      format = value;
    }
  }

  private void setQuality(String[] args) {
    byte value = parser.getArgByte(args, "-q");
    boolean pngvalue = value >= 0 && value <= 5; //unnecessary since wrapper assumes a default value
    boolean jpgvalue = value > 0 && value <= 100;
    if ((format.equals("png") && pngvalue) || (format.equals("jpg") && jpgvalue)) {
      quality = value;
    }
  }

  private void setDelay(String[] args) {
    int value = parser.getArgInt(args, "-t");
    if (value > 0) {delay = value;}
  }
  //something is setting it to null
  private void setDirectory(String[] args) {
    String value = parser.getArgValue(args, "-d");
    if (value == null || !new File(value).isDirectory() || value.length() == 0) {return;}

    char final_char = value.charAt(value.length()-1);
    if (final_char != '/' && final_char != '\\') {value += System.getProperty("file.separator");}
    directory = value;
  }

  private String generateFilename() {
    String name =
      (misc.isWorkingDirectory(directory)) ? "Aya-screenshot"
      : directory + "Aya-screenshot";
    int num = 0;
    String full = name + "-" + num + "." + format;

    while (new File(full).isFile()) {
      num++;
      full = name + "-" + num + "." + format;
    }
    return full;    
  }
}
