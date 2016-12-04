package ru.ifmo.cs.bcomp.ui;

import ru.ifmo.cs.bcomp.MicroProgram;
import ru.ifmo.cs.bcomp.MicroPrograms;
import ru.ifmo.cs.bcomp.ui.CLI;
import ru.ifmo.cs.bcomp.ui.GUI;
import ru.ifmo.cs.bcomp.ui.MPDecoder;

public class BCompApp {

   public static void main(String[] args) throws Exception {
      String mpname;
      String app;
      try {
         mpname = System.getProperty("mp", "base");
         app = System.getProperty("mode", "gui");
      } catch (Exception var5) {
         mpname = "base";
         app = "gui";
      }

      MicroProgram mp = MicroPrograms.getMicroProgram(mpname);
      if(mp == null) {
         System.err.println("Invalid microprogram selected");
         System.exit(1);
      }

      if(app.equals("gui")) {
         GUI mpdecoder2 = new GUI(mp);
         mpdecoder2.gui();
      } else if(app.equals("cli")) {
         CLI mpdecoder1 = new CLI(mp);
         mpdecoder1.cli();
      } else if(app.equals("decoder")) {
         MPDecoder mpdecoder = new MPDecoder(mp);
         mpdecoder.decode();
      } else {
         System.err.println("Invalid mode selected");
      }
   }
}
