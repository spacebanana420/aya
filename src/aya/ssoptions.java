package aya;

import aya.cli.parser;
//import aya.config.reader;

public class ssoptions {
  public int[] crop = new int[4];
  public boolean crop_enabled = false;
  public byte format = 0;
  public byte quality = -1;
  public int[] scale = new int[2];
  
  public void getOpts(String[] args) {
    //String[] conf = reader.openConfig();
    setCrop(args);
    setFormat(args);
    setQuality(args);
  }

  private void setCrop(String[] args) {
    String[] opts = new String[]{"-w", "-h", "-x", "-y"};
    for (int i = 0; i < opts.length; i++) {
      int value = parser.getArgInt(args, opts[i]);
      if (value >= 0) {crop[i] = value;}
    }
    if (crop[0] > 0 && crop[1] > 0 && crop[2] >= 0 && crop[3] >= 0) {crop_enabled = true;}
  }
  
  private void setFormat(String[] args) {
    String value = parser.getArgValue(args, "png");
    if (value != null) {format = 0; return;}
    value = parser.getArgValue(args, "jpg");
    if (value != null) {format = 1;}
  }

  private void setQuality(String[] args) {
    byte value = parser.getArgByte(args, "-q");
    boolean pngvalue = value >= 0 && value <= 5;
    boolean jpgvalue = value > 0 && value <= 100;
    if ((format == 0 && pngvalue) || (format == 1 && jpgvalue)) {
      quality = value;
    }
  }
}
