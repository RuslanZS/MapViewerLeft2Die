import javax.microedition.lcdui.Graphics;

public final class o {
   private static boolean a = true;
   private q a;
   private m[] a;
   private m[] b;
   private int a;
   private b a;
   private int b;
   private int c;
   private int d;
   private int e;
   private boolean[] a = new boolean[8];
   private boolean b;
   private int f;
   private int g;

   public o(m[] var1) {
      this.a = var1;
      m var10001 = this.a[0];
      m var10002 = this.a[1];
      m var4 = this.a[2];
      m var3 = var10002;
      m var2 = var10001;
      this.a = h.a(var10001.a, var2.b, var2.c, var3.a, var3.b, var3.c, var4.a, var4.b, var4.c);
      if (var1.length != 4) {
         System.out.println("PORTAL: предупреждение: нестандартное количество вершин в портале " + var1.length);
      }

      this.b = new m[8];

      for(int var5 = 0; var5 < this.b.length; ++var5) {
         this.b[var5] = new m();
      }

   }

   public final m[] a() {
      return this.a;
   }

   public final q a() {
      return this.a;
   }

   public final void a(q var1) {
      this.a = var1;
   }

   public final int a() {
      return this.b;
   }

   public final int b() {
      return this.d;
   }

   public final int c() {
      return this.c;
   }

   public final int d() {
      return this.e;
   }

