/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ 
/*    */ public class DeathTracker
/*    */ {
/* 15 */   private static final Pattern deathMessagePattern = Pattern.compile("^([A-Za-z0-9_]{1,16})\\b.*");
/* 16 */   private static final Map<UUID, Long> recentlyDead = new ConcurrentHashMap<>();
/*    */   private static final long DEATH_TIMEOUT = 10000L;
/*    */   
/*    */   public static void markPlayerDead(EntityPlayer player) {
/* 20 */     recentlyDead.put(player.func_110124_au(), Long.valueOf(System.currentTimeMillis()));
/*    */   }
/*    */   
/*    */   public static boolean isRecentlyDead(EntityPlayer player) {
/* 24 */     Long deathTime = recentlyDead.get(player.func_110124_au());
/* 25 */     if (deathTime == null) return false; 
/* 26 */     return (System.currentTimeMillis() - deathTime.longValue() <= 10000L);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void clear() {
/* 32 */     recentlyDead.clear();
/*    */   }
/*    */   @SubscribeEvent
/*    */   public void onChat(ClientChatReceivedEvent event) {
/* 36 */     String message = event.message.func_150260_c();
/*    */     
/* 38 */     Matcher matcher = deathMessagePattern.matcher(message);
/* 39 */     if (!matcher.find())
/*    */       return; 
/* 41 */     String playerName = matcher.group(1);
/* 42 */     EntityPlayer player = (Minecraft.func_71410_x()).field_71441_e.func_72924_a(playerName);
/*    */     
/* 44 */     if (player != null && !(player instanceof net.minecraft.client.entity.EntityPlayerSP))
/* 45 */       markPlayerDead(player); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/DeathTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */