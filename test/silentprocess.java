import java.lang.ProcessBuilder.Redirect;

public class silentprocess {
  public static void main(String[] args) {
    var pb = new ProcessBuilder(new String[]{"ffmpeg", "-h"});
    //pb.inheritIO();
    try {
      var p = pb.start();
      var is = p.getInputStream();
      p.waitFor();

      String s = new String(is.readAllBytes());
      //System.out.println(s);
      System.out.println("LENGTH" + s.length());
    }
    catch (Exception e) {e.printStackTrace();}
  }
}
