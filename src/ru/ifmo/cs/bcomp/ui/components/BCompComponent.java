package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.bcomp.ui.components.BorderedComponent;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;

public class BCompComponent extends BorderedComponent {

   protected JLabel title;


   private BCompComponent(String title, int ncells, Color color) {
      super(3 + 25 * (ncells + 1));
      this.title = this.addLabel(title, DisplayStyles.FONT_COURIER_BOLD_21, color);
   }

   public BCompComponent(String title, Color colorTitleBG) {
      this(title, 1, colorTitleBG);
   }

   public BCompComponent(String title, int ncells) {
      this(title, ncells, DisplayStyles.COLOR_TITLE);
   }

   private final JLabel addValueLabel(String value, Color color) {
      return this.addLabel(value, DisplayStyles.FONT_COURIER_BOLD_25, color);
   }

   protected final JLabel addValueLabel(Color color) {
      return this.addValueLabel("", color);
   }

   protected final JLabel addValueLabel(String value) {
      return this.addValueLabel(value, DisplayStyles.COLOR_VALUE);
   }

   protected final JLabel addValueLabel() {
      return this.addValueLabel("", DisplayStyles.COLOR_VALUE);
   }

   protected void setBounds(int x, int y, int width) {
      super.setBounds(x, y, width);
      this.title.setBounds(1, 1, width - 2, 25);
   }

   protected int getValueY(int n) {
      return 2 + 25 * (n + 1);
   }

   protected static int getValueY() {
      return 27;
   }

   private int getPixelWidth(int chars) {
      return 2 + DisplayStyles.FONT_COURIER_BOLD_25_WIDTH * (1 + chars);
   }

   protected int getValueWidth(int width) {
      return this.getPixelWidth(Utils.getHexWidth(width));
   }

   protected int getValueWidth(int width, boolean hex) {
      return hex?this.getValueWidth(width):this.getPixelWidth(Utils.getBinaryWidth(width));
   }

   protected void setTitle(String title) {
      this.title.setText(title);
   }

   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawLine(1, 26, this.width - 2, 26);
   }
}
