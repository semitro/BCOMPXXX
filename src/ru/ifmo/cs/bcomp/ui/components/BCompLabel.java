package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.ui.components.BorderedComponent;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;

public class BCompLabel extends BorderedComponent {

   public BCompLabel(int x, int y, int width, String ... text) {
      super(text.length * 25 + 2);
      this.setBounds(x, y, width);

      for(int i = 0; i < text.length; ++i) {
         JLabel title = this.addLabel(text[i], DisplayStyles.FONT_COURIER_BOLD_21, DisplayStyles.COLOR_TITLE);
         title.setBounds(1, 1 + i * 25, width - 2, 25);
      }

   }
}
