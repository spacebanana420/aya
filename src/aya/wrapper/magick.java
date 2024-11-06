package aya.wrapper;

import java.util.ArrayList;

public class magick {
  public static ArrayList<String> getCaptureArgs() {
    return process.mkList(new String[]{"magick", "import", "-window", "root"});
  }

  public static ArrayList<String> encodeArgs_png(byte quality) {
    byte q_arg = (quality >= 1 && quality <= 100) ? quality : 75;
    var list = new ArrayList<String>();
    list.add("-quality"); list.add(""+q_arg); //""+q_arg seems bad
    return list;
  }

  public static ArrayList<String> encodeArgs_jpg(byte quality) {
    var list = new ArrayList<String>();
    byte quality_filtered = (quality >= 1 && quality <= 100) ? quality : 92;
    list.add("-quality"); list.add(""+quality_filtered);
    return list;
  }

  public static ArrayList<String> cropArgs(int w, int h, int x, int y) {
    var list = new ArrayList<String>();
    if (w <= 0 || h <= 0) {return list;}
    int filtered_x = (x >= 0) ? x : 0;
    int filtered_y = (y >= 0) ? y : 0;
    list.add("-crop");
    list.add(x+"x"+y+"+"+filtered_x+"+"+filtered_y);
    return list;
  }

  public static ArrayList<String> scaleArgs(float factor) {
    var list = new ArrayList<String>();
    if (factor <= 0) {return list;}
    String percentage = "" + (factor * 10) + "%";
    list.add("-resize"); list.add(percentage);
    return list; 
  }
}
