import java.util.Vector;

public final class q {
   private final int a;
   private e a;
   private final int b;
   private final int c;
   private final int d;
   private final int e;
   private boolean a;
   private o[] a;
   private int f;
   private int g;
   private int h;
   private int i;

   public q(e var1, int var2) {
      this.a = var1;
      this.a = var2;
      this.b = var1.c();
      this.c = var1.a();
      this.d = var1.d();
      this.e = var1.b();
      var2 = (this.c + this.b) / 2;
      int var3 = (this.e + this.d) / 2;
      this.a = true;
      p[] var6 = var1.a();

      for(int var4 = 0; var4 < var6.length; ++var4) {
         p var5 = var6[var4];
         if (a(var2, var3, var5) && var5.a > 2048) {
            this.a = false;
         }
      }

   }

   public final void a(o[] var1) {
      this.a = var1;
   }

   public final o[] a() {
      return this.a;
   }

   public final e a() {
      return this.a;
   }

   public final int a() {
      return this.a;
   }

   public final boolean a() {
      return this.a;
   }

   public final void a(int var1, int var2, int var3, int var4) {
      this.f = var1;
      this.g = var2;
      this.h = var3;
      this.i = var4;
   }

   public final void a(d var1) {
      this.a.a(var1, var1.a());
      var1.a(this.a, this.f, this.g, this.h, this.i);
   }

   public final void a(d var1, Vector var2) {
      for(int var3 = 0; var3 < var2.size(); ++var3) {
         var2.elementAt(var3);
      }

   }

   public final int a(int var1, int var2) {
      if (var1 >= this.b && var2 >= this.d && var1 <= this.c && var2 <= this.e) {
         p[] var3 = this.a.a();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (a(var1, var2, var3[var4])) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private static boolean a(int var0, int var1, p var2) {
      m var3;
      m var4;
      m var8;
      if (var2 instanceof c) {
         c var9 = (c)var2;
         short var10 = var9.a;
         var4 = var9.c;
         var3 = var9.b;
         var8 = var9.a;
         if (var10 > 0) {
            return h.a(var0, var1, var8.a, var8.c, var3.a, var3.c, var4.a, var4.c);
         } else {
            return var10 < 0 ? h.a(var0, var1, var4.a, var4.c, var3.a, var3.c, var8.a, var8.c) : false;
         }
      } else if (var2 instanceof a) {
         a var7 = (a)var2;
         short var6 = var7.a;
         m var5 = var7.d;
         var4 = var7.c;
         var3 = var7.b;
         var8 = var7.a;
         if (var6 > 0) {
            return h.a(var0, var1, var8.a, var8.c, var3.a, var3.c, var4.a, var4.c, var5.a, var5.c);
         } else {
            return var6 < 0 ? h.a(var0, var1, var5.a, var5.c, var4.a, var4.c, var3.a, var3.c, var8.a, var8.c) : false;
         }
      } else {
         return false;
      }
   }
}
