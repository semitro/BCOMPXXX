package ru.ifmo.cs.bcomp;

import java.util.HashMap;
import java.util.Set;
import ru.ifmo.cs.bcomp.BaseMicroProgram;
import ru.ifmo.cs.bcomp.ExtendedMicroProgram;
import ru.ifmo.cs.bcomp.MicroProgram;
import ru.ifmo.cs.bcomp.OptimizedMicroProgram;

public class MicroPrograms {

   public static final String DEFAULT_MICROPROGRAM = "base";
   private static final HashMap microprograms = new HashMap();


   public static MicroProgram getMicroProgram(String mptype) throws Exception {
      return (MicroProgram)((Class)microprograms.get(mptype)).newInstance();
   }

   public static Set getMicroProgramsList() {
      return microprograms.keySet();
   }

   static {
      microprograms.put("base", BaseMicroProgram.class);
      microprograms.put("optimized", OptimizedMicroProgram.class);
      microprograms.put("extended", ExtendedMicroProgram.class);
   }
}
