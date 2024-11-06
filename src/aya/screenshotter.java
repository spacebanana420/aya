package aya;

import aya.wrapper.ffmpeg;
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
  public boolean crop_enabled = false;
  public String format = "png";
  public byte quality = -1;
  public float scale = 0f;

  public boolean use_magick = false;
  public String filename = generateFilename();
  public int delay = 0;
  
  public void setOpts(String[] args) {
    setCrop(args);
    setScale(args);
    setFormat(args);
    setQuality(args);
    setDelay(args);
  }

  public ArrayList<String> mkCommand() {
    var args = new ArrayList<String>();
    if (use_magick) {
      args.add("magick"); args.add("import");
    }
    else {
      args.add("ffmpeg"); args.addAll(ffmpeg.getCaptureArgs());
      if (format == "png") {
        args.addAll(ffmpeg.encodeArgs_png(quality));
      }
    }
    
    args.add(filename);
    return args;
  }

  private void setCrop(String[] args) {
    String[] opts = new String[]{"-w", "-h", "-x", "-y"};
    for (int i = 0; i < opts.length; i++) {
      int value = parser.getArgInt(args, opts[i]);
      if (value >= 0) {crop[i] = value;}
    }
    if (crop[0] > 0 && crop[1] > 0 && crop[2] >= 0 && crop[3] >= 0) {crop_enabled = true;}
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

  // private void setFilename(String[] args) {
  //   String path = parser.getArgValue(args, "-o");
  //   if (path == null) {return;}
  //   filename = path;
  //   //finish
  // }

  private String generateFilename() {
    String name = "Aya-screenshot";
    int num = 0;
    String full = name + "-" + num + "." + format;

    while (new File(full).isFile()) {
      num++;
      full = name + "-" + num + "." + format;
    }
    return full;    
  }
}
