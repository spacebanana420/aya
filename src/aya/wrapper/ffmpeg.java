package aya.wrapper;

import java.util.ArrayList;

public class ffmpeg {
  public static ArrayList<String> getCaptureArgs() {
    return process.mkList(new String[]{"-loglevel", "quiet", "-y", "-f", "x11grab", "-i", ":0.0", "-frames:v", "1"});
  }

  public static ArrayList<String> encodeArgs_png(byte quality) {
    String[] qualities = new String[]{"none", "sub", "up", "avg", "paeth", "mixed"};
    String q_arg = null;
    if (quality < 0 || quality > 5) {q_arg = "mixed";}
    else {q_arg = qualities[quality];}

    var list = new ArrayList<String>();
    list.add("-c:v"); list.add("png");
    list.add("-pred"); list.add(q_arg);
    return list;
  }

  //improve later
  public static ArrayList<String> cropArgs(int w, int h, int x, int y) {
    var list = new ArrayList<String>();
    if (w <= 0 || h <= 0) {return list;}
  
    String dimensions = w + ":" + h;
    String offset = "";
    if (x >= 0 && y >= 0) {
      offset=":"+x+":"+y;
    }
    list.add("crop="+dimensions+offset);
    return list;
  }

  public static ArrayList<String> scaleArgs(float factor) {
    var list = new ArrayList<String>();
    if (factor <= 0) {return list;}
    list.add("scale=iw*"+factor+":ih*"+factor);
    return list;
  }
}
