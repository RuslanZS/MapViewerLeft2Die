import javax.microedition.lcdui.Graphics;

public final class d {
   private static final int[] b = new int[]{1, 4, 10, 23, 57, 145, 356, 911, 1968, 4711, 11969, 27901, 84801};
   int[] a;
   int a;
   int b;
   private f a = new f();
   private f b = new f();
   int c;
   int d;
   int e;
   int f;
   private l[] a;
   private int g;

   public d(int var1, int var2) {
      new f();
      this.c = 150;
      this.d = 150;
      this.a = new l[700];
      this.g = 0;
      this.a = var1;
      this.b = var2;
      this.a = new int[var1 * var2];

      for(int var3 = 0; var3 < this.a.length; ++var3) {
         this.a[var3] = new l();
      }

      this.e = var1 / 2;
      this.f = var2 / 2;
      boolean var4 = true;
      this.c = this.e * 6144 >> 12;
      this.d = this.f * 6144 >> 12;
   }

   public final l[] a() {
      return this.a;
   }

   public final int a() {
      return this.g;
   }

   public final int b() {
      return this.a;
   }

   public final int c() {
      return this.b;
   }

   public final void a(f var1) {
      this.b.a(var1);
      this.a.a(var1);
      this.b.a();
   }

   public final f a() {
      return this.b;
   }

   public final f b() {
      return this.a;
   }

   public final void a(e var1, int var2, int var3, int var4, int var5) {
      g var6 = var1.a();
      p[] var13;
      int var7 = (var13 = var1.a()).length;
      l[] var8;
      int var9 = (var8 = this.a).length;

      for(int var11 = 0; var11 < var7; ++var11) {
         if (this.g >= var9) {
            return;
         }

         p var10;
         if ((var10 = var13[var11]).a(var2, var3, var4, var5)) {
            l var12;
            (var12 = var8[this.g]).a = var10;
            var12.a = var6;
            ++this.g;
         }
      }

   }

   public final void a() {
      l[] var1;
      l[] var10000 = var1 = this.a;
      int var3 = this.g;
      l[] var2 = var10000;

      int var8;
      for(var8 = 0; b[var8] < var3; ++var8) {
      }

      while(true) {
         --var8;
         if (var8 < 0) {
            int var10 = this.g - 1;

            for(this.g = 0; var10 >= 0; --var10) {
               l var11;
               (var11 = var1[var10]).a.a(this, var11.a);
            }

            return;
         }

         int var7;
         for(int var5 = var7 = b[var8]; var5 < var3; ++var5) {
            int var6 = var5;

            l var4;
            for(int var9 = (var4 = var2[var5]).a.a; var6 >= var7 && var2[var6 - var7].a.a < var9; var6 -= var7) {
               var2[var6] = var2[var6 - var7];
            }

            var2[var6] = var4;
         }
      }
   }

   public final void a(Graphics var1, int var2, int var3) {
      var1.drawRGB(this.a, 0, this.a, 0, 0, this.a, this.b, false);
   }

   public final void a(m[] var1, f var2) {
      int var3 = this.e;
      int var4 = this.f;
      int var5 = this.c;
      int var6 = this.d;
      int var7 = var2.a >> 2;
      int var8 = var2.b >> 2;
      int var9 = var2.c >> 2;
      int var10 = var2.d;
      int var11 = var2.e >> 2;
      int var12 = var2.f >> 2;
      int var13 = var2.g >> 2;
      int var14 = var2.h;
      int var15 = var2.i >> 2;
      int var16 = var2.j >> 2;
      int var17 = var2.k >> 2;
      int var25 = var2.l;

      for(int var24 = var1.length - 1; var24 >= 0; --var24) {
         m var18;
         int var19 = (var18 = var1[var24]).a;
         int var20 = var18.b;
         int var21 = var18.c;
         int var22 = (var19 * var7 >> 12) + (var20 * var8 >> 12) + (var21 * var9 >> 12) + var10;
         int var23 = (var19 * var11 >> 12) + (var20 * var12 >> 12) + (var21 * var13 >> 12) + var14;
         if ((var19 = (var19 * var15 >> 12) + (var20 * var16 >> 12) + (var21 * var17 >> 12) + var25) <= 0) {
            var22 = var22 * var5 / (-var19 + var5) + var3;
            var23 = -var23 * var6 / (-var19 + var6) + var4;
         } else {
            var22 += var3;
            var23 = -var23 + var4;
         }

         var18.d = var22;
         var18.e = var23;
         var18.f = var19;
      }

   }

   public final void a(int var1) {
      int var2;
      int[] var3;
      for(var2 = (var3 = this.a).length - 1; var2 >= 16; var3[var2--] = 0) {
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
         var3[var2--] = 0;
      }

      while(var2 >= 0) {
         var3[var2--] = 0;
      }

   }
}
