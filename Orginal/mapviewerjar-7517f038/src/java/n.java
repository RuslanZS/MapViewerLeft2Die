import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Hashtable;

public class n {
   private final Hashtable a;

   public n(String var1) {
      this.a = new Hashtable();
      String[] var5 = k.a(var1, '\n');

      for(int var2 = 0; var2 < var5.length; ++var2) {
         int var3;
         if ((var3 = var5[var2].indexOf(61)) >= 0) {
            String var4 = var5[var2].substring(0, var3).trim();
            String var6 = var5[var2].substring(var3 + 1).trim();
            this.a.put(var4, var6);
         }
      }

   }

   public final String a(String var1) {
      String var2;
      if ((var2 = (String)this.a.get(var1)) == null) {
         System.out.println("Stringer: " + var1 + " not found");
      }

      return var2;
   }

   public final float a(String var1) {
      return Float.parseFloat(this.a(var1));
   }

   public n() {
   }

   public static e a(String var0, float var1, float var2, float var3) {
      e var12 = null;
      InputStream var13 = null;
      DataInputStream var14 = null;

      try {
         var13 = (new Object()).getClass().getResourceAsStream(var0);
         var14 = new DataInputStream(var13);
         var12 = a(var0, var14, 1.0F, 1.0F, 1.0F);
      } catch (Exception var10) {
         System.err.println("ERROR in Loader.Load: " + var10);
      } finally {
         try {
            var14.close();
            var13.close();
         } catch (Exception var9) {
         }

      }

      return var12;
   }

   public static e[] a(String var0, float var1, float var2, float var3) {
      e[] var4 = null;
      InputStream var5 = null;
      DataInputStream var6 = null;

      try {
         var5 = (new Object()).getClass().getResourceAsStream(var0);
         var4 = new e[(var6 = new DataInputStream(var5)).readInt()];

         for(int var7 = 0; var7 < var4.length; ++var7) {
            var4[var7] = a(var0, var6, var1, var2, var3);
         }
      } catch (Exception var14) {
         System.err.println("ERROR in Loader.Load: " + var14);
      } finally {
         try {
            var6.close();
            var5.close();
         } catch (Exception var13) {
         }

      }

      return var4;
   }

   private static e a(String var0, DataInputStream var1, float var2, float var3, float var4) {
      m[] var5 = new m[var1.readShort()];

      int var7;
      int var8;
      int var9;
      for(int var6 = 0; var6 < var5.length; ++var6) {
         var7 = (int)((float)var1.readShort() * var2);
         var8 = (int)((float)var1.readShort() * var3);
         var9 = (int)((float)var1.readShort() * var4);
         var5[var6] = new m(var7, var8, var9);
      }

      c[] var27 = new c[var1.readShort()];

      byte var10;
      byte var11;
      byte var12;
      byte var13;
      short var18;
      short var30;
      for(var7 = 0; var7 < var27.length; ++var7) {
         short var29 = var1.readShort();
         var30 = var1.readShort();
         var18 = var1.readShort();
         byte var20 = var1.readByte();
         byte var24 = var1.readByte();
         var10 = var1.readByte();
         var11 = var1.readByte();
         var12 = var1.readByte();
         var13 = var1.readByte();
         m var14 = var5[var29];
         m var15 = var5[var30];
         m var16 = var5[var18];
         var27[var7] = new c(var16, var15, var14, var12, var13, var10, var11, var20, var24);
      }

      a[] var28 = new a[var1.readShort()];

      for(var8 = 0; var8 < var28.length; ++var8) {
         var30 = var1.readShort();
         var18 = var1.readShort();
         short var22 = var1.readShort();
         short var25 = var1.readShort();
         var10 = var1.readByte();
         var11 = var1.readByte();
         var12 = var1.readByte();
         var13 = var1.readByte();
         byte var33 = var1.readByte();
         byte var34 = var1.readByte();
         byte var35 = var1.readByte();
         byte var17 = var1.readByte();
         m var32 = var5[var30];
         m var19 = var5[var18];
         m var23 = var5[var22];
         m var26 = var5[var25];
         var28[var8] = new a(var26, var23, var19, var32, var35, var17, var33, var34, var12, var13, var10, var11);
      }

      p[] var31 = new p[var27.length + var28.length];
      var9 = 0;

      int var21;
      for(var21 = 0; var21 < var27.length; ++var21) {
         var31[var9] = var27[var21];
         ++var9;
      }

      for(var21 = 0; var21 < var28.length; ++var21) {
         var31[var9] = var28[var21];
         ++var9;
      }

      System.out.println("Mesh [" + var0 + "] вершин: " + var5.length + " полигонов: " + var31.length);
      return new e(var5, var31);
   }
}
