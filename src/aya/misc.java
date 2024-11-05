package aya;

public class misc {
  public static void sleep(int delay) {
    try {
      Thread.sleep(delay);
    }
    catch (InterruptedException e) {e.printStackTrace();}
  }

  public static boolean hasExtension(String name, String extension) {
    if (name.length() <= extension.length() || extension.length() == 0) {return false;}
    
    int name_i = name.length()-extension.length();
    int extension_i = 0;
    while (name_i < name.length()) {
      if (name.charAt(name_i) != extension.charAt(extension_i)) {
        return false;
      }
      name_i++; extension_i++;
    }
    return true;
  }
}
