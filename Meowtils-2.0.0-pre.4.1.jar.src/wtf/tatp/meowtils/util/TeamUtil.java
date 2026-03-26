/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class TeamUtil
/*    */ {
/*    */   private static boolean spectator = false;
/* 15 */   private long spectatorTime = 0L;
/*    */   
/*    */   public static boolean inSpectator() {
/* 18 */     return spectator;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean ignoreSelf(String playerName) {
/* 23 */     Minecraft mc = Minecraft.func_71410_x();
/* 24 */     return (mc.field_71439_g != null && playerName != null && playerName.equalsIgnoreCase(mc.field_71439_g.func_70005_c_()));
/*    */   }
/*    */   
/*    */   public static boolean ignoreTeam(String playerName) {
/* 28 */     Minecraft mc = Minecraft.func_71410_x();
/* 29 */     EntityPlayer target = mc.field_71441_e.field_73010_i.stream().filter(p -> p.func_70005_c_().equalsIgnoreCase(playerName)).findFirst().orElse(null);
/* 30 */     if (mc.field_71439_g == null || mc.field_71441_e == null || playerName == null) return false; 
/* 31 */     if (!cfg.v.ignoreTeam) return false; 
/* 32 */     if (target == null) return false; 
/* 33 */     if (target.field_71075_bZ.field_75101_c) return true;
/*    */ 
/*    */     
/*    */     try {
/* 37 */       if (mc.field_71439_g.func_142014_c((EntityLivingBase)target)) return true;
/*    */       
/* 39 */       String selfFormatted = mc.field_71439_g.func_145748_c_().func_150254_d();
/* 40 */       String targetFormatted = target.func_145748_c_().func_150254_d();
/*    */       
/* 42 */       String selfColor = getMostFrequentColor(selfFormatted);
/* 43 */       String targetColor = getMostFrequentColor(targetFormatted);
/*    */ 
/*    */       
/* 46 */       if (selfColor != null && selfColor.equals(targetColor)) {
/* 47 */         return true;
/*    */       }
/* 49 */     } catch (Exception exception) {}
/*    */     
/* 51 */     return false;
/*    */   }
/*    */   
/*    */   private static String getMostFrequentColor(String text) {
/* 55 */     if (text == null) return null;
/*    */     
/* 57 */     Map<String, Integer> colorCounts = new HashMap<>();
/*    */ 
/*    */     
/* 60 */     for (int i = 0; i < text.length() - 1; i++) {
/* 61 */       char c = text.charAt(i);
/* 62 */       if (c == '§') {
/* 63 */         char code = text.charAt(i + 1);
/* 64 */         if ((code >= '0' && code <= '9') || (code >= 'a' && code <= 'f')) {
/* 65 */           String currentColor = "§" + code;
/* 66 */           colorCounts.put(currentColor, Integer.valueOf(((Integer)colorCounts.getOrDefault(currentColor, Integer.valueOf(0))).intValue() + 1));
/*    */         } 
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 72 */     return colorCounts.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 77 */     Minecraft mc = Minecraft.func_71410_x();
/* 78 */     if (mc.field_71439_g == null || mc.field_71441_e == null || event.phase != TickEvent.Phase.END)
/* 79 */       return;  long now = System.currentTimeMillis();
/*    */     
/* 81 */     if (mc.field_71439_g.field_71075_bZ.field_75101_c) {
/* 82 */       spectator = true;
/* 83 */       this.spectatorTime = now;
/*    */     } else {
/* 85 */       spectator = (now - this.spectatorTime < 8000L);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/TeamUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */