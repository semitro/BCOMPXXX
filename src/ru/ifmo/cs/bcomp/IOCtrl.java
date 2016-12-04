package ru.ifmo.cs.bcomp;

import java.util.EnumMap;
import ru.ifmo.cs.bcomp.CPU2IO;
import ru.ifmo.cs.elements.Consts;
import ru.ifmo.cs.elements.DataComparer;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.Register;
import ru.ifmo.cs.elements.Valve;
import ru.ifmo.cs.elements.ValveDecoder;
import ru.ifmo.cs.elements.ValveOnce;

public class IOCtrl {

   private final Register flag;
   private final Register data;
   private final int addr;
   private final IOCtrl.Direction dir;
   private final Valve valveSetFlag;
   private final EnumMap signals;


   public IOCtrl(int addr, IOCtrl.Direction dir, CPU2IO cpu2io) {
      this.valveSetFlag = new Valve(Consts.consts[1], new DataSource[0]);
      this.signals = new EnumMap(IOCtrl.ControlSignal.class);
      this.addr = addr;
      this.dir = dir;
      String name = "РД ВУ" + Integer.toString(addr);
      this.data = new Register(name, name, 8, new DataSource[0]);
      DataComparer dc = new DataComparer(cpu2io.getAddr(), addr, new DataSource[]{cpu2io.getValveIO()});
      ValveDecoder order = new ValveDecoder(cpu2io.getOrder(), new DataSource[]{dc});
      Valve valveClearFlag = new Valve(Consts.consts[0], 0, new DataSource[]{order});
      this.signals.put(IOCtrl.ControlSignal.SETFLAG, new DataHandler[]{this.valveSetFlag, valveClearFlag});
      this.flag = new Register("Ф ВУ" + Integer.toString(addr), "Флаг ВУ" + Integer.toString(addr), 1, new DataSource[]{this.valveSetFlag, valveClearFlag});
      cpu2io.addIntrBusInput(this.flag);
      cpu2io.addIntrCtrlInput(valveClearFlag);
      cpu2io.addIntrCtrlInput(this.valveSetFlag);
      cpu2io.addValveClearFlag(valveClearFlag);
      ValveOnce checkFlag = new ValveOnce(this.flag, 1, new DataSource[]{order});
      cpu2io.addFlagInput(checkFlag);
      this.signals.put(IOCtrl.ControlSignal.CHKFLAG, new DataHandler[]{checkFlag});
      if(dir != IOCtrl.Direction.IN) {
         Valve valveIn = new Valve(cpu2io.getOut(), 3, new DataSource[]{order});
         valveIn.addDestination(this.data);
         this.signals.put(IOCtrl.ControlSignal.OUT, new DataHandler[]{valveIn});
      }

      if(dir != IOCtrl.Direction.OUT) {
         ValveOnce valveIn1 = new ValveOnce(this.data, 2, new DataSource[]{order});
         cpu2io.addInInput(valveIn1);
         this.signals.put(IOCtrl.ControlSignal.IN, new DataHandler[]{valveIn1});
      }

   }

   public int getFlag() {
      return this.flag.getValue();
   }

   public void setFlag() {
      this.valveSetFlag.setValue(1);
   }

   public int getData() {
      return this.data.getValue();
   }

   public Register getRegData() {
      return this.data;
   }

   public void setData(int value) throws Exception {
      if(this.dir != IOCtrl.Direction.OUT) {
         this.data.setValue(value);
      } else {
         throw new Exception("Attempt to write to the output device " + this.addr);
      }
   }

   public void addDestination(IOCtrl.ControlSignal cs, DataDestination dest) {
      DataHandler[] arr$ = (DataHandler[])this.signals.get(cs);
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         DataHandler valve = arr$[i$];
         valve.addDestination(dest);
      }

   }

   public void removeDestination(IOCtrl.ControlSignal cs, DataDestination dest) {
      DataHandler[] arr$ = (DataHandler[])this.signals.get(cs);
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         DataHandler valve = arr$[i$];
         valve.removeDestination(dest);
      }

   }

   public static enum Direction {

      IN("IN", 0),
      OUT("OUT", 1),
      INOUT("INOUT", 2);
      // $FF: synthetic field
      private static final IOCtrl.Direction[] $VALUES = new IOCtrl.Direction[]{IN, OUT, INOUT};


      private Direction(String var1, int var2) {}

   }

   public static enum ControlSignal {

      SETFLAG("SETFLAG", 0),
      CHKFLAG("CHKFLAG", 1),
      IN("IN", 2),
      OUT("OUT", 3);
      // $FF: synthetic field
      private static final IOCtrl.ControlSignal[] $VALUES = new IOCtrl.ControlSignal[]{SETFLAG, CHKFLAG, IN, OUT};


      private ControlSignal(String var1, int var2) {}

   }
}
