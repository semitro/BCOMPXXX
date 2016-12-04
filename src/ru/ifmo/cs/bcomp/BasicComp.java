package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.CPU2IO;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.IODevTimer;
import ru.ifmo.cs.bcomp.MicroProgram;
import ru.ifmo.cs.bcomp.SignalListener;
import ru.ifmo.cs.elements.DataDestination;

public class BasicComp {

   private final CPU cpu;
   private final IOCtrl[] ioctrls;
   private final IODevTimer timer;


   public BasicComp(MicroProgram mp) throws Exception {
      this.cpu = new CPU(mp);
      this.cpu.startCPU();
      CPU2IO cpu2io = this.cpu.getCPU2IO();
      this.ioctrls = new IOCtrl[]{new IOCtrl(0, IOCtrl.Direction.OUT, cpu2io), new IOCtrl(1, IOCtrl.Direction.OUT, cpu2io), new IOCtrl(2, IOCtrl.Direction.IN, cpu2io), new IOCtrl(3, IOCtrl.Direction.INOUT, cpu2io), new IOCtrl(4, IOCtrl.Direction.OUT, cpu2io), new IOCtrl(5, IOCtrl.Direction.OUT, cpu2io), new IOCtrl(6, IOCtrl.Direction.OUT, cpu2io), new IOCtrl(7, IOCtrl.Direction.IN, cpu2io), new IOCtrl(8, IOCtrl.Direction.IN, cpu2io), new IOCtrl(9, IOCtrl.Direction.INOUT, cpu2io)};
      this.timer = new IODevTimer(this.ioctrls[0]);
   }

   public CPU getCPU() {
      return this.cpu;
   }

   public IOCtrl[] getIOCtrls() {
      return this.ioctrls;
   }

   public void startTimer() {
      this.timer.start("IO0");
   }

   public void stopTimer() {
      this.timer.done();
   }

   private void ctrlDestination(ControlSignal cs, DataDestination dest, boolean remove) {
      this.cpu.tickLock();

      try {
         byte iodev;
         IOCtrl.ControlSignal iocs;
         switch(BasicComp.NamelessClass1423149647.$SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[cs.ordinal()]) {
         case 1:
            iodev = 0;
            iocs = IOCtrl.ControlSignal.CHKFLAG;
            break;
         case 2:
            iodev = 1;
            iocs = IOCtrl.ControlSignal.CHKFLAG;
            break;
         case 3:
            iodev = 1;
            iocs = IOCtrl.ControlSignal.SETFLAG;
            break;
         case 4:
            iodev = 1;
            iocs = IOCtrl.ControlSignal.OUT;
            break;
         case 5:
            iodev = 2;
            iocs = IOCtrl.ControlSignal.CHKFLAG;
            break;
         case 6:
            iodev = 2;
            iocs = IOCtrl.ControlSignal.SETFLAG;
            break;
         case 7:
            iodev = 2;
            iocs = IOCtrl.ControlSignal.IN;
            break;
         case 8:
            iodev = 3;
            iocs = IOCtrl.ControlSignal.CHKFLAG;
            break;
         case 9:
            iodev = 3;
            iocs = IOCtrl.ControlSignal.SETFLAG;
            break;
         case 10:
            iodev = 3;
            iocs = IOCtrl.ControlSignal.IN;
            break;
         case 11:
            iodev = 3;
            iocs = IOCtrl.ControlSignal.OUT;
            break;
         case 12:
            iodev = 7;
            iocs = IOCtrl.ControlSignal.IN;
            break;
         case 13:
            iodev = 8;
            iocs = IOCtrl.ControlSignal.IN;
            break;
         case 14:
            iodev = 9;
            iocs = IOCtrl.ControlSignal.IN;
            break;
         default:
            if(remove) {
               this.cpu.removeDestination(cs, dest);
            } else {
               this.cpu.addDestination(cs, dest);
            }

            return;
         }

         if(remove) {
            this.ioctrls[iodev].removeDestination(iocs, dest);
         } else {
            this.ioctrls[iodev].addDestination(iocs, dest);
         }

      } finally {
         this.cpu.tickUnlock();
      }
   }

   public void addDestination(ControlSignal cs, DataDestination dest) {
      this.ctrlDestination(cs, dest, false);
   }

   public void removeDestination(ControlSignal cs, DataDestination dest) {
      this.ctrlDestination(cs, dest, true);
   }

   public void addDestination(SignalListener[] listeners) {
      this.cpu.tickLock();

      try {
         SignalListener[] arr$ = listeners;
         int len$ = listeners.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            SignalListener listener = arr$[i$];
            ControlSignal[] arr$1 = listener.signals;
            int len$1 = arr$1.length;

            for(int i$1 = 0; i$1 < len$1; ++i$1) {
               ControlSignal signal = arr$1[i$1];
               this.addDestination(signal, listener.dest);
            }
         }
      } finally {
         this.cpu.tickUnlock();
      }

   }

   public void removeDestination(SignalListener[] listeners) {
      this.cpu.tickLock();

      try {
         SignalListener[] arr$ = listeners;
         int len$ = listeners.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            SignalListener listener = arr$[i$];
            ControlSignal[] arr$1 = listener.signals;
            int len$1 = arr$1.length;

            for(int i$1 = 0; i$1 < len$1; ++i$1) {
               ControlSignal signal = arr$1[i$1];
               this.removeDestination(signal, listener.dest);
            }
         }
      } finally {
         this.cpu.tickUnlock();
      }

   }

   // $FF: synthetic class
   static class NamelessClass1423149647 {

      // $FF: synthetic field
      static final int[] $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal = new int[ControlSignal.values().length];


      static {
         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO0_TSF.ordinal()] = 1;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO1_TSF.ordinal()] = 2;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO1_SETFLAG.ordinal()] = 3;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO1_OUT.ordinal()] = 4;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO2_TSF.ordinal()] = 5;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO2_SETFLAG.ordinal()] = 6;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO2_IN.ordinal()] = 7;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO3_TSF.ordinal()] = 8;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO3_SETFLAG.ordinal()] = 9;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO3_IN.ordinal()] = 10;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO3_OUT.ordinal()] = 11;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO7_IN.ordinal()] = 12;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO8_IN.ordinal()] = 13;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO9_IN.ordinal()] = 14;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
