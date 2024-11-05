package aya;

import aya.wrapper.ffmpeg;
import aya.wrapper.process;

public class screenshotter {
  private boolean usemagick = false;
  private int delay = 0;
  private String filename = "Screenshot.png";
  
  public void enableMagick() {usemagick = true;}
  public void setDelay(int d) {
    if (d >= 0) {delay = d;}
  }
  public void snap() { //use arraylist later
    String[] capture_args = ffmpeg.getCaptureArgs();
    String[] encode_args = ffmpeg.encodeArgs_png(5);
    
    String[] cmd = process.concatArgs(capture_args, encode_args);
    cmd = process.appendArg(cmd, filename);
    if (delay > 0) {Thread.sleep(delay);}
    process.run(cmd);
  }
}
