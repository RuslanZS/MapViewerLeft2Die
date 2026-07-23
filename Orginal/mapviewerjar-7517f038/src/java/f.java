public final class f {
   private static final short[] a = new short[360];
   private static final short[] b = new short[360];
   public int a = 16384;
   public int b = 0;
   public int c = 0;
   public int d = 0;
   public int e = 0;
   public int f = 16384;
   public int g = 0;
   public int h = 0;
   public int i = 0;
   public int j = 0;
   public int k = 16384;
   public int l = 0;

   public final void a(f var1) {
      this.a = var1.a;
      this.b = var1.b;
      this.c = var1.c;
      this.d = var1.d;
      this.e = var1.e;
      this.f = var1.f;
      this.g = var1.g;
      this.h = var1.h;
      this.i = var1.i;
      this.j = var1.j;
      this.k = var1.k;
      this.l = var1.l;
   }

   public final void a() {
      long var1 = ((long)this.a * (long)this.d >> 14) + ((long)this.e * (long)this.h >> 14) + ((long)this.i * (long)this.l >> 14);
      long var3 = ((long)this.b * (long)this.d >> 14) + ((long)this.f * (long)this.h >> 14) + ((long)this.j * (long)this.l >> 14);
      long var5 = ((long)this.c * (long)this.d >> 14) + ((long)this.g * (long)this.h >> 14) + ((long)this.k * (long)this.l >> 14);
      this.d = (int)(-var1);
      this.h = (int)(-var3);
      this.l = (int)(-var5);
      int var7 = this.b;
      this.b = this.e;
      this.e = var7;
      var7 = this.c;
      this.c = this.i;
      this.i = var7;
      var7 = this.g;
      this.g = this.j;
      this.j = var7;
   }

   private static int a(int var0) {
      while(var0 < 0) {
         var0 += 360;
      }

      while(var0 >= 360) {
         var0 -= 360;
      }

      return var0;
   }

   public final void a(int var1) {
      var1 = a(var1);
      this.a = 16384;
      this.b = 0;
      this.c = 0;
      this.d = 0;
      this.e = 0;
      this.f = b[var1];
      this.g = -a[var1];
      this.h = 0;
      this.i = 0;
      this.j = a[var1];
      this.k = b[var1];
      this.l = 0;
   }

   public final void b(f var1) {
      long var5 = (long)(this.a * var1.a + this.b * var1.e + this.c * var1.i) >> 14;
      long var7 = (long)(this.a * var1.b + this.b * var1.f + this.c * var1.j) >> 14;
      long var9 = (long)(this.a * var1.c + this.b * var1.g + this.c * var1.k) >> 14;
      long var11 = (long)this.a * (long)var1.d + (long)this.b * (long)var1.h + (long)this.c * (long)var1.l + ((long)this.d << 14) >> 14;
      long var13 = (long)(this.e * var1.a + this.f * var1.e + this.g * var1.i) >> 14;
      long var15 = (long)(this.e * var1.b + this.f * var1.f + this.g * var1.j) >> 14;
      long var17 = (long)(this.e * var1.c + this.f * var1.g + this.g * var1.k) >> 14;
      long var19 = (long)this.e * (long)var1.d + (long)this.f * (long)var1.h + (long)this.g * (long)var1.l + ((long)this.h << 14) >> 14;
      long var21 = (long)(this.i * var1.a + this.j * var1.e + this.k * var1.i) >> 14;
      long var23 = (long)(this.i * var1.b + this.j * var1.f + this.k * var1.j) >> 14;
      long var25 = (long)(this.i * var1.c + this.j * var1.g + this.k * var1.k) >> 14;
      long var27 = (long)this.i * (long)var1.d + (long)this.j * (long)var1.h + (long)this.k * (long)var1.l + ((long)this.l << 14) >> 14;
      this.a = (int)var5;
      this.b = (int)var7;
      this.c = (int)var9;
      this.d = (int)var11;
      this.e = (int)var13;
      this.f = (int)var15;
      this.g = (int)var17;
      this.h = (int)var19;
      this.i = (int)var21;
      this.j = (int)var23;
      this.k = (int)var25;
      this.l = (int)var27;
   }

   public final void b(int var1) {
      var1 = a(var1);
      short var2 = b[var1];
      short var8 = a[var1];
      int var3 = this.a * var2 + this.i * var8 >> 14;
      int var4 = this.b * var2 + this.j * var8 >> 14;
      int var5 = this.c * var2 + this.k * var8 >> 14;
      int var6 = this.i * var2 - this.a * var8 >> 14;
      int var7 = this.j * var2 - this.b * var8 >> 14;
      var1 = this.k * var2 - this.c * var8 >> 14;
      this.a = var3;
      this.b = var4;
      this.c = var5;
      this.i = var6;
      this.j = var7;
      this.k = var1;
   }

   public final void a(int var1, int var2, int var3) {
      this.d = 0;
      this.h = 0;
      this.l = 0;
   }

   static {
      for(int var0 = 0; var0 < 360; ++var0) {
         a[var0] = (short)((int)(Math.sin(Math.toRadians((double)var0)) * 16384.0D));
         b[var0] = (short)((int)(Math.cos(Math.toRadians((double)var0)) * 16384.0D));
      }

   }
}
