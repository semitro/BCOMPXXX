package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.bcomp.ui.components.BCompComponent;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;

public class ActiveBitView extends BCompComponent {

   private final JLabel value = this.addValueLabel();


   public ActiveBitView(int x, int y) {
      super("Бит", DisplayStyles.COLOR_INPUT_TITLE);
      this.setBounds(x, y, this.getValueWidth(8, true));
      this.value.setBounds(1, getValueY(), this.width - 2, 25);
   }

   public void setValue(int value) {
      this.value.setText(Utils.toHex(value, 1));
   }
}
