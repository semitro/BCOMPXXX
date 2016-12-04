package ru.ifmo.cs.bcomp.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import ru.ifmo.cs.bcomp.Assembler;
import ru.ifmo.cs.bcomp.BasicComp;
import ru.ifmo.cs.bcomp.CPU;
import ru.ifmo.cs.bcomp.ControlSignal;
import ru.ifmo.cs.bcomp.IOCtrl;
import ru.ifmo.cs.bcomp.MicroProgram;
import ru.ifmo.cs.bcomp.Utils;
import ru.ifmo.cs.bcomp.CPU.Reg;
import ru.ifmo.cs.elements.DataDestination;

public class CLI {
    private final BasicComp bcomp;
    private final MicroProgram mp;
    private final CPU cpu;
    private final IOCtrl[] ioctrls;
    private final Assembler asm;
    private final ArrayList<Integer> writelist = new ArrayList();
    private static final int SLEEP_TIME = 1;
    private volatile int addr;
    private volatile boolean printOnStop = true;
    private volatile boolean printRegsTitle = false;
    private volatile boolean printMicroTitle = false;
    private volatile int sleep = 0;

    public CLI(MicroProgram mp) throws Exception {
        this.bcomp = new BasicComp(this.mp = mp);
        this.cpu = this.bcomp.getCPU();
        this.bcomp.addDestination(ControlSignal.MEMORY_WRITE, new DataDestination() {
            public void setValue(int value) {
                int addr = CLI.this.cpu.getRegValue(Reg.ADDR);
                if(!CLI.this.writelist.contains(Integer.valueOf(addr))) {
                    CLI.this.writelist.add(Integer.valueOf(addr));
                }

            }
        });
        this.cpu.setCPUStartListener(new Runnable() {
            public void run() {
                if(CLI.this.printOnStop) {
                    CLI.this.writelist.clear();
                    CLI.this.addr = CLI.this.getIP();
                    CLI.this.printRegsTitle();
                }

            }
        });
        this.cpu.setCPUStopListener(new Runnable() {
            public void run() {
                CLI.this.sleep = 0;
                if(CLI.this.printOnStop) {
                    String add;
                    if(CLI.this.writelist.isEmpty()) {
                        add = "";
                    } else {
                        add = " " + CLI.this.getMemory(((Integer)CLI.this.writelist.get(0)).intValue());
                        CLI.this.writelist.remove(0);
                    }

                    CLI.this.printRegs(add);
                    Iterator i$ = CLI.this.writelist.iterator();

                    while(i$.hasNext()) {
                        Integer wraddr = (Integer)i$.next();
                        System.out.println(String.format("%1$34s", new Object[]{" "}) + CLI.this.getMemory(wraddr.intValue()));
                    }
                }

            }
        });
        this.cpu.setTickFinishListener(new Runnable() {
            public void run() {
                if(CLI.this.sleep > 0) {
                    try {
                        Thread.sleep((long)CLI.this.sleep);
                    } catch (InterruptedException var2) {
                        ;
                    }
                }

            }
        });
        this.asm = new Assembler(this.cpu.getInstructionSet());
        this.ioctrls = this.bcomp.getIOCtrls();
    }

    private String getReg(Reg reg) {
        return Utils.toHex(this.cpu.getRegValue(reg), this.cpu.getRegWidth(reg));
    }

    private String getFormattedState(int flag) {
        return Utils.toBinaryFlag(this.cpu.getStateValue(flag));
    }

    private void printRegsTitle() {
        if(this.printRegsTitle) {
            System.out.println(this.cpu.getClockState()?"Адр Знчн  СК  РА  РК   РД    А  C Адр Знчн":"Адр МК   СК  РА  РК   РД    А  C   БР  N Z СчМК");
            this.printRegsTitle = false;
        }

    }

    private String getMemory(int addr) {
        return Utils.toHex(addr, 11) + " " + Utils.toHex(this.cpu.getMemoryValue(addr), 16);
    }

    private String getMicroMemory(int addr) {
        return Utils.toHex(addr, 8) + " " + Utils.toHex(this.cpu.getMicroMemoryValue(addr), 16);
    }

    private void printMicroMemory(int addr) {
        if(this.printMicroTitle) {
            System.out.println("Адр МК");
            this.printMicroTitle = false;
        }

        System.out.println(this.getMicroMemory(addr) + " " + this.mp.decodeCmd(this.cpu.getMicroMemoryValue(addr)));
    }

