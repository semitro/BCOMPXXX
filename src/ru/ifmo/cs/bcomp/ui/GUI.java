package ru.ifmo.cs.bcomp.ui;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.MicroProgram;
import ru.ifmo.cs.bcomp.MicroPrograms;
import ru.ifmo.cs.bcomp.ui.components.ActivateblePanel;
import ru.ifmo.cs.bcomp.ui.components.AssemblerView;
import ru.ifmo.cs.bcomp.ui.components.BasicView;
import ru.ifmo.cs.bcomp.ui.components.ComponentManager;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;
import ru.ifmo.cs.bcomp.ui.components.IOView;
import ru.ifmo.cs.bcomp.ui.components.MPView;

public class GUI extends JApplet {

   private ComponentManager cmanager;
   private JTabbedPane tabs;
   private ActivateblePanel activePanel;
   private final BasicComp bcomp;
   private final CPU cpu;
   private final GUI pairgui;


   public GUI(MicroProgram mp, GUI pairgui) throws Exception {
      this.activePanel = null;
      this.bcomp = new BasicComp(mp);
      this.cpu = this.bcomp.getCPU();
      this.pairgui = pairgui;
   }

   public GUI(MicroProgram mp) throws Exception {
      this(mp, (GUI)null);
   }

   public GUI() throws Exception {
      this(MicroPrograms.getMicroProgram("base"));
   }

   public GUI(GUI pairgui) throws Exception {
      this(pairgui.getCPU().getMicroProgram(), pairgui);
   }

   public void init() {
      this.cmanager = new ComponentManager(this);
      this.bcomp.startTimer();
      ActivateblePanel[] panes = new ActivateblePanel[]{new BasicView(this), new IOView(this, this.pairgui), new MPView(this), new AssemblerView(this)};
      this.tabs = new JTabbedPane();
      this.tabs.addKeyListener(this.cmanager.getKeyListener());
      this.tabs.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent e) {
            if(GUI.this.activePanel != null) {
               GUI.this.activePanel.panelDeactivate();
            }

            GUI.this.activePanel = (ActivateblePanel)GUI.this.tabs.getSelectedComponent();
            GUI.this.activePanel.panelActivate();
         }
      });
      ActivateblePanel[] arr$ = panes;
      int len$ = panes.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ActivateblePanel pane = arr$[i$];
         pane.setPreferredSize(DisplayStyles.PANE_SIZE);
         this.tabs.addTab(pane.getPanelName(), pane);
      }

      this.add(this.tabs);
   }

   public void start() {
      this.cmanager.switchFocus();
   }

   public void gui() throws Exception {
      JFrame frame = new JFrame("БЭВМ");
      if(this.pairgui == null) {
         frame.setDefaultCloseOperation(3);
      }

      frame.getContentPane().add(this);
      this.init();
      frame.pack();
      frame.setResizable(false);
      frame.setVisible(true);
      this.start();
   }

   public BasicComp getBasicComp() {
      return this.bcomp;
   }

   public CPU getCPU() {
      return this.cpu;
   }

   public IOCtrl[] getIOCtrls() {
      return this.bcomp.getIOCtrls();
   }

   public ComponentManager getComponentManager() {
      return this.cmanager;
   }

   public String getMicroProgramName() {
      return this.cpu.getMicroProgramName();
   }
}
