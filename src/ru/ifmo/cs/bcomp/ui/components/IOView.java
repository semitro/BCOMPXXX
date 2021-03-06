package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JLabel;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.SignalListener;
import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.bcomp.ui.components.BCompLabel;
import ru.ifmo.cs.bcomp.ui.components.BCompPanel;
import ru.ifmo.cs.bcomp.ui.components.BusView;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;
import ru.ifmo.cs.bcomp.ui.components.InputRegisterView;
import ru.ifmo.cs.bcomp.ui.components.RegisterProperties;
import ru.ifmo.cs.bcomp.ui.components.RegisterView;
import ru.ifmo.cs.bcomp.ui.io.BComp2BCompIODev;
import ru.ifmo.cs.bcomp.ui.io.Keyboard;
import ru.ifmo.cs.bcomp.ui.io.Numpad;
import ru.ifmo.cs.bcomp.ui.io.SevenSegmentDisplay;
import ru.ifmo.cs.bcomp.ui.io.TextPrinter;
import ru.ifmo.cs.bcomp.ui.io.Ticker;
import ru.ifmo.cs.elements.DataDestination;

public class IOView extends BCompPanel {

   private final IOCtrl[] ioctrls;
   private TextPrinter textPrinter = null;
   private Ticker ticker = null;
   private SevenSegmentDisplay ssd = null;
   private Keyboard kbd = null;
   private Numpad numpad = null;
   private GUI pairgui = null;
   private final RegisterView[] ioregs = new RegisterView[3];
   private final JButton[] flags = new JButton[]{new JButton("F1 ВУ1"), new JButton("F2 ВУ2"), new JButton("F3 ВУ3")};
   private final BusView[] intrBuses;


