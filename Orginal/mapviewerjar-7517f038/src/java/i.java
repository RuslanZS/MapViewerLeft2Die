import javax.microedition.lcdui.Canvas;

public final class i {
   private int c;
   private int d;
   private int e;
   private int f;
   private int g;
   public final int a;
   public final int b;
   private boolean a;
   private boolean b;
   private boolean c;
   private boolean d;
   private boolean e;
   private boolean f;
   private boolean g;
   private boolean h;

   public i(Canvas var1) {
      if (a(var1, -26)) {
         this.c = -61;
         this.d = -62;
         this.f = -60;
         this.e = -59;
         this.g = -26;
         this.a = -1;
         this.b = -4;
      } else if (a(var1, -20)) {
         this.c = -2;
         this.d = -5;
         this.f = -6;
         this.e = -1;
         this.g = -20;
         this.a = -21;
         this.b = -22;
      } else {
         this.c = -3;
         this.d = -4;
         this.f = -2;
         this.e = -1;
         this.g = -5;
         this.a = -6;
         this.b = -7;
      }
   }

   private static boolean a(Canvas var0, int var1) {
      try {
         return var0.getKeyName(var1).toUpperCase().indexOf("SELECT") != -1;
      } catch (Exception var2) {
         return false;
      }
   }

   public final void a(int var1) {
      if (var1 == 50 || var1 == this.e) {
         this.a = true;
      }

      if (var1 == 56 || var1 == this.f) {
         this.b = true;
      }

      if (var1 == 52 || var1 == this.c) {
         this.c = true;
      }

      if (var1 == 54 || var1 == this.d) {
         this.d = true;
      }

      if (var1 == 49) {
         this.g = true;
      }

      if (var1 == 51) {
         this.h = true;
      }

      if (var1 == 55) {
         this.e = true;
      }

      if (var1 == 57) {
         this.f = true;
      }

   }

   public final void b(int var1) {
      if (var1 == 50 || var1 == this.e) {
         this.a = false;
      }

      if (var1 == 56 || var1 == this.f) {
         this.b = false;
      }

      if (var1 == 52 || var1 == this.c) {
         this.c = false;
      }

      if (var1 == 54 || var1 == this.d) {
         this.d = false;
      }

      if (var1 == 49) {
         this.g = false;
      }

      if (var1 == 51) {
         this.h = false;
      }

      if (var1 == 55) {
         this.e = false;
      }

      if (var1 == 57) {
         this.f = false;
      }

   }

   public final boolean a() {
      return this.a;
   }

   public final boolean b() {
      return this.b;
   }

   public final boolean c() {
      return this.c;
   }

   public final boolean d() {
      return this.d;
   }

   public final boolean e() {
      return this.g;
   }

   public final boolean f() {
      return this.h;
   }

   public final boolean g() {
      return this.e;
   }

   public final boolean h() {
      return this.f;
   }
}
