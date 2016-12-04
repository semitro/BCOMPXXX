package ru.ifmo.cs.bcomp;


public enum RunningCycle {

   INSTR_FETCH("INSTR_FETCH", 0),
   ADDR_FETCH("ADDR_FETCH", 1),
   EXECUTION("EXECUTION", 2),
   INTERRUPT("INTERRUPT", 3),
   PANEL("PANEL", 4),
   NONE("NONE", 5);
   // $FF: synthetic field
   private static final RunningCycle[] $VALUES = new RunningCycle[]{INSTR_FETCH, ADDR_FETCH, EXECUTION, INTERRUPT, PANEL, NONE};


   private RunningCycle(String var1, int var2) {}

}
