package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataSource;

public class Comparer extends DataHandler {

   private DataSource input;
   private int startbit;


   public Comparer(DataSource input, int startbit, DataSource ... ctrls) {
      super(1, ctrls);
      this.input = input;
      this.startbit = startbit;
   }

   public void setValue(int ctrl) {
      super.setValue((this.input.getValue() >> this.startbit & 1) == ctrl?1:0);
   }
}
