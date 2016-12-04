package ru.ifmo.cs.bcomp.ui.io;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JPanel;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.ui.io.FlagIndicator;
import ru.ifmo.cs.bcomp.ui.io.OutputDevice;

public class Ticker extends OutputDevice {

   private static final Color LED_OFF = new Color(224, 224, 224);
   private static final Color LED_ON = new Color(0, 160, 0);
   private static final int ELEMENTS_COUNT = 32;
   private static final int ELEMENTS_HEIGHT = 8;
   private static final int ELEMENT_SIZE = 4;
   private static final int ELEMENT_SPACE = 2;
   private static final int ELEMENT_FULL_SIZE = 6;
   private final int[] elements = new int[32];
   private int position = 31;
   private Ticker.TickerString ticker;


   public Ticker(IOCtrl ioctrl) {
      super(ioctrl, "Бегущая строка");
      Arrays.fill(this.elements, 0);
   }

   protected Component getContent() {
      JPanel content = new JPanel(new BorderLayout());
      JPanel center = new JPanel(new FlowLayout());
      center.add(this.ticker = new Ticker.TickerString());
      content.add("Center", center);
      JPanel north = new JPanel(new FlowLayout(0));
      north.add(this.getSleepSlider());
      north.add(this.getPowerChkBox());
      north.add(new FlagIndicator(this.ioctrl, 30));
      content.add("North", north);
      return content;
   }

   protected void actionPerformed(int value) {
      this.elements[this.position] = value;
      this.position = (this.position + 1) % 32;
      this.ticker.repaint();
   }


   private class TickerString extends JComponent {

      public TickerString() {
         Dimension d = new Dimension(194, 50);
         this.setMinimumSize(d);
         this.setMaximumSize(d);
         this.setPreferredSize(d);
         this.setSize(d);
      }

      public void paintComponent(Graphics g) {
         for(int x = 0; x < 32; ++x) {
            int value = Ticker.this.elements[(x + Ticker.this.position) % 32];

            for(int y = 0; y < 8; ++y) {
               g.setColor((value >> 8 - y - 1 & 1) == 1?Ticker.LED_ON:Ticker.LED_OFF);
               g.fillRect(x * 6 + 2, y * 6 + 2, 4, 4);
            }
         }

      }
   }
}
