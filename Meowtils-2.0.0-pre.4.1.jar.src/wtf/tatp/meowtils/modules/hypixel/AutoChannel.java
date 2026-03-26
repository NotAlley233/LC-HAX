/*    */ package wtf.tatp.meowtils.modules.hypixel;
/*    */ 
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ 
/*    */ public class AutoChannel
/*    */   extends Module {
/*    */   public AutoChannel() {
/*  9 */     super("AutoChannel", "autoChannelKey", "autoChannel", Module.Category.Hypixel);
/* 10 */     tooltip("Automatically switches chat channel when joining/leaving a party.");
/*    */   }
/*    */   
/*    */   public static void swapToAll() {
/* 14 */     Meowtils.sendCleanMessage("/chat all");
/*    */   }
/*    */   
/*    */   public static void swapToParty() {
/* 18 */     Meowtils.sendCleanMessage("/chat party");
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/hypixel/AutoChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */