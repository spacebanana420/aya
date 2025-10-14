package aya.ui;

import aya.cli.cli;
import aya.config.Config;
import aya.config.config;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.ArrayList;

public class gui {
  public static boolean GUI_ENABLED = false;

  //UI size values
  private static int message_size = 20;
  private static int button_size = 18;
  private static int[] min_window_size = new int[]{450, 100};
  private static int max_button_size = 100;
  private static int blank_gap = 10;

  private static Font message_font = null;
  private static Font button_font = null;

  public static void setupGUI(String[] args, Config conf) {
    GUI_ENABLED = cli.hasArgument(args, "-gui") || config.getGUIToggle(conf);
    if (!GUI_ENABLED) {return;}
    message_font = new Font("SansSerif", Font.PLAIN, message_size);
    button_font = new Font("SansSerif", Font.PLAIN, button_size);

    float scale = cli.getGUIScale(args);
    if (scale == -1) {scale = config.getGUIScale(conf);}

    if (scale < 0.5 || scale > 3) {
      stdout.error("The GUI scale value must not be below 0.5 or 3\nDefaulting to 1x scale");
      return;
    }
    if (scale == 1 || scale == -1) {return;}
    message_size *= scale;
    button_size *= scale;
    min_window_size[0] *= scale;
    min_window_size[1] *= scale;
    max_button_size *= scale;
    blank_gap *= scale;
  }
  
  public static void displayMessage(String title, String message) {
    JFrame window = new JFrame(title);
    JButton button = new JButton("Got it");
    button.setFont(button_font);
    button.setMaximumSize(new Dimension(max_button_size, button.getPreferredSize().height));
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    button.addActionListener(listener -> window.setVisible(false));
    button.setFocusPainted(false);

    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(new EmptyBorder(blank_gap, blank_gap, blank_gap, blank_gap));
    for (String line : separateLines(message)) {
      JLabel message_label = new JLabel(line);
      message_label.setAlignmentX(Component.CENTER_ALIGNMENT);
      message_label.setFont(message_font);
      panel.add(message_label);
    }
    panel.add(Box.createVerticalStrut(blank_gap));
    panel.add(button);
    
    window.setResizable(false);
    window.add(panel);
    window.setMinimumSize(new Dimension(min_window_size[0], min_window_size[1]));
    window.pack();
    window.setVisible(true);
    while (window.isVisible()) {try{Thread.sleep(20);} catch (InterruptedException e) {return;}}
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
