package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import ru.ifmo.cs.bcomp.CPU2IO;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.ControlUnit;
import ru.ifmo.cs.bcomp.Instruction;
import ru.ifmo.cs.bcomp.MicroProgram;
import ru.ifmo.cs.bcomp.RunningCycle;
import ru.ifmo.cs.bcomp.StateReg;
import ru.ifmo.cs.elements.Bus;
import ru.ifmo.cs.elements.Consts;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.Memory;
import ru.ifmo.cs.elements.Register;

public class CPU {

   private final Bus aluOutput = new Bus(16);
   private final Bus intrReq = new Bus(1);
   private final Register regState = new Register("C", "РС", 9, new DataSource[0]);
   private final ControlUnit cu;
   private final EnumMap valves;
   private final Register regAddr;
   private final Memory mem;
   private final Register regData;
   private final Register regInstr;
   private final Register regIP;
   private final Register regAccum;
   private final Register regKey;
   private final Register regBuf;
   private final DataHandler valveRunState;
   private final DataHandler valveSetProgram;
   private final CPU2IO cpu2io;
   private volatile boolean clock;
   private final MicroProgram mp;
   private final ReentrantLock tick;
   private final ReentrantLock lock;
   private final Condition lockStart;
   private final Condition lockFinish;
   private Runnable tickStartListener;
   private Runnable tickFinishListener;
   private Runnable cpuStartListener;
   private Runnable cpuStopListener;
   private final Thread cpu;


