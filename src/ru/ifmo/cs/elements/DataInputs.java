package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.DataWidth;

public class DataInputs extends DataWidth {

   public DataInputs(String name, int width, DataSource ... inputs) {
      super(name, width);
      DataSource[] arr$ = inputs;
      int len$ = inputs.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         DataSource input = arr$[i$];
         if(input instanceof DataHandler) {
            ((DataHandler)input).addDestination((DataDestination)this);
         }
      }

   }
}
