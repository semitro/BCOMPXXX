package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataCtrl;
import ru.ifmo.cs.elements.DataSource;

public class DataRotateRight extends DataCtrl {

   private DataSource input;
   private DataSource c;


   public DataRotateRight(String name, DataSource input, DataSource c, int ctrlbit, DataSource ... ctrls) {
      super(name, input.getWidth() + 1, ctrlbit, ctrls);
      this.input = input;
      this.c = c;
   }

   public void setValue(int ctrl) {
      if(this.isOpen(ctrl)) {
         int i = this.input.getValue();
         super.setValue((i & 1) << 16 | (this.c.getValue() & 1) << 15 | i >> 1);
      }

   }
}
