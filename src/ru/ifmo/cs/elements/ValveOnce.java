package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataCtrl;
import ru.ifmo.cs.elements.DataSource;

public class ValveOnce extends DataCtrl {

   private final DataSource input;


   public ValveOnce(String name, DataSource input, int ctrlbit, DataSource ... ctrls) {
      super(name, input.getWidth(), ctrlbit, ctrls);
      this.input = input;
   }

   public ValveOnce(DataSource input, int ctrlbit, DataSource ... ctrls) {
      this((String)null, input, ctrlbit, ctrls);
   }

   public ValveOnce(String name, DataSource input, DataSource ... ctrls) {
      this(name, input, 0, ctrls);
   }

   public void setValue(int ctrl) {
      if(this.isOpen(ctrl)) {
         super.setValue(this.input.getValue());
      }

   }

   public int getValue() {
      int value = this.value;
      if(value != 0) {
         super.resetValue();
      }

      return value;
   }
}
