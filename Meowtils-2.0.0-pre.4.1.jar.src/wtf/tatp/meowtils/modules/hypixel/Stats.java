/*    */ package wtf.tatp.meowtils.modules.hypixel;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.util.stats.StatsFetcher;
/*    */ 
/*    */ public class Stats
/*    */   extends Module {
/*    */   private BooleanValue chatStats;
/*    */   private BooleanValue tabStats;
/*    */   private ArrayValue starsInTab;
/*    */   
/*    */   public Stats() {
/* 20 */     super("Stats", "statsKey", "stats", Module.Category.Hypixel);
/* 21 */     tooltip("Displays stats of players in your game.");
/*    */     
/* 23 */     addBoolean(this.chatStats = new BooleanValue("Chat stats", "chatStats"));
/* 24 */     addBoolean(this.tabStats = new BooleanValue("Tab stats", "tabStats"));
/* 25 */     addArray(this.starsInTab = new ArrayValue("Tab stars", Arrays.asList(new String[] { "Prefix", "Suffix", "None" }, ), "starsInTab"));
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 30 */     String message = event.message.func_150260_c();
/* 31 */     if (message.startsWith("ONLINE: ") && cfg.v.chatStats) {
/* 32 */       String[] players = message.substring(8).split(", ");
/*    */       
/* 34 */       ArrayList<String> cached = new ArrayList<>();
/* 35 */       ArrayList<String> uncached = new ArrayList<>();
/*    */       
/* 37 */       for (String player : players) {
/* 38 */         if (StatsFetcher.isCached(player)) {
/* 39 */           cached.add(player);
/*    */         } else {
/* 41 */           uncached.add(player);
/*    */         } 
/*    */       } 
/*    */       
/* 45 */       for (String player : cached) {
/* 46 */         StatsFetcher.displayStats(player, StatsFetcher.getCachedStat(player));
/*    */       }
/* 48 */       for (String player : uncached)
/* 49 */         StatsFetcher.fetchStats(player); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/hypixel/Stats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */