package ru.ifmo.cs.bcomp.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import ru.ifmo.cs.bcomp.Assembler;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ui.GUI;
import java.io.FileWriter;
import ru.ifmo.cs.bcomp.ui.components.ActivateblePanel;
import ru.ifmo.cs.bcomp.ui.components.ComponentManager;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;

public class AssemblerView extends ActivateblePanel {

   private final GUI gui;
   private final CPU cpu;
   private final ComponentManager cmanager;
   private final Assembler asm;
   private final JTextArea text;


   public AssemblerView(GUI gui) {
      this.gui = gui;
      this.cpu = gui.getCPU();
      this.cmanager = gui.getComponentManager();
      this.asm = new Assembler(this.cpu.getInstructionSet());
      this.text = new JTextArea();
      this.text.setFont(DisplayStyles.FONT_COURIER_BOLD_21);
      JScrollPane scroll = new JScrollPane(this.text);
      scroll.setBounds(1, 1, 600, 542);
      this.add(scroll);
      JButton button = new JButton("Компилировать");
      JButton myBut = new JButton("Save");
      myBut.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent actionEvent) {
            try{
               FileWriter file = new FileWriter("programm.asm");
               file.write(AssemblerView.this.text.getText());
               file.flush();
            }catch (Exception e){

            }
         }
      });
      this.add(myBut);
      myBut.setBounds(625,40,200,30);
      button.setForeground(DisplayStyles.COLOR_TEXT);
      button.setFont(DisplayStyles.FONT_COURIER_PLAIN_12);
      button.setBounds(625, 1, 200, 30);
      button.setFocusable(false);
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if(AssemblerView.this.cpu.isRunning()) {
               AssemblerView.this.showError("Для компиляции остановите выполняющуюся программу");
            } else {
               AssemblerView.this.cmanager.saveDelay();
               boolean clock = AssemblerView.this.cpu.getClockState();
               AssemblerView.this.cpu.setClockState(true);

               try {
                  AssemblerView.this.asm.compileProgram(AssemblerView.this.text.getText());
                  AssemblerView.this.asm.loadProgram(AssemblerView.this.cpu);
               } catch (Exception var4) {
                  AssemblerView.this.showError(var4.getMessage());
               }

               AssemblerView.this.cpu.setClockState(clock);
               AssemblerView.this.cmanager.clearActiveSignals();
               AssemblerView.this.cmanager.restoreDelay();
            }
         }
      });
      this.add(button);
   }

   public void panelActivate() {
      this.text.requestFocus();
   }

   public void panelDeactivate() {}

   public String getPanelName() {
      return "Ассемблер";
   }

   private void showError(String msg) {
      JOptionPane.showMessageDialog(this.gui, msg, "Ошибка", 0);
   }
}
