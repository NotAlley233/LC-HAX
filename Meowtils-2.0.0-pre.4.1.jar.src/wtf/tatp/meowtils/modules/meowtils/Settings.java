/*    */ package wtf.tatp.meowtils.modules.meowtils;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ 
/*    */ public class Settings
/*    */   extends Module {
/*    */   private BooleanValue autoUpdate;
/*    */   private BooleanValue smoothFont;
/*    */   private ArrayValue hideMod;
/*    */   private ArrayValue prefix;
/*    */   
/*    */   public Settings() {
/* 16 */     super("Settings", null, null, Module.Category.Meowtils, true);
/* 17 */     tooltip("General Meowtils settings.");
/* 18 */     addBoolean(this.autoUpdate = new BooleanValue("Auto-Updates", "autoUpdate"));
/* 19 */     addBoolean(this.smoothFont = new BooleanValue("Smooth font", "smoothFont"));
/* 20 */     addArray(this.hideMod = new ArrayValue("Hide mods", Arrays.asList(new String[] { "Meowtils", "None" }, ), "modID"));
/* 21 */     addArray(this.prefix = new ArrayValue("Prefix", Arrays.asList(new String[] { "Default", "Myau", "Fire", "Nebula", "Air", "Custom", "Short" }, ), "meowtilsPrefixMode"));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/meowtils/Settings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */