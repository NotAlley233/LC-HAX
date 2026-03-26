/*    */ package wtf.tatp.meowtils.modules.hypixel;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*    */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*    */ 
/*    */ public class AutoTip
/*    */   extends Module
/*    */ {
/*    */   private BooleanValue hideMessages;
/*    */   private NumberValue delay;
/* 20 */   private int tickCounter = 0;
/* 21 */   private long lastTipTime = 0L;
/*    */   public AutoTip() {
/* 23 */     super("AutoTip", "autoTipKey", "autoTip", Module.Category.Hypixel);
/* 24 */     tooltip("Automatically runs /tipall every x minutes.");
/* 25 */     addBoolean(this.hideMessages = new BooleanValue("Hide messages", "autoTipHideMessages"));
/* 26 */     addValue(this.delay = new NumberValue("Delay", 1.0D, 10.0D, 1.0D, "min", "autoTipDelay", int.class));
/*    */   }
/*    */   @SubscribeEvent
/*    */   public void onClientTick(TickEvent.ClientTickEvent event) {
/* 30 */     if (event.phase != TickEvent.Phase.END || (Minecraft.func_71410_x()).field_71441_e == null)
/* 31 */       return;  if (!GamemodeUtil.hypixel)
/*    */       return; 
/* 33 */     this.tickCounter++;
/* 34 */     if (this.tickCounter < 200)
/* 35 */       return;  this.tickCounter = 0;
/*    */     
/* 37 */     long currentTime = System.currentTimeMillis();
/* 38 */     long delay = cfg.v.autoTipDelay * 60000L;
/*    */     
/* 40 */     if (currentTime - this.lastTipTime >= delay) {
/* 41 */       Meowtils.sendCleanMessage("/tipall");
/* 42 */       this.lastTipTime = currentTime;
/*    */       
/* 44 */       if (cfg.v.debugMessages)
/* 45 */         Meowtils.addMessage(EnumChatFormatting.YELLOW + "[AutoTip] " + EnumChatFormatting.WHITE + " Tipped all players."); 
/*    */     } 
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 51 */     String msg = event.message.func_150260_c();
/* 52 */     if (!GamemodeUtil.hypixel)
/*    */       return; 
/* 54 */     if (msg.contains("You tipped") || (msg.contains("You already tipped everyone") && cfg.v.autoTipHideMessages))
/* 55 */       event.setCanceled(true); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/hypixel/AutoTip.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */