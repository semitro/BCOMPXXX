package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.DataWidth;

public class BusSplitter extends DataWidth implements DataSource {

   private DataSource input;
   private int startbit;


   public BusSplitter(DataSource input, int startbit, int width) {
      super(width);
      this.input = input;
      this.startbit = startbit;
   }

   public int getValue() {
      return this.input.getValue() >> this.startbit & this.mask;
   }
}
