import javax.microedition.lcdui.Image;

public class g {
   boolean a;
   int a;
   int b;
   int c;
   int[] a;

   g() {
   }

   static final void a(d var0, g var1, m var2, int var3, int var4, m var5, int var6, int var7, m var8, int var9, int var10) {
      m var11;
      int var36;
      if (var5.e < var2.e) {
         var11 = var2;
         var2 = var5;
         var5 = var11;
         var36 = var3;
         var3 = var6;
         var6 = var36;
         var36 = var4;
         var4 = var7;
         var7 = var36;
      }

      if (var8.e < var2.e) {
         var11 = var8;
         var8 = var2;
         var2 = var11;
         var36 = var9;
         var9 = var3;
         var3 = var36;
         var36 = var10;
         var10 = var4;
         var4 = var36;
      }

      if (var8.e < var5.e) {
         var11 = var5;
         var5 = var8;
         var8 = var11;
         var36 = var6;
         var6 = var9;
         var9 = var36;
         var36 = var7;
         var7 = var10;
         var10 = var36;
      }

      if (var2.e != var8.e) {
         int[] var37;
         int var12 = (var37 = var1.a).length - 1;
         int var35 = var1.a;
         int[] var13 = var0.a;
         int var18 = var8.e - var2.e;
         int var19 = (var8.d - var2.d << 12) / var18;
         int var20 = (var9 - var3 << 12) / var18;
         int var21 = (var10 - var4 << 12) / var18;
         int var22 = 0;
         int var23 = 0;
         int var24 = 0;
         if (var5.e != var2.e) {
            var18 = var5.e - var2.e;
            var22 = (var5.d - var2.d << 12) / var18;
            var23 = (var6 - var3 << 12) / var18;
            var24 = (var7 - var4 << 12) / var18;
         }

         var18 = var5.e - var2.e;
         int var25 = (var2.d << 12) + var19 * var18;
         int var26 = (var3 << 12) + var20 * var18;
         int var27 = (var4 << 12) + var21 * var18;
         int var28 = var5.d << 12;
         int var29 = var6 << 12;
         int var30 = var7 << 12;
         if ((var18 = var25 - var28 >> 12) != 0) {
            int var31 = (var26 - var29) / var18;
            int var32 = (var27 - var30) / var18;
            var28 = var25 = var2.d << 12;
            var29 = var26 = var3 << 12;
            var30 = var27 = var4 << 12;
            int var33 = var2.e;

            for(int var34 = var8.e < var0.b ? var8.e : var0.b; var33 < var34; var30 += var24) {
               if (var33 == var5.e) {
                  if (var8.e == var5.e) {
                     return;
                  }

                  var18 = var5.e - var2.e;
                  var25 = (var2.d << 12) + var19 * var18;
                  var26 = (var3 << 12) + var20 * var18;
                  var27 = (var4 << 12) + var21 * var18;
                  var28 = var5.d << 12;
                  var29 = var6 << 12;
                  var30 = var7 << 12;
                  var18 = var8.e - var5.e;
                  var22 = (var8.d - var5.d << 12) / var18;
                  var23 = (var9 - var6 << 12) / var18;
                  var24 = (var10 - var7 << 12) / var18;
               }

               if (var33 >= 0) {
                  int var14;
                  int var15;
                  int var16;
                  int var17;
                  if (var25 > var28) {
                     var18 = var28 % 4096;
                     var14 = var28 >> 12;
                     var16 = var29;
                     var17 = var30;
                     var15 = var25 >> 12;
                  } else {
                     var18 = var25 % 4096;
                     var14 = var25 >> 12;
                     var16 = var26;
                     var17 = var27;
                     var15 = var28 >> 12;
                  }

                  var16 -= var31 * var18 >> 12;
                  var17 -= var32 * var18 >> 12;
                  if (var14 < 0) {
                     var16 -= var31 * var14;
                     var17 -= var32 * var14;
                     var14 = 0;
                  }

                  if (var15 > var0.a) {
                     var15 = var0.a;
                  }

                  var18 = var0.a * var33;
                  var14 += var18;

                  for(var15 += var18; var15 - var14 >= 16; var17 += var32) {
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                  }

                  while(var14 < var15) {
                     var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
                     var16 += var31;
                     var17 += var32;
                  }
               }

               ++var33;
               var25 += var19;
               var26 += var20;
               var27 += var21;
               var28 += var22;
               var29 += var23;
            }

         }
      }
   }

   static final void b(d var0, g var1, m var2, int var3, int var4, m var5, int var6, int var7, m var8, int var9, int var10) {
      m var11;
      int var35;
      if (var5.e < var2.e) {
         var11 = var2;
         var2 = var5;
         var5 = var11;
         var35 = var3;
         var3 = var6;
         var6 = var35;
         var35 = var4;
         var4 = var7;
         var7 = var35;
      }

      if (var8.e < var2.e) {
         var11 = var8;
         var8 = var2;
         var2 = var11;
         var35 = var9;
         var9 = var3;
         var3 = var35;
         var35 = var10;
         var10 = var4;
         var4 = var35;
      }

      if (var5.e > var8.e) {
         var11 = var5;
         var5 = var8;
         var8 = var11;
         var35 = var6;
         var6 = var9;
         var9 = var35;
         var35 = var7;
         var7 = var10;
         var10 = var35;
      }

      if (var2.e != var8.e) {
         int var12;
         if (-var2.f > 0) {
            var12 = 262294 / (-var2.f + 150);
         } else {
            var12 = 1748;
         }

         int var13;
         if (-var5.f > 0) {
            var13 = 262294 / (-var5.f + 150);
         } else {
            var13 = 1748;
         }

         int var14;
         if (-var8.f > 0) {
            var14 = 262294 / (-var8.f + 150);
         } else {
            var14 = 1748;
         }

         var3 *= var12;
         var4 *= var12;
         var6 *= var13;
         var7 *= var13;
         var9 *= var14;
         var10 *= var14;
         var35 = var8.e - var2.e;
         int var15 = (var8.d - var2.d << 12) / var35;
         int var16 = (var14 - var12 << 12) / var35;
         int var17 = (var9 - var3 << 12) / var35;
         int var18 = (var10 - var4 << 12) / var35;
         int var19 = 0;
         int var20 = 0;
         int var21 = 0;
         int var22 = 0;
         if (var5.e != var2.e) {
            var35 = var5.e - var2.e;
            var19 = (var5.d - var2.d << 12) / var35;
            var20 = (var13 - var12 << 12) / var35;
            var21 = (var6 - var3 << 12) / var35;
            var22 = (var7 - var4 << 12) / var35;
         }

         var35 = var5.e - var2.e;
         int var23 = (var2.d << 12) + var15 * var35;
         int var24 = (var12 << 12) + var16 * var35;
         int var25 = (var3 << 12) + var17 * var35;
         int var26 = (var4 << 12) + var18 * var35;
         int var27 = var5.d << 12;
         int var28 = var13 << 12;
         int var29 = var6 << 12;
         int var30 = var7 << 12;
         if ((var35 = var23 - var27 >> 12) != 0) {
            int var31 = (var25 - var29) / var35;
            int var32 = (var26 - var30) / var35;
            int var33 = (var24 - var28) / var35;
            var23 = var27 = var2.d << 12;
            var24 = var28 = var12 << 12;
            var25 = var29 = var3 << 12;
            var26 = var30 = var4 << 12;
            int var34;
            if (var5.e > 0) {
               if ((var35 = var2.e) < 0) {
                  var23 -= var15 * var35;
                  var24 -= var16 * var35;
                  var25 -= var17 * var35;
                  var26 -= var18 * var35;
                  var27 -= var19 * var35;
                  var28 -= var20 * var35;
                  var29 -= var21 * var35;
                  var30 -= var22 * var35;
                  var35 = 0;
               }

               var34 = var5.e < var0.b ? var5.e : var0.b;
               a(var0, var1, var35, var34, var23, var24, var25, var26, var27, var28, var29, var30, var15, var16, var17, var18, var19, var20, var21, var22, var33, var31, var32);
            }

            if (var8.e != var5.e && var8.e >= 0) {
               var35 = var5.e - var2.e;
               var23 = (var2.d << 12) + var15 * var35;
               var24 = (var12 << 12) + var16 * var35;
               var25 = (var3 << 12) + var17 * var35;
               var26 = (var4 << 12) + var18 * var35;
               var27 = var5.d << 12;
               var28 = var13 << 12;
               var29 = var6 << 12;
               var30 = var7 << 12;
               var35 = var8.e - var5.e;
               var19 = (var8.d - var5.d << 12) / var35;
               var20 = (var14 - var13 << 12) / var35;
               var21 = (var9 - var6 << 12) / var35;
               var22 = (var10 - var7 << 12) / var35;
               if ((var35 = var5.e) < 0) {
                  var23 -= var15 * var35;
                  var24 -= var16 * var35;
                  var25 -= var17 * var35;
                  var26 -= var18 * var35;
                  var27 -= var19 * var35;
                  var28 -= var20 * var35;
                  var29 -= var21 * var35;
                  var30 -= var22 * var35;
                  var35 = 0;
               }

               var34 = var8.e < var0.b ? var8.e : var0.b;
               a(var0, var1, var35, var34, var23, var24, var25, var26, var27, var28, var29, var30, var15, var16, var17, var18, var19, var20, var21, var22, var33, var31, var32);
            }
         }
      }
   }