   public final boolean a(d var1, int var2, int var3, int var4, int var5) {
      f var6 = var1.b();
      int var9 = var6.l;
      int var8 = var6.h;
      int var7 = var6.d;
      m var10 = this.a[0];
      int var11 = var7 - var10.a;
      int var12 = var8 - var10.b;
      int var13 = var9 - var10.c;
      if (Math.abs(var11 * this.a.a + var12 * this.a.b + var13 * this.a.c >> 12) < 300) {
         this.b = var2;
         this.d = var3;
         this.c = var4;
         this.e = var5;
         this.a = 0;
         return true;
      } else {
         m[] var38 = this.b;
         m[] var36 = this.a;
         int var35 = var5;
         var9 = var4;
         var8 = var3;
         var7 = var2;
         d var31 = var1;
         f var39 = var1.a();

         int var14;
         m var15;
         int var16;
         int var17;
         int var18;
         int var19;
         int var20;
         int var21;
         int var23;
         int var24;
         int var25;
         int var26;
         int var27;
         for(var14 = 0; var14 < var36.length; ++var14) {
            var15 = var36[var14];
            var17 = var39.a >> 2;
            var18 = var39.b >> 2;
            var19 = var39.c >> 2;
            var20 = var39.d;
            var21 = var39.e >> 2;
            int var22 = var39.f >> 2;
            var23 = var39.g >> 2;
            var24 = var39.h;
            var25 = var39.i >> 2;
            var26 = var39.j >> 2;
            var27 = var39.k >> 2;
            var16 = var39.l;
            var15.d = (var15.a * var17 >> 12) + (var15.b * var18 >> 12) + (var15.c * var19 >> 12) + var20;
            var15.e = (var15.a * var21 >> 12) + (var15.b * var22 >> 12) + (var15.c * var23 >> 12) + var24;
            var15.f = (var15.a * var25 >> 12) + (var15.b * var26 >> 12) + (var15.c * var27 >> 12) + var16;
         }

         var14 = 0;

         m var44;
         m var47;
         for(var13 = 0; var13 < var36.length; ++var13) {
            if ((var16 = var13 + 1) > var36.length - 1) {
               var16 = 0;
            }

            var44 = var36[var13];
            var47 = var36[var16];
            if (var44.f <= 0 || var47.f <= 0) {
               if (var44.f < 0 && var47.f < 0) {
                  a(var38[var14], var44);
                  ++var14;
               }

               if (var44.f < 0 && var47.f > 0) {
                  a(var38[var14], var44);
                  ++var14;
                  a(var44, var47, var38[var14]);
                  ++var14;
               }

               if (var44.f > 0 && var47.f < 0) {
                  a(var44, var47, var38[var14]);
                  ++var14;
               }
            }
         }

         for(var13 = 0; var13 < var14; ++var13) {
            (var15 = var38[var13]).a(var31);
            if (var15.f >= 0) {
               if (var15.d > var7 && var15.d < var9) {
                  var15.d = var15.d > (var9 + var7) / 2 ? var9 : var7;
               }

               if (var15.e > var8 && var15.e < var35) {
                  var15.e = var15.e > (var35 + var8) / 2 ? var35 : var8;
               }
            }
         }

         m var45;
         for(var13 = 0; var13 < var14; ++var13) {
            var15 = a(var38, var14, var13);
            var45 = a(var38, var14, var13 + 1);
            if (var15.d < var7 && var45.d < var7) {
               var44 = a(var38, var14, var13 - 1);
               var47 = a(var38, var14, var13 + 2);
               a(var15, var44, var7);
               a(var45, var47, var7);
            }

            if (var15.d > var9 && var45.d > var9) {
               var44 = a(var38, var14, var13 - 1);
               var47 = a(var38, var14, var13 + 2);
               a(var15, var44, var9 - 1);
               a(var45, var47, var9 - 1);
            }
         }

         this.a = var14;
         if (this.a < 3) {
            return false;
         } else {
            o var32 = this;
            m var37 = this.b[0];
            this.c = var37.d;
            this.b = var37.d;
            this.e = var37.e;
            this.d = var37.e;

            for(var12 = 1; var12 < var32.a; ++var12) {
               if ((var37 = var32.b[var12]).d < var32.b) {
                  var32.b = var37.d;
               }

               if (var37.d > var32.c) {
                  var32.c = var37.d;
               }

               if (var37.e < var32.d) {
                  var32.d = var37.e;
               }

               if (var37.e > var32.e) {
                  var32.e = var37.e;
               }
            }

            if (var32.c >= var2 && var32.e >= var3 && var32.b <= var4 && var32.d <= var5) {
               boolean var55;
               label466: {
                  var32 = this;
                  if (a) {
                     for(var8 = 0; var8 < var32.a.length; ++var8) {
                        var32.a[var8] = false;
                     }

                     var32.b = false;
                     var32.f = var32.g = 0;

                     for(var8 = 0; var8 < var32.a; ++var8) {
                        m var33 = var32.b[var8];
                        var32.f += var33.d;
                        var32.g += var33.e;
                     }

                     var32.f /= var32.a;
                     var32.g /= var32.a;
                     l[] var34 = var1.a();
                     var9 = var1.a();
                     var35 = var32.a[0].f;

                     for(var11 = 1; var11 < var32.a.length; ++var11) {
                        if ((var12 = var32.a[var11].f) < var35) {
                           var35 = var12;
                        }
                     }

                     for(var11 = 0; var11 < var9; ++var11) {
                        int var10000;
                        p var40;
                        c var41;
                        a var42;
                        p var43;
                        if ((var43 = var40 = var34[var11].a) instanceof c) {
                           var16 = (var41 = (c)var43).a.f;
                           if (var41.b.f > var16) {
                              var16 = var41.b.f;
                           }

                           if (var41.c.f > var16) {
                              var16 = var41.c.f;
                           }

                           var10000 = var16;
                        } else if (var43 instanceof a) {
                           var16 = (var42 = (a)var43).a.f;
                           if (var42.b.f > var16) {
                              var16 = var42.b.f;
                           }

                           if (var42.c.f > var16) {
                              var16 = var42.c.f;
                           }

                           if (var42.d.f > var16) {
                              var16 = var42.d.f;
                           }

                           var10000 = var16;
                        } else {
                           var10000 = 0;
                        }

                        var13 = var10000;
                        if (var10000 > var35) {
                           if (var13 - var35 <= 10000) {
                              label439: {
                                 if (var40 instanceof c) {
                                    var41 = (c)var40;
                                    if (var32.a(var41.a)) {
                                       var55 = true;
                                       break label439;
                                    }

                                    if (var32.a(var41.b)) {
                                       var55 = true;
                                       break label439;
                                    }

                                    if (var32.a(var41.c)) {
                                       var55 = true;
                                       break label439;
                                    }
                                 } else if (var40 instanceof a) {
                                    var42 = (a)var40;
                                    if (var32.a(var42.a)) {
                                       var55 = true;
                                       break label439;
                                    }

                                    if (var32.a(var42.b)) {
                                       var55 = true;
                                       break label439;
                                    }

                                    if (var32.a(var42.c)) {
                                       var55 = true;
                                       break label439;
                                    }

                                    if (var32.a(var42.d)) {
                                       var55 = true;
                                       break label439;
                                    }
                                 }

                                 var55 = false;
                              }

                              if (var55) {
                                 continue;
                              }
                           }

                           int var30;
                           int var46;
                           m var48;
                           m var49;
                           if (var40 instanceof a) {
                              var45 = (var42 = (a)var40).a;
                              var44 = var42.b;
                              var47 = var42.c;
                              var15 = var42.d;
                              var49 = var15;
                              var48 = var47;
                              var47 = var44;
                              var44 = var45;
                              if (a(var45.d, var45.e, var47.d, var47.e, var48.d, var48.e)) {
                                 var49 = var45;
                                 var44 = var15;
                                 m var51 = var48;
                                 var48 = var47;
                                 var47 = var51;
                              }

                              o var53 = var32;
                              var27 = var44.d;
                              var30 = var44.e;
                              var2 = var47.d;
                              var3 = var47.e;
                              var4 = var48.d;
                              var5 = var48.e;
                              var7 = var49.d;
                              var12 = var49.e;
                              var13 = var27;
                              if (var2 < var27) {
                                 var13 = var2;
                              }

                              if (var4 < var13) {
                                 var13 = var4;
                              }

                              if (var7 < var13) {
                                 var13 = var7;
                              }

                              var14 = var30;
                              if (var3 < var30) {
                                 var14 = var3;
                              }

                              if (var5 < var14) {
                                 var14 = var5;
                              }

                              if (var12 < var14) {
                                 var14 = var12;
                              }

                              var46 = var27;
                              if (var2 > var27) {
                                 var46 = var2;
                              }

                              if (var4 > var46) {
                                 var46 = var4;
                              }

                              if (var7 > var46) {
                                 var46 = var7;
                              }

                              var16 = var30;
                              if (var3 > var30) {
                                 var16 = var3;
                              }

                              if (var5 > var16) {
                                 var16 = var5;
                              }

                              if (var12 > var16) {
                                 var16 = var12;
                              }

                              var17 = var2 - var27;
                              var18 = var4 - var2;
                              var19 = var7 - var4;
                              var20 = var27 - var7;
                              var23 = var3 - var30;
                              var24 = var5 - var3;
                              var21 = var12 - var5;
                              var25 = var30 - var12;

                              int var54;
                              for(var26 = 0; var26 < var53.a; ++var26) {
                                 if (!var53.a[var26]) {
                                    m var28;
                                    int var29 = (var28 = var53.b[var26]).d;
                                    var54 = var28.e;
                                    if (var29 >= var13 && var54 >= var14 && var29 <= var46 && var54 <= var16) {
                                       var53.a[var26] = var17 * (var54 - var30) <= (var29 - var27) * var23 && var18 * (var54 - var3) <= (var29 - var2) * var24 && var19 * (var54 - var5) <= (var29 - var4) * var21 && var20 * (var54 - var12) <= (var29 - var7) * var25;
                                    }
                                 }
                              }

                              if (!var53.b) {
                                 var26 = var53.f;
                                 var54 = var53.g;
                                 if (var26 >= var13 && var54 >= var14 && var26 <= var46 && var54 <= var16) {
                                    var53.b = var17 * (var54 - var30) <= (var26 - var27) * var23 && var18 * (var54 - var3) <= (var26 - var2) * var24 && var19 * (var54 - var5) <= (var26 - var4) * var21 && var20 * (var54 - var12) <= (var26 - var7) * var25;
                                 }
                              }
                           } else if (var40 instanceof c) {
                              var45 = (var41 = (c)var40).a;
                              var44 = var41.b;
                              var47 = var41.c;
                              var48 = var47;
                              var47 = var44;
                              var44 = var45;
                              if (a(var45.d, var45.e, var47.d, var47.e, var48.d, var48.e)) {
                                 var49 = var48;
                                 var48 = var45;
                                 var44 = var49;
                              }

                              o var52 = var32;
                              var25 = var44.d;
                              var26 = var44.e;
                              var27 = var47.d;
                              var30 = var47.e;
                              var2 = var48.d;
                              var3 = var48.e;
                              var4 = var25;
                              if (var27 < var25) {
                                 var4 = var27;
                              }

                              if (var2 < var4) {
                                 var4 = var2;
                              }

                              var5 = var26;
                              if (var30 < var26) {
                                 var5 = var30;
                              }

                              if (var3 < var5) {
                                 var5 = var3;
                              }

                              var7 = var25;
                              if (var27 > var25) {
                                 var7 = var27;
                              }

                              if (var2 > var7) {
                                 var7 = var2;
                              }

                              var12 = var26;
                              if (var30 > var26) {
                                 var12 = var30;
                              }

                              if (var3 > var12) {
                                 var12 = var3;
                              }

                              var13 = var27 - var25;
                              var14 = var2 - var27;
                              var46 = var25 - var2;
                              var16 = var30 - var26;
                              var17 = var3 - var30;
                              var18 = var26 - var3;

                              for(var19 = 0; var19 < var52.a; ++var19) {
                                 if (!var52.a[var19]) {
                                    var23 = (var49 = var52.b[var19]).d;
                                    var24 = var49.e;
                                    if (var23 >= var4 && var24 >= var5 && var23 <= var7 && var24 <= var12) {
                                       var52.a[var19] = var13 * (var24 - var26) <= (var23 - var25) * var16 && var14 * (var24 - var30) <= (var23 - var27) * var17 && var46 * (var24 - var3) <= (var23 - var2) * var18;
                                    }
                                 }
                              }

                              if (!var52.b) {
                                 var19 = var52.f;
                                 var20 = var52.g;
                                 if (var19 >= var4 && var20 >= var5 && var19 <= var7 && var20 <= var12) {
                                    var52.b = var13 * (var20 - var26) <= (var19 - var25) * var16 && var14 * (var20 - var30) <= (var19 - var27) * var17 && var46 * (var20 - var3) <= (var19 - var2) * var18;
                                 }
                              }
                           }

                           o var50 = var32;
                           if (!var32.b) {
                              var55 = false;
                           } else {
                              var13 = 0;

                              while(true) {
                                 if (var13 >= var50.a) {
                                    var55 = true;
                                    break;
                                 }

                                 if (!var50.a[var13]) {
                                    var55 = false;
                                    break;
                                 }

                                 ++var13;
                              }
                           }

                           if (var55) {
                              var55 = true;
                              break label466;
                           }
                        }
                     }
                  }

                  var55 = false;
               }

               if (!var55) {
                  return true;
               }
            }

            return false;
         }
      }
   }

