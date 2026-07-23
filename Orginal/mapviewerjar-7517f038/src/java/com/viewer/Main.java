package com.viewer;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

public class Main extends MIDlet {
   protected void startApp() {
      this.setScreen(new com.viewer.c(this));
   }

   protected void pauseApp() {
   }

   protected void destroyApp(boolean var1) {
   }

   public void setScreen(Canvas var1) {
      Display.getDisplay(this).setCurrent(var1);
   }
}
