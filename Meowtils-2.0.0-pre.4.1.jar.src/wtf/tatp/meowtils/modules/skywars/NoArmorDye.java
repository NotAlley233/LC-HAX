/*    */ package wtf.tatp.meowtils.modules.skywars;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*    */ 
/*    */ public class NoArmorDye
/*    */   extends Module {
/*    */   private ArrayValue mode;
/*    */   
/*    */   public NoArmorDye() {
/* 12 */     super("NoArmorDye", "noArmorDyeKey", "noArmorDye", Module.Category.Skywars);
/* 13 */     tooltip("Removes dye from leather armor.");
/* 14 */     addArray(this.mode = new ArrayValue("Mode", Arrays.asList(new String[] { "Both", "Model", "Item" }, ), "noArmorDyeMode"));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/skywars/NoArmorDye.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */