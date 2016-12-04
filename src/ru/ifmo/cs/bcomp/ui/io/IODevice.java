package ru.ifmo.cs.bcomp.ui.io;

import java.awt.Component;
import javax.swing.JFrame;
import ru.ifmo.cs.bcomp.IOCtrl;

public abstract class IODevice {

   protected final IOCtrl ioctrl;
   protected final String title;
   private JFrame frame = null;


   public IODevice(IOCtrl ioctrl, String title) {
      this.ioctrl = ioctrl;
      this.title = title;
   }

   protected abstract Component getContent();

   public void activate() {
      if(this.frame == null) {
         this.frame = new JFrame(this.title);
         this.frame.add(this.getContent());
         this.frame.pack();
      }

      this.frame.setVisible(true);
      this.frame.requestFocus();
   }
}
