package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataCtrl;
import ru.ifmo.cs.elements.DataSource;

public class DataCheckZero extends DataCtrl {

   private DataSource input;
   private int inputmask;


   public DataCheckZero(String name, DataSource input, int width, int ctrlbit, DataSource ... ctrls) {
      super(name, 1, ctrlbit, ctrls);
      this.input = input;
      this.inputmask = getMask(width);
   }

   public void setValue(int ctrl) {
      if(this.isOpen(ctrl)) {
         super.setValue((this.input.getValue() & this.inputmask) == 0?1:0);
      }

   }
}
