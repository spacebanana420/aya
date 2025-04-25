package aya.config;

import java.util.ArrayList;

class confio {
  static String readSetting(Setting[] conf, String key) {
    for (Setting s : conf) {
      if (s.keysMatch(key)) {return s.value;}
    }
    return null;
  }
  
  static int readSetting_int(Setting[] conf, String setting) {
    String svalue = readSetting(conf, setting);
    if (svalue == null) {return -1;}
    try {
      return Integer.parseInt(svalue);
    }
    catch (NumberFormatException e) {return -1;}
  }

  static byte readSetting_byte(Setting[] conf, String setting) {
    String svalue = readSetting(conf, setting);
    if (svalue == null) {return -1;}
    try {
      return Byte.parseByte(svalue);
    }
    catch (NumberFormatException e) {return -1;}
  }
  
  static ArrayList<String> readCommand(Setting[] conf, String setting, String filename) {
    String cmd_str = readSetting(conf, setting);
    if (cmd_str == null) {return null;}
    
    var cmd = new ArrayList<String>();
    String buffer = "";
    
    for (int i = 0; i < cmd_str.length(); i++) {
      char c = cmd_str.charAt(i);
      if (c == ' ' || c == '\t') {
        if (buffer.length() == 0) {continue;}
        cmd.add(buffer);
        buffer = "";
      }
      else {buffer += c;}
    }
    if (buffer.length() > 0) {cmd.add(buffer);}
    
    boolean has_filename_position = false;
    for (int i = 0; i < cmd.size(); i++) {
      String arg = cmd.get(i);
      if (arg.equals("%F")) {
        has_filename_position = true;
        cmd.set(i, filename);
      }
    }
    if (!has_filename_position) {cmd.add(filename);}
    return cmd;
  }
}