    private String getRegs() {
        return this.getReg(Reg.IP) + " " + this.getReg(Reg.ADDR) + " " + this.getReg(Reg.INSTR) + " " + this.getReg(Reg.DATA) + " " + this.getReg(Reg.ACCUM) + " " + this.getFormattedState(0);
    }

    private void printRegs(String add) {
        System.out.println(this.cpu.getClockState()?this.getMemory(this.addr) + " " + this.getRegs() + add:this.getMicroMemory(this.addr) + " " + this.getRegs() + " " + this.getReg(Reg.BUF) + " " + this.getFormattedState(2) + " " + this.getFormattedState(1) + "  " + this.getReg(Reg.MIP));
    }

    private void printIO(int ioaddr) {
        System.out.println("ВУ" + ioaddr + ": Флаг = " + Utils.toBinaryFlag(this.ioctrls[ioaddr].getFlag()) + " РДВУ = " + Utils.toHex(this.ioctrls[ioaddr].getData(), 8));
    }

    private int getIP() {
        return this.cpu.getClockState()?this.cpu.getRegValue(Reg.IP):this.cpu.getRegValue(Reg.MIP);
    }

    private boolean checkCmd(String cmd, String check) {
        return cmd.equalsIgnoreCase(check.substring(0, Math.min(check.length(), cmd.length())));
    }

    private void checkResult(boolean result) throws Exception {
        if(!result) {
            throw new Exception("операция не выполнена: выполняется программа");
        }
    }

    private void printHelp() {
        System.out.println("Доступные команды:\na[ddress]\t- Пультовая операция \"Ввод адреса\"\nw[rite]\t\t- Пультовая операция \"Запись\"\nr[ead]\t\t- Пультовая операция \"Чтение\"\ns[tart]\t\t- Пультовая операция \"Пуск\"\nc[continue]\t- Пультовая операция \"Продолжить\"\nru[n]\t\t- Переключение режима Работа/Останов\ncl[ock]\t\t- Переключение режима потактового выполнения\nma[ddress]\t- Переход на микрокоманду\nmw[rite]\t- Запись микрокоманды\nmr[ead]\t\t- Чтение микрокоманды\nio\t\t- Вывод состояния всех ВУ\nio addr\t\t- Вывод состояния указанного ВУ\nio addr value\t- Запись value в указанное ВУ\nflag addr\t- Установка флага готовности указанного ВУ\nasm\t\t- Ввод программы на ассемблере\n{exit|quit}\t- Выход из эмулятора\nvalue\t\t- Ввод шестнадцатеричного значения в клавишный регистр\nlabel\t\t- Ввод адреса метки в клавишный регистр");
    }

