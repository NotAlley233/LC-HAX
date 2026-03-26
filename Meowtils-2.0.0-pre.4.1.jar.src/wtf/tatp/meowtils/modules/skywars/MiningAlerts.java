/*    */ package wtf.tatp.meowtils.modules.skywars;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.events.ReceivePacketEvent;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*    */ import wtf.tatp.meowtils.util.NameUtil;
/*    */ import wtf.tatp.meowtils.util.PlaySound;
/*    */ 
/*    */ public class MiningAlerts extends Module {
/*    */   private BooleanValue ping;
/*    */   
/*    */   public MiningAlerts() {
/* 22 */     super("MiningAlerts", "miningAlertsKey", "miningAlerts", Module.Category.Skywars);
/* 23 */     tooltip("Alerts you when a player mines Diamond Ore.");
/* 24 */     addBoolean(this.ping = new BooleanValue("Ping sound", "miningAlertsSound"));
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceived(ReceivePacketEvent event) {
/* 29 */     if (this.mc.field_71441_e == null || this.mc.field_71439_g == null)
/* 30 */       return;  if (!(event.getPacket() instanceof S25PacketBlockBreakAnim))
/* 31 */       return;  if (!GamemodeUtil.skywarsGame && !GamemodeUtil.skywarsMiniGame)
/* 32 */       return;  S25PacketBlockBreakAnim packet = (S25PacketBlockBreakAnim)event.getPacket();
/* 33 */     BlockPos pos = packet.func_179821_b();
/* 34 */     int progress = packet.func_148846_g();
/* 35 */     if (this.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150482_ag)
/* 36 */       return;  if (progress != 9)
/* 37 */       return;  EntityPlayer closest = null;
/* 38 */     double closestDistance = Double.MAX_VALUE;
/*    */     
/* 40 */     for (EntityPlayer player : this.mc.field_71441_e.field_73010_i) {
/* 41 */       if (player == null || player == this.mc.field_71439_g)
/*    */         continue; 
/* 43 */       double distanceSq = player.func_174818_b(pos);
/* 44 */       if (distanceSq < closestDistance) {
/* 45 */         closestDistance = distanceSq;
/* 46 */         closest = player;
/*    */       } 
/*    */     } 
/*    */     
/* 50 */     if (closest != null) {
/* 51 */       String playerName = closest.func_70005_c_();
/* 52 */       Meowtils.addMessage(NameUtil.getTabDisplayName(playerName) + EnumChatFormatting.GRAY + " mined " + EnumChatFormatting.AQUA + "Diamond Ore");
/* 53 */       if (cfg.v.miningAlertsSound)
/* 54 */         PlaySound.getInstance().playPingSoundDeep(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/skywars/MiningAlerts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */