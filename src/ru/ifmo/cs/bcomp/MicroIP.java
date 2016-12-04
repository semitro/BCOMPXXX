package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.Register;

public class MicroIP extends Register {

   public MicroIP(String name, String fullname, int width) {
      super(name, fullname, width, new DataSource[0]);
      super.setValue(1);
   }

   public void setValue(int value) {
      super.setValue((value & this.mask) == 0?this.value + 1:value);
   }
}
