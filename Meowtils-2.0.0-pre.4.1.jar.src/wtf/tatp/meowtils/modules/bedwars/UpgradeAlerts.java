/*     */ package wtf.tatp.meowtils.modules.bedwars;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.scoreboard.Team;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.util.ColorUtil;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.NameUtil;
/*     */ import wtf.tatp.meowtils.util.NotifyUtil;
/*     */ import wtf.tatp.meowtils.util.TeamUtil;
/*     */ 
/*     */ public class UpgradeAlerts extends Module {
/*  25 */   private int tickCounter = 0; private BooleanValue upgradeTrackerSound;
/*  26 */   private static Map<String, Set<String>> playerUpgradeMap = new HashMap<>();
/*  27 */   private static Map<String, Set<String>> teamUpgradeMap = new HashMap<>();
/*     */   public UpgradeAlerts() {
/*  29 */     super("UpgradeAlerts", "upgradeTrackerKey", "upgradeTracker", Module.Category.Bedwars);
/*  30 */     tooltip("Alerts when a team buys a team upgrade.");
/*  31 */     addBoolean(this.upgradeTrackerSound = new BooleanValue("Ping sound", "upgradeTrackerSound"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTick(TickEvent.ClientTickEvent event) {
/*  36 */     if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*  37 */       return;  if (event.phase == TickEvent.Phase.END && 
/*  38 */       this.mc.field_71441_e != null) {
/*     */       
/*  40 */       List<EntityPlayer> playerList = this.mc.field_71441_e.field_73010_i;
/*  41 */       if (++this.tickCounter % 20 != 0)
/*     */         return; 
/*  43 */       for (EntityPlayer player : playerList) {
/*  44 */         checkForEnchantments(player);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkForEnchantments(EntityPlayer player) {
/*  51 */     if (player == null || player.field_71071_by == null)
/*     */       return; 
/*  53 */     String playerName = player.func_70005_c_();
/*     */     
/*  55 */     if (TeamUtil.ignoreSelf(playerName))
/*  56 */       return;  if (TeamUtil.ignoreTeam(playerName))
/*  57 */       return;  if (TeamUtil.inSpectator())
/*     */       return; 
/*  59 */     Set<String> upgrades = playerUpgradeMap.getOrDefault(playerName, new HashSet<>());
/*     */     
/*  61 */     ItemStack heldItem = player.func_70694_bm();
/*  62 */     if (heldItem != null && heldItem.func_77973_b() instanceof net.minecraft.item.ItemSword && heldItem.func_77948_v()) {
/*  63 */       notifyUpgrade(playerName, "Sharpened Swords", upgrades);
/*     */     }
/*     */     
/*  66 */     ItemStack chestplate = player.field_71071_by.field_70460_b[2];
/*  67 */     if (chestplate != null && chestplate.func_77973_b() instanceof net.minecraft.item.ItemArmor && chestplate.func_77948_v()) {
/*  68 */       notifyUpgrade(playerName, "Reinforced Armor", upgrades);
/*     */     }
/*     */     
/*  71 */     playerUpgradeMap.put(playerName, upgrades);
/*     */   }
/*     */ 
/*     */   
/*     */   private void notifyUpgrade(String playerName, String upgrade, Set<String> upgrades) {
/*  76 */     String displayName = getTabDisplayName(playerName);
/*     */     
/*  78 */     if (!"NONE".equals(displayName)) {
/*     */       
/*  80 */       ScorePlayerTeam playerTeam = this.mc.field_71441_e.func_96441_U().func_96509_i(playerName);
/*  81 */       if (playerTeam == null)
/*     */         return; 
/*  83 */       String formattedName = ScorePlayerTeam.func_96667_a((Team)playerTeam, playerName);
/*  84 */       EnumChatFormatting colorCode = NameUtil.getNameColor(formattedName);
/*  85 */       String teamName = getFormattedTeamName(colorCode);
/*     */       
/*  87 */       if (teamName == null)
/*     */         return; 
/*  89 */       Set<String> teamUpgrades = teamUpgradeMap.getOrDefault(teamName, new HashSet<>());
/*     */       
/*  91 */       if (teamUpgrades.contains(upgrade))
/*     */         return; 
/*  93 */       EnumChatFormatting color = EnumChatFormatting.WHITE;
/*  94 */       if (upgrade.contains("Sharpened Swords")) {
/*  95 */         color = EnumChatFormatting.DARK_AQUA;
/*  96 */       } else if (upgrade.contains("Reinforced Armor")) {
/*  97 */         color = EnumChatFormatting.DARK_AQUA;
/*     */       } 
/*     */ 
/*     */       
/* 101 */       Meowtils.addMessage(teamName + EnumChatFormatting.GRAY + " purchased " + color + upgrade);
/* 102 */       teamUpgrades.add(upgrade);
/* 103 */       teamUpgradeMap.put(teamName, teamUpgrades);
/*     */       
/* 105 */       if (cfg.v.notify && cfg.v.notifyUpgradeAlerts) {
/* 106 */         NotifyUtil.notifyParty(ColorUtil.unformattedText(teamName) + " purchased " + upgrade);
/*     */       }
/*     */       
/* 109 */       if (cfg.v.upgradeTrackerSound) {
/* 110 */         PlaySound.getInstance().playPingSound();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getTabDisplayName(String playerName) {
/* 116 */     if ((Minecraft.func_71410_x()).field_71441_e == null || (Minecraft.func_71410_x()).field_71441_e.func_96441_U() == null) {
/* 117 */       return "NONE";
/*     */     }
/*     */     
/* 120 */     ScorePlayerTeam playerTeam = (Minecraft.func_71410_x()).field_71441_e.func_96441_U().func_96509_i(playerName);
/* 121 */     if (playerTeam == null) {
/* 122 */       return "NONE";
/*     */     }
/*     */     
/* 125 */     return ScorePlayerTeam.func_96667_a((Team)playerTeam, playerName);
/*     */   }
/*     */   private String getFormattedTeamName(EnumChatFormatting color) {
/* 128 */     switch (color) { case RED:
/* 129 */         return EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + "Red Team";
/* 130 */       case BLUE: return EnumChatFormatting.BLUE.toString() + EnumChatFormatting.BOLD + "Blue Team";
/* 131 */       case GREEN: return EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "Green Team";
/* 132 */       case YELLOW: return EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.BOLD + "Yellow Team";
/* 133 */       case AQUA: return EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Aqua Team";
/* 134 */       case WHITE: return EnumChatFormatting.WHITE.toString() + EnumChatFormatting.BOLD + "White Team";
/* 135 */       case LIGHT_PURPLE: return EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.BOLD + "Pink Team";
/* 136 */       case DARK_GRAY: return EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.BOLD + "Gray Team"; }
/* 137 */      return null;
/*     */   }
/*     */   
/*     */   public static void clear() {
/* 141 */     teamUpgradeMap.clear();
/* 142 */     playerUpgradeMap.clear();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/bedwars/UpgradeAlerts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */