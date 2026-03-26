/*    */ package wtf.tatp.meowtils.modules.hypixel;
/*    */ 
/*    */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.EventPriority;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ 
/*    */ public class AutoWho
/*    */   extends Module {
/* 14 */   private int ticksRemaining = -1;
/*    */   private BooleanValue hideMessage;
/*    */   
/*    */   public AutoWho() {
/* 18 */     super("AutoWho", "autoWhoKey", "autoWho", Module.Category.Hypixel);
/* 19 */     tooltip("Automatically runs /who after a game started. May be required by some modules.");
/* 20 */     addBoolean(this.hideMessage = new BooleanValue("Hide message", "hideAutoWhoMessage"));
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 25 */     String message = event.message.func_150260_c();
/*    */     
/* 27 */     if (message.equals("The game starts in 1 second!"))
/* 28 */       this.ticksRemaining = 60; 
/*    */   }
/*    */   
/*    */   @SubscribeEvent(priority = EventPriority.LOWEST)
/*    */   public void hideAutoWhoMessage(ClientChatReceivedEvent event) {
/* 33 */     String message = event.message.func_150260_c();
/*    */     
/* 35 */     if (message.contains("ONLINE:") && cfg.v.hideAutoWhoMessage) {
/* 36 */       event.setCanceled(true);
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTick(TickEvent.ClientTickEvent event) {
/* 42 */     if (this.ticksRemaining > 0) {
/* 43 */       this.ticksRemaining--;
/*    */       
/* 45 */       if (this.ticksRemaining == 0) {
/* 46 */         Meowtils.sendCleanMessage("/who");
/* 47 */         this.ticksRemaining = -1;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/hypixel/AutoWho.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */