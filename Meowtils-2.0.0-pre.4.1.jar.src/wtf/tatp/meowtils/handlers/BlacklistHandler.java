/*    */ package wtf.tatp.meowtils.handlers;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.event.entity.EntityJoinWorldEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.util.BlacklistUtil;
/*    */ import wtf.tatp.meowtils.util.NameUtil;
/*    */ 
/*    */ 
/*    */ public class BlacklistHandler
/*    */ {
/* 19 */   private static final Set<String> warnedPlayers = new HashSet<>();
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onEntityJoinWorld(EntityJoinWorldEvent event) {
/* 23 */     if (!(event.entity instanceof EntityPlayer))
/*    */       return; 
/* 25 */     EntityPlayer player = (EntityPlayer)event.entity;
/* 26 */     EntityPlayerSP entityPlayerSP = (Minecraft.func_71410_x()).field_71439_g;
/*    */     
/* 28 */     if (entityPlayerSP == null || player == entityPlayerSP)
/* 29 */       return;  if (!cfg.v.warnBlacklistedPlayer)
/*    */       return; 
/* 31 */     String name = player.func_70005_c_();
/* 32 */     String uuid = player.func_110124_au().toString();
/*    */     
/* 34 */     if ((BlacklistUtil.isBlacklistedByUUID(uuid) || BlacklistUtil.isBlacklisted(name)) && !warnedPlayers.contains(uuid)) {
/* 35 */       String entry = BlacklistUtil.getEntry(uuid);
/* 36 */       if (entry == null) entry = BlacklistUtil.getEntry(name); 
/* 37 */       if (entry != null) {
/* 38 */         String reason = entry.split(" ", 2)[1];
/* 39 */         Meowtils.addMessage(EnumChatFormatting.RED + "Warning: " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(name) + EnumChatFormatting.GRAY + " is blacklisted for: " + BlacklistUtil.getFormattedReason(reason));
/* 40 */         warnedPlayers.add(uuid);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   public static void clear() {
/* 45 */     warnedPlayers.clear();
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/handlers/BlacklistHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */