/*    */ package wtf.tatp.meowtils.handlers;
/*    */ 
/*    */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import wtf.tatp.meowtils.modules.hypixel.AutoChannel;
/*    */ 
/*    */ public class PartyHandler
/*    */ {
/*    */   private static boolean inParty;
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 13 */     String msg = event.message.func_150260_c();
/* 14 */     if (msg.contains(":")) {
/*    */       return;
/*    */     }
/* 17 */     if (msg.endsWith("has disbanded the party!")) {
/* 18 */       inParty = false;
/* 19 */       AutoChannel.swapToAll();
/*    */     } 
/*    */ 
/*    */     
/* 23 */     if (msg.equals("The party was disbanded because all invites expired and the party was empty.")) {
/* 24 */       inParty = false;
/* 25 */       AutoChannel.swapToAll();
/*    */     } 
/*    */ 
/*    */     
/* 29 */     if (msg.startsWith("You have joined") && msg.endsWith("party!")) {
/* 30 */       inParty = true;
/* 31 */       AutoChannel.swapToParty();
/*    */     } 
/*    */ 
/*    */     
/* 35 */     if (msg.endsWith("joined the party.")) {
/* 36 */       inParty = true;
/* 37 */       AutoChannel.swapToParty();
/*    */     } 
/*    */ 
/*    */     
/* 41 */     if (msg.equals("You left the party.")) {
/* 42 */       inParty = false;
/* 43 */       AutoChannel.swapToAll();
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean inParty() {
/* 48 */     return inParty;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/handlers/PartyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */