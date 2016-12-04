package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.bcomp.Instruction;
import ru.ifmo.cs.bcomp.Utils;

public class MicroProgram {

   public final String microprogramName;
   public final Instruction[] instructionSet;
   public final String[][] microprogram;


   public MicroProgram(String microprogramName, Instruction[] instructionSet, String[][] microprogram) {
      this.microprogramName = microprogramName;
      this.instructionSet = instructionSet;
      this.microprogram = microprogram;
   }

   private boolean checkBit(int cmd, int bit) {
      return (cmd >> bit & 1) == 1;
   }

   private int getBits(int cmd, int startbit, int width) {
      return cmd >> startbit & (1 << width) - 1;
   }

   private String getRegister(int cmd) {
      switch(this.getBits(cmd, 12, 2)) {
      case 0:
         return "РС";
      case 1:
         return "РД";
      case 2:
         return "РК";
      case 3:
         return "А";
      default:
         return null;
      }
   }

   private String getLeftInput(int cmd) {
      switch(this.getBits(cmd, 12, 2)) {
      case 0:
         return "0";
      case 1:
         return "А";
      case 2:
         return "РС";
      case 3:
         return "КлР";
      default:
         return null;
      }
   }

   private String getRightInput(int cmd) {
      switch(this.getBits(cmd, 8, 2)) {
      case 0:
         return "0";
      case 1:
         return "РД";
      case 2:
         return "РК";
      case 3:
         return "СК";
      default:
         return null;
      }
   }

   private String getInvert(String reg, int inv) {
      return inv == 1?"COM(" + reg + ")":reg;
   }

   private String getOperation(int cmd, String left, String right) {
      switch(this.getBits(cmd, 4, 2)) {
      case 0:
         return left + " + " + right;
      case 1:
         return left + " + " + right + " + 1";
      case 2:
         return left + " & " + right;
      default:
         return null;
      }
   }

   private String getRotate(int cmd) {
      switch(this.getBits(cmd, 2, 2)) {
      case 0:
         return this.getOperation(cmd, this.getInvert(this.getLeftInput(cmd), this.getBits(cmd, 6, 1)), this.getInvert(this.getRightInput(cmd), this.getBits(cmd, 7, 1)));
      case 1:
         return "RAR(А)";
      case 2:
         return "RAL(А)";
      default:
         return null;
      }
   }

   private String getMemory(int cmd) {
      switch(this.getBits(cmd, 0, 2)) {
      case 0:
         return "";
      case 1:
         return ", ОП(РА) ==> РД";
      case 2:
         return ", РД ==> ОП(РА)";
      default:
         return null;
      }
   }

   private String getOutput(int cmd) {
      switch(this.getBits(cmd, 0, 3)) {
      case 0:
         return "";
      case 1:
         return "БР ==> РА";
      case 2:
         return "БР ==> РД";
      case 3:
         return "БР ==> РК";
      case 4:
         return "БР ==> СК";
      case 5:
         return "БР ==> А";
      default:
         return null;
      }
   }

   private String getC(int cmd) {
      switch(this.getBits(cmd, 6, 2)) {
      case 0:
         return "";
      case 1:
         return ", С";
      case 2:
         return "0 ==> C";
      case 3:
         return "1 ==> C";
      default:
         return null;
      }
   }

   public String decodeCmd(int cmd) {
      if(this.checkBit(cmd, 15)) {
         int addr = this.getBits(cmd, 0, 8);
         return "IF " + this.getRegister(cmd) + "(" + this.getBits(cmd, 8, 4) + ") = " + this.getBits(cmd, 14, 1) + " THEN " + (addr >= this.microprogram.length?"":this.microprogram[addr][0]) + "(" + Utils.toHex(addr, 8) + ")";
      } else {
         return this.checkBit(cmd, 14)?(this.checkBit(cmd, 11)?"Разрешение прерывания ":"") + (this.checkBit(cmd, 10)?"Запрещение прерывания ":"") + (this.checkBit(cmd, 9)?"Сброс флагов ВУ ":"") + (this.checkBit(cmd, 8)?"Ввод/вывод":"") + (this.checkBit(cmd, 3)?"Останов машины":"") + this.getOutput(cmd) + this.getC(cmd) + (this.checkBit(cmd, 5)?", N":"") + (this.checkBit(cmd, 4)?", Z":""):this.getRotate(cmd) + " ==> БР" + this.getMemory(cmd);
      }
   }
}
