package ru.ifmo.cs.bcomp.ui.io;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.ui.components.DisplayStyles;
import ru.ifmo.cs.bcomp.ui.io.FlagIndicator;
import ru.ifmo.cs.bcomp.ui.io.IODevice;

public class Keyboard extends IODevice {

   static final Dimension DIMS = new Dimension(45, 30);
   private static final String[][][] KEYS = new String[][][]{{{"1", "!", "1", "!"}, {"2", "@", "2", "\""}, {"3", "#", "3", "#"}, {"4", "$", "4", "*"}, {"5", "%", "5", ":"}, {"6", "^", "6", ","}, {"7", "&", "7", "."}, {"8", "*", "8", ";"}, {"9", "(", "9", "("}, {"0", ")", "0", ")"}, {"-", "_", "-", "_"}, {"=", "+", "=", "+"}, {"\\", "|", "\\", "|"}}, {{"q", "Q", "й", "Й"}, {"w", "W", "ц", "Ц"}, {"e", "E", "у", "У"}, {"r", "R", "к", "К"}, {"t", "T", "е", "Е"}, {"y", "Y", "н", "Н"}, {"u", "U", "г", "Г"}, {"i", "I", "ш", "Ш"}, {"o", "O", "щ", "Щ"}, {"p", "P", "з", "З"}, {"[", "{", "х", "Х"}, {"]", "}", "ъ", "Ъ"}}, {{"a", "A", "ф", "Ф"}, {"s", "S", "ы", "Ы"}, {"d", "D", "в", "В"}, {"f", "F", "а", "А"}, {"g", "G", "п", "П"}, {"h", "H", "р", "Р"}, {"j", "J", "о", "О"}, {"k", "K", "л", "Л"}, {"l", "L", "д", "Д"}, {";", ":", "ж", "Ж"}, {"\'", "\"", "э", "Э"}}, {{"z", "Z", "я", "Я"}, {"x", "X", "ч", "Ч"}, {"c", "C", "с", "С"}, {"v", "V", "м", "М"}, {"b", "B", "и", "И"}, {"n", "N", "т", "Т"}, {"m", "M", "ь", "Ь"}, {",", "<", "б", "Б"}, {".", ">", "ю", "Ю"}, {"/", "?", "ё", "Ё"}}};
   private final ArrayList keys = new ArrayList();
   private String charset;


   public Keyboard(IOCtrl ioctrl) {
      super(ioctrl, "Клавиатура");
   }

   protected Component getContent() {
      JPanel content = new JPanel(new GridLayout(5, 1, 0, 0));
      String[][][] jrow = KEYS;
      int caps = jrow.length;

      for(int space = 0; space < caps; ++space) {
         String[][] latrus = jrow[space];
         JPanel charsetbox = new JPanel(new FlowLayout(1, 0, 0));
         String[][] arr$ = latrus;
         int len$ = latrus.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            String[] key = arr$[i$];
            Keyboard.Key k = new Keyboard.Key(key);
            charsetbox.add(k);
            this.keys.add(k);
         }

         content.add(charsetbox);
      }

      JPanel var12 = new JPanel(new FlowLayout(1, 0, 0));
      var12.add(new FlagIndicator(this.ioctrl, 30));
      Keyboard.SizedButton var13 = new Keyboard.SizedButton("Caps Lock");
      var13.buttonSetSize(new Dimension(120, 30));
      var13.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Iterator i$ = Keyboard.this.keys.iterator();

            while(i$.hasNext()) {
               Keyboard.Key key = (Keyboard.Key)i$.next();
               key.caps = !key.caps;
               key.setActiveLayout();
            }

         }
      });
      var12.add(var13);
      Keyboard.SizedButton var14 = new Keyboard.SizedButton(" ");
      var14.buttonSetSize(new Dimension(180, 30));
      var14.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Keyboard.this.buttonPressed(" ");
         }
      });
      var12.add(var14);
      Keyboard.SizedButton var15 = new Keyboard.SizedButton("Lat/Рус");
      var15.buttonSetSize(new Dimension(120, 30));
      var15.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            Iterator i$ = Keyboard.this.keys.iterator();

            while(i$.hasNext()) {
               Keyboard.Key key = (Keyboard.Key)i$.next();
               key.lang = key.lang == Keyboard.Lang.EN?Keyboard.Lang.RU:Keyboard.Lang.EN;
               key.setActiveLayout();
            }

         }
      });
      var12.add(var15);
      JComboBox var16 = new JComboBox(new String[]{this.charset = "KOI8-R", "ISO8859-5", "CP866", "CP1251"});
      var16.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            JComboBox source = (JComboBox)e.getSource();
            Keyboard.this.charset = (String)source.getSelectedItem();
         }
      });
      var12.add(var16);
      content.add(var12);
      return content;
   }

   private void buttonPressed(String s) {
      try {
         int c = s.getBytes(this.charset)[0] & 255;
         this.ioctrl.setData(c);
         this.ioctrl.setFlag();
      } catch (Exception var3) {
         ;
      }

   }


   private static enum Lang {

      EN("EN", 0),
      RU("RU", 1);
      // $FF: synthetic field
      private static final Keyboard.Lang[] $VALUES = new Keyboard.Lang[]{EN, RU};


      private Lang(String var1, int var2) {}

   }

   private class SizedButton extends JButton {

      public SizedButton(String title) {
         super(title);
      }

      public final void buttonSetSize(Dimension d) {
         this.setFont(DisplayStyles.FONT_COURIER_PLAIN_12);
         this.setSize(d);
         this.setMinimumSize(d);
         this.setMaximumSize(d);
         this.setPreferredSize(d);
      }
   }

   private class Key extends Keyboard.SizedButton {

      private final String[] values;
      private String active;
      private Keyboard.Lang lang;
      private boolean caps;


      public Key(String[] values) {
         super(values[0]);
         this.lang = Keyboard.Lang.EN;
         this.caps = false;
         this.values = values;
         this.active = values[0];
         this.buttonSetSize(Keyboard.DIMS);
         this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               Keyboard.this.buttonPressed(Key.this.active);
            }
         });
      }

      private void setActiveLayout() {
         switch(Keyboard.NamelessClass2010860541.$SwitchMap$ru$ifmo$cs$bcomp$ui$io$Keyboard$Lang[this.lang.ordinal()]) {
         case 1:
            this.active = this.caps?this.values[1]:this.values[0];
            break;
         case 2:
            this.active = this.caps?this.values[3]:this.values[2];
         }

         this.setText(this.active);
      }
   }

   // $FF: synthetic class
   static class NamelessClass2010860541 {

      // $FF: synthetic field
      static final int[] $SwitchMap$ru$ifmo$cs$bcomp$ui$io$Keyboard$Lang = new int[Keyboard.Lang.values().length];


      static {
         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ui$io$Keyboard$Lang[Keyboard.Lang.EN.ordinal()] = 1;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$ui$io$Keyboard$Lang[Keyboard.Lang.RU.ordinal()] = 2;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
