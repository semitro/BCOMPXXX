package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.DataStorage;

public class Register extends DataStorage {

   public final String fullname;


   public Register(String name, String fullname, int width, DataSource ... inputs) {
      super(name, width, inputs);
      this.fullname = fullname;
   }

   public int getValue(int startbit) {
      return this.value >> startbit & 1;
   }

   public void setValue(int value, int startbit, int width) {
      int valuemask = getMask(width);
      this.setValue(this.value & ~(valuemask << startbit) | (value & valuemask) << startbit);
   }

   public void setValue(int value, int startbit) {
      this.setValue(this.value & ~(1 << startbit) | (value & 1) << startbit);
   }

   public void invertBit(int startbit) {
      int bitpos = 1 << startbit;
      this.value = this.value & ~bitpos | ~(this.value & bitpos) & bitpos;
   }
}
