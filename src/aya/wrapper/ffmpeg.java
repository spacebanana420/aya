package aya.wrapper;

public class ffmpeg {
  public static String[] getCaptureArgs() {
    return new String[]{"-f", "x11grab", "-i", ":0.0", "-frames:v", "1"};
  }

  public static String[] encodeArgs_png(byte quality) {
    String[] qualities = new String[]{"none", "sub", "up", "avg", "paeth", "mixed"};
    String q_arg = null;
    if (quality < 0) {q_arg = "none";}
    else if (quality > 5) {q_arg = "mixed";}
    else {q_arg = qualities[quality];}
    
    return new String[]{"-pred", q_arg};
  } 
}
