package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataSource;

public class DataCtrl extends DataHandler {

   private int ctrlbit;


   public DataCtrl(String name, int width, int ctrlbit, DataSource ... ctrls) {
      super(name, width, ctrls);
      this.ctrlbit = ctrlbit;
   }

   public DataCtrl(String name, int width, DataSource ... ctrls) {
      this(name, width, 0, ctrls);
   }

   public DataCtrl(int width, int ctrlbit, DataSource ... ctrls) {
      this((String)null, width, ctrlbit, ctrls);
   }

   public DataCtrl(int width, DataSource ... ctrls) {
      this(width, 0, ctrls);
   }

   public final boolean isOpen(int ctrl) {
      return (ctrl >> this.ctrlbit & 1) == 1;
   }
}
