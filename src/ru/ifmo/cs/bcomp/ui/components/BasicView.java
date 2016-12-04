package ru.ifmo.cs.bcomp.ui.components;

import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.SignalListener;
import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.bcomp.ui.components.ALUView;
import ru.ifmo.cs.bcomp.ui.components.BCompPanel;
import ru.ifmo.cs.bcomp.ui.components.BusView;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;
import ru.ifmo.cs.bcomp.ui.components.RegisterProperties;
import ru.ifmo.cs.bcomp.ui.components.RunningCycleView;

public class BasicView extends BCompPanel {

   private final CPU cpu;
   private final RunningCycleView cycleview;


   public BasicView(GUI gui) {
      super(gui.getComponentManager(), new RegisterProperties[]{new RegisterProperties(CPU.Reg.ADDR, DisplayStyles.REG_ACCUM_X_BV, 17, false), new RegisterProperties(CPU.Reg.DATA, DisplayStyles.REG_ACCUM_X_BV, 86, false), new RegisterProperties(CPU.Reg.IP, DisplayStyles.REG_IP_X_BV, 155, false), new RegisterProperties(CPU.Reg.INSTR, DisplayStyles.REG_INSTR_X_BV, 17, false), new RegisterProperties(CPU.Reg.ACCUM, DisplayStyles.REG_ACCUM_X_BV, 376, false), new RegisterProperties(CPU.Reg.STATE, DisplayStyles.REG_C_X_BV, 376, false)}, new BusView[]{new BusView(new int[][]{{DisplayStyles.BUS_RIGHT_X1, 143}, {DisplayStyles.BUS_RIGHT_X1, 224}, {DisplayStyles.BUS_RIGHT_X, 224}, {DisplayStyles.BUS_RIGHT_X, 231}}, new ControlSignal[]{ControlSignal.DATA_TO_ALU}), new BusView(new int[][]{{DisplayStyles.BUS_FROM_INSTR_X, 74}, {DisplayStyles.BUS_FROM_INSTR_X, 224}, {DisplayStyles.BUS_RIGHT_X, 224}, {DisplayStyles.BUS_RIGHT_X, 231}}, new ControlSignal[]{ControlSignal.INSTR_TO_ALU}), new BusView(new int[][]{{DisplayStyles.BUS_FROM_IP_X, 181}, {DisplayStyles.BUS_RIGHT_X1, 181}, {DisplayStyles.BUS_RIGHT_X1, 224}, {DisplayStyles.BUS_RIGHT_X, 224}, {DisplayStyles.BUS_RIGHT_X, 231}}, new ControlSignal[]{ControlSignal.IP_TO_ALU}), new BusView(new int[][]{{DisplayStyles.BUS_FROM_ACCUM_X, 402}, {DisplayStyles.BUS_LEFT_INPUT_X1, 402}, {DisplayStyles.BUS_LEFT_INPUT_X1, 224}, {DisplayStyles.BUS_LEFT_INPUT_X, 224}, {DisplayStyles.BUS_LEFT_INPUT_X, 231}}, new ControlSignal[]{ControlSignal.ACCUM_TO_ALU}), new BusView(new int[][]{{DisplayStyles.BUS_LEFT_INPUT_X1, 440}, {DisplayStyles.BUS_LEFT_INPUT_X1, 224}, {DisplayStyles.BUS_LEFT_INPUT_X, 224}, {DisplayStyles.BUS_LEFT_INPUT_X, 231}}, new ControlSignal[]{ControlSignal.KEY_TO_ALU}), new BusView(new int[][]{{DisplayStyles.FROM_ALU_X, 339}, {DisplayStyles.FROM_ALU_X, 355}, {DisplayStyles.BUS_RIGHT_TO_X, 355}, {DisplayStyles.BUS_RIGHT_TO_X, 43}, {DisplayStyles.BUS_TO_ADDR_X, 43}}, new ControlSignal[]{ControlSignal.BUF_TO_ADDR}), new BusView(new int[][]{{DisplayStyles.FROM_ALU_X, 339}, {DisplayStyles.FROM_ALU_X, 355}, {DisplayStyles.BUS_RIGHT_TO_X, 355}, {DisplayStyles.BUS_RIGHT_TO_X, 112}, {DisplayStyles.BUS_TO_DATA_X, 112}}, new ControlSignal[]{ControlSignal.BUF_TO_DATA}), new BusView(new int[][]{{DisplayStyles.FROM_ALU_X, 339}, {DisplayStyles.FROM_ALU_X, 355}, {DisplayStyles.BUS_RIGHT_TO_X, 355}, {DisplayStyles.BUS_RIGHT_TO_X, 43}, {DisplayStyles.BUS_TO_INSTR_X, 43}}, new ControlSignal[]{ControlSignal.BUF_TO_INSTR}), new BusView(new int[][]{{DisplayStyles.FROM_ALU_X, 339}, {DisplayStyles.FROM_ALU_X, 355}, {DisplayStyles.BUS_RIGHT_TO_X, 355}, {DisplayStyles.BUS_RIGHT_TO_X, 181}, {DisplayStyles.BUS_TO_DATA_X, 181}}, new ControlSignal[]{ControlSignal.BUF_TO_IP}), new BusView(new int[][]{{DisplayStyles.FROM_ALU_X, 339}, {DisplayStyles.FROM_ALU_X, 362}}, new ControlSignal[]{ControlSignal.BUF_TO_ACCUM}), new BusView(new int[][]{{DisplayStyles.BUS_ADDR_X1, 43}, {DisplayStyles.BUS_ADDR_X2, 43}}, new ControlSignal[]{ControlSignal.MEMORY_READ, ControlSignal.MEMORY_WRITE}), new BusView(new int[][]{{DisplayStyles.BUS_READ_X2, 99}, {DisplayStyles.BUS_READ_X1, 99}}, new ControlSignal[]{ControlSignal.MEMORY_READ}), new BusView(new int[][]{{DisplayStyles.BUS_ADDR_X1, 125}, {DisplayStyles.BUS_ADDR_X2, 125}}, new ControlSignal[]{ControlSignal.MEMORY_WRITE}), new BusView(new int[][]{{DisplayStyles.BUS_INSTR_TO_CU_X, 74}, {DisplayStyles.BUS_INSTR_TO_CU_X, 231}}, new ControlSignal[0])});
      this.cpu = gui.getCPU();
      this.setSignalListeners(new SignalListener[0]);
      this.add(new ALUView(DisplayStyles.REG_C_X_BV, 245, 181, 90));
      this.cycleview = new RunningCycleView(this.cpu, DisplayStyles.REG_INSTR_X_BV, 245);
      this.add(this.cycleview);
   }

   public void panelActivate() {
      this.cycleview.update();
      super.panelActivate();
   }

   public String getPanelName() {
      return "Базовая ЭВМ";
   }

   public void stepFinish() {
      super.stepFinish();
      this.cycleview.update();
   }
}
