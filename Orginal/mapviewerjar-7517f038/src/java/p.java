public abstract class p {
   public int a;
   public final short a;

   p() {
      this.a = 0;
   }

   p(m var1, m var2, m var3) {
      long var4 = (long)(var1.b - var2.b) * (long)(var1.c - var3.c) - (long)(var1.c - var2.c) * (long)(var1.b - var3.b);
      long var6 = (long)(var1.c - var2.c) * (long)(var1.a - var3.a) - (long)(var1.a - var2.a) * (long)(var1.c - var3.c);
      long var8 = (long)(var1.a - var2.a) * (long)(var1.b - var3.b) - (long)(var1.b - var2.b) * (long)(var1.a - var3.a);
      double var10 = Math.sqrt((double)(var4 * var4 + var6 * var6 + var8 * var8)) / 4096.0D;
      this.a = (short)((int)((double)var6 / var10));
   }

   public abstract void a(d var1, g var2);

   public abstract boolean a(int var1, int var2, int var3, int var4);
}
