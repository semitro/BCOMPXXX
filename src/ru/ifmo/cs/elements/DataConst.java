package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.DataValue;

public class DataConst extends DataValue {

   public DataConst(int value, int width) {
      super(Integer.toHexString(value), width, new DataSource[0]);
      this.value = value & this.mask;
   }
}
