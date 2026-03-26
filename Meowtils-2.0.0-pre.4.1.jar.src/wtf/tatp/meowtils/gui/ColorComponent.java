/*    */ package wtf.tatp.meowtils.gui;
/*    */ import java.awt.Color;
/*    */ import java.lang.reflect.Field;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class ColorComponent {
/*    */   private final Field redField;
/*    */   private final Field greenField;
/*    */   private final Field blueField;
/*    */   private final Object configInstance;
/*    */   private float hue;
/*    */   private float saturation;
/*    */   private float brightness;
/*    */   
/*    */   public ColorComponent(String red, String green, String blue) {
/*    */     try {
/* 17 */       this.configInstance = cfg.v;
/* 18 */       this.redField = this.configInstance.getClass().getField(red);
/* 19 */       this.greenField = this.configInstance.getClass().getField(green);
/* 20 */       this.blueField = this.configInstance.getClass().getField(blue);
/* 21 */       this.redField.setAccessible(true);
/* 22 */       this.greenField.setAccessible(true);
/* 23 */       this.blueField.setAccessible(true);
/* 24 */     } catch (Exception e) {
/* 25 */       throw new RuntimeException("Failed to link config field: ", e);
/*    */     } 
/*    */     
/* 28 */     syncFromConfig();
/*    */   }
/*    */   
/*    */   public void syncFromConfig() {
/*    */     try {
/* 33 */       int r = this.redField.getInt(this.configInstance);
/* 34 */       int g = this.greenField.getInt(this.configInstance);
/* 35 */       int b = this.blueField.getInt(this.configInstance);
/*    */       
/* 37 */       float[] hsb = Color.RGBtoHSB(r, g, b, null);
/* 38 */       this.hue = hsb[0];
/* 39 */       this.saturation = hsb[1];
/* 40 */       this.brightness = hsb[2];
/* 41 */     } catch (Exception e) {
/* 42 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/*    */   public void apply(float hue, float sat, float bri) {
/* 47 */     this.hue = hue;
/* 48 */     this.saturation = sat;
/* 49 */     this.brightness = bri;
/*    */     
/*    */     try {
/* 52 */       int rgb = Color.HSBtoRGB(hue, sat, bri) & 0xFFFFFF;
/* 53 */       int r = rgb >> 16 & 0xFF;
/* 54 */       int g = rgb >> 8 & 0xFF;
/* 55 */       int b = rgb & 0xFF;
/*    */       
/* 57 */       this.redField.setInt(this.configInstance, r);
/* 58 */       this.greenField.setInt(this.configInstance, g);
/* 59 */       this.blueField.setInt(this.configInstance, b);
/*    */       
/* 61 */       cfg.save();
/* 62 */     } catch (Exception e) {
/* 63 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */   
/* 67 */   public float getHue() { return this.hue; }
/* 68 */   public float getSaturation() { return this.saturation; } public float getBrightness() {
/* 69 */     return this.brightness;
/*    */   } public int getPureHueRGB() {
/* 71 */     return Color.HSBtoRGB(this.hue, 1.0F, 1.0F) & 0xFFFFFF;
/*    */   }
/*    */   
/*    */   public int getRGB() {
/* 75 */     int rgb = Color.HSBtoRGB(this.hue, this.saturation, this.brightness) & 0xFFFFFF;
/* 76 */     return rgb;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/ColorComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */