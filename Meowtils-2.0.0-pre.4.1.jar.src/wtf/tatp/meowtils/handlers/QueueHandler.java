/*    */ package wtf.tatp.meowtils.handlers;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*    */ import net.minecraftforge.event.entity.EntityJoinWorldEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*    */ 
/*    */ public class QueueHandler
/*    */ {
/* 15 */   private String detectedMode = "";
/* 16 */   private int ticks = 0;
/*    */   
/*    */   private boolean scoreboardAvailable = false;
/*    */   private boolean awaitingLocraw = false;
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTick(TickEvent.ClientTickEvent event) {
/* 23 */     Minecraft mc = Minecraft.func_71410_x();
/* 24 */     if (event.phase != TickEvent.Phase.START || mc.field_71441_e == null || mc.field_71439_g == null)
/*    */       return; 
/* 26 */     if (this.awaitingLocraw && !this.scoreboardAvailable && mc.field_71441_e.func_96441_U() != null) {
/* 27 */       this.scoreboardAvailable = true;
/*    */     }
/*    */     
/* 30 */     if (this.scoreboardAvailable && ++this.ticks == 20 && GamemodeUtil.hypixel) {
/* 31 */       Meowtils.sendCleanMessage("/locraw");
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 37 */     String msg = event.message.func_150260_c();
/*    */ 
/*    */     
/* 40 */     if (this.awaitingLocraw) {
/* 41 */       String locraw = msg.trim();
/*    */       
/* 43 */       if (!locraw.startsWith("{"))
/*    */         return; 
/* 45 */       this.awaitingLocraw = false;
/*    */       
/*    */       try {
/* 48 */         if (!locraw.contains("dynamic") && !locraw.contains("REPLAY") && !locraw.contains("hub") && !locraw.equals("{\"server\":\"limbo\"}") && !locraw.equals("HOUSING")) {
/* 49 */           int index = locraw.indexOf("mode\":\"");
/* 50 */           if (index != -1) {
/* 51 */             this.detectedMode = locraw.substring(index + 7).split("\"")[0];
/* 52 */             cfg.v.lastPlayCommand = this.detectedMode;
/* 53 */             if (cfg.v.requeueSavedCommandMessage) {
/* 54 */               Meowtils.addMessage(EnumChatFormatting.GREEN + "Saved play command: " + this.detectedMode);
/* 55 */               cfg.save();
/*    */             } 
/*    */           } 
/*    */         } 
/* 59 */       } catch (Exception e) {
/* 60 */         e.printStackTrace();
/*    */       } 
/* 62 */       event.setCanceled(true);
/*    */     } 
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onEntityJoin(EntityJoinWorldEvent event) {
/* 68 */     Minecraft mc = Minecraft.func_71410_x();
/* 69 */     if (event.entity == mc.field_71439_g) {
/*    */       
/* 71 */       this.scoreboardAvailable = false;
/* 72 */       this.awaitingLocraw = true;
/* 73 */       this.ticks = 0;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/handlers/QueueHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */