public final class h {
   public static b a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      double var9 = (double)((long)(var1 - var4) * (long)(var2 - var8) - (long)(var2 - var5) * (long)(var1 - var7));
      double var11 = (double)((long)(var2 - var5) * (long)(var0 - var6) - (long)(var0 - var3) * (long)(var2 - var8));
      double var13 = (double)((long)(var0 - var3) * (long)(var1 - var7) - (long)(var1 - var4) * (long)(var0 - var6));
      double var15 = Math.sqrt(var9 * var9 + var11 * var11 + var13 * var13) / 4096.0D;
      return new b((int)(var9 / var15), (int)(var11 / var15), (int)(var13 / var15));
   }

   public static boolean a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if ((var4 - var2) * (var1 - var3) - (var0 - var2) * (var5 - var3) > 0) {
         return false;
      } else if ((var6 - var4) * (var1 - var5) - (var0 - var4) * (var7 - var5) > 0) {
         return false;
      } else {
         return (var2 - var6) * (var1 - var7) - (var0 - var6) * (var3 - var7) <= 0;
      }
   }

   public static boolean a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      if ((var4 - var2) * (var1 - var3) - (var0 - var2) * (var5 - var3) > 0) {
         return false;
      } else if ((var6 - var4) * (var1 - var5) - (var0 - var4) * (var7 - var5) > 0) {
         return false;
      } else if ((var8 - var6) * (var1 - var7) - (var0 - var6) * (var9 - var7) > 0) {
         return false;
      } else {
         return (var2 - var8) * (var1 - var9) - (var0 - var8) * (var3 - var9) <= 0;
      }
   }

   static {
      new b();
   }
}
