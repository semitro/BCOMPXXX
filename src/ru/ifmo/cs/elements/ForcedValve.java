package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataCtrl;
import ru.ifmo.cs.elements.DataSource;

public class ForcedValve extends DataCtrl {

   private final DataSource input;


   public ForcedValve(DataSource input, int width, DataSource ... ctrls) {
      super(width, 0, ctrls);
      this.input = input;
   }

   public void setValue(int ctrl) {
      super.setValue(this.isOpen(ctrl)?this.input.getValue():0);
   }
}