   public CPU(MicroProgram mp) throws Exception {
      this.cu = new ControlUnit(this.aluOutput);
      this.valves = new EnumMap(ControlSignal.class);
      this.regAddr = new Register("РА", "Регистр адреса", 11, new DataSource[]{this.getValve(ControlSignal.BUF_TO_ADDR, new DataSource[]{this.aluOutput})});
      this.mem = new Memory("Память", 16, this.regAddr);
      this.regData = new Register("РД", "Регистр данных", 16, new DataSource[]{this.getValve(ControlSignal.BUF_TO_DATA, new DataSource[]{this.aluOutput}), this.getValve(ControlSignal.MEMORY_READ, new DataSource[]{this.mem})});
      this.regInstr = new Register("РК", "Регистр команд", 16, new DataSource[]{this.getValve(ControlSignal.BUF_TO_INSTR, new DataSource[]{this.aluOutput})});
      this.regIP = new Register("СК", "Счётчик команд", 11, new DataSource[]{this.getValve(ControlSignal.BUF_TO_IP, new DataSource[]{this.aluOutput})});
      this.regAccum = new Register("Акк", "Аккумулятор", 16, new DataSource[]{this.getValve(ControlSignal.BUF_TO_ACCUM, new DataSource[]{this.aluOutput})});
      this.regKey = new Register("КР", "Клавишный регистр", 16, new DataSource[0]);
      this.clock = true;
      this.tick = new ReentrantLock();
      this.lock = new ReentrantLock();
      this.lockStart = this.lock.newCondition();
      this.lockFinish = this.lock.newCondition();
      this.tickStartListener = null;
      this.tickFinishListener = null;
      this.cpuStartListener = null;
      this.cpuStopListener = null;
      this.cpu = new Thread(new Runnable() {
         public void run() {
            CPU.this.lock.lock();

            try {
               while(true) {
                  CPU.this.lockFinish.signalAll();
                  CPU.this.lockStart.await();
                  if(CPU.this.cpuStartListener != null) {
                     CPU.this.cpuStartListener.run();
                  }

                  if(CPU.this.clock) {
                     CPU.this.valveSetProgram.setValue(1);
                  }

                  do {
                     if(CPU.this.tickStartListener != null) {
                        CPU.this.tickStartListener.run();
                     }

                     CPU.this.tick.lock();

                     try {
                        CPU.this.cu.step();
                     } finally {
                        CPU.this.tick.unlock();
                     }

                     if(CPU.this.tickFinishListener != null) {
                        CPU.this.tickFinishListener.run();
                     }
                  } while(CPU.this.regState.getValue(8) == 1);

                  if(CPU.this.cpuStopListener != null) {
                     CPU.this.cpuStopListener.run();
                  }
               }
            } catch (InterruptedException var10) {
               ;
            } finally {
               CPU.this.lock.unlock();
            }

         }
      }, "BComp");
      this.getValve(ControlSignal.MEMORY_WRITE, new DataSource[]{this.regData}).addDestination(this.mem);
      this.regState.setValue(2);
      Bus aluRight = new Bus(new DataSource[]{this.getValve(ControlSignal.DATA_TO_ALU, new DataSource[]{this.regData}), this.getValve(ControlSignal.INSTR_TO_ALU, new DataSource[]{this.regInstr}), this.getValve(ControlSignal.IP_TO_ALU, new DataSource[]{this.regIP})});
      Bus aluLeft = new Bus(new DataSource[]{this.getValve(ControlSignal.ACCUM_TO_ALU, new DataSource[]{this.regAccum}), this.getValve(ControlSignal.STATE_TO_ALU, new DataSource[]{this.regState}), this.getValve(ControlSignal.KEY_TO_ALU, new DataSource[]{this.regKey})});
      DataHandler notLeft = this.getValve(ControlSignal.INVERT_LEFT, new DataSource[]{aluLeft});
      DataHandler notRight = this.getValve(ControlSignal.INVERT_RIGHT, new DataSource[]{aluRight});
      DataHandler aluplus1 = this.getValve(ControlSignal.ALU_PLUS_1, new DataSource[]{Consts.consts[1]});
      this.regBuf = new Register("БР", "Буферный регистр", 17, new DataSource[]{this.getValve(ControlSignal.ALU_AND, new DataSource[]{notLeft, notRight, aluplus1}), this.getValve(ControlSignal.SHIFT_RIGHT, new DataSource[]{this.regAccum, this.regState}), this.getValve(ControlSignal.SHIFT_LEFT, new DataSource[]{this.regAccum, this.regState})});
      this.aluOutput.addInput(new DataSource[]{this.regBuf});
      new StateReg(this.regState, 4, new DataSource[]{this.getValve(ControlSignal.DISABLE_INTERRUPTS, new DataSource[]{Consts.consts[0]}), this.getValve(ControlSignal.ENABLE_INTERRUPTS, new DataSource[]{Consts.consts[1]})});
      new StateReg(this.regState, 0, new DataSource[]{this.getValve(ControlSignal.BUF_TO_STATE_C, new DataSource[]{this.regBuf}), this.getValve(ControlSignal.CLEAR_STATE_C, new DataSource[]{Consts.consts[0]}), this.getValve(ControlSignal.SET_STATE_C, new DataSource[]{Consts.consts[1]})});
      new StateReg(this.regState, 2, new DataSource[]{this.getValve(ControlSignal.BUF_TO_STATE_N, new DataSource[]{this.regBuf})});
      new StateReg(this.regState, 1, new DataSource[]{this.getValve(ControlSignal.BUF_TO_STATE_Z, new DataSource[]{this.regBuf})});
      new StateReg(this.regState, 8, new DataSource[]{this.getValve(ControlSignal.HALT, new DataSource[]{Consts.consts[0]}), this.valveSetProgram = this.getValve(ControlSignal.SET_PROGRAM, new DataSource[0])});
      DataHandler intrctrl = this.getValve(ControlSignal.SET_REQUEST_INTERRUPT, new DataSource[]{this.regState, this.intrReq, this.getValve(ControlSignal.DISABLE_INTERRUPTS, new DataSource[0]), this.getValve(ControlSignal.ENABLE_INTERRUPTS, new DataSource[0])});
      new StateReg(this.regState, 5, new DataSource[]{intrctrl});
      this.cpu2io = new CPU2IO(this.regAccum, this.regState, this.intrReq, this.getValve(ControlSignal.INPUT_OUTPUT, new DataSource[]{this.regData}), this.getValve(ControlSignal.CLEAR_ALL_FLAGS, new DataSource[]{Consts.consts[1]}), intrctrl);
      this.valveRunState = this.getValve(ControlSignal.SET_RUN_STATE, new DataSource[0]);
      new StateReg(this.regState, 7, new DataSource[]{this.valveRunState});
      this.cu.compileMicroProgram(this.mp = mp);
      this.cu.jump(8);
   }

