package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataInputs;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.Register;

public class PseudoRegister extends DataInputs implements DataDestination {

   private Register reg;
   private int startbit;


   public PseudoRegister(String name, Register reg, int startbit, int width, DataSource ... inputs) {
      super(name, width, inputs);
      this.reg = reg;
      this.startbit = startbit;
   }

   public PseudoRegister(String name, Register reg, int startbit, DataSource ... inputs) {
      this(name, reg, startbit, 1, inputs);
   }

   public PseudoRegister(Register reg, int startbit, int width, DataSource ... inputs) {
      this((String)null, reg, startbit, width, inputs);
   }

   public PseudoRegister(Register reg, int startbit, DataSource ... inputs) {
      this(reg, startbit, 1, inputs);
   }

   public void setValue(int value) {
      this.reg.setValue(value, this.startbit, this.width);
   }
}
