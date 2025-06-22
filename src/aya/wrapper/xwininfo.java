package aya.wrapper;

import aya.stdio;
import aya.main;

import java.util.ArrayList;

public class xwininfo {
  public static int[] getWindowCoordinates() {
    if (!main.systemSupported()) {
      stdio.print_verbose("Ignoring -window argument, operating system unsupported");
      return null;
    }
    stdio.print("Please click on the window to capture"); 
    String[] cmd = new String[]{"xwininfo"};
    String result = process.runAndGet(cmd);
    if (result == null) {
      stdio.print_error("Error retrieving window coordinates! Make sure you have \"xwininfo\" installed!");
      return null;
    }

    return getCoordinates(result);
  }

  private static int[] getCoordinates(String s) {
    String buf = "";
    var list = new ArrayList<String>();
    
    for (int i = 0; i < s.length() || list.size() < 4; i++) {
      char c = s.charAt(i);
      if (c == '\n') {
        if (isRelevant(buf)) {list.add(buf.trim());}
        buf = "";
      }
      else {buf += c;}
    }
    stdio.print_debug("Parsed xwininfo output:", list);
    
    String[] settings = new String[]
    {
      "Absolute upper-left X:  ",
      "Absolute upper-left Y:  ",
      "Width: ",
      "Height: ",  
    };
    int[] coordinates = new int[4];
    for (int i = 0; i < coordinates.length; i++) {
      coordinates[i] = parseLine(list.get(i), settings[i].length());
    }
    reorderCoordinates(coordinates);

    stdio.print_debug("Width:  " + coordinates[0]);
    stdio.print_debug("Height:  " + coordinates[1]);
    stdio.print_debug("X:  " + coordinates[2]);
    stdio.print_debug("Y:  " + coordinates[3]);
    return coordinates;
  }
  
  private static boolean isRelevant(String line) {
    return
      line.length() > 0
      && (line.contains("Width") || line.contains("Height") || line.contains("Absolute upper-left"))
    ;
  }

  private static int parseLine(String line, int setting_i) {
    String buf = "";
    for (int i = setting_i; i < line.length(); i++) {buf += line.charAt(i);}
    int num = Integer.parseInt(buf);
    if (num < 0) {num = 0;}
    return num;
  }

  private static void reorderCoordinates(int[] coords) { //order goes from X-Y-WIDTH-HEIGHT to WIDTH-HEIGHT-X-Y
    int temp;
    temp = coords[0]; coords[0] = coords[2]; coords[2] = temp;
    temp = coords[1]; coords[1] = coords[3]; coords[3] = temp;
  }
}
