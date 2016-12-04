package ru.ifmo.cs.bcomp;


public class Instruction {

   private int instr;
   private String mnemonics;
   private Instruction.Type type;


   public Instruction(int instr, String mnemonics, Instruction.Type type) {
      this.instr = instr;
      this.mnemonics = mnemonics;
      this.type = type;
   }

   public int getInstr() {
      return this.instr;
   }

   public String getMnemonics() {
      return this.mnemonics;
   }

   public Instruction.Type getType() {
      return this.type;
   }

   public static enum Type {

      ADDR("ADDR", 0),
      NONADDR("NONADDR", 1),
      IO("IO", 2);
      // $FF: synthetic field
      private static final Instruction.Type[] $VALUES = new Instruction.Type[]{ADDR, NONADDR, IO};


      private Type(String var1, int var2) {}

   }
}
