/*    */ package wtf.tatp.meowtils.modules.meowtils;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import wtf.tatp.meowtils.gui.ColorComponent;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*    */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*    */ import wtf.tatp.meowtils.gui.values.SaturationValue;
/*    */ 
/*    */ public class GUI
/*    */   extends Module {
/*    */   private ArrayValue test;
/*    */   private ArrayValue mode;
/*    */   private BooleanValue guiTooltips;
/*    */   
/*    */   public GUI() {
/* 19 */     super("GUI", "guiBind", null, Module.Category.Meowtils, true);
/* 20 */     tooltip("GUI related settings.\nYou may also middle-click to bind GUI or any other module.");
/* 21 */     this.color = new ColorComponent("meowtils_red", "meowtils_green", "meowtils_blue");
/* 22 */     addColor(this.rgb = new ColorValue("GUI color", this.color));
/* 23 */     addSaturation(this.saturation = new SaturationValue(this.color));
/* 24 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/* 25 */     addBoolean(this.guiTooltips = new BooleanValue("Show tooltips", "guiTooltips"));
/* 26 */     addArray(this.mode = new ArrayValue("GUI Scale", Arrays.asList(new String[] { "Tiny", "Small", "Normal", "Large", "Huge" }, ), "guiScale"));
/*    */   }
/*    */   
/*    */   private ColorValue rgb;
/*    */   private SaturationValue saturation;
/*    */   private BrightnessValue brightness;
/*    */   private ColorComponent color;
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/meowtils/GUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */