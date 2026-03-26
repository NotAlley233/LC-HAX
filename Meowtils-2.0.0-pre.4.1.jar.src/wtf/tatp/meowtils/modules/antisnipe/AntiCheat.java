/*     */ package wtf.tatp.meowtils.modules.antisnipe;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.event.HoverEvent;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.ChatStyle;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.util.BlacklistUtil;
/*     */ import wtf.tatp.meowtils.util.ColorUtil;
/*     */ import wtf.tatp.meowtils.util.NameUtil;
/*     */ import wtf.tatp.meowtils.util.NotifyUtil;
/*     */ import wtf.tatp.meowtils.util.PlaySound;
/*     */ import wtf.tatp.meowtils.util.Prefix;
/*     */ import wtf.tatp.meowtils.util.anticheat.AntiCheatData;
/*     */ import wtf.tatp.meowtils.util.anticheat.PlayerData;
/*     */ 
/*     */ public class AntiCheat extends Module {
/*     */   private BooleanValue detectAutoClicker;
/*     */   private BooleanValue detectKillaura;
/*     */   private BooleanValue detectAutoBlock;
/*     */   private BooleanValue detectLegitScaffold;
/*  35 */   private static final Map<String, AntiCheatData> antiCheatDataMap = new HashMap<>(); private BooleanValue detectNoSlow; private BooleanValue wdrButton; private BooleanValue pingSound; private BooleanValue autoBlacklist; private NumberValue violationLevel;
/*  36 */   private static final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
/*  37 */   private static final Map<String, Map<String, Integer>> violationLevels = new HashMap<>();
/*     */   
/*     */   public AntiCheat() {
/*  40 */     super("AntiCheat", "antiCheatKey", "antiCheat", Module.Category.Antisnipe);
/*  41 */     tooltip("Detects suspicious behaviour of players around you.");
/*  42 */     addValue(this.violationLevel = new NumberValue("Violation level", 0.0D, 10.0D, 1.0D, null, "violationLevel", int.class));
/*  43 */     addBoolean(this.pingSound = new BooleanValue("Flag sound", "flagPingSound"));
/*  44 */     addBoolean(this.autoBlacklist = new BooleanValue("Blacklist on flag", "antiCheatAutoBlacklist"));
/*  45 */     addBoolean(this.wdrButton = new BooleanValue("WDR Button", "flagWDRButton"));
/*  46 */     addBoolean(this.detectAutoBlock = new BooleanValue("AutoBlock", "detectAutoBlock"));
/*  47 */     addBoolean(this.detectNoSlow = new BooleanValue("NoSlow", "detectNoSlow"));
/*  48 */     addBoolean(this.detectKillaura = new BooleanValue("Killaura", "detectKillaura"));
/*  49 */     addBoolean(this.detectAutoClicker = new BooleanValue("AutoClicker", "detectAutoClicker"));
/*  50 */     addBoolean(this.detectLegitScaffold = new BooleanValue("Legit Scaffold", "detectLegitScaffold"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  55 */     if (event.phase != TickEvent.Phase.END || this.mc.field_71441_e == null || this.mc.field_71439_g == null)
/*     */       return; 
/*  57 */     for (EntityPlayer player : this.mc.field_71441_e.field_73010_i) {
/*  58 */       if (player == this.mc.field_71439_g)
/*     */         continue; 
/*  60 */       PlayerData data = playerDataMap.computeIfAbsent(player.func_110124_au(), k -> new PlayerData());
/*  61 */       data.update(player);
/*     */     } 
/*     */     
/*  64 */     for (EntityPlayer player : this.mc.field_71441_e.field_73010_i) {
/*  65 */       if (player == this.mc.field_71439_g || player.func_70005_c_() == null)
/*     */         continue; 
/*  67 */       if (TeamUtil.ignoreTeam(player.func_70005_c_()))
/*     */         continue; 
/*  69 */       AntiCheatData data = antiCheatDataMap.computeIfAbsent(player.func_70005_c_(), k -> new AntiCheatData());
/*  70 */       data.anticheatCheck(player);
/*     */       
/*  72 */       String playerName = player.func_70005_c_();
/*     */       
/*  74 */       if (data.failedAutoBlock() && incrementViolation(playerName, "AutoBlock")) {
/*  75 */         sendFlagMessage(playerName, "AutoBlock");
/*  76 */         data.autoBlockCheck.reset();
/*  77 */         tryAutoBlacklist(playerName, "autoblock");
/*     */       } 
/*  79 */       if (data.failedNoSlow() && incrementViolation(playerName, "NoSlow")) {
/*  80 */         sendFlagMessage(playerName, "NoSlow");
/*  81 */         data.noSlowCheck.reset();
/*  82 */         tryAutoBlacklist(playerName, "noslow");
/*     */       } 
/*  84 */       if (data.failedLegitScaffold() && incrementViolation(playerName, "Legit Scaffold")) {
/*  85 */         sendFlagMessage(playerName, "Legit Scaffold");
/*  86 */         data.legitScaffoldCheck.reset();
/*  87 */         tryAutoBlacklist(playerName, "legit scaffold");
/*     */       } 
/*  89 */       if (data.failedKillauraB() && incrementViolation(playerName, "KillAura")) {
/*  90 */         sendFlagMessage(playerName, "Killaura");
/*  91 */         data.killauraBCheck.reset();
/*  92 */         tryAutoBlacklist(playerName, "killaura");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean incrementViolation(String playerName, String checkType) {
/*  99 */     Map<String, Integer> playerViolations = violationLevels.computeIfAbsent(playerName, k -> new HashMap<>());
/* 100 */     int newLevel = ((Integer)playerViolations.getOrDefault(checkType, Integer.valueOf(0))).intValue() + 1;
/* 101 */     playerViolations.put(checkType, Integer.valueOf(newLevel));
/*     */     
/* 103 */     if (newLevel >= cfg.v.violationLevel) {
/* 104 */       playerViolations.put(checkType, Integer.valueOf(0));
/* 105 */       return true;
/*     */     } 
/*     */     
/* 108 */     return false;
/*     */   }
/*     */   private void tryAutoBlacklist(String playerName, String reason) {
/* 111 */     if (!cfg.v.antiCheatAutoBlacklist)
/* 112 */       return;  if (GamemodeUtil.replay)
/*     */       return; 
/* 114 */     for (NetworkPlayerInfo netInfo : this.mc.func_147114_u().func_175106_d()) {
/* 115 */       if (netInfo.func_178845_a().getName().equalsIgnoreCase(playerName)) {
/* 116 */         String uuid = netInfo.func_178845_a().getId().toString();
/* 117 */         handleBlacklist(uuid, playerName, reason);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 122 */     handleBlacklist((String)null, playerName, reason);
/*     */   }
/*     */   private void handleBlacklist(String uuid, String name, String newReason) {
/* 125 */     String combinedReasons, key = (uuid != null) ? uuid : name;
/* 126 */     boolean alreadyBlacklisted = BlacklistUtil.isBlacklisted(key);
/*     */     
/* 128 */     String existingEntry = BlacklistUtil.getEntry(key);
/*     */ 
/*     */     
/* 131 */     if (alreadyBlacklisted && existingEntry != null) {
/* 132 */       String[] parts = existingEntry.split(" ", 2);
/* 133 */       String oldReasons = (parts.length > 1) ? parts[1] : "";
/* 134 */       Set<String> reasonSet = new LinkedHashSet<>(Arrays.asList(oldReasons.split(" \\| ")));
/* 135 */       reasonSet.add(newReason);
/*     */       
/* 137 */       combinedReasons = String.join(" | ", (Iterable)reasonSet);
/* 138 */       BlacklistUtil.removeFromBlacklist(key);
/*     */     } else {
/* 140 */       combinedReasons = newReason;
/*     */     } 
/*     */     
/* 143 */     BlacklistUtil.addToBlacklist(key, combinedReasons);
/*     */   }
/*     */   
/*     */   private void sendFlagMessage(String playerName, String checkType) {
/* 147 */     String msg = NameUtil.getTabDisplayName(playerName) + EnumChatFormatting.GRAY + " failed " + ColorUtil.getColorFromString(cfg.v.flagMessageComponentColor) + checkType;
/*     */     
/* 149 */     if (cfg.v.flagWDRButton) {
/* 150 */       ChatComponentText message = new ChatComponentText(Prefix.getPrefix() + msg + " ");
/* 151 */       ChatComponentText wdrButton = new ChatComponentText(ColorUtil.getColorFromString(cfg.v.flagMessageBracketColor) + "[" + ColorUtil.getColorFromString(cfg.v.flagMessageButtonColor) + "WDR" + ColorUtil.getColorFromString(cfg.v.flagMessageBracketColor) + "]");
/* 152 */       wdrButton.func_150255_a((new ChatStyle()).func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wdr " + playerName)).func_150209_a(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (IChatComponent)new ChatComponentText(EnumChatFormatting.DARK_AQUA + "Click to report this player."))));
/* 153 */       message.func_150257_a((IChatComponent)wdrButton);
/* 154 */       this.mc.field_71439_g.func_145747_a((IChatComponent)message);
/*     */     } else {
/* 156 */       Meowtils.addMessage(msg);
/*     */     } 
/*     */     
/* 159 */     if (cfg.v.flagPingSound) {
/* 160 */       PlaySound.getInstance().playPingSound();
/*     */     }
/*     */     
/* 163 */     if (cfg.v.notify && cfg.v.notifyAntiCheat)
/* 164 */       NotifyUtil.notifyParty(playerName + " failed " + checkType); 
/*     */   }
/*     */   
/*     */   public static void clear() {
/* 168 */     antiCheatDataMap.clear();
/* 169 */     playerDataMap.clear();
/* 170 */     violationLevels.clear();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/antisnipe/AntiCheat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */