package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.DataStorage;

public class DataDecoder extends DataHandler {

   private int startbit;
   private int inputmask;


   public DataDecoder(DataStorage input, int startbit, int width) {
      super(1 << width, new DataSource[]{input});
      this.startbit = startbit;
      this.inputmask = getMask(width);
   }

   public void setValue(int value) {
      super.setValue(1 << (value >> this.startbit & this.inputmask));
   }
}
