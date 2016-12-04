package ru.ifmo.cs.bcomp.ui.io;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;
import ru.ifmo.cs.bcomp.ui.io.FlagIndicator;
import ru.ifmo.cs.bcomp.ui.io.OutputDevice;

public class TextPrinter extends OutputDevice {

   private JTextArea text = null;
   private String charset = null;


   public TextPrinter(IOCtrl ioctrl) {
      super(ioctrl, "Тектовый принтер");
   }

   protected Component getContent() {
      JPanel content = new JPanel(new BorderLayout());
      this.text = new JTextArea(10, 40);
      this.text.setFont(DisplayStyles.FONT_COURIER_BOLD_21);
      this.text.setEditable(false);
      JScrollPane sp = new JScrollPane(this.text);
      content.add("Center", sp);
      JPanel north = new JPanel(new FlowLayout(0));
      JComboBox charsetbox = new JComboBox(new String[]{this.charset = "KOI8-R", "ISO8859-5", "CP866", "CP1251"});
      charsetbox.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JComboBox source = (JComboBox)e.getSource();
            TextPrinter.this.charset = (String)source.getSelectedItem();
         }
      });
      north.add(new JLabel("Кодировка"));
      north.add(charsetbox);
      north.add(new JLabel("Задержка"));
      north.add(this.getSleepSlider());
      north.add(this.getPowerChkBox());
      north.add(new FlagIndicator(this.ioctrl, 30));
      content.add("North", north);
      return content;
   }

   protected void actionPerformed(int value) {
      if(value == 0) {
         this.text.setText("");
      } else {
         byte[] array = new byte[]{(byte)value};

         try {
            this.text.append(new String(array, this.charset));
         } catch (UnsupportedEncodingException var4) {
            ;
         }
      }

   }
}
