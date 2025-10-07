package aya.config;

import aya.ui.stdout;
import java.util.ArrayList;

public class Config {
  private ArrayList<String> keys = new ArrayList<String>();
  private ArrayList<String> values = new ArrayList<String>();

  public void addSetting(String line) {
    var parsed_key = new StringBuilder();
    var parsed_value = new StringBuilder();
    int value_start = -1;
    
    for (int i = 0; i < line.length(); i++) { //Get the key
      char c = line.charAt(i);
      if (c == '=') {value_start = i+1; break;}
      parsed_key.append(c);
    }
    if (value_start == -1 || parsed_key.length() == 0) {return;}
    
    for (int i = value_start; i < line.length(); i++) { //Get the value
      parsed_value.append(line.charAt(i));
    } 
    if (parsed_value.length() == 0) {return;}

    this.keys.add(parsed_key.toString().trim());
    this.values.add(parsed_value.toString().trim());
  }

  public String readSetting(String key) {
    int value_i = -1;
    for (int i = 0; i < keys.size(); i++) {
      if (key.equals(this.keys.get(i))) {value_i = i; break;}
    }
    if (value_i == -1) {
      stdout.error_verbose("The value for key " + key + " was not found as the key is not in the configuration!");
      return null;
    }
    return this.values.get(value_i);
  }

  public int readSetting_int(String setting) {
    String svalue = readSetting(setting);
    if (svalue == null) {return -1;}

    try {
      return Integer.parseInt(svalue);
    }
    catch (NumberFormatException e) {
      printSettingError(setting, svalue, "int");
      return -1;
    }
  }

  public byte readSetting_byte(String setting) {
    String svalue = readSetting(setting);
    if (svalue == null) {return -1;}
    try {
      return Byte.parseByte(svalue);
    }
    catch (NumberFormatException e) {
      printSettingError(setting, svalue, "byte");
      return -1;
    }
  }

  public float readSetting_float(String setting) {
    String svalue = readSetting(setting);
    if (svalue == null) {return -1;}
    try {
      return Float.parseFloat(svalue);
    }
    catch (NumberFormatException e) {
      printSettingError(setting, svalue, "float");
      return -1;
    }
  }
  
  public boolean readSetting_bool(String setting) {
    String value = readSetting(setting);
    if (value == null) {return false;}
    value = value.toLowerCase();
    return value.equals("true") || value.equals("yes");
  }
  
  public ArrayList<String> readCommand(String setting, String filename) {
    String cmd_str = readSetting(setting);
    if (cmd_str == null) {return null;}
    
    var cmd = new ArrayList<String>();
    var arg = new StringBuilder();
    
    for (int i = 0; i < cmd_str.length(); i++) {
      char c = cmd_str.charAt(i);
      if (c == ' ' || c == '\t') {
        if (arg.length() == 0) {continue;}
        cmd.add(arg.toString());
        arg = new StringBuilder();
      }
      else {arg.append(c);}
    }
    if (arg.length() > 0) {cmd.add(arg.toString());}
    
    boolean has_filename_position = false;
    for (int i = 0; i < cmd.size(); i++) {
      if (cmd.get(i).equals("%F")) {
        has_filename_position = true;
        cmd.set(i, filename);
      }
    }
    if (!has_filename_position) {cmd.add(filename);}
    return cmd;
  }

  private static void printSettingError(String key, String value, String type) {
    stdout.error_verbose("[Aya config] Failed to convert the value " + value + " of key " + key + " from a String to a " + type);
  }
}
