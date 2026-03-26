/*    */ package wtf.tatp.meowtils.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class GuiScale
/*    */ {
/*    */   public static int getEffectiveGuiScale() {
/*  9 */     Minecraft mc = Minecraft.func_71410_x();
/* 10 */     int scaleSetting = mc.field_71474_y.field_74335_Z;
/* 11 */     int scaled = (scaleSetting == 0) ? 1000 : scaleSetting;
/* 12 */     int scale = 0;
/*    */     
/* 14 */     while (scale < scaled && mc.field_71443_c / (scale + 1) >= 320 && mc.field_71440_d / (scale + 1) >= 240) {
/* 15 */       scale++;
/*    */     }
/* 17 */     return scale;
/*    */   }
/*    */   public static float customGuiScale() {
/* 20 */     String guiScale = cfg.v.guiScale;
/* 21 */     switch (guiScale) {
/*    */       case "Tiny":
/* 23 */         return 0.8F;
/*    */       case "Small":
/* 25 */         return 0.9F;
/*    */       case "Normal":
/* 27 */         return 1.0F;
/*    */       case "Large":
/* 29 */         return 1.1F;
/*    */       case "Huge":
/* 31 */         return 1.2F;
/*    */     } 
/* 33 */     return 1.0F;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/GuiScale.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */