package aya.config;

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
}
