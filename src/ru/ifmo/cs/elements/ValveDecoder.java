package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataCtrl;
import ru.ifmo.cs.elements.DataSource;

public class ValveDecoder extends DataCtrl {

   private DataSource input;
   private int inputmask;


   public ValveDecoder(DataSource input, DataSource ... ctrls) {
      super(1 << input.getWidth(), ctrls);
      this.input = input;
   }

   public void setValue(int ctrl) {
      if(this.isOpen(ctrl)) {
         super.setValue(1 << this.input.getValue());
      }

   }
}
