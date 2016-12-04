package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.DataValue;

public class DataStorage extends DataValue implements DataDestination {

   public DataStorage(String name, int width, DataSource ... inputs) {
      super(name, width, inputs);
   }

   public void setValue(int value) {
      this.value = value & this.mask;
   }
}
