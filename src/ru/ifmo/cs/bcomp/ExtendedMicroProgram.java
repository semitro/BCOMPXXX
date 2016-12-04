package ru.ifmo.cs.bcomp;

import ru.ifmo.cs.bcomp.ExtendedInstrSet;
import ru.ifmo.cs.bcomp.MicroProgram;

class ExtendedMicroProgram extends MicroProgram {

   static final String NAME = "extended";
   private static final String[][] mp = new String[][]{{null, "0000", null}, {"BEGIN", "0300", null}, {null, "4001", null}, {null, "0311", null}, {null, "4004", null}, {null, "0100", null}, {null, "4003", null}, {null, "EF00", "ADDRCHK"}, {null, "EE00", "ADDRCHK"}, {null, "AD00", "NONADDR"}, {"ADDRCHK", "AB00", "EXEC"}, {"ADDRGET", "0100", null}, {null, "4001", null}, {null, "0001", null}, {null, "9F00", "EXEC"}, {null, "0110", null}, {null, "4002", null}, {null, "0142", null}, {null, "4002", null}, {"EXEC", "EF00", "8-F"}, {null, "EE00", "4-7"}, {null, "EC00", "JMP"}, {"CALL", "0100", null}, {null, "4003", null}, {"CALLSTR", "0040", null}, {null, "4001", null}, {null, "0001", null}, {null, "0140", null}, {null, "4002", null}, {null, "0102", null}, {null, "4001", null}, {null, "0300", null}, {null, "4002", null}, {null, "0202", null}, {null, "4004", null}, {null, "8300", "INTR"}, {"4-7", "ED00", "6-7"}, {null, "EC00", "BPL"}, {"BCS", "8000", "INTR"}, {"JMP", "0100", null}, {null, "4004", null}, {null, "8300", "INTR"}, {"BPL", "C200", "INTR"}, {null, "8300", "JMP"}, {"6-7", "EC00", "BEQ"}, {"BMI", "8200", "INTR"}, {null, "8300", "JMP"}, {"BEQ", "8100", "INTR"}, {null, "8300", "JMP"}, {"8-F", "0100", null}, {null, "4001", null}, {null, "EE00", "C-F"}, {null, "ED00", "A-B"}, {null, "EC00", "CMP"}, {"MOV", "1000", null}, {null, "4002", null}, {"STORE", "0002", null}, {null, "8300", "INTR"}, {"CMP", "0001", null}, {null, "1190", null}, {null, "4070", null}, {null, "8300", "INTR"}, {"A-B", "0001", null}, {null, "EC00", "ISZ"}, {"LOOP", "0140", null}, {null, "4002", null}, {"SAVE", "0002", null}, {null, "DF00", "INTR"}, {"SKPCMD", "0310", null}, {null, "4004", null}, {null, "8300", "INTR"}, {"ISZ", "0110", null}, {null, "4002", null}, {null, "8300", "SAVE"}, {"C-F", "0001", null}, {null, "ED00", "E-F"}, {null, "EC00", "ADC"}, {"SUB", "1190", null}, {null, "4075", null}, {null, "8300", "INTR"}, {"ADC", "8000", "ADD"}, {null, "1110", null}, {null, "4075", null}, {null, "8300", "INTR"}, {"E-F", "EC00", "AND"}, {"ADD", "1100", null}, {null, "4075", null}, {null, "8300", "INTR"}, {"AND", "1120", null}, {null, "4035", null}, {null, "8300", "INTR"}, {"NONADDR", "EC00", "IO"}, {null, "EB00", "08-F"}, {null, "EA00", "04-7"}, {null, "E900", "02-3"}, {null, "A800", "INTR"}, {"HLT", "4008", null}, {"STP", "8300", "BEGIN"}, {"02-3", "E800", "DI"}, {"EI", "4800", null}, {null, "8300", "INTR"}, {"DI", "4400", null}, {null, "8300", "INTR"}, {"04-7", "E900", "06-7"}, {"02-3", "E800", "CMC"}, {"CLC", "4080", null}, {null, "8300", "INTR"}, {"CMC", "C000", "CLC"}, {null, "40C0", null}, {null, "8300", "INTR"}, {"06-7", "E800", "RIGHT"}, {"LEFT", "A700", "ROL"}, {"SHL", "4080", null}, {"ROL", "0008", null}, {null, "4075", null}, {null, "8300", "INTR"}, {"RIGHT", "A700", "ROR"}, {"SHR", "4080", null}, {"ROR", "0004", null}, {null, "4075", null}, {null, "8300", "INTR"}, {"08-F", "EA00", "0C-F"}, {null, "E900", "0A-B"}, {null, "E800", "CMA"}, {"CLA", "0020", null}, {null, "4035", null}, {null, "8300", "INTR"}, {"CMA", "1040", null}, {null, "4035", null}, {null, "8300", "INTR"}, {"0A-B", "E800", "DEC"}, {"INC", "1010", null}, {null, "4075", null}, {null, "8300", "INTR"}, {"DEC", "1080", null}, {null, "4075", null}, {null, "8300", "INTR"}, {"0C-F", "0040", null}, {null, "4001", null}, {null, "0001", null}, {null, "E900", "0E-F"}, {null, "E800", "PUSH"}, {"SWAP", "0100", null}, {null, "4001", null}, {null, "0001", null}, {null, "0100", null}, {null, "4003", null}, {null, "1000", null}, {null, "4002", null}, {null, "0202", null}, {null, "4035", null}, {null, "8300", "INTR"}, {"PUSH", "0140", null}, {null, "4002", null}, {null, "0102", null}, {null, "4001", null}, {null, "E700", "PUSHF"}, {null, "1000", null}, {null, "4002", null}, {null, "8300", "STORE"}, {"PUSHF", "2000", null}, {null, "4002", null}, {null, "8300", "STORE"}, {"0E-F", "0110", null}, {null, "4002", null}, {null, "0142", null}, {null, "4001", null}, {null, "0001", null}, {null, "E800", "RET"}, {null, "E700", "POPF"}, {"POP", "0100", null}, {null, "4035", null}, {null, "8300", "INTR"}, {"RET", "0100", null}, {null, "4004", null}, {null, "A700", "INTR"}, {"IRET", "0040", null}, {null, "4001", null}, {null, "0001", null}, {null, "0110", null}, {null, "4002", null}, {null, "0142", null}, {null, "4001", null}, {null, "0001", null}, {"POPF", "D000", "SETC"}, {null, "4080", null}, {null, "8300", "CHKZ"}, {"SETC", "40C0", null}, {"CHKZ", "D100", "SETZ"}, {null, "0040", null}, {null, "4010", null}, {null, "8300", "CHKN"}, {"SETZ", "0020", null}, {null, "4010", null}, {"CHKN", "D200", "SETN"}, {null, "0020", null}, {null, "4020", null}, {null, "8300", "CHKINTR"}, {"SETN", "0040", null}, {null, "4020", null}, {"CHKINTR", "D400", "EI"}, {null, "8300", "DI"}, {"IO", "4100", null}, {"TSF", "C600", "SKPCMD"}, {"INTR", "8700", "HLT"}, {null, "8500", "BEGIN"}, {null, "0040", null}, {null, "4001", null}, {null, "0001", null}, {null, "0140", null}, {null, "4002", null}, {null, "0102", null}, {null, "4001", null}, {null, "2000", null}, {null, "4002", null}, {null, "0022", null}, {null, "4001", null}, {null, "0001", null}, {null, "0100", null}, {null, "4403", null}, {null, "8300", "CALLSTR"}, {"ADDR", "3000", null}, {null, "4004", null}, {null, "8300", "HLT"}, {"READ", "0300", null}, {null, "4001", null}, {null, "0311", null}, {null, "4004", null}, {null, "8300", "HLT"}, {"WRITE", "0300", null}, {null, "4001", null}, {null, "3000", null}, {null, "4002", null}, {null, "0312", null}, {null, "4004", null}, {null, "8300", "HLT"}, {"START", "0020", null}, {null, "4675", null}, {null, "0040", null}, {null, "4001", null}, {null, "4002", null}, {null, "8300", "STORE"}, {"EXECCNT", "0000", null}};


   ExtendedMicroProgram() {
      super("расширенная", ExtendedInstrSet.instructions, mp);
   }

}
