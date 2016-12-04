package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.elements.Bus;
import ru.ifmo.cs.elements.BusSplitter;
import ru.ifmo.cs.elements.DataDestination;
import ru.ifmo.cs.elements.DataHandler;
import ru.ifmo.cs.elements.DataSource;
import ru.ifmo.cs.elements.DummyValve;
import ru.ifmo.cs.elements.PseudoRegister;
import ru.ifmo.cs.elements.Register;

public class CPU2IO {

   private final DummyValve valveio;
   private final Bus addr = new Bus(8);
   private final BusSplitter order;
   private final Bus in = new Bus(8);
   private final Bus out = new Bus(8);
   private final Bus intr;
   private final Bus flagstate = new Bus(1);
   private final DataHandler intrctrl;
   private final DummyValve valveIn;
   private final DataHandler valveClearAll;


   public CPU2IO(Register accum, Register state, Bus intrReq, DataSource valveio, DataHandler valveClearAll, DataHandler intrctrl) {
      this.valveIn = new DummyValve(this.in, new DataSource[0]);
      this.intr = intrReq;
      this.intrctrl = intrctrl;
      this.valveClearAll = valveClearAll;
      this.valveio = new DummyValve(valveio, new DataSource[]{valveio});
      this.addr.addInput(new DataSource[]{valveio});
      this.order = new BusSplitter(valveio, 8, 4);
      DummyValve valveSetState = new DummyValve(this.flagstate, new DataSource[]{valveio});
      valveSetState.addDestination(new PseudoRegister(state, 6, new DataSource[0]));
      this.valveIn.addDestination(new PseudoRegister(accum, 0, 8, new DataSource[0]));
      this.out.addInput(new DataSource[]{accum});
   }

   public DataSource getValveIO() {
      return this.valveio;
   }

   public Bus getAddr() {
      return this.addr;
   }

   public DataSource getOrder() {
      return this.order;
   }

   public Bus getOut() {
      return this.out;
   }

   public void addInInput(DataHandler ctrl) {
      this.in.addInput(new DataSource[]{ctrl});
      ctrl.addDestination(this.valveIn);
   }

   public void addFlagInput(DataSource valve) {
      this.flagstate.addInput(new DataSource[]{valve});
   }

   public void addIntrBusInput(DataSource input) {
      this.intr.addInput(new DataSource[]{input});
   }

   public void addIntrCtrlInput(DataHandler ctrl) {
      ctrl.addDestination(this.intrctrl);
   }

   public void addValveClearFlag(DataDestination valve) {
      this.valveClearAll.addDestination(valve);
   }
}
