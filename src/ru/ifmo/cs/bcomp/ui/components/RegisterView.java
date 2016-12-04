package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.bcomp.ui.components.BCompComponent;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataWidth;
import ru.ifmo.cs.elements.Register;

public class RegisterView extends BCompComponent implements DataDestination {

   private int formatWidth;
   private int valuemask;
   private boolean hex;
   private final Register reg;
   protected final JLabel value;


   public RegisterView(Register reg, Color colorTitleBG) {
      super("", colorTitleBG);
      this.value = this.addValueLabel();
      this.reg = reg;
   }

   public RegisterView(Register reg) {
      this(reg, DisplayStyles.COLOR_TITLE);
   }

   protected void setProperties(int x, int y, boolean hex, int regWidth) {
      this.hex = hex;
      this.formatWidth = regWidth;
      this.valuemask = DataWidth.getMask(regWidth);
      this.setBounds(x, y, this.getValueWidth(regWidth, hex));
      this.setTitle(hex?this.reg.name:this.reg.fullname);
      this.setValue();
      this.value.setBounds(1, getValueY(), this.width - 2, 25);
   }

   public void setProperties(int x, int y, boolean hex) {
      this.setProperties(x, y, hex, this.reg.getWidth());
   }

   protected int getRegWidth() {
      return this.reg.getWidth();
   }

   protected void setValue(String val) {
      this.value.setText(val);
   }

   public void setValue() {
      this.setValue(this.hex?Utils.toHex(this.reg.getValue() & this.valuemask, this.formatWidth):Utils.toBinary(this.reg.getValue() & this.valuemask, this.formatWidth));
   }

   public void setValue(int value) {
      this.setValue();
   }
}
