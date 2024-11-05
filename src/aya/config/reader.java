package aya.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class reader {
  public static String[] openConfig() {
    var home = System.getProperty("user.home");
    try {
      var is = new FileInputStream(home + "/.config/aya/aya.conf");
      String conf = new String(is.readAllBytes());
      is.close();
      
      String buffer = "";
      ArrayList<String> conflist = new ArrayList<String>();
      for (int i = 0; i < conf.length(); i++) {
        char c = conf.charAt(i);
        if (c == '\n') {
          if (!buffer.equals("") && buffer.charAt(0) != '#') {conflist.add(buffer);}
          buffer = "";
          continue;
        }
        buffer += conf.charAt(i);
      }
      return conflist.toArray(new String[0]);
    }
    catch (IOException e) {return new String[0];}    
  }
}
