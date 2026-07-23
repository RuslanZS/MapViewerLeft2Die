import java.util.Vector;

public class e {
   private g a;
   private m[] a;
   private p[] a;

   public e(m[] var1, p[] var2) {
      this.a = var1;
      this.a = var2;
   }

   public final void a(g var1) {
      this.a = var1;
   }

   public final void a(d var1, f var2) {
      var1.a(this.a, var2);
   }

   public final int a() {
      int var1 = Integer.MIN_VALUE;

      for(int var2 = 0; var2 < this.a.length; ++var2) {
         if (this.a[var2].a > var1) {
            var1 = this.a[var2].a;
         }
      }

      return var1;
   }

   public final int b() {
      int var1 = Integer.MIN_VALUE;

      for(int var2 = 0; var2 < this.a.length; ++var2) {
         if (this.a[var2].c > var1) {
            var1 = this.a[var2].c;
         }
      }

      return var1;
   }

   public final int c() {
      int var1 = Integer.MAX_VALUE;

      for(int var2 = 0; var2 < this.a.length; ++var2) {
         if (this.a[var2].a < var1) {
            var1 = this.a[var2].a;
         }
      }

      return var1;
   }

   public final int d() {
      int var1 = Integer.MAX_VALUE;

      for(int var2 = 0; var2 < this.a.length; ++var2) {
         if (this.a[var2].c < var1) {
            var1 = this.a[var2].c;
         }
      }

      return var1;
   }

   public final g a() {
      return this.a;
   }

   public final p[] a() {
      return this.a;
   }

   public e() {
   }

   public static j a(e[] var0) {
      p[] var1 = var0[var0.length - 1].a;
      q[] var2 = new q[var0.length - 1];

      int var3;
      for(var3 = 0; var3 < var2.length; ++var3) {
         var2[var3] = new q(var0[var3], var3);
      }

      for(var3 = 0; var3 < var2.length; ++var3) {
         q var26;
         q var10000 = var26 = var2[var3];
         p[] var5 = var1;
         q var4 = var10000;
         Vector var6 = new Vector();

         for(int var7 = 0; var7 < var5.length; ++var7) {
            p var9;
            m[] var37;
            if ((var9 = var5[var7]) instanceof c) {
               c var10 = (c)var9;
               var37 = new m[]{var10.a, var10.b, var10.c};
            } else if (var9 instanceof a) {
               a var32 = (a)var9;
               var37 = new m[]{var32.a, var32.b, var32.c, var32.d};
            } else {
               var37 = null;
            }

            m[] var8 = var37;
            if (a(var4.a(), var8)) {
               e var33 = var4.a();
               b var34 = a(var33.a);
               b var11 = a(var8);
               m var38 = var8[0];
               m var10001 = var8[1];
               m var29 = var8[2];
               m var13 = var10001;
               m var12 = var38;
               long var18 = (long)(var38.b - var13.b) * (long)(var12.c - var29.c) - (long)(var12.c - var13.c) * (long)(var12.b - var29.b);
               long var20 = (long)(var12.c - var13.c) * (long)(var12.a - var29.a) - (long)(var12.a - var13.a) * (long)(var12.c - var29.c);
               long var22 = (long)(var12.a - var13.a) * (long)(var12.b - var29.b) - (long)(var12.b - var13.b) * (long)(var12.a - var29.a);
               double var24 = Math.sqrt((double)(var18 * var18 + var20 * var20 + var22 * var22)) / 4096.0D;
               int var30 = (int)((double)var18 / var24);
               int var35 = (int)((double)var20 / var24);
               int var36 = (int)((double)var22 / var24);
               b var31 = new b(var30, var35, var36);
               var34.a -= var11.a;
               var34.b -= var11.b;
               var34.c -= var11.c;
               if (var31.a * var34.a + var31.b * var34.b + var31.c * var34.c >= 0) {
                  a(var8);
               }

               var6.addElement(new o(var8));
            }
         }

         o[] var28 = new o[var6.size()];
         var6.copyInto(var28);
         var4.a(var28);
         a(var26, var2);
      }

      a(var2);
      q[][] var27 = a(var2);
      return new j(var2, var27);
   }

   private static void a(q[] var0) {
      int var1 = 0;
      int var2 = 0;

      for(int var3 = 0; var3 < var0.length; ++var3) {
         o[] var4;
         if ((var4 = var0[var3].a()) == null || var4.length == 0) {
            ++var2;
         }

         for(int var5 = 0; var5 < var4.length; ++var5) {
            if (var4[var5].a() == null) {
               ++var1;
            }
         }
      }

      if (var1 > 0) {
         System.out.println("HouseCreator: " + var1 + " порталам не найдены комнаты");
      }

      if (var2 > 0) {
         System.out.println("HouseCreator: " + var2 + " комнатам не найдены порталы");
      }

   }

   private static b a(m[] var0) {
      long var1 = 0L;
      long var3 = 0L;
      long var5 = 0L;

      for(int var7 = 0; var7 < var0.length; ++var7) {
         m var8 = var0[var7];
         var1 += (long)var8.a;
         var3 += (long)var8.b;
         var5 += (long)var8.c;
      }

      var1 /= (long)var0.length;
      var3 /= (long)var0.length;
      var5 /= (long)var0.length;
      return new b((int)var1, (int)var3, (int)var5);
   }

   private static void a(m[] var0) {
      m[] var1 = new m[var0.length];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = var0[var0.length - 1 - var2];
      }

      System.arraycopy(var1, 0, var0, 0, var1.length);
   }

   private static void a(q var0, q[] var1) {
      o[] var2 = var0.a();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         o var4 = var2[var3];

         for(int var5 = 0; var5 < var1.length; ++var5) {
            q var6;
            if ((var6 = var1[var5]) != var0 && a(var6.a(), var4.a())) {
               var4.a(var6);
               break;
            }
         }
      }

   }

   private static q[][] a(q[] var0) {
      q[][] var1 = new q[var0.length][];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         o[] var3 = var0[var2].a();
         Vector var4 = new Vector();

         for(int var5 = 0; var5 < var3.length; ++var5) {
            q var6 = var3[var5].a();
            if (!var4.contains(var6)) {
               var4.addElement(var6);
            }
         }

         var1[var2] = new q[var4.size()];
         var4.copyInto(var1[var2]);
      }

      return var1;
   }

   private static boolean a(e var0, m[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         m var4 = var1[var2];
         m[] var3 = var0.a;
         int var5 = 0;

         boolean var10000;
         while(true) {
            if (var5 >= var3.length) {
               var10000 = false;
               break;
            }

            m var6;
            if ((var6 = var3[var5]).a / 50 == var4.a / 50 && var6.b / 50 == var4.b / 50 && var6.c / 50 == var4.c / 50) {
               var10000 = true;
               break;
            }

            ++var5;
         }

         if (var10000) {
            return true;
         }
      }

      return false;
   }
}
