/*    */ package wtf.tatp.meowtils.util.stats;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ 
/*    */ public class BedwarsStatsUtil
/*    */ {
/*    */   public static int calculateBedwarsLevel(JsonObject stats) {
/*  9 */     if (!stats.has("Experience") || stats.get("Experience").isJsonNull()) return 0; 
/* 10 */     int exp = stats.get("Experience").getAsInt();
/* 11 */     int[] levelExp = { 500, 1000, 2000, 3500 };
/* 12 */     int fullCycleXP = 487000;
/* 13 */     int level = 0;
/*    */     
/* 15 */     while (exp >= fullCycleXP) {
/* 16 */       exp -= fullCycleXP;
/* 17 */       level += 100;
/*    */     } 
/*    */     
/* 20 */     for (int i = 0; i < 4; i++) {
/* 21 */       if (exp < levelExp[i]) return level; 
/* 22 */       exp -= levelExp[i];
/* 23 */       level++;
/*    */     } 
/*    */     
/* 26 */     level += exp / 5000;
/* 27 */     return level;
/*    */   }
/*    */   
/*    */   public static EnumChatFormatting getFKDRColor(double fkdr) {
/* 31 */     if (fkdr == 0.0D) return EnumChatFormatting.DARK_BLUE; 
/* 32 */     if (fkdr < 1.0D) return EnumChatFormatting.GRAY; 
/* 33 */     if (fkdr < 3.0D) return EnumChatFormatting.WHITE; 
/* 34 */     if (fkdr < 5.0D) return EnumChatFormatting.GREEN; 
/* 35 */     if (fkdr < 7.0D) return EnumChatFormatting.DARK_GREEN; 
/* 36 */     if (fkdr < 10.0D) return EnumChatFormatting.YELLOW; 
/* 37 */     if (fkdr < 20.0D) return EnumChatFormatting.GOLD; 
/* 38 */     if (fkdr < 30.0D) return EnumChatFormatting.RED; 
/* 39 */     if (fkdr < 50.0D) return EnumChatFormatting.DARK_RED; 
/* 40 */     if (fkdr < 100.0D) return EnumChatFormatting.LIGHT_PURPLE; 
/* 41 */     return EnumChatFormatting.DARK_PURPLE;
/*    */   }
/*    */   
/*    */   public static EnumChatFormatting getWLRColor(double wlr) {
/* 45 */     if (wlr == 0.0D) return EnumChatFormatting.DARK_BLUE; 
/* 46 */     if (wlr < 0.3D) return EnumChatFormatting.GRAY; 
/* 47 */     if (wlr < 0.9D) return EnumChatFormatting.WHITE; 
/* 48 */     if (wlr < 1.5D) return EnumChatFormatting.GREEN; 
/* 49 */     if (wlr < 2.0D) return EnumChatFormatting.DARK_GREEN; 
/* 50 */     if (wlr < 3.0D) return EnumChatFormatting.YELLOW; 
/* 51 */     if (wlr < 6.0D) return EnumChatFormatting.GOLD; 
/* 52 */     if (wlr < 9.0D) return EnumChatFormatting.RED; 
/* 53 */     if (wlr < 15.0D) return EnumChatFormatting.DARK_RED; 
/* 54 */     if (wlr < 30.0D) return EnumChatFormatting.LIGHT_PURPLE; 
/* 55 */     return EnumChatFormatting.DARK_PURPLE;
/*    */   }
/*    */   
/*    */   public static EnumChatFormatting getWSColor(int ws) {
/* 59 */     if (ws == 0) return null; 
/* 60 */     if (ws < 3) return EnumChatFormatting.GRAY; 
/* 61 */     if (ws < 5) return EnumChatFormatting.WHITE; 
/* 62 */     if (ws < 15) return EnumChatFormatting.GREEN; 
/* 63 */     if (ws < 20) return EnumChatFormatting.DARK_GREEN; 
/* 64 */     if (ws < 30) return EnumChatFormatting.YELLOW; 
/* 65 */     if (ws < 40) return EnumChatFormatting.GOLD; 
/* 66 */     if (ws < 50) return EnumChatFormatting.RED; 
/* 67 */     if (ws < 80) return EnumChatFormatting.DARK_RED; 
/* 68 */     return EnumChatFormatting.LIGHT_PURPLE;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/stats/BedwarsStatsUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */