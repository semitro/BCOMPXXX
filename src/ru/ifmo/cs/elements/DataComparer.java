package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataSource;

public class DataComparer extends DataHandler {

   private final DataSource input;
   private final int cmp2;


   public DataComparer(DataSource input, int cmp2, DataSource ... ctrls) {
      super(1, ctrls);
      this.input = input;
      this.cmp2 = cmp2;
   }

   public void setValue(int ctrl) {
      if(this.input.getValue() == this.cmp2) {
         super.setValue(1);
      }

   }
}
