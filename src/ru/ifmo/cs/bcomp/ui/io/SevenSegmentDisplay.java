package ru.ifmo.cs.bcomp.ui.io;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JPanel;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.ui.io.FlagIndicator;
import ru.ifmo.cs.bcomp.ui.io.OutputDevice;

public class SevenSegmentDisplay extends OutputDevice {

   private static final Color LED_OFF = new Color(224, 224, 224);
   private static final Color LED_ON = new Color(0, 160, 0);
   private static final int COUNT = 8;
   private static final int SEGMENT_LENGTH = 16;
   private static final int SEGMENT_WIDTH = 2;
   private static final Dimension DIMS = new Dimension(24, 42);
   private static final boolean[][] NUMBERS = new boolean[][]{{true, false, true, true, true, true, true}, {false, false, false, false, false, true, true}, {true, true, true, false, true, true, false}, {true, true, true, false, false, true, true}, {false, true, false, true, false, true, true}, {true, true, true, true, false, false, true}, {true, true, true, true, true, false, true}, {true, false, false, false, false, true, true}, {true, true, true, true, true, true, true}, {true, true, true, true, false, true, true}, {false, true, false, false, false, false, false}, {false, false, false, false, false, false, false}, {false, false, false, false, false, false, false}, {false, false, false, false, false, false, false}, {false, false, false, false, false, false, false}, {false, false, false, false, false, false, false}};
   private static final int[][] COORDINATES = new int[][]{{pos(0, 2), pos(0, 1), 16, 2}, {pos(0, 2), pos(1, 2), 16, 2}, {pos(0, 2), pos(2, 3), 16, 2}, {pos(0, 1), pos(0, 2), 2, 16}, {pos(0, 1), pos(1, 3), 2, 16}, {pos(1, 2), pos(0, 2), 2, 16}, {pos(1, 2), pos(1, 3), 2, 16}};
   private final SevenSegmentDisplay.SSD[] ssd = new SevenSegmentDisplay.SSD[8];


   public SevenSegmentDisplay(IOCtrl ioctrl) {
      super(ioctrl, "Семисегментный индикатор");
   }

   private static int pos(int length, int width) {
      return length * 16 + width * 2;
   }

   protected Component getContent() {
      JPanel content = new JPanel(new BorderLayout());
      JPanel center = new JPanel(new FlowLayout());
      int north = 8;

      while(north > 0) {
         --north;
         center.add(this.ssd[north] = new SevenSegmentDisplay.SSD());
      }

      content.add("Center", center);
      JPanel var4 = new JPanel(new FlowLayout(0));
      var4.add(this.getSleepSlider());
      var4.add(this.getPowerChkBox());
      var4.add(new FlagIndicator(this.ioctrl, 30));
      content.add("North", var4);
      return content;
   }

   protected void actionPerformed(int value) {
      int pos = value >> 4 & 7;
      this.ssd[pos].value = value & 15;
      this.ssd[pos].repaint();
   }


   private class SSD extends JComponent {

      private int value = 15;


      public SSD() {
         this.setMinimumSize(SevenSegmentDisplay.DIMS);
         this.setMaximumSize(SevenSegmentDisplay.DIMS);
         this.setPreferredSize(SevenSegmentDisplay.DIMS);
         this.setSize(SevenSegmentDisplay.DIMS);
      }

      public void paintComponent(Graphics g) {
         for(int i = 0; i < SevenSegmentDisplay.COORDINATES.length; ++i) {
            g.setColor(SevenSegmentDisplay.NUMBERS[this.value][i]?SevenSegmentDisplay.LED_ON:SevenSegmentDisplay.LED_OFF);
            g.fillRect(SevenSegmentDisplay.COORDINATES[i][0], SevenSegmentDisplay.COORDINATES[i][1], SevenSegmentDisplay.COORDINATES[i][2], SevenSegmentDisplay.COORDINATES[i][3]);
         }

      }
   }
}
