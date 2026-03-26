/*     */ package wtf.tatp.meowtils.util.stats;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.util.NameUtil;
/*     */ 
/*     */ public class StatsFetcher {
/*  19 */   private static final Set<String> pendingFetches = Collections.synchronizedSet(new HashSet<>());
/*  20 */   private static final HashMap<String, CachedStat> statsCache = new HashMap<>();
/*     */ 
/*     */   
/*     */   public static class CachedStat
/*     */   {
/*     */     public long timestamp;
/*     */     public double fkdr;
/*     */     public double wlr;
/*     */     
/*     */     public CachedStat(double fkdr, double wlr, int level, int winstreak, String rank, EnumChatFormatting plusColor) {
/*  30 */       this.timestamp = System.currentTimeMillis();
/*  31 */       this.fkdr = fkdr;
/*  32 */       this.wlr = wlr;
/*  33 */       this.level = level;
/*  34 */       this.winstreak = winstreak;
/*  35 */       this.rank = rank;
/*  36 */       this.plusColor = plusColor;
/*     */     }
/*     */     public int level; public int winstreak; public String rank; public EnumChatFormatting plusColor; }
/*     */   
/*     */   public static boolean isCached(String player) {
/*  41 */     CachedStat stat = statsCache.get(player);
/*  42 */     return (stat != null && System.currentTimeMillis() - stat.timestamp < 86400000L);
/*     */   }
/*     */   
/*     */   public static CachedStat getCachedStat(String player) {
/*  46 */     return statsCache.get(player);
/*     */   }
/*     */   
/*     */   public static void fetchStats(String player) {
/*  50 */     fetchStats(player, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void fetchStats(String player, boolean showMessage) {
/*  55 */     (new Thread(() -> {
/*     */           try {
/*     */             String urlStr = "https://api.hypixel.net/player?key=" + cfg.v.apiKey + "&name=" + player;
/*     */             
/*     */             URL url = new URL(urlStr);
/*     */             
/*     */             HttpURLConnection conn = (HttpURLConnection)url.openConnection();
/*     */             
/*     */             conn.setRequestMethod("GET");
/*     */             
/*     */             conn.setRequestProperty("User-Agent", "Mozilla/5.0");
/*     */             
/*     */             BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
/*     */             StringBuilder response = new StringBuilder();
/*     */             String line;
/*     */             while ((line = reader.readLine()) != null) {
/*     */               response.append(line);
/*     */             }
/*     */             reader.close();
/*     */             JsonObject json = (new JsonParser()).parse(response.toString()).getAsJsonObject();
/*     */             if (!json.get("success").getAsBoolean()) {
/*     */               if (showMessage) {
/*     */                 Meowtils.addMessage(EnumChatFormatting.RED + "Invalid API key or request failed.");
/*     */               }
/*     */               return;
/*     */             } 
/*     */             JsonObject playerData = json.getAsJsonObject("player");
/*     */             if (playerData == null || playerData.isJsonNull()) {
/*     */               if (showMessage) {
/*     */                 Meowtils.addMessage(EnumChatFormatting.DARK_PURPLE + player + EnumChatFormatting.RED + " might be nicked.");
/*     */               }
/*     */               return;
/*     */             } 
/*     */             JsonObject stats = playerData.getAsJsonObject("stats").getAsJsonObject("Bedwars");
/*     */             int level = BedwarsStatsUtil.calculateBedwarsLevel(stats);
/*  90 */             double fkdr = (stats.has("final_kills_bedwars") && stats.has("final_deaths_bedwars")) ? (stats.get("final_kills_bedwars").getAsDouble() / Math.max(1.0D, stats.get("final_deaths_bedwars").getAsDouble())) : 0.0D;
/*  91 */             double wlr = (stats.has("wins_bedwars") && stats.has("losses_bedwars")) ? (stats.get("wins_bedwars").getAsDouble() / Math.max(1.0D, stats.get("losses_bedwars").getAsDouble())) : 0.0D;
/*     */             
/*     */             int winstreak = stats.has("winstreak") ? stats.get("winstreak").getAsInt() : 0;
/*     */             
/*     */             String rank = StatsUtil.getRank(playerData);
/*     */             
/*     */             EnumChatFormatting plusColor = StatsUtil.getPlusColor(playerData);
/*     */             CachedStat stat = new CachedStat(fkdr, wlr, level, winstreak, rank, plusColor);
/*     */             statsCache.put(player, stat);
/*     */             if (showMessage) {
/*     */               displayStats(player, stat);
/*     */             }
/* 103 */           } catch (Exception e) {
/*     */             if (showMessage) {
/*     */               Meowtils.addMessage(EnumChatFormatting.RED + "Failed to fetch stats for " + EnumChatFormatting.GRAY + player + EnumChatFormatting.RED + ".");
/*     */             }
/*     */             e.printStackTrace();
/*     */           } 
/* 109 */         })).start();
/*     */   }
/*     */   
/*     */   public static void displayStats(String player, CachedStat stat) {
/* 113 */     EnumChatFormatting rankColor = StatsUtil.getRankColor(stat.rank);
/* 114 */     String rankDisplay = StatsUtil.getFormattedRankDisplay(stat.rank, stat.plusColor);
/* 115 */     String displayName = (stat.rank == null || stat.rank.isEmpty()) ? (EnumChatFormatting.RESET + NameUtil.getTabDisplayName(player)) : (rankDisplay + rankColor + player);
/*     */ 
/*     */     
/* 118 */     String message = BedwarsStarColor.getFormattedBedwarsLevel(stat.level) + " " + displayName + EnumChatFormatting.DARK_GRAY + " | " + BedwarsStatsUtil.getFKDRColor(stat.fkdr) + "FKDR: " + String.format("%.2f", new Object[] { Double.valueOf(stat.fkdr) }) + EnumChatFormatting.DARK_GRAY + " | " + BedwarsStatsUtil.getWLRColor(stat.wlr) + "WLR: " + String.format("%.2f", new Object[] { Double.valueOf(stat.wlr) });
/*     */     
/* 120 */     if (BedwarsStatsUtil.getWSColor(stat.winstreak) != null) {
/* 121 */       message = message + EnumChatFormatting.DARK_GRAY + " | " + BedwarsStatsUtil.getWSColor(stat.winstreak) + "WS: " + stat.winstreak;
/*     */     }
/* 123 */     Meowtils.addMessage(message);
/*     */   }
/*     */   public static void queueFetch(String player) {
/* 126 */     if (!isCached(player))
/* 127 */       pendingFetches.add(player); 
/*     */   }
/*     */   
/*     */   static {
/* 131 */     (new Thread(() -> {
/*     */           while (true) {
/*     */             try {
/*     */               if (!pendingFetches.isEmpty()) {
/*     */                 String nextPlayer = pendingFetches.iterator().next();
/*     */                 
/*     */                 pendingFetches.remove(nextPlayer);
/*     */                 fetchStats(nextPlayer, false);
/*     */               } 
/*     */               Thread.sleep(150L);
/* 141 */             } catch (Exception e) {
/*     */               e.printStackTrace();
/*     */             } 
/*     */           } 
/* 145 */         }"StatsQueue")).start();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/stats/StatsFetcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */