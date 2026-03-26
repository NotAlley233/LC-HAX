/*    */ package wtf.tatp.meowtils.gui.values;
/*    */ 
/*    */ import wtf.tatp.meowtils.gui.ColorComponent;
/*    */ 
/*    */ public class ColorValue {
/*    */   private final String name;
/*  7 */   private final double min = 0.0D;
/*  8 */   private final double max = 360.0D;
/*    */   private double hue;
/*    */   private final ColorComponent link;
/*    */   
/*    */   public ColorValue(String name, ColorComponent link) {
/* 13 */     this.name = name;
/* 14 */     this.link = link;
/* 15 */     this.hue = link.getHue() * 360.0D;
/*    */   }
/*    */   
/* 18 */   public double get() { return this.hue; } public String getName() {
/* 19 */     return this.name;
/*    */   }
/*    */   public void set(double newHue) {
/* 22 */     this.hue = clamp(newHue, 0.0D, 360.0D);
/* 23 */     float h = (float)(this.hue / 360.0D);
/* 24 */     this.link.apply(h, this.link.getSaturation(), this.link.getBrightness());
/*    */   }
/*    */   
/*    */   public ColorComponent getLink() {
/* 28 */     return this.link;
/*    */   }
/*    */   
/*    */   public void syncFromConfig() {
/* 32 */     this.link.syncFromConfig();
/* 33 */     this.hue = this.link.getHue() * 360.0D;
/*    */   }
/*    */   
/*    */   private double clamp(double v, double min, double max) {
/* 37 */     return Math.max(min, Math.min(max, v));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/values/ColorValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */