import java.io.IOException;
import java.util.ArrayList;

//Tests for Wayland support for aya, using grim and slurp for screen capture, and ffmpeg for feature-parity with x11 aya
public class wayland {
  public static void main(String[] args) {//maybe the only way is by using startPipeline
    grim_test();
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
      captureScreen(coordinates_s);
      
      //print("Running Grim");
      //String[] grim_cmd = new String[]{"grim", "-g", coordinates_s, "-l", "0", "-"};
      //Process g = new ProcessBuilder(grim_cmd)
        //.redirectInput(ProcessBuilder.Redirect.INHERIT)
        //.redirectOutput(ProcessBuilder.Redirect.INHERIT)
        //.start();
      //g.waitFor();
      //byte[] screenshot_image = g.getInputStream().readAllBytes();
      //String test = new String(g.getErrorStream().readAllBytes());
      //print(test);
      
      //print("Running FFmpeg");
      //Process f = new ProcessBuilder(ffmpeg_cmd)
        //.redirectOutput(ProcessBuilder.Redirect.INHERIT)
        //.redirectError(ProcessBuilder.Redirect.INHERIT)
        //.start();
      //print(new String(screenshot_image));
      //var ff_stdin = f.getOutputStream();
      //ff_stdin.write(screenshot_image);
      //ff_stdin.flush(); ff_stdin.close();
      //f.waitFor();
    }
    catch(IOException | InterruptedException e) {e.printStackTrace();}
  }
  
  static void print(String message) {System.out.println(message);}
  
  static void captureScreen(String coordinates) {
    try {      
      print("Saving screenshot");
      String[] grim_cmd = new String[]{"grim", "-g", coordinates, "-l", "0", "-"};
      String[] ffmpeg_cmd = new String[]{"ffmpeg", "-y", "-i", "-", "-pred", "mixed", "test-image.png"};
      
      var pipeline = new ArrayList<ProcessBuilder>();
      pipeline.add(new ProcessBuilder(grim_cmd));
      pipeline.add(new ProcessBuilder(ffmpeg_cmd).redirectError(ProcessBuilder.Redirect.INHERIT));
      var processes = ProcessBuilder.startPipeline(pipeline);
      for (Process p : processes) {p.waitFor();}
    }
    catch(IOException | InterruptedException e) {e.printStackTrace();}
  }
  
  static void grim_test() {
    try {
      print("Running Grim (standalone)");
      String[] grim_cmd = new String[]{"grim", "-l", "0", "-"};
      Process g = new ProcessBuilder(grim_cmd)
        //.redirectInput(ProcessBuilder.Redirect.INHERIT)
        //.redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .start();
      
      var stream1 = g.getInputStream();
      byte[] data = stream1.readAllBytes();
      var stream2 = g.getErrorStream();
      g.waitFor();
      print("Image size is " + data.length + " bytes");
    } catch(IOException | InterruptedException e) {e.printStackTrace();}
  }
}
