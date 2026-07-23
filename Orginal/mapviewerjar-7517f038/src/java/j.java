import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public final class j {
   private q[] a;
   private q[][] a;
   private Vector a = new Vector();
   private Vector b = new Vector();
   private k a;
   private Vector c = new Vector();

   public j(q[] var1, q[][] var2) {
      this.a = var1;
      this.a = var2;
   }

   public final void a(g var1) {
      for(int var2 = 0; var2 < this.a.length; ++var2) {
         this.a[var2].a().a(var1);
      }

   }

   public final void a(k var1) {
      this.a = var1;
   }

   public final k a() {
      return this.a;
   }

   public final int a(d var1, int var2) {
      if (this.a != null && this.a.a()) {
         this.a.a(var1);
         var1.a();
         this.a.a();
      }

      if (var2 == -1) {
         if (this.a != null) {
            this.a.a(0, 0, var1.b(), var1.c());
         }

         for(var2 = 0; var2 < this.a.length; ++var2) {
            q var3;
            (var3 = this.a[var2]).a(0, 0, var1.b(), var1.c());
            var3.a(var1);
            var3.a(var1, this.c);
         }

         return this.a.length;
      } else {
         this.a.removeAllElements();
         this.b.removeAllElements();
         this.a(var1, this.a[var2], 0, 0, var1.b(), var1.c());

         for(var2 = 0; var2 < this.a.size(); ++var2) {
            ((q)this.a.elementAt(var2)).a(var1, this.c);
         }

         return this.a.size();
      }
   }

   private void a(d var1, q var2, int var3, int var4, int var5, int var6) {
      if (var2 != null) {
         var2.a(var3, var4, var5, var6);
         var2.a(var1);
         this.a.addElement(var2);
         if (var2.a() && this.a != null) {
            this.a.a(var3, var4, var5, var6);
         }

         o[] var15 = var2.a();

         for(int var7 = 0; var7 < var15.length; ++var7) {
            o var8 = var15[var7];
            if (!this.a.contains(var8.a()) && var8.a(var1, var3, var4, var5, var6)) {
               int var9 = var8.a();
               int var10 = var8.b();
               int var11 = var8.c();
               int var12 = var8.d();
               if (var9 < var3) {
                  var9 = var3;
               }

               if (var10 < var4) {
                  var10 = var4;
               }

               if (var11 > var5) {
                  var11 = var5;
               }

               if (var12 > var6) {
                  var12 = var6;
               }

               int var13 = var11 - var9;
               int var14 = var12 - var10;
               if ((var13 >= 20 || var14 >= 20) && var13 >= 5 && var14 >= 5) {
                  this.b.addElement(var8);
                  this.a(var1, var8.a(), var9, var10, var11, var12);
               }
            }
         }

      }
   }

   public final int a(int var1, int var2, int var3) {
      if (var1 != -1) {
         if (this.a[var1].a(var2, var3) != -1) {
            return var1;
         }

         q[] var5 = this.a[var1];

         for(int var4 = 0; var4 < var5.length; ++var4) {
            if (var5[var4].a(var2, var3) != -1) {
               return var5[var4].a();
            }
         }
      }

      for(var1 = 0; var1 < this.a.length; ++var1) {
         if (this.a[var1].a(var2, var3) != -1) {
            return var1;
         }
      }

      return -1;
   }

   public final void a(Graphics var1, int var2, int var3) {
      for(var2 = 0; var2 < this.b.size(); ++var2) {
         ((o)this.b.elementAt(var2)).a((Graphics)var1, 0, 0);
      }

   }
}
