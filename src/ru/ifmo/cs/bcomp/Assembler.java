package ru.ifmo.cs.bcomp;

import java.util.ArrayList;
import java.util.Iterator;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.Instruction;
import ru.ifmo.cs.bcomp.Utils;

public class Assembler {

   private ArrayList labels;
   private ArrayList cmds;
   private Instruction[] instrset;


   public Assembler(Instruction[] instrset) {
      this.instrset = instrset;
   }

   public void compileProgram(String program) throws Exception {
      String[] prog = program.replace("\r", "").toUpperCase().split("\n");
      int addr = 0;
      int lineno = 0;
      this.labels = new ArrayList();
      this.cmds = new ArrayList();

      try {
         String[] e = prog;
         int label = prog.length;

         for(int i$ = 0; i$ < label; ++i$) {
            String l = e[i$];
            ++lineno;
            String[] line = l.trim().split("[#;]+");
            if(line.length != 0 && !line[0].equals("")) {
               line = line[0].trim().split("[ \t]+");
               if(line.length != 0 && !line[0].equals("")) {
                  if(line[0].equals("ORG")) {
                     if(line.length != 2) {
                        throw new Exception("Директива ORG требует один и только один аргумент");
                     }

                     this.checkAddr(addr = Integer.parseInt(line[1], 16));
                  } else {
                     int col = 0;
                     String instr;
                     if(line[col].charAt(line[col].length() - 1) == 58) {
                        instr = line[col].substring(0, line[col].length() - 1);
                        Assembler.Label labelname = this.getLabel(instr);
                        if(labelname.hasAddress()) {
                           throw new Exception("Метка " + instr + " объявлена повторно");
                        }

                        labelname.setAddr(addr);
                        ++col;
                     }

                     if(col != line.length) {
                        String var23;
                        if(line[col].equals("WORD")) {
                           if(col++ == line.length - 1) {
                              throw new Exception("Директива WORD должна иметь как минимум один аргумент");
                           }

                           if(line.length - col == 3 && line[col + 1].equals("DUP")) {
                              int var20 = Integer.parseInt(line[col], 16);
                              if(var20 < 1 || addr + var20 > 2047) {
                                 throw new Exception("Указано недопустимое количество значений");
                              }

                              col += 2;
                              if(line[col].charAt(0) != 40 || line[col].charAt(line[col].length() - 1) != 41) {
                                 throw new Exception("Значение после DUP должно быть в скобках");
                              }

                              var23 = line[col].substring(1, line[col].length() - 1);
                              this.createWord(addr, var23, var20);
                              addr += var20;
                           } else {
                              for(instr = line[col++]; col < line.length; instr = instr.concat(" ").concat(line[col++])) {
                                 ;
                              }

                              String[] var22 = instr.split(",");
                              String[] addrtype = var22;
                              int len$ = var22.length;

                              for(int i$1 = 0; i$1 < len$; ++i$1) {
                                 String value = addrtype[i$1];
                                 this.createWord(addr++, value.trim(), 1);
                              }
                           }
                        } else {
                           Instruction var21 = this.findInstruction(line[col]);
                           if(var21 == null) {
                              throw new Exception("Неизвестная команда " + line[col]);
                           }

                           switch(Assembler.NamelessClass127842164.$SwitchMap$ru$ifmo$cs$bcomp$Instruction$Type[var21.getType().ordinal()]) {
                           case 1:
                              if(col != line.length - 2) {
                                 throw new Exception("Адресная команда " + line[col] + " требует один аргумент");
                              }

                              var23 = line[col + 1];
                              short var24;
                              if(var23.charAt(0) == 40) {
                                 if(var23.charAt(var23.length() - 1) != 41) {
                                    throw new Exception("Нет закрывающей скобки");
                                 }

                                 var23 = var23.substring(1, var23.length() - 1);
                                 var24 = 2048;
                              } else {
                                 var24 = 0;
                              }

                              if(Utils.isHexNumeric(var23)) {
                                 this.cmds.add(new Assembler.Command(addr++, var21.getInstr() + var24 + Integer.parseInt(var23, 16), (Assembler.NamelessClass127842164)null));
                              } else {
                                 this.cmds.add(new Assembler.Command(addr++, var21.getInstr() + var24, this.getLabel(var23), (Assembler.NamelessClass127842164)null));
                              }
                              break;
                           case 2:
                              if(col != line.length - 1) {
                                 throw new Exception("Безадресная команда " + line[col] + " не требует аргументов");
                              }

                              this.cmds.add(new Assembler.Command(addr++, var21.getInstr(), (Assembler.NamelessClass127842164)null));
                              break;
                           case 3:
                              if(col != line.length - 2) {
                                 throw new Exception("Строка " + lineno + ": Команда ввода-вывода " + line[col] + " требует один и только один аргумент");
                              }

                              this.cmds.add(new Assembler.Command(addr++, var21.getInstr() + (Integer.parseInt(line[col + 1], 16) & 255), (Assembler.NamelessClass127842164)null));
                           }
                        }
                     }
                  }
               }
            }
         }

         Iterator var18 = this.labels.iterator();

         Assembler.Label var19;
         do {
            if(!var18.hasNext()) {
               return;
            }

            var19 = (Assembler.Label)var18.next();
         } while(var19.hasAddress());

         throw new Exception("Ссылка на неопределённую метку " + var19.label);
      } catch (Exception var17) {
         throw new Exception("Строка " + lineno + ": " + var17.getMessage());
      }
   }

