package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataSource;

public class Inverter extends DataHandler {

   private int startbit;


   public Inverter(int startbit, DataSource ... inputs) {
      super(1, inputs);
      this.startbit = startbit;
   }

   public Inverter(DataSource ... inputs) {
      this(0, inputs);
   }

   public void setValue(int value) {
      super.setValue(~(value >> this.startbit));
   }
}
