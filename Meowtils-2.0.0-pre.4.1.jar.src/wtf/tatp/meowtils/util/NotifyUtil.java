/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.handlers.PartyHandler;
/*    */ 
/*    */ 
/*    */ public class NotifyUtil
/*    */ {
/* 16 */   private static Queue<String> messageQueue = new LinkedList<>();
/* 17 */   private static int tickCounter = 0;
/*    */   
/*    */   public static void notifyPartyQueue(String message) {
/* 20 */     messageQueue.add(message);
/*    */   }
/*    */   
/*    */   public static void notifyParty(String text) {
/* 24 */     if (!GamemodeUtil.hypixel)
/* 25 */       return;  if (!cfg.v.notify)
/* 26 */       return;  Meowtils.debugMessage(EnumChatFormatting.YELLOW + "[Notify]: " + EnumChatFormatting.WHITE + "Sent: " + text);
/* 27 */     if (!PartyHandler.inParty())
/*    */       return; 
/* 29 */     Meowtils.sendMessage(text);
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 35 */     Minecraft mc = Minecraft.func_71410_x();
/* 36 */     if (mc.field_71439_g == null || mc.field_71441_e == null || event.phase != TickEvent.Phase.END)
/*    */       return; 
/* 38 */     tickCounter++;
/* 39 */     if (tickCounter < 20)
/* 40 */       return;  tickCounter = 0;
/*    */     
/* 42 */     if (messageQueue.size() > 20) {
/* 43 */       messageQueue.clear();
/* 44 */       Meowtils.addMessage(EnumChatFormatting.RED + "Error notifying party. Cleared queue.");
/*    */     } 
/*    */     
/* 47 */     if (!messageQueue.isEmpty())
/* 48 */       notifyParty(messageQueue.poll()); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/NotifyUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */