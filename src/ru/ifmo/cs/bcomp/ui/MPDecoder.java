package ru.ifmo.cs.bcomp.ui;

import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.MicroProgram;
import ru.ifmo.cs.bcomp.Utils;

public class MPDecoder {

   private final MicroProgram mp;
   private final CPU cpu;


   public MPDecoder(MicroProgram mp) throws Exception {
      this.mp = mp;
      this.cpu = new CPU(mp);
   }

   public void decode() {
      int cmd;
      for(int addr = 1; (cmd = this.cpu.getMicroMemoryValue(addr)) != 0; ++addr) {
         System.out.println(Utils.toHex(addr, 8) + "\t" + Utils.toHex(cmd, 16) + "\t" + (this.mp.microprogram[addr][0] == null?"":this.mp.microprogram[addr][0]) + "\t" + this.mp.decodeCmd(cmd));
      }

   }
}
