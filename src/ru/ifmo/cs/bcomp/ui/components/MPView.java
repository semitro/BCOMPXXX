package ru.ifmo.cs.bcomp.ui.components;

import java.util.ArrayList;
import javax.swing.JCheckBox;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.SignalListener;
import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.bcomp.ui.components.ALUView;
import ru.ifmo.cs.bcomp.ui.components.BCompPanel;
import ru.ifmo.cs.bcomp.ui.components.BusView;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;
import ru.ifmo.cs.bcomp.ui.components.MicroMemoryView;
import ru.ifmo.cs.bcomp.ui.components.RegisterProperties;
import ru.ifmo.cs.bcomp.ui.components.RegisterView;

public class MPView extends BCompPanel {

   private final MicroMemoryView mem;
   private final RegisterView regMIP;
   private final RegisterView regMInstr;
   private final RegisterView regBuf;
   private final RegisterView regState;
   private final JCheckBox cucheckbox;
   private static final ControlSignal[] statesignals = new ControlSignal[0];


   public MPView(GUI gui) {
      super(gui.getComponentManager(), new RegisterProperties[]{new RegisterProperties(CPU.Reg.ADDR, DisplayStyles.CU_X_IO, 17, true), new RegisterProperties(CPU.Reg.IP, DisplayStyles.REG_IP_X_MP, 17, true), new RegisterProperties(CPU.Reg.DATA, DisplayStyles.CU_X_IO, 86, true), new RegisterProperties(CPU.Reg.INSTR, DisplayStyles.REG_INSTR_X_MP, 86, true), new RegisterProperties(CPU.Reg.ACCUM, DisplayStyles.CU_X_IO, 261, true), new RegisterProperties(CPU.Reg.STATE, DisplayStyles.REG_STATE_X, 330, true)}, new BusView[0]);
      this.add(this.mem = this.cmanager.getMicroMemory());
      this.regMIP = this.cmanager.getRegisterView(CPU.Reg.MIP);
      this.regMIP.setProperties(400, 1, false);
      this.add(this.regMIP);
      this.regMInstr = this.cmanager.getRegisterView(CPU.Reg.MINSTR);
      this.regMInstr.setProperties(400, 100, false);
      this.add(this.regMInstr);
      this.regBuf = this.cmanager.getRegisterView(CPU.Reg.BUF);
      this.regBuf.setProperties(DisplayStyles.REG_BUF_X_MP, 261, true);
      this.add(this.regBuf);
      this.regState = this.cmanager.getRegisterView(CPU.Reg.STATE);
      this.setSignalListeners(new SignalListener[]{new SignalListener(this.regState, new ControlSignal[]{ControlSignal.HALT, ControlSignal.BUF_TO_STATE_N, ControlSignal.BUF_TO_STATE_Z, ControlSignal.DISABLE_INTERRUPTS, ControlSignal.ENABLE_INTERRUPTS, ControlSignal.IO0_TSF, ControlSignal.IO1_TSF, ControlSignal.IO2_TSF, ControlSignal.IO3_TSF, ControlSignal.SET_RUN_STATE, ControlSignal.SET_PROGRAM, ControlSignal.SET_REQUEST_INTERRUPT}), new SignalListener(this.regBuf, new ControlSignal[]{ControlSignal.ALU_AND, ControlSignal.SHIFT_RIGHT, ControlSignal.SHIFT_LEFT})});
      this.cucheckbox = this.cmanager.getMPCheckBox();
      this.cucheckbox.setBounds(450, 400, 300, 30);
      this.add(this.cucheckbox);
      this.add(new ALUView(DisplayStyles.ALU_X_MP, 155, 181, 90));
   }

   public void panelActivate() {
      this.mem.updateLastAddr();
      this.mem.updateMemory();
      this.regMIP.setValue();
      this.regMInstr.setValue();
      this.regBuf.setValue();
      this.cucheckbox.setSelected(false);
      super.panelActivate();
   }

   public String getPanelName() {
      return "Работа с МПУ";
   }

   public void stepStart() {
      this.mem.eventRead();
   }

   public void stepFinish() {
      ArrayList signals = this.cmanager.getActiveSignals();
      this.regMIP.setValue();
      this.regMInstr.setValue();
   }

}
