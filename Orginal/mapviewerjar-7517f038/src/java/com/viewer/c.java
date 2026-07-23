package com.viewer;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public final class c extends Canvas implements Runnable {
   private Main a;
   private final int a;
   private final int b;
   private boolean a = false;
   private Thread a = null;
   private int c = 0;
   private .i a = new .i(this);
   private .d a;
   private .j a;
   private .g a;
   private boolean b = false;
   private com.viewer.a a = new com.viewer.a();

   public c(Main var1) {
      this.a = var1;
      this.a = this.getWidth();
      this.b = this.getHeight();
      this.a = new .d(this.a, this.b);
      String var3 = "/setting.txt";
      var3 = .k.a("/setting.txt");
      .n var4;
      float var2 = (var4 = new .n(var3)).a("SCALE");
      this.a = .e.a(.n.a(var4.a("MODEL"), var2, var2, var2));
      this.a = .g.a(var4.a("TEXTURE"));
      this.a.a(this.a);
      this.a.a(true);
      if (var4.a("SKY_MODEL") != null && var4.a("SKY_TEXTURE") != null) {
         .k var5 = new .k(var4.a("SKY_MODEL"), var4.a("SKY_TEXTURE"));
         this.a.a(var5);
      }

      if (!this.a) {
         this.a = true;
         this.a = new Thread(this);
         this.a.start();
      } else {
         System.out.println("ScreenViewer: поток уже запущен");
      }
   }

   protected final void paint(Graphics var1) {
      .f var2 = this.a.a();
      this.a.a(this.a.a());
      this.a.a(this.a);
      int var3 = this.a.a();
      if (this.a.a() == null) {
         this.a.a(0);
      }

      this.a.a(this.a, var3);
      this.a.a();
      this.a.a(var1, 0, 0);
      if (this.b) {
         this.a.a(var1, 0, 0);
      }

      int var4 = var1.getFont().getHeight();
      var1.setColor(16711680);
      var1.drawString("x = " + var2.d, 2, 2, 0);
      var1.drawString("y = " + var2.h, 2, var4 + 2, 0);
      var1.drawString("z = " + var2.l, 2, 2 + var4 * 2, 0);
      var1.drawString("fps " + com.viewer.b.a(), this.a - 2, 2, 24);
      var1.drawString("part " + var3, this.a - 2, var4 + 2, 24);
   }

   protected final void keyPressed(int var1) {
      this.a.a(var1);
      this.c = var1;
   }

   protected final void keyReleased(int var1) {
      this.a.b(var1);
      this.c = 0;
   }

   public final void run() {
      while(this.a) {
         try {
            if (this.c == this.a.b) {
               if (this.a) {
                  this.a = false;
               } else {
                  System.out.println("ScreenViewer: поток уже остановлен");
               }

               this.a.notifyDestroyed();
            }

            if (this.c == 35) {
               this.a.a(!this.a.a());
               this.c = 0;
            }

            if (this.c == 42) {
               this.b = !this.b;
               this.c = 0;
            }

            if (this.c == this.a.a) {
               .f var1 = this.a.a();
               System.out.println(var1.d + ", " + var1.h + ", " + var1.l + "; ");
            }

            if (this.a.a()) {
               this.a.a();
            }

            if (this.a.b()) {
               this.a.b();
            }

            if (this.a.h()) {
               this.a.e();
            }

            if (this.a.g()) {
               this.a.f();
            }

            if (this.a.c()) {
               this.a.c();
            }

            if (this.a.d()) {
               this.a.d();
            }

            if (this.a.e()) {
               this.a.h();
            }

            if (this.a.f()) {
               this.a.g();
            }

            this.repaint();
            this.serviceRepaints();
            Thread.sleep(3L);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

   }
}
