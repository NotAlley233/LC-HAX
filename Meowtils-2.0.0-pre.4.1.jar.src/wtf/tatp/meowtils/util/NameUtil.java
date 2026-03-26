/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*    */ import net.minecraft.scoreboard.Scoreboard;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.modules.hypixel.Denicker;
/*    */ import wtf.tatp.meowtils.util.stats.BedwarsStarColor;
/*    */ import wtf.tatp.meowtils.util.stats.BedwarsStatsUtil;
/*    */ import wtf.tatp.meowtils.util.stats.StatsFetcher;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NameUtil
/*    */ {
/*    */   public static String getTabDisplayName(String playerName) {
/* 19 */     Minecraft mc = Minecraft.func_71410_x();
/*    */     
/* 21 */     if (mc.field_71441_e == null || mc.field_71441_e.func_96441_U() == null) {
/* 22 */       return playerName;
/*    */     }
/*    */     
/* 25 */     Scoreboard scoreboard = mc.field_71441_e.func_96441_U();
/* 26 */     ScorePlayerTeam team = scoreboard.func_96509_i(playerName);
/*    */     
/* 28 */     if (team != null) {
/* 29 */       return team.func_96668_e() + playerName + team.func_96663_f();
/*    */     }
/*    */     
/* 32 */     return playerName;
/*    */   }
/*    */   public static String appendTabName(NetworkPlayerInfo info, String baseName) {
/* 35 */     String name = info.func_178845_a().getName();
/* 36 */     String uuid = info.func_178845_a().getId().toString();
/*    */     
/* 38 */     String icon = BlacklistUtil.getFormattedIcon(uuid, name);
/* 39 */     boolean isNicked = (info.func_178845_a().getId().version() == 1);
/* 40 */     String nickIcon = (isNicked && cfg.v.nickReveal) ? Denicker.NICK_ICON : null;
/*    */     
/* 42 */     StringBuilder out = new StringBuilder();
/*    */     
/* 44 */     if (cfg.v.stats && cfg.v.tabStats && GamemodeUtil.bedwarsGame && !GamemodeUtil.bedwarsLobby && 
/* 45 */       !isNicked) {
/* 46 */       if (StatsFetcher.isCached(name)) {
/* 47 */         StatsFetcher.CachedStat stat = StatsFetcher.getCachedStat(name);
/* 48 */         if (stat != null && cfg.v.starsInTab.equals("Prefix")) {
/* 49 */           out.append(BedwarsStarColor.getFormattedBedwarsLevel(stat.level)).append(" ");
/*    */         }
/*    */       } else {
/* 52 */         StatsFetcher.queueFetch(name);
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 57 */     if (icon != null) out.append(icon); 
/* 58 */     out.append(baseName);
/* 59 */     if (nickIcon != null) out.append(" ").append(nickIcon);
/*    */     
/* 61 */     if (cfg.v.stats && cfg.v.tabStats && GamemodeUtil.bedwarsGame && !GamemodeUtil.bedwarsLobby && 
/* 62 */       !isNicked && 
/* 63 */       StatsFetcher.isCached(name)) {
/* 64 */       StatsFetcher.CachedStat stat = StatsFetcher.getCachedStat(name);
/* 65 */       if (stat != null && cfg.v.starsInTab.equals("Suffix"))
/* 66 */       { out.append(EnumChatFormatting.DARK_GRAY).append(" | ").append(BedwarsStarColor.getFormattedBedwarsLevel(stat.level)).append(BedwarsStatsUtil.getFKDRColor(stat.fkdr)).append(" ").append(String.format("%.2f", new Object[] { Double.valueOf(stat.fkdr) })); }
/* 67 */       else if (stat != null) { out.append(EnumChatFormatting.DARK_GRAY).append(" | ").append(BedwarsStatsUtil.getFKDRColor(stat.fkdr)).append(String.format("%.2f", new Object[] { Double.valueOf(stat.fkdr) })); }
/*    */     
/*    */     } 
/*    */ 
/*    */     
/* 72 */     return out.toString();
/*    */   }
/*    */   public static EnumChatFormatting getNameColor(String formattedName) {
/* 75 */     if (formattedName == null || formattedName.isEmpty()) return EnumChatFormatting.GRAY;
/*    */     
/* 77 */     for (int i = 0; i < formattedName.length() - 1; i++) {
/* 78 */       if (formattedName.charAt(i) == '§') {
/* 79 */         char colorCode = formattedName.charAt(i + 1);
/* 80 */         return ColorUtil.getColorFromCode(colorCode);
/*    */       } 
/*    */     } 
/* 83 */     return EnumChatFormatting.WHITE;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/NameUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */