/*    */ package wtf.tatp.meowtils.gui.values;
/*    */ 
/*    */ import wtf.tatp.meowtils.gui.ColorComponent;
/*    */ 
/*    */ public class SaturationValue {
/*  6 */   private final double min = 0.0D;
/*  7 */   private final double max = 100.0D;
/*    */   private double saturation;
/*    */   private final ColorComponent link;
/*    */   
/*    */   public SaturationValue(ColorComponent link) {
/* 12 */     this.link = link;
/* 13 */     this.saturation = link.getSaturation() * 100.0D;
/*    */   }
/*    */   public double get() {
/* 16 */     return this.saturation;
/*    */   }
/*    */   public void set(double newSaturation) {
/* 19 */     this.saturation = clamp(newSaturation, 0.0D, 100.0D);
/* 20 */     float s = (float)(this.saturation / 100.0D);
/* 21 */     this.link.apply(this.link.getHue(), s, this.link.getBrightness());
/*    */   }
/*    */   
/*    */   public void syncFromConfig() {
/* 25 */     this.link.syncFromConfig();
/* 26 */     this.saturation = this.link.getSaturation() * 100.0D;
/*    */   }
/*    */   
/*    */   public ColorComponent getLink() {
/* 30 */     return this.link;
/*    */   }
/*    */   
/*    */   public int getCurrentRGB() {
/* 34 */     return this.link.getRGB();
/*    */   }
/*    */   
/*    */   private double clamp(double v, double min, double max) {
/* 38 */     return Math.max(min, Math.min(max, v));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/values/SaturationValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */