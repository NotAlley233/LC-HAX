/*    */ package wtf.tatp.meowtils.modules.utility;
/*    */ 
/*    */ import net.minecraft.scoreboard.ScoreObjective;
/*    */ import net.minecraft.scoreboard.Scoreboard;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.events.ReceivePacketEvent;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*    */ 
/*    */ public class LatencyAlerts extends Module {
/*    */   private BooleanValue ignoreLimbo;
/*    */   private NumberValue thresHold;
/*    */   private long lastPacket;
/*    */   private long lastAlert;
/*    */   
/*    */   public LatencyAlerts() {
/* 22 */     super("LatencyAlerts", "latencyAlertsKey", "latencyAlerts", Module.Category.Utility);
/* 23 */     tooltip("Warns you in chat when you lose connection to the server. \n May not work on all servers.");
/* 24 */     addValue(this.thresHold = new NumberValue("Latency threshold", 0.0D, 3000.0D, 50.0D, "ms", "latencyLossThreshold", int.class));
/* 25 */     addBoolean(this.ignoreLimbo = new BooleanValue("Ignore limbo", "latencyAlertsIgnoreLimbo"));
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceive(ReceivePacketEvent e) {
/* 30 */     this.lastPacket = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 35 */     if (this.mc.func_71356_B() || this.mc.field_71439_g == null || this.mc.field_71441_e == null)
/* 36 */       return;  Scoreboard scoreboard = this.mc.field_71441_e.func_96441_U();
/* 37 */     ScoreObjective sidebar = scoreboard.func_96539_a(1);
/* 38 */     if (sidebar == null && cfg.v.latencyAlertsIgnoreLimbo)
/*    */       return; 
/* 40 */     long now = System.currentTimeMillis();
/* 41 */     long last = now - this.lastPacket;
/*    */     
/* 43 */     if (last >= cfg.v.latencyLossThreshold && now - this.lastAlert >= 3000L) {
/* 44 */       Meowtils.addMessage(EnumChatFormatting.DARK_GRAY + "Packet loss detected: " + EnumChatFormatting.RED + last + "ms");
/* 45 */       this.lastAlert = now;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/utility/LatencyAlerts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */