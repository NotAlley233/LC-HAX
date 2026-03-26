/*    */ package wtf.tatp.meowtils.modules.render;
/*    */ 
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import wtf.tatp.meowtils.events.ReceivePacketEvent;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ 
/*    */ public class NoTitles
/*    */   extends Module
/*    */ {
/*    */   public NoTitles() {
/* 11 */     super("NoTitles", "noTitlesKey", "noTitles", Module.Category.Render);
/* 12 */     tooltip("Disables titles");
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceived(ReceivePacketEvent event) {
/* 17 */     if (event.getPacket() instanceof net.minecraft.network.play.server.S45PacketTitle)
/* 18 */       event.setCanceled(true); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/render/NoTitles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */