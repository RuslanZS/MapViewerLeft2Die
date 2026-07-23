import java.io.InputStream;
import java.util.Vector;

public class k {
   private boolean a;
   private f a;
   private g a;
   private e a;
   private int a;
   private int b;
   private int c;
   private int d;
   private int e;
   private boolean b;

   public k(String var1, String var2) {
      this.a = false;
      this.a = new f();
      this.a = 0;
      this.b = false;
      this.a = g.a(var2);
      this.a.a(false);
      this.a = n.a(var1, 1.0F, 1.0F, 1.0F);
      this.a.a(this.a);
   }

   public final void a() {
      this.b = true;
   }

   public final void a(int var1, int var2, int var3, int var4) {
      if (this.b) {
         this.b = false;
         this.b = var1;
         this.c = var2;
         this.d = var3;
         this.e = var4;
      } else {
         if (var1 < this.b) {
            this.b = var1;
         }

         if (var2 < this.c) {
            this.c = var2;
         }

         if (var3 > this.d) {
            this.d = var3;
         }

         if (var4 > this.e) {
            this.e = var4;
         }

      }
   }

   public final void a(d var1) {
      this.a.a(var1.a());
      this.a.a(0, 0, 0);
      this.a.a(var1, this.a);
      var1.a(this.a, this.b, this.c, this.d, this.e);
   }

   public final boolean a() {
      return !this.b;
   }

   public k() {
   }

   public static String a(String var0) {
      InputStream var1 = null;

      try {
         var1 = (new Object()).getClass().getResourceAsStream(var0);
         StringBuffer var5 = new StringBuffer();

         while(true) {
            int var2;
            do {
               if ((var2 = var1.read()) == -1) {
                  var1.close();
                  return var5.toString();
               }
            } while(var2 == 13);

            var2 = var2 >= 192 && var2 <= 255 ? var2 + 848 : var2;
            var5.append((char)var2);
         }
      } catch (Exception var4) {
         System.out.println("ERROR in getStr: " + var4);
         if (var1 != null) {
            try {
               var1.close();
            } catch (Exception var3) {
            }
         }

         return null;
      }
   }

   public static String[] a(String var0, char var1) {
      byte var6 = 10;
      var0 = var0;
      Vector var2 = new Vector();
      int var5 = 0;

      while(var5 < var0.length()) {
         while(var5 < var0.length() && var0.charAt(var5) == var6) {
            ++var5;
         }

         int var3;
         for(var3 = var5; var5 < var0.length() && var0.charAt(var5) != var6; ++var5) {
         }

         if (var3 < var5) {
            String var8 = var0.substring(var3, var5);
            var2.addElement(var8);
         }
      }

      String[] var7 = new String[var2.size()];
      var2.copyInto(var7);
      return var7;
   }
}
