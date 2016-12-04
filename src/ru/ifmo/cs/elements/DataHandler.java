package ru.ifmo.cs.elements;

import java.util.ArrayList;
import java.util.Iterator;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.DataStorage;

public class DataHandler extends DataStorage {

   private final ArrayList dests;


   public DataHandler(String name, int width, DataSource ... inputs) {
      super(name, width, inputs);
      this.dests = new ArrayList();
   }

   public DataHandler(int width, DataSource ... inputs) {
      this((String)null, width, inputs);
   }

   public void addDestination(DataDestination dest) {
      this.dests.add(dest);
   }

   public void removeDestination(DataDestination dest) {
      int index = this.dests.indexOf(dest);
      if(index >= 0) {
         this.dests.remove(index);
      }

   }

   public void setValue(int value) {
      super.setValue(value);
      Iterator i$ = this.dests.iterator();

      while(i$.hasNext()) {
         DataDestination dest = (DataDestination)i$.next();
         dest.setValue(this.value);
      }

   }

   public void resetValue() {
      super.setValue(0);
   }
}
