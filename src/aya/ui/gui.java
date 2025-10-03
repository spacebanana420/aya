package aya.ui;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class gui {
  public static boolean GUI_ENABLED = false;
  
  public static void displayMessage(String title, String message) {
    Font message_font = new Font("SansSerif", Font.PLAIN, 28);
    Font button_font = new Font("SansSerif", Font.PLAIN, 24);
    JFrame window = new JFrame(title);
        
    JButton button = new JButton("Got it");
    button.setFont(button_font);
    button.setMaximumSize(new Dimension(100, button.getPreferredSize().height));
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.addActionListener(listener -> window.setVisible(false));
    button.setFocusPainted(false);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    for (String line : separateLines(message)) {
      JLabel message_label = new JLabel(line);
      message_label.setAlignmentX(Component.CENTER_ALIGNMENT);
      message_label.setFont(message_font);
      panel.add(message_label);
    }
    panel.add(Box.createVerticalStrut(10));
    panel.add(button);
    panel.add(Box.createVerticalStrut(10));
    
    window.setResizable(false);
    window.add(panel);
    window.setMinimumSize(new Dimension(500, 180));
    window.pack();
    window.setVisible(true);
    while (window.isVisible()) {try{Thread.sleep(100);} catch (InterruptedException e) {return;}}
  }

  private static ArrayList<String> separateLines(String message) {
    var line = new StringBuilder();
    var lines = new ArrayList<String>();

    for (int i = 0; i < message.length(); i++)  {
      char c = message.charAt(i);
      if (c == '\n') {
        if (line.length() == 0) {continue;}
        lines.add(line.toString());
        line = new StringBuilder();
      }
      line.append(c);
    }
    if (line.length() != 0) {lines.add(line.toString());}
    if (lines.size() == 0) {lines.add(message);}
    return lines;
  }
}
