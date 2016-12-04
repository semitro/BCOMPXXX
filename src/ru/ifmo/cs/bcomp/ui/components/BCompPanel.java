package ru.ifmo.cs.bcomp.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.SignalListener;
import ru.ifmo.cs.bcomp.ui.components.ActivateblePanel;
import ru.ifmo.cs.bcomp.ui.components.BusView;
import ru.ifmo.cs.bcomp.ui.components.ComponentManager;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;
import ru.ifmo.cs.bcomp.ui.components.RegisterProperties;
import ru.ifmo.cs.bcomp.ui.components.RegisterView;

public abstract class BCompPanel extends ActivateblePanel {

   protected final ComponentManager cmanager;
   private final RegisterProperties[] regProps;
   private final BusView[] buses;
   private SignalListener[] listeners;


   public BCompPanel(ComponentManager cmanager, RegisterProperties[] regProps, BusView[] buses) {
      this.cmanager = cmanager;
      this.regProps = regProps;
      this.buses = buses;
   }

   protected void setSignalListeners(SignalListener[] listeners) {
      this.listeners = listeners;
   }

   protected SignalListener[] getSignalListeners() {
      return this.listeners;
   }

   private void drawBuses(Graphics g) {
      ArrayList openbuses = new ArrayList();
      ArrayList signals = this.cmanager.getActiveSignals();
      BusView[] i$ = this.buses;
      int bus = i$.length;

      for(int i$1 = 0; i$1 < bus; ++i$1) {
         BusView bus1 = i$[i$1];
         ControlSignal[] arr$ = bus1.getSignals();
         int len$ = arr$.length;

         for(int i$2 = 0; i$2 < len$; ++i$2) {
            ControlSignal signal = arr$[i$2];
            if(signals.contains(signal)) {
               openbuses.add(bus1);
            }
         }

         bus1.draw(g, DisplayStyles.COLOR_BUS);
      }

      Iterator var12 = openbuses.iterator();

      while(var12.hasNext()) {
         BusView var13 = (BusView)var12.next();
         var13.draw(g, DisplayStyles.COLOR_ACTIVE);
      }

   }

   private void drawOpenBuses(Color color) {
      Graphics g = this.getGraphics();
      ArrayList signals = this.cmanager.getActiveSignals();
      BusView[] arr$ = this.buses;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         BusView bus = arr$[i$];
         ControlSignal[] arr$1 = bus.getSignals();
         int len$1 = arr$1.length;

         for(int i$1 = 0; i$1 < len$1; ++i$1) {
            ControlSignal signal = arr$1[i$1];
            if(signals.contains(signal)) {
               bus.draw(g, color);
            }
         }
      }

   }

   public void stepStart() {
      this.drawOpenBuses(DisplayStyles.COLOR_BUS);
   }

   public void stepFinish() {
      this.drawOpenBuses(DisplayStyles.COLOR_ACTIVE);
   }

   public void panelActivate() {
      RegisterProperties[] arr$ = this.regProps;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         RegisterProperties prop = arr$[i$];
         RegisterView reg = this.cmanager.getRegisterView(prop.reg);
         reg.setProperties(prop.x, prop.y, prop.hex);
         this.add(reg);
      }

      this.cmanager.panelActivate(this);
   }

   public void panelDeactivate() {
      this.cmanager.panelDeactivate();
   }

   public void paintComponent(Graphics g) {
      this.drawBuses(g);
   }
}
