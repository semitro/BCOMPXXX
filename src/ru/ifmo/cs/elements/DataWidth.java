package ru.ifmo.cs.elements;


public class DataWidth {

   public final String name;
   protected final int width;
   protected final int mask;


   public DataWidth(String name, int width) {
      this.name = name;
      this.width = width;
      this.mask = getMask(width);
   }

   public DataWidth(int width) {
      this((String)null, width);
   }

   public static int getMask(int width) {
      return (1 << width) - 1;
   }

   public int getWidth() {
      return this.width;
   }
}