   private static final void a(d var0, g var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13, int var14, int var15, int var16, int var17, int var18, int var19, int var20, int var21, int var22) {
      int[] var23;
      int var24 = (var23 = var1.a).length - 1;
      int var41 = var1.a;
      int[] var25 = var0.a;

      for(int var40 = var2; var40 < var3; ++var40) {
         int var26;
         int var27;
         long var28;
         int var30;
         int var31;
         int var32;
         int var33;
         int var34;
         int var35;
         if (var4 > var8) {
            var28 = (long)(var8 % 4096);
            var34 = var8 >> 12;
            var35 = var4 >> 12;
            var26 = var10;
            var27 = var6;
            var30 = var11;
            var31 = var7;
            var32 = var9;
            var33 = var5;
         } else {
            var28 = (long)(var4 % 4096);
            var34 = var4 >> 12;
            var35 = var8 >> 12;
            var26 = var6;
            var27 = var10;
            var30 = var7;
            var31 = var11;
            var32 = var5;
            var33 = var9;
         }

         if (var34 < 0) {
            var26 -= var21 * var34;
            var30 -= var22 * var34;
            var32 -= var20 * var34;
            var34 = 0;
         }

         if (var35 > var0.a) {
            var35 -= var0.a;
            var27 -= var21 * var35;
            var31 -= var22 * var35;
            var33 -= var20 * var35;
            var35 = var0.a;
         }

         var2 = var40 * var0.a;
         var34 += var2;
         var2 = (var35 += var2) - var34;
         var26 = (int)((long)var26 - ((long)var21 * var28 >> 12));
         var30 = (int)((long)var30 - ((long)var22 * var28 >> 12));
         if ((var32 = (int)((long)var32 - ((long)var20 * var28 >> 12))) == 0) {
            return;
         }

         int var42 = (int)(((long)var26 << 12) / (long)var32);

         int var29;
         int var38;
         int var39;
         for(var29 = (int)(((long)var30 << 12) / (long)var32); var2 >= 16; var2 -= 16) {
            int var36 = var26 + (var21 << 4);
            int var37 = var30 + (var22 << 4);
            var32 += var20 << 4;
            var38 = (int)(((long)var36 << 12) / (long)var32);
            var39 = (int)(((long)var37 << 12) / (long)var32);
            var26 = var38 - var42 >> 4;
            var30 = var39 - var29 >> 4;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var42 += var26;
            var29 += var30;
            var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
            var26 = var36;
            var30 = var37;
            var42 = var38;
            var29 = var39;
         }

         if (var2 > 0) {
            var38 = (int)(((long)var27 << 12) / (long)var33);
            var39 = (int)(((long)var31 << 12) / (long)var33);
            var26 = (var38 - var42) / var2;

            for(var30 = (var39 - var29) / var2; var34 < var35; ++var34) {
               var25[var34] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
               var42 += var26;
               var29 += var30;
            }
         }

         var4 += var12;
         var5 += var13;
         var6 += var14;
         var7 += var15;
         var8 += var16;
         var9 += var17;
         var10 += var18;
         var11 += var19;
      }

   }

   private g(Image var1) {
      this.a = false;
      this.b = var1.getWidth();
      this.c = var1.getHeight();
      this.a = a(this.b);
      this.a = new int[this.b * this.c];
      var1.getRGB(this.a, 0, this.b, 0, 0, this.b, this.c);
   }

   private static int a(int var0) {
      for(int var1 = 0; var1 < 32; ++var1) {
         if (var0 >> var1 == 1 && 1 << var1 == var0) {
            return var1;
         }
      }

      return 0;
   }

   public void a(boolean var1) {
      this.a = var1;
   }

   public boolean a() {
      return this.a;
   }

   public static g a(String var0) {
      try {
         return new g(Image.createImage(var0));
      } catch (Exception var2) {
         System.err.println("ERROR in createTexture " + var0 + ": " + var2);
         return null;
      }
   }
}