   void startCPU() throws InterruptedException {
      this.lock.lock();

      try {
         this.cpu.start();
         this.lockFinish.await();
      } finally {
         this.lock.unlock();
      }

   }

   public void stopCPU() {
      this.cpu.interrupt();
   }

   private DataHandler getValve(ControlSignal cs, DataSource ... inputs) {
      DataHandler valve = (DataHandler)this.valves.get(cs);
      if(valve == null) {
         this.valves.put(cs, valve = this.cu.createValve(cs, inputs));
      }

      return valve;
   }

   public CPU2IO getCPU2IO() {
      return this.cpu2io;
   }

   public void setTickStartListener(Runnable tickStartListener) {
      this.tickStartListener = tickStartListener;
   }

   public void setTickFinishListener(Runnable tickFinishListener) {
      this.tickFinishListener = tickFinishListener;
   }

   public void setCPUStartListener(Runnable cpuStartListener) {
      this.cpuStartListener = cpuStartListener;
   }

   public void setCPUStopListener(Runnable cpuStopListener) {
      this.cpuStopListener = cpuStopListener;
   }

   void tickLock() {
      this.tick.lock();
   }

   void tickUnlock() {
      this.tick.unlock();
   }

   void addDestination(ControlSignal cs, DataDestination dest) {
      ((DataHandler)this.valves.get(cs)).addDestination(dest);
   }

   void removeDestination(ControlSignal cs, DataDestination dest) {
      ((DataHandler)this.valves.get(cs)).removeDestination(dest);
   }

   public Register getRegister(CPU.Reg reg) {
      switch(CPU.NamelessClass418855688.$SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[reg.ordinal()]) {
      case 1:
         return this.regAccum;
      case 2:
         return this.regBuf;
      case 3:
         return this.regData;
      case 4:
         return this.regAddr;
      case 5:
         return this.regIP;
      case 6:
         return this.regInstr;
      case 7:
         return this.regState;
      case 8:
         return this.regKey;
      case 9:
         return this.cu.getIP();
      case 10:
         return this.cu.getInstr();
      default:
         return null;
      }
   }

   public CPU.Reg findRegister(String reg) {
      return this.regAccum.name.equals(reg)?CPU.Reg.ACCUM:(this.regBuf.name.equals(reg)?CPU.Reg.BUF:(this.regData.name.equals(reg)?CPU.Reg.DATA:(this.regAddr.name.equals(reg)?CPU.Reg.ADDR:(this.regIP.name.equals(reg)?CPU.Reg.IP:(this.regInstr.name.equals(reg)?CPU.Reg.INSTR:(this.regState.name.equals(reg)?CPU.Reg.STATE:(this.regKey.name.equals(reg)?CPU.Reg.KEY:(this.cu.getIP().name.equals(reg)?CPU.Reg.MIP:(this.cu.getInstr().name.equals(reg)?CPU.Reg.MINSTR:null)))))))));
   }

   public int getRegValue(CPU.Reg reg) {
      return this.getRegister(reg).getValue();
   }

   public int getRegWidth(CPU.Reg reg) {
      return this.getRegister(reg).getWidth();
   }

   public int getStateValue(int startbit) {
      return this.regState.getValue(startbit);
   }

   public boolean isRunning() {
      return this.lock.isLocked();
   }

   public Memory getMemory() {
      return this.mem;
   }

   public int getMemoryValue(int addr) {
      return this.mem.getValue(addr);
   }

   public Memory getMicroMemory() {
      return this.cu.getMemory();
   }

   public int getMicroMemoryValue(int addr) {
      return this.cu.getMemoryValue(addr);
   }

   public void setRegKey(int value) {
      this.regKey.setValue(value);
   }

   public void setRunState(boolean state) {
      this.tick.lock();

      try {
         this.valveRunState.setValue(state?1:0);
      } finally {
         this.tick.unlock();
      }

   }

   public void invertRunState() {
      this.tick.lock();

      try {
         this.valveRunState.setValue(~this.regState.getValue(7));
      } finally {
         this.tick.unlock();
      }

   }

