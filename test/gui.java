import java.awt.*;
import java.awt.event.*;
import javax.swing.BoxLayout;

public class gui {
  public static void main(String[] args) {
    Frame window = new Frame("Aya GUI Test");
    Panel panel = new Panel();
    //window.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    window.setResizable(false);
    //window.setSize(1000, 160);

    Font message_font = new Font("SansSeirf", Font.PLAIN, 32);
    Font button_font = new Font("SansSeirf", Font.PLAIN, 24);
    Label message = new Label("Sample status message");
    message.setFont(message_font);
    Button button = new Button("Got it");
    button.setFont(button_font);
    //button.setSize(10,120);
    button.setMaximumSize(new Dimension(100, button.getPreferredSize().height));
    

    //window.add(message);
    //window.add(button);
    panel.add(message);
    panel.add(button);
    window.add(panel);
    window.pack();
    window.setVisible(true);
  }
}
