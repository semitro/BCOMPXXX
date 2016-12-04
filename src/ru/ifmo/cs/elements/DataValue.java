package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataInputs;
import ru.ifmo.cs.elements.DataSource;

public class DataValue extends DataInputs implements DataSource {

   protected volatile int value = 0;


   public DataValue(String name, int width, DataSource ... inputs) {
      super(name, width, inputs);
   }

   public int getValue() {
      return this.value;
   }
}