   public boolean getClockState() {
      return this.clock;
   }

   public void setClockState(boolean clock) {
      this.tick.lock();

      try {
         this.clock = clock;
         if(!clock) {
            this.valveSetProgram.setValue(0);
         }
      } finally {
         this.tick.unlock();
      }

   }

   public boolean invertClockState() {
      this.setClockState(!this.clock);
      return this.clock;
   }

   private void jump(int label) {
      if(label != -1) {
         this.cu.jump(label);
      }

   }

   private boolean startFrom(int label) {
      if(this.lock.tryLock()) {
         try {
            this.jump(label);
            this.lockStart.signal();
         } finally {
            this.lock.unlock();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean startSetAddr() {
      return this.startFrom(4);
   }

   public boolean startWrite() {
      return this.startFrom(6);
   }

   public boolean startRead() {
      return this.startFrom(5);
   }

   public boolean startStart() {
      return this.startFrom(7);
   }

   public boolean startContinue() {
      return this.startFrom(-1);
   }

   private boolean runFrom(int label) {
      if(this.lock.tryLock()) {
         try {
            this.jump(label);
            this.lockStart.signal();
            this.lockFinish.await();
         } catch (InterruptedException var6) {
            ;
         } finally {
            this.lock.unlock();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean runSetAddr() {
      return this.runFrom(4);
   }

   public boolean runSetAddr(int addr) {
      this.setRegKey(addr);
      return this.runSetAddr();
   }

   public boolean runWrite() {
      return this.runFrom(6);
   }

   public boolean runWrite(int value) {
      this.setRegKey(value);
      return this.runWrite();
   }

   public boolean runRead() {
      return this.runFrom(5);
   }

   public boolean runStart() {
      return this.runFrom(7);
   }

   public boolean runContinue() {
      return this.runFrom(-1);
   }

   public boolean runSetMAddr() {
      if(this.lock.tryLock()) {
         try {
            this.cu.setIP(this.regKey.getValue());
         } finally {
            this.lock.unlock();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean runMWrite() {
      if(this.lock.tryLock()) {
         try {
            this.cu.setMemory(this.regKey.getValue());
         } finally {
            this.lock.unlock();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean runMRead() {
      if(this.lock.tryLock()) {
         try {
            this.cu.readInstr();
         } finally {
            this.lock.unlock();
         }

         return true;
      } else {
         return false;
      }
   }

   public MicroProgram getMicroProgram() {
      return this.mp;
   }

   public String getMicroProgramName() {
      return this.mp.microprogramName;
   }

   public int getIntrCycleStartAddr() {
      return this.cu.getIntrCycleStartAddr();
   }

   public Instruction[] getInstructionSet() {
      return this.mp.instructionSet;
   }

   public RunningCycle getRunningCycle() {
      return this.cu.getCycle();
   }

   protected void finalize() throws Throwable {
      super.finalize();
      this.stopCPU();
   }

   // $FF: synthetic class
   static class NamelessClass418855688 {

      // $FF: synthetic field
      static final int[] $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg = new int[CPU.Reg.values().length];


      static {
         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.ACCUM.ordinal()] = 1;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.BUF.ordinal()] = 2;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.DATA.ordinal()] = 3;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.ADDR.ordinal()] = 4;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.IP.ordinal()] = 5;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.INSTR.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.STATE.ordinal()] = 7;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.KEY.ordinal()] = 8;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.MIP.ordinal()] = 9;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.MINSTR.ordinal()] = 10;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum Reg {

      ACCUM("ACCUM", 0),
      BUF("BUF", 1),
      DATA("DATA", 2),
      ADDR("ADDR", 3),
      IP("IP", 4),
      INSTR("INSTR", 5),
      STATE("STATE", 6),
      KEY("KEY", 7),
      MIP("MIP", 8),
      MINSTR("MINSTR", 9);
      // $FF: synthetic field
      private static final CPU.Reg[] $VALUES = new CPU.Reg[]{ACCUM, BUF, DATA, ADDR, IP, INSTR, STATE, KEY, MIP, MINSTR};


      private Reg(String var1, int var2) {}

   }
}