   private final boolean a(m var1) {
      for(int var2 = 0; var2 < this.a.length; ++var2) {
         m var3;
         int var4 = (var3 = this.a[var2]).a - var1.a;
         int var5 = var3.b - var1.b;
         int var6 = var3.c - var1.c;
         if (var4 < 0) {
            var4 = -var4;
         }

         if (var5 < 0) {
            var5 = -var5;
         }

         if (var6 < 0) {
            var6 = -var6;
         }

         if (var4 < 400 && var5 < 400 && var6 < 400) {
            return true;
         }
      }

      return false;
   }

   private static final boolean a(int var0, int var1, int var2, int var3, int var4, int var5) {
      return (var0 - var2) * (var3 - var5) > (var1 - var3) * (var2 - var4);
   }

   public final void a(Graphics var1, int var2, int var3) {
      if (this.a != null) {
         var1.setColor(16711680);
      } else {
         var1.setColor(0);
      }

      int var4;
      m var5;
      for(var4 = 0; var4 < this.a; ++var4) {
         var5 = this.b[var4];
         m var6 = this.b[(var4 + 1) % this.a];
         var1.drawLine(var5.d + var2, var5.e + var3, var6.d + var2, var6.e + var3);
      }

      var1.setColor(0);

      for(var4 = 0; var4 < this.a; ++var4) {
         if (this.a[var4]) {
            var5 = this.b[var4];
            var1.fillRect(var5.d + var2, var5.e + var3, 3, 3);
         }
      }

      if (this.b && this.a != 0) {
         var1.fillRect(this.f + var2, this.g + var3, 3, 3);
      }

   }

