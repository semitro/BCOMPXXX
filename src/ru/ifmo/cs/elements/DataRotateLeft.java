package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataCtrl;
import ru.ifmo.cs.elements.DataSource;

public class DataRotateLeft extends DataCtrl {

   private DataSource input;
   private DataSource c;


   public DataRotateLeft(String name, DataSource input, DataSource c, int ctrlbit, DataSource ... ctrls) {
      super(name, input.getWidth() + 1, ctrlbit, ctrls);
      this.input = input;
      this.c = c;
   }

   public void setValue(int ctrl) {
      if(this.isOpen(ctrl)) {
         super.setValue(this.input.getValue() << 1 | this.c.getValue() & 1);
      }

   }
}
