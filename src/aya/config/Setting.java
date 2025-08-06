package aya.config;

public class Setting {
  String key = null;
  String value = null;
  
  public Setting(String line) {
    String parsed_key = "";
    String parsed_value = "";
    int value_start = -1;
    
    for (int i = 0; i < line.length(); i++) { //Get the key
      char c = line.charAt(i);
      if (c == '=') {value_start = i+1; break;}
      parsed_key += c;
    }
    parsed_key = parsed_key.trim();
    if (value_start == -1 || parsed_key.isEmpty()) {return;}
    
    for (int i = value_start; i < line.length(); i++) { //Get the value
      parsed_value += line.charAt(i);
    } 
    parsed_value = parsed_value.trim();
    if (parsed_value.isEmpty()) {return;}
    
    key = parsed_key;
    value = parsed_value;
  }
  
  public boolean keysMatch(String check_key) {return check_key.equals(key);}
}