   private Assembler.Label findLabel(String labelname) {
      Iterator i$ = this.labels.iterator();

      Assembler.Label label;
      do {
         if(!i$.hasNext()) {
            return null;
         }

         label = (Assembler.Label)i$.next();
      } while(!label.label.equals(labelname));

      return label;
   }

   private Assembler.Label getLabel(String labelname) throws Exception {
      Assembler.Label label = this.findLabel(labelname);
      if(label == null) {
         label = new Assembler.Label(labelname, (Assembler.NamelessClass127842164)null);
         this.labels.add(label);
      }

      return label;
   }

   private void createWord(int addr, String value, int size) throws Exception {
      if(value.equals("")) {
         throw new Exception("Пустое значение");
      } else if(!value.equals("?")) {
         int cmd = 0;
         Assembler.Label arg = null;
         if(Utils.isHexNumeric(value)) {
            cmd = Integer.parseInt(value, 16);
         } else {
            arg = this.getLabel(value);
         }

         this.cmds.add(new Assembler.Command(addr, cmd, arg, size, (Assembler.NamelessClass127842164)null));
      }
   }

   private void checkAddr(int addr) throws Exception {
      if(addr < 0 || addr > 2047) {
         throw new Exception("Адрес выходит из допустимых значений");
      }
   }

   public Instruction findInstruction(String mnemonics) {
      Instruction[] arr$ = this.instrset;
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Instruction instr = arr$[i$];
         if(instr.getMnemonics().equals(mnemonics)) {
            return instr;
         }
      }

      return null;
   }

   public void loadProgram(CPU cpu) throws Exception {
      if(cpu.isRunning()) {
         throw new Exception("Операция невозможна: выполняется программа");
      } else {
         Iterator i$ = this.cmds.iterator();

         while(i$.hasNext()) {
            Assembler.Command cmd = (Assembler.Command)i$.next();
            if(!cpu.runSetAddr(cmd.addr)) {
               throw new Exception("Операция прервана: выполняется программа");
            }

            for(int i = 0; i < cmd.size; ++i) {
               if(!cpu.runWrite(cmd.getCommand())) {
                  throw new Exception("Операция прервана: выполняется программа");
               }
            }
         }

         if(!cpu.runSetAddr(this.getBeginAddr())) {
            throw new Exception("Операция прервана: выполняется программа");
         }
      }
   }

   public int getLabelAddr(String labelname) throws Exception {
      Assembler.Label label = this.findLabel(labelname);
      if(label == null) {
         throw new Exception("Метка " + labelname + " не найдена");
      } else {
         return label.addr.intValue();
      }
   }

   public int getBeginAddr() throws Exception {
      return this.getLabelAddr("BEGIN");
   }

   private class Label {

      private final String label;
      private Integer addr;


      private Label(String label) throws Exception {
         this.label = label;
         if(label.equals("")) {
            throw new Exception("Имя метки не может быть пустым");
         } else if(Utils.isHexNumeric(label)) {
            throw new Exception("Имя метки не должно быть шестнадцатеричным числом");
         }
      }

      private void setAddr(int addr) throws Exception {
         Assembler.this.checkAddr(addr);
         this.addr = Integer.valueOf(addr);
      }

      private boolean hasAddress() {
         return this.addr != null;
      }

      // $FF: synthetic method
      Label(String x1, Assembler.NamelessClass127842164 x2) throws Exception {
         this(x1);
      }
   }

   // $FF: synthetic class
   static class NamelessClass127842164 {

      // $FF: synthetic field
      static final int[] $SwitchMap$ru$ifmo$cs$bcomp$Instruction$Type = new int[Instruction.Type.values().length];


      static {
         try {
            $SwitchMap$ru$ifmo$cs$bcomp$Instruction$Type[Instruction.Type.ADDR.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$Instruction$Type[Instruction.Type.NONADDR.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$ru$ifmo$cs$bcomp$Instruction$Type[Instruction.Type.IO.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   protected class Command {

      private final Assembler.Label arg;
      private final int cmd;
      private final int addr;
      private final int size;


      private Command(int addr, int cmd, Assembler.Label arg, int size) throws Exception {
         this.addr = addr;
         this.cmd = cmd;
         this.arg = arg;
         this.size = size;
         Assembler.this.checkAddr(addr);
      }

      private Command(int addr, int cmd, Assembler.Label arg) throws Exception {
         this(addr, cmd, arg, 1);
      }

      private Command(int addr, int cmd) throws Exception {
         this(addr, cmd, (Assembler.Label)null, 1);
      }

      protected int getCommand() {
         return this.arg == null?this.cmd:this.cmd + this.arg.addr.intValue();
      }

      // $FF: synthetic method
      Command(int x1, int x2, Assembler.NamelessClass127842164 x3) throws Exception {
         this(x1, x2);
      }

      // $FF: synthetic method
      Command(int x1, int x2, Assembler.Label x3, Assembler.NamelessClass127842164 x4) throws Exception {
         this(x1, x2, x3);
      }

      // $FF: synthetic method
      Command(int x1, int x2, Assembler.Label x3, int x4, Assembler.NamelessClass127842164 x5) throws Exception {
         this(x1, x2, x3, x4);
      }
   }
}
