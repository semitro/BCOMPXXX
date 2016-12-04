package ru.ifmo.cs.bcomp;


public class Utils {

   private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
   private static final String[] flags = new String[]{"0", "1"};


   public static String toBinaryFlag(int value) {
      return flags[value];
   }

   public static int getBinaryWidth(int width) {
      return width + (width - 1 >> 2);
   }

   public static String toBinary(int value, int width) {
      int chars = getBinaryWidth(width);
      char[] buf = new char[chars];
      int pos = chars;

      for(int i = 0; i < width; ++i) {
         if(i != 0 && (i & 3) == 0) {
            --pos;
            buf[pos] = 32;
         }

         --pos;
         buf[pos] = digits[value & 1];
         value >>= 1;
      }

      return new String(buf);
   }

   public static int getBitNo(int pos, int width, int charWidth) {
      pos -= charWidth >> 1;
      if(pos < 0) {
         return -1;
      } else {
         pos = width - pos / charWidth;
         return pos % 5 == 0?-1:pos - pos / 5 - 1;
      }
   }

   public static int getHexWidth(int width) {
      return width + 3 >> 2;
   }

   public static String toHex(int value, int width) {
      int chars = getHexWidth(width);

      char[] buf;
      for(buf = new char[chars]; chars > 0; value >>= 4) {
         --chars;
         buf[chars] = digits[value & 15];
      }

      return new String(buf);
   }

   public static boolean isNumeric(String s, int radix) {
      if(s != null && s.length() != 0) {
         int i = 0;
         if(s.charAt(0) == 45) {
            ++i;
            if(s.length() == 1) {
               return false;
            }
         }

         while(i < s.length()) {
            if(Character.digit(s.charAt(i), radix) < 0) {
               return false;
            }

            ++i;
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean isHexNumeric(String s) {
      return isNumeric(s, 16);
   }

}
