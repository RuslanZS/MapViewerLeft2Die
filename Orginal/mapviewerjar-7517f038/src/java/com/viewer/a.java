package com.viewer;

public final class a {
   private .f a = new .f();
   private .f b = new .f();
   private int a = -1;
   private int b = -1;

   public final void a() {
      this.a(-180);
   }

   public final void b() {
      this.a(180);
   }

   public final void c() {
      this.b.b(6);
   }

   public final void d() {
      this.b.b(-6);
   }

   public final void e() {
      .f var10000 = this.b;
      var10000.h += 180;
   }

   public final void f() {
      .f var10000 = this.b;
      var10000.h -= 180;
   }

   public final void g() {
      this.a.a(6);
      this.b.b(this.a);
   }

   public final void h() {
      this.a.a(-6);
      this.b.b(this.a);
   }

   private void a(int var1) {
      .f var10000 = this.b;
      var10000.d += this.b.c * var1 >> 14;
      var10000 = this.b;
      var10000.l += this.b.k * var1 >> 14;
   }

   public final void a(.j var1) {
      this.b = var1.a(this.a, this.b.d, this.b.l);
      this.a = this.b;
   }

   public final int a() {
      return this.b;
   }

   public final .f a() {
      return this.b;
   }
}
