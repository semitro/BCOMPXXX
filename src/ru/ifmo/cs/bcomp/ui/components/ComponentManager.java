package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EnumMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.SignalListener;
import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.bcomp.ui.components.ActiveBitView;
import ru.ifmo.cs.bcomp.ui.components.BCompPanel;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;
import ru.ifmo.cs.bcomp.ui.components.InputRegisterView;
import ru.ifmo.cs.bcomp.ui.components.MemoryView;
import ru.ifmo.cs.bcomp.ui.components.MicroMemoryView;
import ru.ifmo.cs.bcomp.ui.components.RegisterView;
import ru.ifmo.cs.bcomp.ui.components.StateRegisterView;
import ru.ifmo.cs.elements.DataDestination;

public class ComponentManager {

   private Color[] buttonColors;
   private ComponentManager.ButtonProperties[] buttonProperties;
   private final KeyAdapter keyListener;
   private static final int BUTTON_RUN = 5;
   private static final int BUTTON_CLOCK = 6;
   private JButton[] buttons;
   private ComponentManager.ButtonsPanel buttonsPanel;
   private final GUI gui;
   private final BasicComp bcomp;
   private final CPU cpu;
   private final IOCtrl[] ioctrls;
   private final MemoryView mem;
   private final MicroMemoryView micromem;
   private EnumMap regs;
   private ActiveBitView activeBit;
   private volatile BCompPanel activePanel;
   private final long[] delayPeriods;
   private volatile int currentDelay;
   private volatile int savedDelay;
   private final Object lockActivePanel;
   private final JCheckBox cucheckbox;
   private volatile boolean cuswitch;
   private final SignalListener[] listeners;
   private ArrayList openBuses;
   private static final ControlSignal[] busSignals = new ControlSignal[]{ControlSignal.DATA_TO_ALU, ControlSignal.INSTR_TO_ALU, ControlSignal.IP_TO_ALU, ControlSignal.ACCUM_TO_ALU, ControlSignal.STATE_TO_ALU, ControlSignal.KEY_TO_ALU, ControlSignal.BUF_TO_ADDR, ControlSignal.BUF_TO_DATA, ControlSignal.BUF_TO_INSTR, ControlSignal.BUF_TO_IP, ControlSignal.BUF_TO_ACCUM, ControlSignal.MEMORY_READ, ControlSignal.MEMORY_WRITE, ControlSignal.INPUT_OUTPUT, ControlSignal.IO0_TSF, ControlSignal.IO1_TSF, ControlSignal.IO1_OUT, ControlSignal.IO2_TSF, ControlSignal.IO2_IN, ControlSignal.IO3_TSF, ControlSignal.IO3_IN, ControlSignal.IO3_OUT};


