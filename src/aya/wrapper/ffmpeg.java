package aya.wrapper;

import java.util.ArrayList;

public class ffmpeg {
  public static ArrayList<String> getCaptureArgs(boolean select_region) {
    var base_list = process.mkList(new String[]{"-loglevel", "quiet", "-y", "-f", "x11grab", "-draw_mouse", "0"});
    if (select_region) {base_list.add("-select_region"); base_list.add("1");}
    
    var final_list = process.mkList(new String[]{"-i", ":0.0", "-frames:v", "1"});
    base_list.addAll(final_list);

    return base_list;
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
  
  public static ArrayList<String> encodeArgs_bmp() {
    var list = new ArrayList<String>();
    list.add("-c:v"); list.add("bmp");
    return list;
  }

  public static ArrayList<String> encodeArgs_jpg(byte quality) {
    var list = new ArrayList<String>();
    byte quality_filtered = (quality >= 1 && quality <= 100) ? (byte)(101 - quality) : 1; //101-quality reverses the quality value so that 100 is highest quality and 1 is lowest
    list.add("-qmin"); list.add("1");
    list.add("-qmax"); list.add("100");
    list.add("-q:v"); list.add(""+quality_filtered);
    return list;
  }

  public static ArrayList<String> encodeArgs_avif(byte quality) {
    var list = process.mkList(new String[]{"-c:v", "libaom-av1", "-still-picture", "true", "-cpu-used", "8", "-row-mt", "true"});
    byte quality_filtered = (quality >= 0 && quality <= 63) ? quality : 0;
    list.add("-crf"); list.add(""+quality_filtered);
    return list;
  }

  public static String cropArgs(int w, int h, int x, int y) {
    if (w <= 0 && h <= 0) {return "";}
   
    String[] args = new String[4];
    args[0] = (w > 0) ? "w="+w : "";
    args[1] = (h > 0) ? "h="+h : "";
    args[2] = (x > 0) ? "x="+x : "";
    args[3] = (y > 0) ? "y="+y : "";
    
    String full = "crop=";
    boolean first = true;
    for (int i = 0; i < args.length; i++) {
      if (!args[i].equals("")) {
        if (first) {full +=args[i]; first = false;}
        else {full += ":"+args[i];}
      }
    }
    return full;
  }

  public static String scaleArgs(float factor) {
    if (factor <= 0) {return "";}
    return "scale=iw*"+factor+":ih*"+factor;
  }

  public static ArrayList<String> assembleFilters(String... filters) {
    var list = new ArrayList<String>();
    
    boolean hasFilters = false;
    for (String f : filters) {if (f.length() > 0) {hasFilters = true; break;}}
    if (!hasFilters) {return list;}

    list.add("-filter:v");
    String arg = filters[0]; //First ffmpeg filter does not have comma
    for (int i = 1; i < filters.length; i++) {
      if (filters[i].length() == 0) {continue;}
      arg+=","+filters[i];
    }
    list.add(arg);
    return list;
  }
}
