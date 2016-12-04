package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataSource;

public class DataAnd extends DataHandler {

   private final DataSource input1;
   private final int startbit1;
   private final DataSource input2;


   public DataAnd(DataSource input1, int startbit1, DataSource input2, DataSource ... ctrls) {
      super(1, ctrls);
      this.input1 = input1;
      this.startbit1 = startbit1;
      this.input2 = input2;
   }

   public void setValue(int ctrl) {
      if((this.input1.getValue() >> this.startbit1 & 1) == 1) {
         super.setValue(this.input2.getValue());
      } else {
         super.setValue(0);
      }

   }
}
