import java.io.IOException;

//Tests for Wayland support for aya, using grim and slurp for screen capture, and ffmpeg for feature-parity with x11 aya
public class wayland {
  public static void main(String[] args) {
    String[] slurp_cmd = new String[]{"slurp"};
    String[] grim_cmd = new String[]{"grim", "-l", "0", "-"};
    String[] ffmpeg_cmd = new String[]{"ffmpeg", "-y", "-i", "-", "-pred", "-mixed", "test-image.png"};
    
    try {
      print("Running Slurp");
      Process s = new ProcessBuilder(slurp_cmd).start();
      s.waitFor();
      byte[] coordinates = s.getInputStream().readAllBytes();
      
      print("Running Grim");
      Process g = new ProcessBuilder(grim_cmd).start();
      g.getOutputStream().write(coordinates);
      g.waitFor();
      byte[] screenshot_image = g.getInputStream().readAllBytes();
      
      print("Running FFmpeg");
      Process f = new ProcessBuilder(ffmpeg_cmd).start();
      g.getOutputStream().write(screenshot_image);
      g.waitFor();
    }
    catch(IOException | InterruptedException e) {e.printStackTrace();}
  }
  
  static void print(String message) {System.out.println(message);}
}