    public void cli() {
        Scanner input = new Scanner(System.in);
        this.bcomp.startTimer();
        System.out.println("Эмулятор Базовой ЭВМ. Версия r" + CLI.class.getPackage().getImplementationVersion() + "\n" + "Загружена " + this.cpu.getMicroProgramName() + " микропрограмма\n" + "Цикл прерывания начинается с адреса " + Utils.toHex(this.cpu.getIntrCycleStartAddr(), 8) + "\n" + "БЭВМ готова к работе.\n" + "Используйте ? или help для получения справки");

        while(true) {
            String line;
            String[] cmds;
            do {
                try {
                    line = input.nextLine();
                } catch (Exception var11) {
                    System.exit(0);
                    return;
                }

                cmds = line.split("[ \t]+");
            } while(cmds.length == 0);

            int i = 0;

            label202:
            for(this.printRegsTitle = this.printMicroTitle = true; i < cmds.length; ++i) {
                String cmd = cmds[i];
                if(!cmd.equals("")) {
                    if(cmd.charAt(0) == 35) {
                        break;
                    }

                    if(this.checkCmd(cmd, "exit") || this.checkCmd(cmd, "quit")) {
                        System.exit(0);
                    }

                    if(!this.checkCmd(cmd, "?") && !this.checkCmd(cmd, "help")) {
                        int value;
                        try {
                            if(this.checkCmd(cmd, "address")) {
                                this.checkResult(this.cpu.runSetAddr());
                                continue;
                            }

                            if(this.checkCmd(cmd, "write")) {
                                this.checkResult(this.cpu.runWrite());
                                continue;
                            }

                            if(this.checkCmd(cmd, "read")) {
                                this.checkResult(this.cpu.runRead());
                                continue;
                            }

                            if(this.checkCmd(cmd, "start")) {
                                if(i == cmds.length - 1) {
                                    this.sleep = 1;
                                    this.checkResult(this.cpu.startStart());
                                } else {
                                    this.checkResult(this.cpu.runStart());
                                }
                                continue;
                            }

                            if(this.checkCmd(cmd, "continue")) {
                                if(i == cmds.length - 1) {
                                    this.sleep = 1;
                                    this.checkResult(this.cpu.startContinue());
                                } else {
                                    this.checkResult(this.cpu.runContinue());
                                }
                                continue;
                            }

                            if(this.checkCmd(cmd, "clock")) {
                                System.out.println("Такт: " + (this.cpu.invertClockState()?"Нет":"Да"));
                                continue;
                            }

                            if(this.checkCmd(cmd, "run")) {
                                this.cpu.invertRunState();
                                System.out.println("Режим работы: " + (this.cpu.getStateValue(7) == 1?"Работа":"Останов"));
                                continue;
                            }

                            if(this.checkCmd(cmd, "maddress")) {
                                this.checkResult(this.cpu.runSetMAddr());
                                this.printMicroMemory(this.cpu.getRegValue(Reg.MIP));
                                continue;
                            }

                            int var13;
                            if(this.checkCmd(cmd, "mwrite")) {
                                var13 = this.cpu.getRegValue(Reg.MIP);
                                this.checkResult(this.cpu.runMWrite());
                                this.printMicroMemory(var13);
                                continue;
                            }

                            if(this.checkCmd(cmd, "mread")) {
                                var13 = this.cpu.getRegValue(Reg.MIP);
                                this.checkResult(this.cpu.runMRead());
                                this.printMicroMemory(var13);
                                continue;
                            }

                            if(this.checkCmd(cmd, "io")) {
                                if(i == cmds.length - 1) {
                                    var13 = 0;

                                    while(true) {
                                        if(var13 >= 4) {
                                            continue label202;
                                        }

                                        this.printIO(var13);
                                        ++var13;
                                    }
                                }

                                ++i;
                                var13 = Integer.parseInt(cmds[i], 16);
                                if(i < cmds.length - 1) {
                                    ++i;
                                    value = Integer.parseInt(cmds[i], 16);
                                    this.ioctrls[var13].setData(value);
                                }

                                this.printIO(var13);
                                continue;
                            }

                            if(this.checkCmd(cmd, "flag")) {
                                if(i == cmds.length - 1) {
                                    throw new Exception("команда flag требует аргумент");
                                }

                                ++i;
                                var13 = Integer.parseInt(cmds[i], 16);
                                this.ioctrls[var13].setFlag();
                                this.printIO(var13);
                                continue;
                            }

                            if(this.checkCmd(cmd, "asm") || this.checkCmd(cmd, "assembler")) {
                                String e = "";
                                System.out.println("Введите текст программы. Для окончания введите END");

                                while(true) {
                                    line = input.nextLine();
                                    if(line.equalsIgnoreCase("END")) {
                                        this.printOnStop = false;
                                        this.asm.compileProgram(e);
                                        this.asm.loadProgram(this.cpu);
                                        System.out.println("Программа начинается с адреса " + Utils.toHex(this.asm.getBeginAddr(), 11));
                                        this.printOnStop = true;

                                        try {
                                            System.out.println("Результат по адресу " + Utils.toHex(this.asm.getLabelAddr("R"), 11));
                                        } catch (Exception var10) {
                                            ;
                                        }
                                        continue label202;
                                    }

                                    e = e.concat(line.concat("\n"));
                                }
                            }
                        } catch (Exception var12) {
                            this.printOnStop = true;
                            System.out.println("Ошибка: " + var12.getMessage());
                            continue;
                        }

                        try {
                            if(Utils.isHexNumeric(cmd)) {
                                value = Integer.parseInt(cmd, 16);
                            } else {
                                value = this.asm.getLabelAddr(cmd.toUpperCase());
                            }

                            this.cpu.setRegKey(value);
                        } catch (Exception var9) {
                            System.out.println("Неизвестная команда " + cmd);
                        }
                    } else {
                        this.printHelp();
                    }
                }
            }
        }
    }
}