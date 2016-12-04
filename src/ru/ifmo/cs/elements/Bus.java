package ru.ifmo.cs.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.DataWidth;

public class Bus extends DataWidth implements DataSource {

   private final ArrayList inputs = new ArrayList();


   public Bus(DataSource ... inputs) {
      super(getMaxWidth(inputs));
      this.inputs.addAll(Arrays.asList(inputs));
   }

   public Bus(int width) {
      super(width);
   }

   private static int getMaxWidth(DataSource ... inputs) {
      int width = 0;
      DataSource[] arr$ = inputs;
      int len$ = inputs.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         DataSource input = arr$[i$];
         if(width < input.getWidth()) {
            width = input.getWidth();
         }
      }

      return width;
   }

   public void addInput(DataSource ... newinputs) {
      this.inputs.addAll(Arrays.asList(newinputs));
   }

   public int getValue() {
      int value = 0;

      DataSource input;
      for(Iterator i$ = this.inputs.iterator(); i$.hasNext(); value |= input.getValue()) {
         input = (DataSource)i$.next();
      }

      return value & this.mask;
   }
}
