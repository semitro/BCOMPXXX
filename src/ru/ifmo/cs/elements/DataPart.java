package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataStorage;

public class DataPart extends DataHandler {

   protected int startbit;


   public DataPart(int startbit, int width, DataStorage ... inputs) {
      super(width, inputs);
      this.startbit = startbit;
   }

   public DataPart(int startbit, DataStorage ... inputs) {
      this(startbit, 1, inputs);
   }

   public void setValue(int value) {
      super.setValue(value >> this.startbit);
   }
}
