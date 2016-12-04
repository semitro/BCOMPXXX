package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataCtrl;
import ru.ifmo.cs.elements.DataSource;

public class DataAdder extends DataCtrl {

   private DataSource left;
   private DataSource right;
   private DataSource c;


   public DataAdder(String name, DataSource left, DataSource right, DataSource c, DataSource ... ctrls) {
      super(name, left.getWidth() + 1, ctrls);
      this.left = left;
      this.right = right;
      this.c = c;
   }

   public void setValue(int ctrl) {
      int c = this.c.getValue();
      super.setValue(this.isOpen(ctrl)?this.left.getValue() & this.right.getValue():this.left.getValue() + this.right.getValue() + c);
   }
}
