package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;

public class ALUView extends JComponent {

   private int[] xpoints;
   private int[] ypoints;


   public ALUView(int x, int y, int width, int height) {
      int half = width / 2;
      int offset = height / 3;
      int soffset = offset / 3;
      this.xpoints = new int[]{0, half - soffset, half, half + soffset, width - 1, width - 1 - offset, offset};
      this.ypoints = new int[]{0, 0, offset, 0, 0, height - 1, height - 1};
      JLabel title = new JLabel("АЛУ", 0);
      title.setFont(DisplayStyles.FONT_COURIER_BOLD_45);
      title.setBounds(offset, offset, width - 2 * offset, height - offset);
      this.add(title);
      this.setBounds(x, y, width, height);
   }

   public void paintComponent(Graphics g) {
      g.setColor(DisplayStyles.COLOR_TITLE);
      g.fillPolygon(this.xpoints, this.ypoints, this.xpoints.length);
      g.setColor(DisplayStyles.COLOR_TEXT);
      g.drawPolygon(this.xpoints, this.ypoints, this.xpoints.length);
   }
}
