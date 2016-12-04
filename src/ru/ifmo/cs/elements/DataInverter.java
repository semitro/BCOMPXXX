package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataCtrl;
import ru.ifmo.cs.elements.DataSource;

public class DataInverter extends DataCtrl {

   private DataSource input;


   public DataInverter(String name, DataSource input, DataSource ... ctrls) {
      super(name, input.getWidth(), ctrls);
      this.input = input;
   }

   public void setValue(int ctrl) {
      super.setValue(this.isOpen(ctrl)?~this.input.getValue():this.input.getValue());
   }
}
