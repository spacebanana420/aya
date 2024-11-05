package aya;

import aya.wrapper.ffmpeg;
import aya.wrapper.process;
import java.util.ArrayList;

public class screenshotter {
  private boolean usemagick = false;
  private int delay = 0;
  private String filename = "Screenshot.png";
  
  public void enableMagick() {usemagick = true;}
  public void setDelay(int d) {
    if (d >= 0) {delay = d;}
  }
  public void snap() { //use arraylist later
    var cmd = new ArrayList<String>();
    cmd.add("ffmpeg");
    cmd.addAll(ffmpeg.getCaptureArgs());
    cmd.addAll(ffmpeg.encodeArgs_png(5));
    cmd.add(filename);
    
    if (delay > 0) {Thread.sleep(delay);}
    process.run(cmd);
  }
}
