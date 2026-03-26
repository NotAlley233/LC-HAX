/*    */ package wtf.tatp.meowtils.util.stats;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ 
/*    */ public class StatsUtil
/*    */ {
/*    */   public static String getRank(JsonObject playerData) {
/*  9 */     if (playerData.has("rank") && !playerData.get("rank").isJsonNull()) {
/* 10 */       String rank = playerData.get("rank").getAsString();
/* 11 */       if (!rank.equals("NORMAL")) {
/* 12 */         return rank;
/*    */       }
/*    */     } 
/* 15 */     if (playerData.has("monthlyPackageRank") && !playerData.get("monthlyPackageRank").isJsonNull() && 
/* 16 */       "SUPERSTAR".equals(playerData.get("monthlyPackageRank").getAsString())) return "MVP++";
/*    */     
/* 18 */     if (playerData.has("newPackageRank") && !playerData.get("newPackageRank").isJsonNull()) {
/* 19 */       String newRank = playerData.get("newPackageRank").getAsString();
/* 20 */       if ("VIP_PLUS".equals(newRank)) return "VIP+"; 
/* 21 */       if ("MVP_PLUS".equals(newRank)) return "MVP+"; 
/* 22 */       return newRank;
/*    */     } 
/* 24 */     if (playerData.has("packageRank") && !playerData.get("packageRank").isJsonNull()) {
/* 25 */       return playerData.get("packageRank").getAsString();
/*    */     }
/* 27 */     return "NONE";
/*    */   }
/*    */   
/*    */   public static EnumChatFormatting getPlusColor(JsonObject playerData) {
/* 31 */     if (playerData.has("rankPlusColor") && !playerData.get("rankPlusColor").isJsonNull()) {
/*    */       try {
/* 33 */         String color = playerData.get("rankPlusColor").getAsString().toUpperCase();
/* 34 */         return EnumChatFormatting.valueOf(color);
/* 35 */       } catch (IllegalArgumentException e) {
/* 36 */         return EnumChatFormatting.RED;
/*    */       } 
/*    */     }
/* 39 */     return EnumChatFormatting.RED;
/*    */   }
/*    */   
/*    */   public static EnumChatFormatting getRankColor(String rank) {
/* 43 */     switch (rank) { case "MVP":
/*    */       case "MVP+":
/* 45 */         return EnumChatFormatting.AQUA;
/* 46 */       case "MVP++": return EnumChatFormatting.GOLD;
/*    */       case "VIP": case "VIP+":
/* 48 */         return EnumChatFormatting.GREEN; }
/* 49 */      return EnumChatFormatting.GRAY;
/*    */   }
/*    */ 
/*    */   
/*    */   public static String getFormattedRankDisplay(String rank, EnumChatFormatting plusColor) {
/* 54 */     if ("MVP++".equals(rank)) return EnumChatFormatting.GOLD + "[MVP" + plusColor + "++" + EnumChatFormatting.GOLD + "] "; 
/* 55 */     if ("MVP+".equals(rank)) return EnumChatFormatting.AQUA + "[MVP" + plusColor + "+" + EnumChatFormatting.AQUA + "] "; 
/* 56 */     if ("VIP+".equals(rank)) return EnumChatFormatting.GREEN + "[VIP" + EnumChatFormatting.GOLD + "+" + EnumChatFormatting.GREEN + "] "; 
/* 57 */     if (!"NONE".equals(rank)) return getRankColor(rank) + "[" + rank + "] "; 
/* 58 */     return "";
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/stats/StatsUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */