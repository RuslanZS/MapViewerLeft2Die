public final class c extends p {
   public final m a;
   public final m b;
   public final m c;
   private byte a;
   private byte b;
   private byte c;
   private byte d;
   private byte e;
   private byte f;

   public c(m var1, m var2, m var3, byte var4, byte var5, byte var6, byte var7, byte var8, byte var9) {
      super(var1, var2, var3);
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.a = var4;
      this.b = var5;
      this.c = var6;
      this.d = var7;
      this.e = var8;
      this.f = var9;
   }

   private static int a(int var0, int var1, int var2) {
      int var3;
      if (var1 < var0) {
         var3 = var0;
         var0 = var1;
         var1 = var3;
      }

      if (var2 < var0) {
         var3 = var2;
         var2 = var0;
         var0 = var3;
      }

      if (var2 < var1) {
         var2 = var1;
      }

      return var2 - var0;
   }

   public final void a(d var1, g var2) {
      if (var2.a && (a(this.a.d, this.b.d, this.c.d) > 45 || a(this.a.e, this.b.e, this.c.e) > 45) && a(this.a.f, this.b.f, this.c.f) > 200) {
         g.b(var1, var2, this.a, this.a & 255, this.b & 255, this.b, this.c & 255, this.d & 255, this.c, this.e & 255, this.f & 255);
      } else {
         g.a(var1, var2, this.a, this.a & 255, this.b & 255, this.b, this.c & 255, this.d & 255, this.c, this.e & 255, this.f & 255);
      }
   }

   public final boolean a(int var1, int var2, int var3, int var4) {
      if ((this.a.d - this.b.d) * (this.b.e - this.c.e) <= (this.a.e - this.b.e) * (this.b.d - this.c.d)) {
         return false;
      } else {
         this.a = (this.a.f + this.b.f + this.c.f) / 3;
         if (this.a > 0) {
            return false;
         } else if (this.a.d <= var1 && this.b.d <= var1 && this.c.d <= var1) {
            return false;
         } else if (this.a.e <= var2 && this.b.e <= var2 && this.c.e <= var2) {
            return false;
         } else if (this.a.d >= var3 && this.b.d >= var3 && this.c.d >= var3) {
            return false;
         } else {
            return this.a.e < var4 || this.b.e < var4 || this.c.e < var4;
         }
      }
   }
}
