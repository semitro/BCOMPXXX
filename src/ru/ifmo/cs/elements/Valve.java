package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataCtrl;
import ru.ifmo.cs.elements.DataSource;

public class Valve extends DataCtrl {

   private final DataSource input;
   private final int startbit;


   public Valve(String name, DataSource input, int startbit, int width, int ctrlbit, DataSource ... ctrls) {
      super(name, width, ctrlbit, ctrls);
      this.input = input;
      this.startbit = startbit;
   }

   public Valve(String name, DataSource input, int ctrlbit, DataSource ... ctrls) {
      this(name, input, 0, input.getWidth(), ctrlbit, ctrls);
   }

   public Valve(DataSource input, int startbit, int width, int ctrlbit, DataSource ... ctrls) {
      this((String)null, input, startbit, width, ctrlbit, ctrls);
   }

   public Valve(DataSource input, int ctrlbit, DataSource ... ctrls) {
      this(input, 0, input.getWidth(), ctrlbit, ctrls);
   }

   public Valve(DataSource input, DataSource ... ctrls) {
      this(input, 0, ctrls);
   }

   public void setValue(int ctrl) {
      if(this.isOpen(ctrl)) {
         super.setValue(this.input.getValue() >> this.startbit);
      }

   }
}
