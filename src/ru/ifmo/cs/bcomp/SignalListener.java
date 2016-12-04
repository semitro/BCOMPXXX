package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.elements.DataDestination;

public class SignalListener {

   public final DataDestination dest;
   public final ControlSignal[] signals;


   public SignalListener(DataDestination dest, ControlSignal ... signals) {
      this.dest = dest;
      this.signals = signals;
   }
}
