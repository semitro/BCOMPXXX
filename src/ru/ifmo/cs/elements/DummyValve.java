package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataSource;

public class DummyValve extends DataHandler {

   private DataSource input;
   private int startbit;


   public DummyValve(DataSource input, int startbit, int width, DataSource ... ctrls) {
      super(width, ctrls);
      this.input = input;
      this.startbit = startbit;
   }

   public DummyValve(DataSource input, DataSource ... ctrls) {
      this(input, 0, input.getWidth(), ctrls);
   }

   public void setValue(int ctrl) {
      super.setValue(this.input.getValue() >> this.startbit);
   }
}