   public ComponentManager(GUI gui) {
      this.buttonColors = new Color[]{DisplayStyles.COLOR_TEXT, DisplayStyles.COLOR_ACTIVE};
      this.buttonProperties = new ComponentManager.ButtonProperties[]{new ComponentManager.ButtonProperties(135, new String[]{"F4 Ввод адреса"}, new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ComponentManager.this.cmdEnterAddr();
         }
      }), new ComponentManager.ButtonProperties(115, new String[]{"F5 Запись"}, new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ComponentManager.this.cmdWrite();
         }
      }), new ComponentManager.ButtonProperties(115, new String[]{"F6 Чтение"}, new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ComponentManager.this.cmdRead();
         }
      }), new ComponentManager.ButtonProperties(90, new String[]{"F7 Пуск"}, new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ComponentManager.this.cmdStart();
         }
      }), new ComponentManager.ButtonProperties(135, new String[]{"F8 Продолжение"}, new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ComponentManager.this.cmdContinue();
         }
      }), new ComponentManager.ButtonProperties(110, new String[]{"F9 Останов", "F9 Работа"}, new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ComponentManager.this.cmdInvertRunState();
         }
      }), new ComponentManager.ButtonProperties(130, new String[]{"Shift+F9 Такт"}, new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            ComponentManager.this.cmdInvertClockState();
         }
      })};
      this.keyListener = new KeyAdapter() {
         public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
            case 81:
               if(e.isControlDown()) {
                  System.exit(0);
               }
            case 82:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            default:
               break;
            case 112:
               if(e.isShiftDown()) {
                  ComponentManager.this.cmdAbout();
               } else {
                  ComponentManager.this.cmdSetIOFlag(1);
               }
               break;
            case 113:
               ComponentManager.this.cmdSetIOFlag(2);
               break;
            case 114:
               ComponentManager.this.cmdSetIOFlag(3);
               break;
            case 115:
               ComponentManager.this.cmdEnterAddr();
               break;
            case 116:
               ComponentManager.this.cmdWrite();
               break;
            case 117:
               ComponentManager.this.cmdRead();
               break;
            case 118:
               ComponentManager.this.cmdStart();
               break;
            case 119:
               ComponentManager.this.cmdContinue();
               break;
            case 120:
               if(e.isShiftDown()) {
                  ComponentManager.this.cmdInvertClockState();
               } else {
                  ComponentManager.this.cmdInvertRunState();
               }
               break;
            case 121:
               System.exit(0);
               break;
            case 122:
               ComponentManager.this.cmdPrevDelay();
               break;
            case 123:
               ComponentManager.this.cmdNextDelay();
            }

         }
      };
      this.buttonsPanel = new ComponentManager.ButtonsPanel();
      this.regs = new EnumMap(CPU.Reg.class);
      this.activeBit = new ActiveBitView(DisplayStyles.ACTIVE_BIT_X, 445);
      this.delayPeriods = new long[]{0L, 1L, 5L, 10L, 25L, 50L, 100L, 1000L};
      this.currentDelay = 3;
      this.lockActivePanel = new Object();
      this.cuswitch = false;
      this.openBuses = new ArrayList();
      this.gui = gui;
      this.bcomp = gui.getBasicComp();
      this.cpu = gui.getCPU();
      this.ioctrls = gui.getIOCtrls();
      this.cpu.setTickStartListener(new Runnable() {
         public void run() {
            synchronized(ComponentManager.this.lockActivePanel) {
               if(ComponentManager.this.activePanel != null) {
                  ComponentManager.this.activePanel.stepStart();
               }
            }

            ComponentManager.this.openBuses.clear();
         }
      });
      this.cpu.setTickFinishListener(new Runnable() {
         public void run() {
            synchronized(ComponentManager.this.lockActivePanel) {
               if(ComponentManager.this.activePanel != null) {
                  ComponentManager.this.activePanel.stepFinish();
               }
            }

            if(ComponentManager.this.delayPeriods[ComponentManager.this.currentDelay] != 0L) {
               try {
                  Thread.sleep(ComponentManager.this.delayPeriods[ComponentManager.this.currentDelay]);
               } catch (InterruptedException var3) {
                  ;
               }
            }

         }
      });
      ControlSignal[] arr$ = busSignals;
      int len$ = arr$.length;

      int i$;
      for(i$ = 0; i$ < len$; ++i$) {
         ControlSignal reg = arr$[i$];
         this.bcomp.addDestination(reg, new ComponentManager.SignalHandler(reg));
      }

      CPU.Reg[] var7 = CPU.Reg.values();
      len$ = var7.length;

      for(i$ = 0; i$ < len$; ++i$) {
         CPU.Reg var8 = var7[i$];
         switch(ComponentManager.NamelessClass2099982138.$SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[var8.ordinal()]) {
         case 1:
            InputRegisterView regKey = new InputRegisterView(this, this.cpu.getRegister(var8));
            this.regs.put(var8, regKey);
            regKey.setProperties(8, 445, false);
            break;
         case 2:
            this.regs.put(var8, new StateRegisterView(this.cpu.getRegister(var8)));
            break;
         default:
            this.regs.put(var8, new RegisterView(this.cpu.getRegister(var8)));
         }
      }

      this.listeners = new SignalListener[]{new SignalListener((DataDestination)this.regs.get(CPU.Reg.STATE), new ControlSignal[]{ControlSignal.BUF_TO_STATE_C, ControlSignal.CLEAR_STATE_C, ControlSignal.SET_STATE_C}), new SignalListener((DataDestination)this.regs.get(CPU.Reg.ADDR), new ControlSignal[]{ControlSignal.BUF_TO_ADDR}), new SignalListener((DataDestination)this.regs.get(CPU.Reg.DATA), new ControlSignal[]{ControlSignal.BUF_TO_DATA, ControlSignal.MEMORY_READ}), new SignalListener((DataDestination)this.regs.get(CPU.Reg.INSTR), new ControlSignal[]{ControlSignal.BUF_TO_INSTR}), new SignalListener((DataDestination)this.regs.get(CPU.Reg.IP), new ControlSignal[]{ControlSignal.BUF_TO_IP}), new SignalListener((DataDestination)this.regs.get(CPU.Reg.ACCUM), new ControlSignal[]{ControlSignal.BUF_TO_ACCUM, ControlSignal.IO2_IN, ControlSignal.IO3_IN, ControlSignal.IO7_IN, ControlSignal.IO8_IN, ControlSignal.IO9_IN})};
      this.mem = new MemoryView(this.cpu.getMemory(), 1, 1);
      this.micromem = new MicroMemoryView(this.cpu, DisplayStyles.MICROMEM_X, 1);
      this.bcomp.addDestination(ControlSignal.MEMORY_READ, new DataDestination() {
         public void setValue(int value) {
            if(ComponentManager.this.activePanel != null) {
               ComponentManager.this.mem.eventRead();
            } else {
               ComponentManager.this.mem.updateLastAddr();
            }

         }
      });
      this.bcomp.addDestination(ControlSignal.MEMORY_WRITE, new DataDestination() {
         public void setValue(int value) {
            if(ComponentManager.this.activePanel != null) {
               ComponentManager.this.mem.eventWrite();
            } else {
               ComponentManager.this.mem.updateLastAddr();
            }

         }
      });
      this.cucheckbox = new JCheckBox("Ввод в Устройство управления");
      this.cucheckbox.setOpaque(false);
      this.cucheckbox.addKeyListener(this.keyListener);
      this.cucheckbox.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            ComponentManager.this.cuswitch = e.getStateChange() == 1;
         }
      });
   }

   public void panelActivate(BCompPanel component) {
      Object var2 = this.lockActivePanel;
      synchronized(this.lockActivePanel) {
         this.activePanel = component;
         this.bcomp.addDestination(this.activePanel.getSignalListeners());
         this.bcomp.addDestination(this.listeners);
      }

      component.add(this.mem);
      component.add(this.buttonsPanel);
      component.add(this.activeBit);
      component.add((Component)this.regs.get(CPU.Reg.KEY));
      this.mem.updateMemory();
      this.cuswitch = false;
      this.switchFocus();
   }

   public void panelDeactivate() {
      Object var1 = this.lockActivePanel;
      synchronized(this.lockActivePanel) {
         this.bcomp.removeDestination(this.listeners);
         this.bcomp.removeDestination(this.activePanel.getSignalListeners());
         this.activePanel = null;
      }
   }

   public void keyPressed(KeyEvent e) {
      this.keyListener.keyPressed(e);
   }

   public void switchFocus() {
      ((InputRegisterView)this.regs.get(CPU.Reg.KEY)).setActive();
   }

   public RegisterView getRegisterView(CPU.Reg reg) {
      return (RegisterView)this.regs.get(reg);
   }

   public void cmdContinue() {
      this.cpu.startContinue();
   }

   public void cmdEnterAddr() {
      if(this.cuswitch) {
         this.cpu.runSetMAddr();
         ((RegisterView)this.regs.get(CPU.Reg.MIP)).setValue();
      } else {
         this.cpu.startSetAddr();
      }

   }

   public void cmdWrite() {
      if(this.cuswitch) {
         this.micromem.updateLastAddr();
         this.cpu.runMWrite();
         this.micromem.updateMemory();
         ((RegisterView)this.regs.get(CPU.Reg.MIP)).setValue();
      } else {
         this.cpu.startWrite();
      }

   }

   public void cmdRead() {
      if(this.cuswitch) {
         this.micromem.eventRead();
         this.cpu.runMRead();
         ((RegisterView)this.regs.get(CPU.Reg.MIP)).setValue();
         ((RegisterView)this.regs.get(CPU.Reg.MINSTR)).setValue();
      } else {
         this.cpu.startRead();
      }

   }

   public void cmdStart() {
      this.cpu.startStart();
   }

   public void cmdInvertRunState() {
      this.cpu.invertRunState();
      int state = this.cpu.getStateValue(7);
      this.buttons[5].setForeground(this.buttonColors[state]);
      this.buttons[5].setText(this.buttonProperties[5].texts[state]);
   }

   public void cmdInvertClockState() {
      boolean state = this.cpu.invertClockState();
      this.buttons[6].setForeground(this.buttonColors[state?0:1]);
   }

   public void cmdSetIOFlag(int dev) {
      this.ioctrls[dev].setFlag();
   }

   public void cmdNextDelay() {
      this.currentDelay = this.currentDelay < this.delayPeriods.length - 1?this.currentDelay + 1:0;
   }

   public void cmdPrevDelay() {
      this.currentDelay = (this.currentDelay > 0?this.currentDelay:this.delayPeriods.length) - 1;
   }

   public void saveDelay() {
      this.savedDelay = this.currentDelay;
      this.currentDelay = 0;
   }

   public void restoreDelay() {
      this.currentDelay = this.savedDelay;
   }

   public void cmdAbout() {
      JOptionPane.showMessageDialog(this.gui, "Эмулятор Базовой ЭВМ. Версия r" + GUI.class.getPackage().getImplementationVersion() + "\n\nЗагружена " + this.gui.getMicroProgramName() + " микропрограмма", "О программе", 1);
   }

   public MicroMemoryView getMicroMemory() {
      return this.micromem;
   }

   public JCheckBox getMPCheckBox() {
      return this.cucheckbox;
   }

   public ActiveBitView getActiveBit() {
      return this.activeBit;
   }

   public KeyListener getKeyListener() {
      return this.keyListener;
   }

   public ArrayList getActiveSignals() {
      return this.openBuses;
   }

   public void clearActiveSignals() {
      this.openBuses.clear();
   }


   // $FF: synthetic class
   static class NamelessClass2099982138 {

      // $FF: synthetic field
      static final int[] $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg = new int[CPU.Reg.values().length];


      static {
         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.KEY.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$CPU$Reg[CPU.Reg.STATE.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   private class ButtonsPanel extends JComponent {

      public ButtonsPanel() {
         this.setBounds(0, 514, 856, 30);
         int buttonsX = 1;
         ComponentManager.this.buttons = new JButton[ComponentManager.this.buttonProperties.length];

         for(int i = 0; i < ComponentManager.this.buttons.length; ++i) {
            ComponentManager.this.buttons[i] = new JButton(ComponentManager.this.buttonProperties[i].texts[0]);
            ComponentManager.this.buttons[i].setForeground(ComponentManager.this.buttonColors[0]);
            ComponentManager.this.buttons[i].setFont(DisplayStyles.FONT_COURIER_PLAIN_12);
            ComponentManager.this.buttons[i].setBounds(buttonsX, 0, ComponentManager.this.buttonProperties[i].width, 30);
            buttonsX += ComponentManager.this.buttonProperties[i].width + 2;
            ComponentManager.this.buttons[i].setFocusable(false);
            ComponentManager.this.buttons[i].addActionListener(ComponentManager.this.buttonProperties[i].listener);
            this.add(ComponentManager.this.buttons[i]);
         }

      }
   }

   private class SignalHandler implements DataDestination {

      private final ControlSignal signal;


      public SignalHandler(ControlSignal signal) {
         this.signal = signal;
      }

      public void setValue(int value) {
         ComponentManager.this.openBuses.add(this.signal);
      }
   }

   private class ButtonProperties {

      public final int width;
      public final String[] texts;
      public final ActionListener listener;


      public ButtonProperties(int width, String[] texts, ActionListener listener) {
         this.width = width;
         this.texts = texts;
         this.listener = listener;
      }
   }
}
