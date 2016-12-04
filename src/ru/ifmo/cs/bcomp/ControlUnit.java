package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.MicroIP;
import ru.ifmo.cs.bcomp.MicroProgram;
import ru.ifmo.cs.bcomp.RunningCycle;
import ru.ifmo.cs.elements.Bus;
import ru.ifmo.cs.elements.Comparer;
import ru.ifmo.cs.elements.Consts;
import ru.ifmo.cs.elements.DataAdder;
import ru.ifmo.cs.elements.DataAnd;
import ru.ifmo.cs.elements.DataCheckZero;
import ru.ifmo.cs.elements.DataDecoder;
import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataInverter;
import ru.ifmo.cs.elements.DataPart;
import ru.ifmo.cs.elements.DataRotateLeft;
import ru.ifmo.cs.elements.DataRotateRight;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.DataStorage;
import ru.ifmo.cs.elements.DummyValve;
import ru.ifmo.cs.elements.ForcedValve;
import ru.ifmo.cs.elements.Inverter;
import ru.ifmo.cs.elements.Memory;
import ru.ifmo.cs.elements.Register;
import ru.ifmo.cs.elements.Valve;
import ru.ifmo.cs.elements.ValveOnce;

public class ControlUnit {

   private final MicroIP ip = new MicroIP("СМ", "Счётчик МК", 8);
   private final Memory mem;
   private final Valve clock;
   private final Register instr;
   private final EnumMap decoders;
   private final DataHandler vr00;
   private final DataHandler vr01;
   private final DataHandler valve4ctrlcmd;
   private static final String[] labels = new String[]{"ADDRGET", "EXEC", "INTR", "EXECCNT", "ADDR", "READ", "WRITE", "START", "STP"};
   private final int[] labelsaddr;
   static final int NO_LABEL = -1;
   private static final int LABEL_CYCLE_ADDR = 0;
   private static final int LABEL_CYCLE_EXEC = 1;
   private static final int LABEL_CYCLE_INTR = 2;
   private static final int LABEL_CYCLE_EXECCNT = 3;
   static final int LABEL_ADDR = 4;
   static final int LABEL_READ = 5;
   static final int LABEL_WRITE = 6;
   static final int LABEL_START = 7;
   static final int LABEL_STP = 8;


   public ControlUnit(Bus aluOutput) {
      this.mem = new Memory("Память МК", 16, this.ip);
      this.clock = new Valve(this.mem, new DataSource[0]);
      this.instr = new Register("РМ", "Регистр микрокоманд", 16, new DataSource[]{this.clock});
      this.decoders = new EnumMap(ControlUnit.Decoders.class);
      this.labelsaddr = new int[labels.length];
      Valve vr0 = new Valve(this.clock, new DataSource[]{new Inverter(15, new DataSource[]{this.clock})});
      this.vr00 = new Valve(vr0, new DataSource[]{new Inverter(14, new DataSource[]{vr0})});
      this.decoders.put(ControlUnit.Decoders.LEFT_INPUT, new DataDecoder(this.vr00, 12, 2));
      this.decoders.put(ControlUnit.Decoders.RIGHT_INPUT, new DataDecoder(this.vr00, 8, 2));
      this.vr01 = new Valve(vr0, 14, new DataSource[]{vr0});
      this.decoders.put(ControlUnit.Decoders.FLAG_C, new DataDecoder(this.vr01, 6, 2));
      this.decoders.put(ControlUnit.Decoders.BR_TO, new DataDecoder(this.vr01, 0, 3));
      Valve vr1 = new Valve(this.clock, 15, new DataSource[]{this.clock});
      this.decoders.put(ControlUnit.Decoders.CONTROL_CMD_REG, new DataDecoder(vr1, 12, 2));
      this.valve4ctrlcmd = new DummyValve(Consts.consts[0], new DataSource[]{vr1});
      DataDecoder bitselector = new DataDecoder(vr1, 8, 4);
      Valve[] bits = new Valve[16];

      for(int writeMIP = 0; writeMIP < 16; ++writeMIP) {
         bits[writeMIP] = new Valve(aluOutput, writeMIP, 1, writeMIP, new DataSource[]{bitselector});
      }

      ForcedValve var7 = new ForcedValve(vr1, 8, new DataSource[]{new Comparer(vr1, 14, bits), new DummyValve(Consts.consts[0], new DataSource[]{vr0})});
      var7.addDestination(this.ip);
   }

