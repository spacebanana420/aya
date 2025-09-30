import java.awt.*;
import java.awt.event.*;

public class gui {
  public static void main(String[] args) {
    Frame window = new Frame("Aya GUI Test");
    window.setLayout(new FlowLayout());
    window.setSize(500, 160);

    Font font = new Font("SansSeirf", Font.PLAIN, 32);
    Label message = new Label("Sample status message");
    message.setFont(font);
    Button button = new Button("Got it");
    

    window.add(message);
    window.add(button);
    window.pack();
    window.setVisible(true);
  }
}