   private static final m a(m[] var0, int var1, int var2) {
      if (var2 < 0) {
         var2 += var1;
      }

      var2 %= var1;
      return var0[var2];
   }

   private static final void a(m var0, m var1, int var2) {
      int var10001;
      if (var0.d != var1.d && var0.e != var1.e) {
         int var3 = (var1.e - var0.e << 12) / (var1.d - var0.d);
         var10001 = var0.e + (var3 * (var2 - var0.d) >> 12);
      } else {
         var10001 = var0.e;
      }

      var0.e = var10001;
      var0.d = var2;
   }

   private static final void a(m var0, m var1, m var2) {
      int var3;
      if ((var3 = var1.f - var0.f) == 0) {
         var3 = 1;
      }

      int var4 = (var1.d - var0.d << 12) / var3;
      int var6 = (var1.e - var0.e << 12) / var3;
      var3 = var0.d - (var4 * var0.f >> 12);
      int var5 = var0.e - (var6 * var0.f >> 12);
      var2.a = var2.d = var3;
      var2.b = var2.e = var5;
      var2.c = var2.f = 0;
   }

   private static final void a(m var0, m var1) {
      var0.a = var0.d = var1.d;
      var0.b = var0.e = var1.e;
      var0.c = var0.f = var1.f;
   }
}
