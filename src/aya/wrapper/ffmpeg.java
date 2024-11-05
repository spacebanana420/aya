package aya.wrapper;

import java.util.ArrayList;

public class ffmpeg {
  public static ArrayList<String> getCaptureArgs() {
    return process.mkList(new String[]{"-loglevel", "quiet", "-y", "-f", "x11grab", "-i", ":0.0", "-frames:v", "1"});
  }

  public static ArrayList<String> encodeArgs_png(byte quality) {
    String[] qualities = new String[]{"none", "sub", "up", "avg", "paeth", "mixed"};
    String q_arg = null;
    if (quality < 0) {q_arg = "none";}
    else if (quality > 5) {q_arg = "mixed";}
    else {q_arg = qualities[quality];}

    var list = new ArrayList<String>();
    list.add("-pred"); list.add(q_arg);
    return list;
  }
}
