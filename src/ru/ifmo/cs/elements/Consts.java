package ru.ifmo.cs.elements;

import ru.ifmo.cs.elements.DataConst;
import ru.ifmo.cs.elements.DataSource;

public class Consts {

   public static final DataSource[] consts = new DataSource[2];


   static {
      for(int i = 0; i < consts.length; ++i) {
         consts[i] = new DataConst(i, 1);
      }

   }
}
