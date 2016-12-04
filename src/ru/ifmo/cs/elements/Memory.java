package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.DataWidth;

public class Memory extends DataWidth implements DataSource, DataDestination {

   private int[] memory;
   private DataSource addr;
   private int size;


   public Memory(String name, int width, DataSource addr) {
      super(name, width);
      this.memory = new int[this.size = 1 << (this.addr = addr).getWidth()];
   }

   public int getValue(int addr) {
      return this.memory[addr];
   }

   public int getValue() {
      return this.getValue(this.addr.getValue());
   }

   public void setValue(int addr, int value) {
      this.memory[addr] = value & this.mask;
   }

   public void setValue(int value) {
      this.setValue(this.addr.getValue(), value);
   }

   public int getSize() {
      return this.size;
   }

   public int getAddrValue() {
      return this.addr.getValue();
   }

   public int getAddrWidth() {
      return this.addr.getWidth();
   }
}
