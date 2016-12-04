package ru.ifmo.cs.bcomp.ui.io;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.ui.io.IODevice;

public abstract class OutputDevice extends IODevice {

   private Thread timer = null;
   private long sleeptime = 100L;
   private volatile boolean poweredon = true;


   public OutputDevice(IOCtrl ioctrl, String title) {
      super(ioctrl, title);
   }

   protected abstract void actionPerformed(int var1);

   protected Component getSleepSlider() {
      JSlider slider = new JSlider(0, 0, 3, 2);
      slider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();
            OutputDevice.this.sleeptime = (long)((int)Math.pow(10.0D, (double)source.getValue()));
         }
      });
      return slider;
   }

   protected Component getPowerChkBox() {
      JCheckBox power = new JCheckBox("Вкл", true);
      power.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent e) {
            switch(e.getStateChange()) {
            case 1:
               OutputDevice.this.poweredon = true;
               break;
            case 2:
               OutputDevice.this.poweredon = false;
            }

         }
      });
      return power;
   }

   public void activate() {
      super.activate();
      if(this.timer == null) {
         this.timer = new Thread(new Runnable() {
            public void run() {
               OutputDevice.this.ioctrl.setFlag();

               while(true) {
                  try {
                     Thread.sleep(OutputDevice.this.sleeptime);
                  } catch (Exception var2) {
                     ;
                  }

                  if(OutputDevice.this.poweredon && OutputDevice.this.ioctrl.getFlag() == 0) {
                     OutputDevice.this.actionPerformed(OutputDevice.this.ioctrl.getData());
                     OutputDevice.this.ioctrl.setFlag();
                  }
               }
            }
         }, this.title);
         this.timer.start();
      }

   }
}
