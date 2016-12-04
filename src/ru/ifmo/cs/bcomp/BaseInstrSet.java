package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.bcomp.Instruction;

public class BaseInstrSet {

   public static final Instruction[] instructions = new Instruction[]{new Instruction(0, "ISZ", Instruction.Type.ADDR), new Instruction(4096, "AND", Instruction.Type.ADDR), new Instruction(8192, "JSR", Instruction.Type.ADDR), new Instruction(12288, "MOV", Instruction.Type.ADDR), new Instruction(16384, "ADD", Instruction.Type.ADDR), new Instruction(20480, "ADC", Instruction.Type.ADDR), new Instruction(24576, "SUB", Instruction.Type.ADDR), new Instruction('\u8000', "BCS", Instruction.Type.ADDR), new Instruction('\u9000', "BPL", Instruction.Type.ADDR), new Instruction('\ua000', "BMI", Instruction.Type.ADDR), new Instruction('\ub000', "BEQ", Instruction.Type.ADDR), new Instruction('\uc000', "BR", Instruction.Type.ADDR), new Instruction('\ue000', "CLF", Instruction.Type.IO), new Instruction('\ue100', "TSF", Instruction.Type.IO), new Instruction('\ue200', "IN", Instruction.Type.IO), new Instruction('\ue300', "OUT", Instruction.Type.IO), new Instruction('\uf000', "HLT", Instruction.Type.NONADDR), new Instruction('\uf100', "NOP", Instruction.Type.NONADDR), new Instruction('\uf200', "CLA", Instruction.Type.NONADDR), new Instruction('\uf300', "CLC", Instruction.Type.NONADDR), new Instruction('\uf400', "CMA", Instruction.Type.NONADDR), new Instruction('\uf500', "CMC", Instruction.Type.NONADDR), new Instruction('\uf600', "ROL", Instruction.Type.NONADDR), new Instruction('\uf700', "ROR", Instruction.Type.NONADDR), new Instruction('\uf800', "INC", Instruction.Type.NONADDR), new Instruction('\uf900', "DEC", Instruction.Type.NONADDR), new Instruction('\ufa00', "EI", Instruction.Type.NONADDR), new Instruction('\ufb00', "DI", Instruction.Type.NONADDR)};


}