   public DataHandler createValve(ControlSignal cs, DataSource ... inputs) {
      switch(ControlUnit.NamelessClass264558126.$SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[cs.ordinal()]) {
      case 1:
         return new Valve("В0", inputs[0], 3, new DataSource[]{this.vr01});
      case 2:
         return new ValveOnce("В1", inputs[0], 1, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.RIGHT_INPUT), (DataSource)this.decoders.get(ControlUnit.Decoders.CONTROL_CMD_REG)});
      case 3:
         return new ValveOnce("В2", inputs[0], 2, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.RIGHT_INPUT), (DataSource)this.decoders.get(ControlUnit.Decoders.CONTROL_CMD_REG)});
      case 4:
         return new ValveOnce("В3", inputs[0], 3, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.RIGHT_INPUT)});
      case 5:
         return new ValveOnce("В4", inputs[0], new DataSource[]{new DataPart(1, new DataStorage[]{(DataStorage)this.decoders.get(ControlUnit.Decoders.LEFT_INPUT)}), new DataPart(3, new DataStorage[]{(DataStorage)this.decoders.get(ControlUnit.Decoders.CONTROL_CMD_REG)})});
      case 6:
         return new ValveOnce("В5", inputs[0], new DataSource[]{new DataPart(2, new DataStorage[]{(DataStorage)this.decoders.get(ControlUnit.Decoders.LEFT_INPUT)}), new DataPart(0, new DataStorage[]{(DataStorage)this.decoders.get(ControlUnit.Decoders.CONTROL_CMD_REG)})});
      case 7:
         return new ValveOnce("В6", inputs[0], 3, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.LEFT_INPUT)});
      case 8:
         return new DataInverter("В7", inputs[0], new DataSource[]{new DataPart(6, new DataStorage[]{this.vr00}), this.valve4ctrlcmd});
      case 9:
         return new DataInverter("В8", inputs[0], new DataSource[]{new DataPart(7, new DataStorage[]{this.vr00}), this.valve4ctrlcmd});
      case 10:
         return new DataAdder("В9", inputs[0], inputs[1], inputs[2], new DataSource[]{new DataPart(5, new DataStorage[]{this.vr00}), this.valve4ctrlcmd});
      case 11:
         return new ValveOnce("В10", inputs[0], 4, new DataSource[]{this.vr00});
      case 12:
         return new DataRotateRight("В11", inputs[0], inputs[1], 2, new DataSource[]{this.vr00});
      case 13:
         return new DataRotateLeft("В12", inputs[0], inputs[1], 3, new DataSource[]{this.vr00});
      case 14:
         return new Valve("В13", inputs[0], 16, 1, 1, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.FLAG_C)});
      case 15:
         return new Valve("В14", inputs[0], 15, 1, 5, new DataSource[]{this.vr01});
      case 16:
         return new DataCheckZero("В15", inputs[0], 16, 4, new DataSource[]{this.vr01});
      case 17:
         return new Valve("В16", inputs[0], 2, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.FLAG_C)});
      case 18:
         return new Valve("В17", inputs[0], 3, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.FLAG_C)});
      case 19:
         return new Valve("В18", inputs[0], 1, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.BR_TO)});
      case 20:
         return new Valve("В19", inputs[0], 2, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.BR_TO)});
      case 21:
         return new Valve("В20", inputs[0], 3, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.BR_TO)});
      case 22:
         return new Valve("В21", inputs[0], 4, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.BR_TO)});
      case 23:
         return new Valve("В22", inputs[0], 5, new DataSource[]{(DataSource)this.decoders.get(ControlUnit.Decoders.BR_TO)});
      case 24:
         return new Valve("В23", inputs[0], 0, new DataSource[]{this.vr00});
      case 25:
         return new Valve("В24", inputs[0], 1, new DataSource[]{this.vr00});
      case 26:
         return new Valve("В25", inputs[0], 8, new DataSource[]{this.vr01});
      case 27:
         return new Valve("В26", inputs[0], 9, new DataSource[]{this.vr01});
      case 28:
         return new Valve("В27", inputs[0], 10, new DataSource[]{this.vr01});
      case 29:
         return new Valve("В28", inputs[0], 11, new DataSource[]{this.vr01});
      case 30:
         return new DataPart(0, new DataStorage[0]);
      case 31:
         return new DataPart(0, new DataStorage[0]);
      case 32:
         return new DataAnd(inputs[0], 4, inputs[1], new DataSource[]{inputs[2], inputs[3]});
      default:
         return null;
      }
   }

   private int getLabelAddr(String[][] mp, String label) {
      for(int i = 0; i < mp.length; ++i) {
         if(mp[i][0] != null && mp[i][0].equals(label)) {
            return i;
         }
      }

      return -1;
   }

   public void compileMicroProgram(MicroProgram mpsrc) throws Exception {
      String[][] mp = mpsrc.microprogram;

      int i;
      for(i = 0; i < this.labelsaddr.length; this.labelsaddr[i++] = 0) {
         ;
      }

      for(i = 0; i < mp.length; ++i) {
         int cmd = Integer.parseInt(mp[i][1], 16);
         int label;
         if(mp[i][0] != null) {
            for(label = 0; label < labels.length; ++label) {
               if(mp[i][0].equals(labels[label])) {
                  this.labelsaddr[label] = i;
               }
            }
         }

         if(mp[i][2] != null) {
            label = this.getLabelAddr(mp, mp[i][2]);
            if(label < 0) {
               throw new Exception("Label " + mp[i][2] + " not found!");
            }

            cmd += label;
         }

         this.mem.setValue(i, cmd);
      }

      for(i = 0; i < labels.length; ++i) {
         if(this.labelsaddr[i] == 0) {
            throw new Exception("Required label \'" + labels[i] + "\' not found");
         }
      }

   }

   public Register getIP() {
      return this.ip;
   }

   public int getIPValue() {
      return this.ip.getValue();
   }

   public void setIP(int value) {
      this.ip.setValue(value);
   }

   public void jump(int label) {
      this.ip.setValue(this.labelsaddr[label]);
   }

   public Register getInstr() {
      return this.instr;
   }

   public int getInstrValue() {
      return this.instr.getValue();
   }

   public void readInstr() {
      this.instr.setValue(this.mem.getValue());
      this.setIP(0);
   }

   public Memory getMemory() {
      return this.mem;
   }

   public int getMemoryValue(int addr) {
      return this.mem.getValue(addr);
   }

   public void setMemory(int value) {
      this.mem.setValue(value);
      this.ip.setValue(0);
   }

   public void step() {
      this.clock.setValue(1);
   }

   public RunningCycle getCycle() {
      int ipvalue = this.ip.getValue();
      return ipvalue < this.labelsaddr[0]?RunningCycle.INSTR_FETCH:(ipvalue < this.labelsaddr[1]?RunningCycle.ADDR_FETCH:(ipvalue < this.labelsaddr[2]?(ipvalue == this.labelsaddr[8]?RunningCycle.NONE:RunningCycle.EXECUTION):(ipvalue < this.labelsaddr[4]?RunningCycle.INTERRUPT:(ipvalue < this.labelsaddr[3]?RunningCycle.PANEL:RunningCycle.EXECUTION))));
   }

   public int getIntrCycleStartAddr() {
      return this.labelsaddr[2];
   }


   private static enum Decoders {

      LEFT_INPUT("LEFT_INPUT", 0),
      RIGHT_INPUT("RIGHT_INPUT", 1),
      FLAG_C("FLAG_C", 2),
      BR_TO("BR_TO", 3),
      CONTROL_CMD_REG("CONTROL_CMD_REG", 4);
      // $FF: synthetic field
      private static final ControlUnit.Decoders[] $VALUES = new ControlUnit.Decoders[]{LEFT_INPUT, RIGHT_INPUT, FLAG_C, BR_TO, CONTROL_CMD_REG};


      private Decoders(String var1, int var2) {}

   }

   // $FF: synthetic class
   static class NamelessClass264558126 {

      // $FF: synthetic field
      static final int[] $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal = new int[ControlSignal.values().length];


      static {
         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.HALT.ordinal()] = 1;
         } catch (NoSuchFieldError var32) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.DATA_TO_ALU.ordinal()] = 2;
         } catch (NoSuchFieldError var31) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.INSTR_TO_ALU.ordinal()] = 3;
         } catch (NoSuchFieldError var30) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IP_TO_ALU.ordinal()] = 4;
         } catch (NoSuchFieldError var29) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.ACCUM_TO_ALU.ordinal()] = 5;
         } catch (NoSuchFieldError var28) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.STATE_TO_ALU.ordinal()] = 6;
         } catch (NoSuchFieldError var27) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.KEY_TO_ALU.ordinal()] = 7;
         } catch (NoSuchFieldError var26) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.INVERT_LEFT.ordinal()] = 8;
         } catch (NoSuchFieldError var25) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.INVERT_RIGHT.ordinal()] = 9;
         } catch (NoSuchFieldError var24) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.ALU_AND.ordinal()] = 10;
         } catch (NoSuchFieldError var23) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.ALU_PLUS_1.ordinal()] = 11;
         } catch (NoSuchFieldError var22) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.SHIFT_RIGHT.ordinal()] = 12;
         } catch (NoSuchFieldError var21) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.SHIFT_LEFT.ordinal()] = 13;
         } catch (NoSuchFieldError var20) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.BUF_TO_STATE_C.ordinal()] = 14;
         } catch (NoSuchFieldError var19) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.BUF_TO_STATE_N.ordinal()] = 15;
         } catch (NoSuchFieldError var18) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.BUF_TO_STATE_Z.ordinal()] = 16;
         } catch (NoSuchFieldError var17) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.CLEAR_STATE_C.ordinal()] = 17;
         } catch (NoSuchFieldError var16) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.SET_STATE_C.ordinal()] = 18;
         } catch (NoSuchFieldError var15) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.BUF_TO_ADDR.ordinal()] = 19;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.BUF_TO_DATA.ordinal()] = 20;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.BUF_TO_INSTR.ordinal()] = 21;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.BUF_TO_IP.ordinal()] = 22;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.BUF_TO_ACCUM.ordinal()] = 23;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.MEMORY_READ.ordinal()] = 24;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.MEMORY_WRITE.ordinal()] = 25;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.INPUT_OUTPUT.ordinal()] = 26;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.CLEAR_ALL_FLAGS.ordinal()] = 27;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.DISABLE_INTERRUPTS.ordinal()] = 28;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.ENABLE_INTERRUPTS.ordinal()] = 29;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.SET_RUN_STATE.ordinal()] = 30;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.SET_PROGRAM.ordinal()] = 31;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.SET_REQUEST_INTERRUPT.ordinal()] = 32;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