   public IOView(final GUI gui, GUI _pairgui) {
      super(gui.getComponentManager(), new RegisterProperties[]{new RegisterProperties(CPU.Reg.ADDR, DisplayStyles.CU_X_IO, 86, true), new RegisterProperties(CPU.Reg.IP, DisplayStyles.CU_X_IO, 155, true), new RegisterProperties(CPU.Reg.DATA, DisplayStyles.CU_X_IO, 224, true), new RegisterProperties(CPU.Reg.INSTR, DisplayStyles.CU_X_IO, 293, true), new RegisterProperties(CPU.Reg.ACCUM, DisplayStyles.REG_ACC_X_IO, 362, true), new RegisterProperties(CPU.Reg.STATE, DisplayStyles.CU_X_IO, 362, false)}, new BusView[]{new BusView(new int[][]{{DisplayStyles.IO1_CENTER, 80}, {DisplayStyles.IO1_CENTER, 96}, {DisplayStyles.BUS_TSF_X, 96}, {DisplayStyles.BUS_TSF_X, 82}}, new ControlSignal[]{ControlSignal.IO1_TSF}), new BusView(new int[][]{{DisplayStyles.IO2_CENTER, 80}, {DisplayStyles.IO2_CENTER, 96}, {DisplayStyles.BUS_TSF_X, 96}, {DisplayStyles.BUS_TSF_X, 82}}, new ControlSignal[]{ControlSignal.IO2_TSF}), new BusView(new int[][]{{DisplayStyles.IO3_CENTER, 80}, {DisplayStyles.IO3_CENTER, 96}, {DisplayStyles.BUS_TSF_X, 96}, {DisplayStyles.BUS_TSF_X, 82}}, new ControlSignal[]{ControlSignal.IO3_TSF}), new BusView(new int[][]{{DisplayStyles.IO1_CENTER, 145}, {DisplayStyles.IO1_CENTER, 152}}, new ControlSignal[]{ControlSignal.INPUT_OUTPUT}), new BusView(new int[][]{{DisplayStyles.IO2_CENTER, 145}, {DisplayStyles.IO2_CENTER, 152}}, new ControlSignal[]{ControlSignal.INPUT_OUTPUT}), new BusView(new int[][]{{DisplayStyles.BUS_IO_ADDR_X, 237}, {DisplayStyles.BUS_TSF_X, 237}, {DisplayStyles.BUS_TSF_X, 145}, {DisplayStyles.IO3_CENTER, 145}, {DisplayStyles.IO3_CENTER, 152}}, new ControlSignal[]{ControlSignal.INPUT_OUTPUT}), new BusView(new int[][]{{DisplayStyles.IO1_CENTER, 263}, {DisplayStyles.IO1_CENTER, 256}}, new ControlSignal[]{ControlSignal.INPUT_OUTPUT}), new BusView(new int[][]{{DisplayStyles.IO2_CENTER, 263}, {DisplayStyles.IO2_CENTER, 256}}, new ControlSignal[]{ControlSignal.INPUT_OUTPUT}), new BusView(new int[][]{{DisplayStyles.BUS_IO_ADDR_X, 263}, {DisplayStyles.IO3_CENTER, 263}, {DisplayStyles.IO3_CENTER, 256}}, new ControlSignal[]{ControlSignal.INPUT_OUTPUT}), new BusView(new int[][]{{DisplayStyles.IO2_CENTER, 323}, {DisplayStyles.IO2_CENTER, 308}, {DisplayStyles.BUS_TSF_X, 308}, {DisplayStyles.BUS_TSF_X, 375}, {DisplayStyles.BUS_IN_X, 375}}, new ControlSignal[]{ControlSignal.IO2_IN}), new BusView(new int[][]{{DisplayStyles.IO3_CENTER, 323}, {DisplayStyles.IO3_CENTER, 308}, {DisplayStyles.BUS_TSF_X, 308}, {DisplayStyles.BUS_TSF_X, 375}, {DisplayStyles.BUS_IN_X, 375}}, new ControlSignal[]{ControlSignal.IO3_IN}), new BusView(new int[][]{{DisplayStyles.BUS_OUT_X, 401}, {DisplayStyles.IO1_CENTER, 401}, {DisplayStyles.IO1_CENTER, 394}}, new ControlSignal[]{ControlSignal.IO1_OUT}), new BusView(new int[][]{{DisplayStyles.BUS_OUT_X, 401}, {DisplayStyles.IO3_CENTER, 401}, {DisplayStyles.IO3_CENTER, 394}}, new ControlSignal[]{ControlSignal.IO3_OUT})});
      this.intrBuses = new BusView[]{new BusView(new int[][]{{DisplayStyles.IO1_CENTER, 46}, {DisplayStyles.IO1_CENTER, 30}, {DisplayStyles.BUS_INTR_LEFT_X, 30}}, new ControlSignal[0]), new BusView(new int[][]{{DisplayStyles.IO2_CENTER, 46}, {DisplayStyles.IO2_CENTER, 30}, {DisplayStyles.BUS_INTR_LEFT_X, 30}}, new ControlSignal[0]), new BusView(new int[][]{{DisplayStyles.IO3_CENTER, 46}, {DisplayStyles.IO3_CENTER, 30}, {DisplayStyles.BUS_INTR_LEFT_X, 30}}, new ControlSignal[0])};
      this.pairgui = _pairgui;
      this.ioctrls = gui.getIOCtrls();
      JButton button = new JButton("ВУ4");
      button.setFont(DisplayStyles.FONT_COURIER_PLAIN_12);
      button.setBounds(DisplayStyles.IO1_CENTER, 445, 100, 25);
      button.setFocusable(false);
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if(IOView.this.textPrinter == null) {
               IOView.this.textPrinter = new TextPrinter(IOView.this.ioctrls[4]);
            }

            IOView.this.textPrinter.activate();
         }
      });
      this.add(button);
      button = new JButton("ВУ5");
      button.setFont(DisplayStyles.FONT_COURIER_PLAIN_12);
      button.setBounds(DisplayStyles.IO2_CENTER, 445, 100, 25);
      button.setFocusable(false);
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if(IOView.this.ticker == null) {
               IOView.this.ticker = new Ticker(IOView.this.ioctrls[5]);
            }

            IOView.this.ticker.activate();
         }
      });
      this.add(button);
      button = new JButton("ВУ6");
      button.setFont(DisplayStyles.FONT_COURIER_PLAIN_12);
      button.setBounds(DisplayStyles.IO3_CENTER - 30, 445, 100, 25);
      button.setFocusable(false);
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if(IOView.this.ssd == null) {
               IOView.this.ssd = new SevenSegmentDisplay(IOView.this.ioctrls[6]);
            }

            IOView.this.ssd.activate();
         }
      });
      this.add(button);
      button = new JButton("ВУ7");
      button.setFont(DisplayStyles.FONT_COURIER_PLAIN_12);
      button.setBounds(DisplayStyles.IO1_CENTER, 475, 100, 25);
      button.setFocusable(false);
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if(IOView.this.kbd == null) {
               IOView.this.kbd = new Keyboard(IOView.this.ioctrls[7]);
            }

            IOView.this.kbd.activate();
         }
      });
      this.add(button);
      button = new JButton("ВУ8");
      button.setFont(DisplayStyles.FONT_COURIER_PLAIN_12);
      button.setBounds(DisplayStyles.IO2_CENTER, 475, 100, 25);
      button.setFocusable(false);
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if(IOView.this.numpad == null) {
               IOView.this.numpad = new Numpad(IOView.this.ioctrls[8]);
            }

            IOView.this.numpad.activate();
         }
      });
      this.add(button);
      button = new JButton("ВУ9");
      button.setFont(DisplayStyles.FONT_COURIER_PLAIN_12);
      button.setBounds(DisplayStyles.IO3_CENTER - 30, 475, 100, 25);
      button.setFocusable(false);
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if(IOView.this.pairgui == null) {
               try {
                  IOView.this.pairgui = new GUI(gui);
                  IOView.this.pairgui.gui();
                  new BComp2BCompIODev(gui.getBasicComp().getIOCtrls()[9], IOView.this.pairgui.getBasicComp().getIOCtrls()[9]);
               } catch (Exception var3) {
                  ;
               }
            }

            gui.requestFocus();
         }
      });
      this.add(button);

      for(int intrListener = 0; intrListener < this.ioregs.length; ++intrListener) {
         int x = DisplayStyles.IO_X + intrListener * DisplayStyles.IO_DELIM;
         this.ioregs[intrListener] = (RegisterView)(intrListener == 0?new RegisterView(this.ioctrls[intrListener + 1].getRegData()):new InputRegisterView(this.cmanager, this.ioctrls[intrListener + 1].getRegData()));
         this.ioregs[intrListener].setProperties(x, 328, false);
         this.add(this.ioregs[intrListener]);
         this.flags[intrListener].setFont(DisplayStyles.FONT_COURIER_PLAIN_12);
         this.flags[intrListener].setBounds(x + DisplayStyles.FLAG_OFFSET, 51, 100, 25);
         this.flags[intrListener].setFocusable(false);
         this.add(this.flags[intrListener]);
         this.flags[intrListener].addActionListener(new IOView.FlagButtonListener(this.ioctrls[intrListener + 1]));
         this.ioctrls[intrListener + 1].addDestination(IOCtrl.ControlSignal.SETFLAG, new IOView.FlagListener(this.flags[intrListener]));
         this.add(new BCompLabel(x, 166, DisplayStyles.REG_8_WIDTH, new String[]{"Дешифратор", "адреса и", "приказов"}));
      }

      this.add(new BCompLabel(DisplayStyles.CU_X_IO, 17, DisplayStyles.REG_8_WIDTH, new String[]{"Устройство", "управления"}));
      this.addLabel("Запрос прерывания", 6);
      this.addLabel("Состояние флага ВУ", 104);
      this.addLabel("Адрес ВУ", 124);
      this.addLabel("Приказ на ввод/вывод", 271);
      this.addLabel("Шина ввода", 287);
      this.addLabel("Шина вывода", 409);
      DataDestination var6 = new DataDestination() {
         public void setValue(int value) {
            IOView.this.drawIntrBuses(IOView.this.getGraphics());
         }
      };
      this.setSignalListeners(new SignalListener[]{new SignalListener(this.ioregs[0], new ControlSignal[]{ControlSignal.IO1_OUT}), new SignalListener(this.ioregs[2], new ControlSignal[]{ControlSignal.IO3_OUT}), new SignalListener(var6, new ControlSignal[]{ControlSignal.IO1_SETFLAG}), new SignalListener(var6, new ControlSignal[]{ControlSignal.IO2_SETFLAG}), new SignalListener(var6, new ControlSignal[]{ControlSignal.IO3_SETFLAG})});
   }

   private void addLabel(String text, int y) {
      JLabel l = new JLabel(text, 0);
      l.setFont(DisplayStyles.FONT_COURIER_BOLD_18);
      l.setBounds(DisplayStyles.IO1_CENTER, y, DisplayStyles.IO3_CENTER - DisplayStyles.IO1_CENTER, 16);
      this.add(l);
   }

   private void drawIntrBuses(Graphics g) {
      int i;
      for(i = 0; i < 3; ++i) {
         if(this.ioctrls[i + 1].getFlag() == 0) {
            this.intrBuses[i].draw(g, DisplayStyles.COLOR_BUS);
         }
      }

      for(i = 0; i < 3; ++i) {
         if(this.ioctrls[i + 1].getFlag() == 1) {
            this.intrBuses[i].draw(g, DisplayStyles.COLOR_ACTIVE);
         }
      }

   }

   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      this.drawIntrBuses(g);
   }

   public String getPanelName() {
      return "Работа с ВУ";
   }

   public void stepFinish() {
      super.stepFinish();
      Iterator i$ = this.cmanager.getActiveSignals().iterator();

      while(i$.hasNext()) {
         ControlSignal signal = (ControlSignal)i$.next();
         switch(IOView.NamelessClass1116746541.$SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[signal.ordinal()]) {
         case 1:
            this.ioregs[0].setValue();
            break;
         case 2:
            this.ioregs[2].setValue();
         }
      }

   }

   // $FF: synthetic class
   static class NamelessClass1116746541 {

      // $FF: synthetic field
      static final int[] $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal = new int[ControlSignal.values().length];


      static {
         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO1_OUT.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ControlSignal[ControlSignal.IO3_OUT.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   private class FlagListener implements DataDestination {

      private final JButton flag;


      public FlagListener(JButton flag) {
         this.flag = flag;
      }

      public void setValue(int value) {
         this.flag.setForeground(value == 1?DisplayStyles.COLOR_ACTIVE:DisplayStyles.COLOR_TEXT);
      }
   }

   private class FlagButtonListener implements ActionListener {

      private final IOCtrl ioctrl;


      public FlagButtonListener(IOCtrl ioctrl) {
         this.ioctrl = ioctrl;
      }

      public void actionPerformed(ActionEvent e) {
         this.ioctrl.setFlag();
      }
   }
}
