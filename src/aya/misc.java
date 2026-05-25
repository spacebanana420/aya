package aya;

//Generic functions used by some other part of the program
public class misc {
  public static void sleep(int delay) {
    try {Thread.sleep(delay);}
    catch (InterruptedException e) {e.printStackTrace();}
  }

  //Check if a filename has a specific extension by iterating only the necessary amount of characters
  public static boolean hasExtension(String name, String extension) {
    if (name.length() <= extension.length() || extension.length() == 0) return false;
    
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

  //Retrieve the extension of a filename by finding where the "." character is
  public static String getExtension(String name) {
    if (name.length() < 3) return ""; //1 char name + . + 1 char extension is the minimum for a filename
    int start = -1;
    for (int i = name.length()-1; i >= 0; i--) {if (name.charAt(i) == '.') {start = i; break;}}
    if (start == -1 || start == name.length()-1) return "";
   
    start++; //skips the . character
    var extension = new StringBuilder();
    for (int i = start; i < name.length(); i++) {extension.append(name.charAt(i));}
    return extension.toString();
  }
}
