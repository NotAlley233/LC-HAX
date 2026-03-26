/*     */ package wtf.tatp.meowtils.modules.antisnipe;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.NameUtil;
/*     */ import wtf.tatp.meowtils.util.PlaySound;
/*     */ import wtf.tatp.meowtils.util.TeamUtil;
/*     */ 
/*     */ public class SniperWarning extends Module {
/*     */   private BooleanValue sniperPingSound;
/*  26 */   private int tickCounter = 0; private BooleanValue alertParty;
/*  27 */   private static final Map<String, Boolean> notifiedPlayers = new HashMap<>();
/*     */   
/*     */   public SniperWarning() {
/*  30 */     super("SniperWarning", "sniperWarningKey", "sniperWarning", Module.Category.Antisnipe);
/*  31 */     tooltip("Warns you of potential snipers in your game.");
/*  32 */     addBoolean(this.sniperPingSound = new BooleanValue("Ping sound", "sniperWarningSound"));
/*  33 */     addBoolean(this.alertParty = new BooleanValue("Alert party", "sniperWarningAlertParty"));
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTick(TickEvent.ClientTickEvent event) {
/*  39 */     if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*  40 */       return;  if (event.phase == TickEvent.Phase.END && 
/*  41 */       this.mc.field_71441_e != null) {
/*     */       
/*  43 */       List<EntityPlayer> playerList = this.mc.field_71441_e.field_73010_i;
/*  44 */       if (++this.tickCounter % 20 != 0)
/*     */         return; 
/*  46 */       for (EntityPlayer player : playerList) {
/*  47 */         checkGear(player);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkGear(EntityPlayer player) {
/*  54 */     if (player == null || player.field_71071_by == null)
/*  55 */       return;  if (player == this.mc.field_71439_g)
/*  56 */       return;  if (TeamUtil.ignoreTeam(player.func_70005_c_()))
/*     */       return; 
/*  58 */     String name = player.func_70005_c_();
/*  59 */     if ((name.contains("mcalt_") || name.contains("mcalts_") || name.contains("hassalt_") || name.contains("dogalt_") || name.contains("mal_") || name.contains("bym_") || name.contains("jy6_") || name.contains("lf_") || name.contains("wg_") || name.contains("ggnekito") || name.contains("dahai_") || name.contains("tzi_") || name.contains("nicegen") || name.contains("opalalts") || name.contains("tzi") || name.contains("msmc")) && !notifiedPlayers.containsKey(name)) {
/*  60 */       notifyPlayer(name);
/*  61 */       notifiedPlayers.put(name, Boolean.valueOf(true));
/*     */       
/*     */       return;
/*     */     } 
/*  65 */     boolean hasChain = false;
/*  66 */     boolean hasIronSword = false;
/*     */     
/*  68 */     for (ItemStack armor : player.field_71071_by.field_70460_b) {
/*  69 */       if (armor != null && armor.func_77973_b() instanceof ItemArmor) {
/*  70 */         ItemArmor itemArmor = (ItemArmor)armor.func_77973_b();
/*  71 */         if (itemArmor.func_82812_d() == ItemArmor.ArmorMaterial.CHAIN) {
/*  72 */           hasChain = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*  78 */     ItemStack heldItem = player.func_70694_bm();
/*  79 */     if (heldItem != null && heldItem.func_77973_b() instanceof ItemSword) {
/*  80 */       ItemSword sword = (ItemSword)heldItem.func_77973_b();
/*  81 */       if (sword.func_150932_j().equals("IRON")) {
/*  82 */         hasIronSword = true;
/*     */       }
/*     */     } 
/*     */     
/*  86 */     if (hasChain && hasIronSword && !notifiedPlayers.containsKey(player.func_70005_c_())) {
/*  87 */       notifyPlayer(player.func_70005_c_());
/*  88 */       notifiedPlayers.put(player.func_70005_c_(), Boolean.valueOf(true));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void notifyPlayer(String playerName) {
/*  93 */     String displayName = NameUtil.getTabDisplayName(playerName);
/*  94 */     String displayNameClean = getCleanTabDisplayName(playerName);
/*  95 */     if (!"NONE".equals(displayName)) {
/*  96 */       Meowtils.addMessage(EnumChatFormatting.RED + "Warning: " + displayName + EnumChatFormatting.GRAY + " might be a sniper!");
/*  97 */       if (cfg.v.sniperWarningSound) {
/*  98 */         PlaySound.getInstance().playPingSoundDeep();
/*     */       }
/* 100 */       if (cfg.v.sniperWarningAlertParty) {
/* 101 */         (Minecraft.func_71410_x()).field_71439_g.func_71165_d("/pc [M] » Warning: " + displayNameClean + " might be a sniper!");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isPlayerInGame() {
/* 119 */     return (this.mc.field_71439_g != null && this.mc.field_71441_e != null);
/*     */   }
/*     */   
/*     */   private String getCleanTabDisplayName(String playerName) {
/* 123 */     ScorePlayerTeam playerTeam = (Minecraft.func_71410_x()).field_71441_e.func_96441_U().func_96509_i(playerName);
/* 124 */     if (playerTeam == null) {
/* 125 */       return "NONE";
/*     */     }
/*     */     
/* 128 */     String prefix = playerTeam.func_96668_e().replaceAll("§.", "");
/* 129 */     String suffix = playerTeam.func_96663_f().replaceAll("§.", "");
/*     */     
/* 131 */     return prefix + playerName + suffix;
/*     */   }
/*     */   public static void clear() {
/* 134 */     notifiedPlayers.clear();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/antisnipe/SniperWarning.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */