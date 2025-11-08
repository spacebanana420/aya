import java.nio.file.Files;
import java.nio.file.Path;

public class clipboard {
    public static void main(String[] args) {
      try {
        byte[] image_file = Files.readAllBytes(Path.of("/home/space/Pictures/froge.png"));
        String[] cmd = new String[]{"xclip", "-target", "image/png", "-selection", "clipboard"};
        System.out.println("Number of image bytes: " + image_file.length);
        
        Process process = new ProcessBuilder(cmd).start();
        OutputStream stdin = process.getOutputStream();
        stdin.write(image_file);
        stdin.close();
        process.waitFor();
      }
      catch(Exception e) {e.printStackTrace();}
    }
}
