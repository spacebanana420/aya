package aya;

import aya.wrapper.ffmpeg;
import aya.wrapper.process;
import java.util.ArrayList;

public class screenshotter {
  //private boolean usemagick = false;
  private int delay = 0;
  private String filename = "Screenshot.png";
  
  //public void enableMagick() {usemagick = true;}
  public void setDelay(int d) {
    if (d >= 0) {delay = d;}
  }
  
  public int snap(String[] args) {
    var opts = new ssoptions();
    opts.getOpts(args);
    var cmd = new ArrayList<String>();
    cmd.add("ffmpeg");
    cmd.addAll(ffmpeg.getCaptureArgs());
    if (opts.format == 0) {
      cmd.addAll(ffmpeg.encodeArgs_png(opts.quality));
    }
    cmd.add(filename);
    
    if (delay > 0) {misc.sleep(delay);}
    return process.run(cmd);
  }
}
