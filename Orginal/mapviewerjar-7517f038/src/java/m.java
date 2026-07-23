public final class m {
   public int a;
   public int b;
   public int c;
   public int d;
   public int e;
   public int f;

   public m() {
   }

   public m(int var1, int var2, int var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   public final void a(d var1) {
      if (this.f <= 0) {
         this.d = this.d * var1.c / (-this.f + var1.c) + var1.e;
         this.e = -this.e * var1.d / (-this.f + var1.d) + var1.f;
      } else {
         this.d += var1.e;
         this.e = -this.e + var1.f;
      }
   }
}
