package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;

public class BorderedComponent extends JComponent {

   protected int width;
   protected final int height;


   protected BorderedComponent(int height) {
      this.height = height;
   }

   protected final JLabel addLabel(String value, Font font, Color color) {
      JLabel label = new JLabel(value, 0);
      label.setFont(font);
      label.setBackground(color);
      label.setOpaque(true);
      this.add(label);
      return label;
   }

   protected void setBounds(int x, int y, int width) {
      this.setBounds(x, y, this.width = width, this.height);
   }

   public void paintComponent(Graphics g) {
      g.setColor(DisplayStyles.COLOR_TEXT);
      g.drawRect(0, 0, this.width - 1, this.height - 1);
   }
}
