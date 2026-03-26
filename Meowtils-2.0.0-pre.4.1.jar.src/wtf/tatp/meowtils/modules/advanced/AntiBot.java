/*    */ package wtf.tatp.meowtils.modules.advanced;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*    */ 
/*    */ public class AntiBot extends Module {
/* 15 */   private static final Minecraft mc = Minecraft.func_71410_x();
/* 16 */   private static final Map<UUID, Long> tabJoinTimes = new HashMap<>();
/* 17 */   private static final Set<UUID> currentTabList = new HashSet<>();
/*    */   
/*    */   private ArrayValue mode;
/*    */   
/*    */   public AntiBot() {
/* 22 */     super("AntiBot", "antiBotKey", "antiBot", Module.Category.Advanced);
/* 23 */     tooltip("Excludes bots from modules.");
/* 24 */     addArray(this.mode = new ArrayValue("Mode", Arrays.asList(new String[] { "Hypixel", "Universal" }, ), "antiBotMode"));
/*    */   }
/*    */   
/*    */   public static void updateTabList() {
/* 28 */     if (mc.func_147114_u() == null)
/*    */       return; 
/* 30 */     Set<UUID> newTabList = new HashSet<>();
/* 31 */     for (NetworkPlayerInfo info : mc.func_147114_u().func_175106_d()) {
/* 32 */       newTabList.add(info.func_178845_a().getId());
/*    */     }
/*    */     
/* 35 */     long now = System.currentTimeMillis();
/*    */ 
/*    */     
/* 38 */     for (UUID id : newTabList) {
/* 39 */       tabJoinTimes.putIfAbsent(id, Long.valueOf(now));
/*    */     }
/*    */ 
/*    */     
/* 43 */     tabJoinTimes.keySet().removeIf(id -> !newTabList.contains(id));
/*    */     
/* 45 */     currentTabList.clear();
/* 46 */     currentTabList.addAll(newTabList);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 51 */     if (mc.field_71439_g == null || mc.field_71441_e == null || event.phase != TickEvent.Phase.END)
/* 52 */       return;  if (cfg.v.antiBotMode.equals("Universal")) {
/* 53 */       updateTabList();
/*    */     }
/*    */   }
/*    */   
/*    */   public static boolean isBot(EntityPlayer player) {
/* 58 */     if (player == null) return true; 
/* 59 */     if (!cfg.v.antiBot) return false; 
/* 60 */     UUID id = player.func_110124_au();
/*    */ 
/*    */     
/* 63 */     if (cfg.v.antiBotMode.equals("Universal")) {
/* 64 */       if (!currentTabList.contains(id)) return true;
/*    */       
/* 66 */       Long joinTime = tabJoinTimes.get(id);
/* 67 */       if (joinTime == null) return true;
/*    */       
/* 69 */       long timeInTab = System.currentTimeMillis() - joinTime.longValue();
/* 70 */       return (timeInTab < 10000L);
/* 71 */     }  if (cfg.v.antiBotMode.equals("Hypixel")) {
/* 72 */       if (id.version() != 1 && id.version() != 4) {
/* 73 */         return true;
/*    */       }
/* 75 */       return false;
/*    */     } 
/*    */     
/* 78 */     return true;
/*    */   }
/*    */   
/*    */   public static void clear() {
/* 82 */     currentTabList.clear();
/* 83 */     tabJoinTimes.clear();
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/advanced/AntiBot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */