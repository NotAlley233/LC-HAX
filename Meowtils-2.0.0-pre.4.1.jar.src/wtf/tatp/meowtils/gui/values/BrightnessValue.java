/*    */ package wtf.tatp.meowtils.gui.values;
/*    */ 
/*    */ import wtf.tatp.meowtils.gui.ColorComponent;
/*    */ 
/*    */ public class BrightnessValue {
/*  6 */   private final double min = 0.0D;
/*  7 */   private final double max = 100.0D;
/*    */   private double brightness;
/*    */   private final ColorComponent link;
/*    */   
/*    */   public BrightnessValue(ColorComponent link) {
/* 12 */     this.link = link;
/* 13 */     this.brightness = link.getBrightness() * 100.0D;
/*    */   }
/*    */   public double get() {
/* 16 */     return this.brightness;
/*    */   }
/*    */   public void set(double newBrightness) {
/* 19 */     this.brightness = clamp(newBrightness, 0.0D, 100.0D);
/* 20 */     float b = (float)(this.brightness / 100.0D);
/* 21 */     this.link.apply(this.link.getHue(), this.link.getSaturation(), b);
/*    */   }
/*    */   
/*    */   public void syncFromConfig() {
/* 25 */     this.link.syncFromConfig();
/* 26 */     this.brightness = this.link.getBrightness() * 100.0D;
/*    */   }
/*    */   
/*    */   public int getCurrentRGB() {
/* 30 */     return this.link.getRGB();
/*    */   }
/*    */   
/*    */   public ColorComponent getLink() {
/* 34 */     return this.link;
/*    */   }
/*    */   
/*    */   private double clamp(double v, double min, double max) {
/* 38 */     return Math.max(min, Math.min(max, v));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/values/BrightnessValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */