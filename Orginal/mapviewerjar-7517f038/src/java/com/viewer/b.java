package com.viewer;

public final class b {
   private static long a;
   private static long b = 0L;
   private static int a;
   private static int b;

   public static int a() {
      long var0;
      long var2 = (var0 = System.currentTimeMillis()) - a;
      a = var0;
      b += var2;
      ++a;
      if (b >= 1000L) {
         b = a;
         a = 0;
         b = 0L;
      }

      return b;
   }
}
