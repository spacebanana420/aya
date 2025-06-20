import java.io.IOException;

//Tests for Wayland support for aya, using grim and slurp for screen capture, and ffmpeg for feature-parity with x11 aya
public class wayland {
  public static void main(String[] args) {
    String[] slurp_cmd = new String[]{"slurp"};
    String[] ffmpeg_cmd = new String[]{"ffmpeg", "-y", "-i", "-", "-pred", "-mixed", "test-image.png"};
    
    try {
      print("Running Slurp");
      Process s = new ProcessBuilder(slurp_cmd)
        .redirectInput(ProcessBuilder.Redirect.INHERIT)
        .start();
      s.waitFor();
      byte[] coordinates = s.getInputStream().readAllBytes();
      String coordinates_s = new String(coordinates).trim();
      print("COORDINATES: " + coordinates_s);
      
      print("Running Grim");
      String[] grim_cmd = new String[]{"grim", "-g", coordinates_s, "-l", "0", "-"};
      Process g = new ProcessBuilder(grim_cmd)
        .redirectInput(ProcessBuilder.Redirect.INHERIT)
        .start();
      g.waitFor();
      byte[] screenshot_image = g.getInputStream().readAllBytes();
      String test = new String(g.getErrorStream().readAllBytes());
      print(test);
      
      print("Running FFmpeg");
      Process f = new ProcessBuilder(ffmpeg_cmd)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start();
      print(new String(screenshot_image));
      var ff_stdin = f.getOutputStream();
      ff_stdin.write(screenshot_image);
      ff_stdin.flush(); ff_stdin.close();
      f.waitFor();
    }
    catch(IOException | InterruptedException e) {e.printStackTrace();}
  }
  
  static void print(String message) {System.out.println(message);}
}
