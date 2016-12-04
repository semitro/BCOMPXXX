package ru.ifmo.cs.bcomp.ui.components;

import javax.swing.JComponent;

public abstract class ActivateblePanel extends JComponent {

   public abstract void panelActivate();

   public abstract void panelDeactivate();

   public abstract String getPanelName();
}
