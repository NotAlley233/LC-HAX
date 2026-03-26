/*    */ package wtf.tatp.meowtils;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MeowtilsAlert
/*    */ {
/* 14 */   private int tickCounter = 0;
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 18 */     Minecraft mc = Minecraft.func_71410_x();
/* 19 */     if (mc.field_71439_g == null || mc.field_71441_e == null || event.phase != TickEvent.Phase.END) {
/*    */       return;
/*    */     }
/* 22 */     this.tickCounter++;
/* 23 */     if (this.tickCounter < 60) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 28 */     Meowtils.addMessage("§7This is a §5§lMeowtils §7pre-release. §7Some features are subject to change. §c§lThis version may not be complete!");
/*    */     
/* 30 */     MinecraftForge.EVENT_BUS.unregister(this);
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/MeowtilsAlert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */