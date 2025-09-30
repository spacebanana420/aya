import java.awt.*;
import java.awt.event.*;
import javax.swing.BoxLayout;
import javax.swing.Box;

public class gui {  
  public static void main(String[] args) {
    Frame window = new Frame("Aya GUI Test");
    Panel panel = new Panel();
    //window.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    window.setResizable(false);

    Font message_font = new Font("SansSeirf", Font.PLAIN, 32);
    Font button_font = new Font("SansSeirf", Font.PLAIN, 24);
    Label message = new Label("Sample status message");
    message.setAlignment(Label.CENTER);
    message.setFont(message_font);
    Button button = new Button("Got it");
    button.setFont(button_font);
    button.setMaximumSize(new Dimension(100, button.getPreferredSize().height));
    button.addActionListener(listener -> System.out.println("Button clicked"));
    

    //window.add(message);
    //window.add(button);
    panel.add(message);
    panel.add(button);
    panel.add(Box.createVerticalStrut(10));
    
    window.add(panel);
    window.setMinimumSize(new Dimension(600, 200));
    window.pack();
    WindowListener on_window_close = new WindowAdapter() {
      public void windowClosing(WindowEvent e) {System.out.println("Window Closing");}
    };
    window.addWindowListener(on_window_close);
    window.setVisible(true);
  }
}
