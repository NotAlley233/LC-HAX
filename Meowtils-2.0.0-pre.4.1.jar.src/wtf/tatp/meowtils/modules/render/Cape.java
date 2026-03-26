/*    */ package wtf.tatp.meowtils.modules.render;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*    */ 
/*    */ public class Cape
/*    */   extends Module {
/*    */   private ArrayValue cape;
/*    */   
/*    */   public Cape() {
/* 12 */     super("Cape", "capeKey", "capeSelector", Module.Category.Render);
/* 13 */     tooltip("Renders a cape on you. You may import your own \"custom\" cape file.\n§5/capefolder §7- §fto open");
/* 14 */     addArray(this.cape = new ArrayValue("Cape", Arrays.asList(new String[] { "2011", "2012", "2013", "2015", "2016", "Experience", "Founder", "Cobalt", "Astolfo", "Moon", "Myau", "Raven", "Custom" }, ), "selectedCape"));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/render/Cape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */