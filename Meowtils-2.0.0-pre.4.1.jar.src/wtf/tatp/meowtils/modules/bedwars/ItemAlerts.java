/*     */ package wtf.tatp.meowtils.modules.bedwars;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.util.ColorUtil;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.NameUtil;
/*     */ import wtf.tatp.meowtils.util.NotifyUtil;
/*     */ import wtf.tatp.meowtils.util.PlaySound;
/*     */ import wtf.tatp.meowtils.util.StripFormattingCodes;
/*     */ import wtf.tatp.meowtils.util.TeamUtil;
/*     */ 
/*     */ public class ItemAlerts
/*     */   extends Module {
/*     */   private BooleanValue potions;
/*     */   private BooleanValue bows;
/*     */   private BooleanValue pickaxes;
/*     */   private BooleanValue mobs;
/*     */   private BooleanValue common;
/*  39 */   private final Minecraft mc = Minecraft.func_71410_x(); private BooleanValue important; private BooleanValue explosives; private BooleanValue rotation; private NumberValue cooldown; private ArrayValue soundMode; private ArrayValue distanceMode;
/*  40 */   private static final Map<String, Map<String, Long>> cooldowns = new HashMap<>();
/*     */   
/*     */   public ItemAlerts() {
/*  43 */     super("ItemAlerts", "itemAlertsKey", "itemAlerts", Module.Category.Bedwars);
/*  44 */     tooltip("Alerts in chat when a player holds a specific item.");
/*  45 */     addArray(this.soundMode = new ArrayValue("Ping sound", Arrays.asList(new String[] { "All", "Important", "None" }, ), "itemAlertsSoundMode"));
/*  46 */     addArray(this.distanceMode = new ArrayValue("Distance", Arrays.asList(new String[] { "All", "Important", "None" }, ), "itemAlertsDistanceMode"));
/*  47 */     addValue(this.cooldown = new NumberValue("Cooldown", 1.0D, 30.0D, 1.0D, "s", "itemAlertsCooldown", int.class));
/*  48 */     addBoolean(this.common = new BooleanValue("§7Common Items", "itemAlerts_commonItems"));
/*  49 */     addBoolean(this.important = new BooleanValue("§dImportant Items", "itemAlerts_importantItems"));
/*  50 */     addBoolean(this.rotation = new BooleanValue("§3Rotation Items", "itemAlerts_rotationItems"));
/*  51 */     addBoolean(this.potions = new BooleanValue("§ePotions", "itemAlerts_potions"));
/*  52 */     addBoolean(this.pickaxes = new BooleanValue("§bPickaxes", "itemAlerts_pickaxes"));
/*  53 */     addBoolean(this.explosives = new BooleanValue("§cExplosives", "itemAlerts_explosives"));
/*  54 */     addBoolean(this.bows = new BooleanValue("§6Bows", "itemAlerts_bows"));
/*  55 */     addBoolean(this.mobs = new BooleanValue("Mobs", "itemAlerts_mobs"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  60 */     if (event.phase != TickEvent.Phase.END)
/*  61 */       return;  if (this.mc.field_71439_g == null || this.mc.field_71441_e == null)
/*  62 */       return;  if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*     */       return; 
/*  64 */     for (EntityPlayer player : this.mc.field_71441_e.field_73010_i) {
/*  65 */       if (player == null || 
/*  66 */         player == this.mc.field_71439_g || 
/*  67 */         TeamUtil.ignoreTeam(player.func_70005_c_()))
/*  68 */         continue;  if (TeamUtil.inSpectator())
/*  69 */         return;  ItemStack held = player.func_70694_bm();
/*  70 */       String itemName = null;
/*  71 */       if (held == null)
/*     */         continue; 
/*  73 */       if (cfg.v.itemAlerts_commonItems) {
/*  74 */         if (held.func_77973_b() == Items.field_151040_l) {
/*  75 */           itemName = EnumChatFormatting.WHITE + "Iron Sword";
/*  76 */         } else if (held.func_77973_b() == Items.field_151153_ao) {
/*  77 */           itemName = EnumChatFormatting.GOLD + "Golden Apple";
/*     */         } 
/*     */       }
/*  80 */       if (cfg.v.itemAlerts_importantItems) {
/*  81 */         if (held.func_77973_b() == Items.field_151048_u) {
/*  82 */           itemName = EnumChatFormatting.AQUA + "Diamond Sword";
/*  83 */         } else if (held.func_77973_b() == Items.field_151055_y) {
/*  84 */           itemName = EnumChatFormatting.GOLD + "Knockback Stick";
/*  85 */         } else if (held.func_77973_b() == Items.field_151131_as) {
/*  86 */           itemName = EnumChatFormatting.BLUE + "Water Bucket";
/*  87 */         } else if (held.func_77973_b() == Items.field_151079_bi && !held.func_77962_s()) {
/*  88 */           itemName = EnumChatFormatting.DARK_PURPLE + "Ender Pearl";
/*  89 */         } else if (held.func_77973_b() == Items.field_151110_aK) {
/*  90 */           itemName = EnumChatFormatting.DARK_AQUA + "Bridge Egg";
/*  91 */         } else if (held.func_77973_b() == Item.func_150898_a(Blocks.field_150343_Z)) {
/*  92 */           itemName = EnumChatFormatting.DARK_GRAY + "Obsidian";
/*     */         } 
/*     */       }
/*  95 */       if (cfg.v.itemAlerts_potions) {
/*  96 */         if (held.func_77973_b() == Items.field_151117_aB) {
/*  97 */           itemName = EnumChatFormatting.WHITE + "Milk";
/*  98 */         } else if (held.func_82833_r().toLowerCase().contains("jump")) {
/*  99 */           itemName = EnumChatFormatting.GREEN + "Jump Potion";
/* 100 */         } else if (held.func_82833_r().toLowerCase().contains("speed")) {
/* 101 */           itemName = EnumChatFormatting.YELLOW + "Speed Potion";
/* 102 */         } else if (held.func_82833_r().toLowerCase().contains("invis")) {
/* 103 */           itemName = EnumChatFormatting.AQUA + "Invis Potion";
/*     */         } 
/*     */       }
/* 106 */       if (cfg.v.itemAlerts_pickaxes) {
/* 107 */         if (held.func_77973_b() == Items.field_151046_w) {
/* 108 */           itemName = EnumChatFormatting.AQUA + "Diamond Pickaxe";
/* 109 */         } else if (held.func_77973_b() == Items.field_151005_D) {
/* 110 */           itemName = EnumChatFormatting.GOLD + "Golden Pickaxe";
/*     */         } 
/*     */       }
/* 113 */       if (cfg.v.itemAlerts_explosives) {
/* 114 */         if (held.func_77973_b() == Items.field_151059_bz) {
/* 115 */           itemName = EnumChatFormatting.RED + "Fireball";
/* 116 */         } else if (held.func_77973_b() == Item.func_150898_a(Blocks.field_150335_W) && !held.func_77962_s()) {
/* 117 */           itemName = EnumChatFormatting.RED + "T" + EnumChatFormatting.WHITE + "N" + EnumChatFormatting.RED + "T";
/*     */         } 
/*     */       }
/* 120 */       if (cfg.v.itemAlerts_bows) {
/* 121 */         if (held.func_77973_b() == Items.field_151031_f && held.func_77962_s()) {
/* 122 */           itemName = EnumChatFormatting.GOLD + "Enchanted Bow";
/* 123 */         } else if (held.func_77973_b() == Items.field_151031_f) {
/* 124 */           itemName = EnumChatFormatting.GOLD + "Bow";
/*     */         } 
/*     */       }
/* 127 */       if (cfg.v.itemAlerts_mobs) {
/* 128 */         if (held.func_77973_b() == Items.field_151126_ay && !held.func_77962_s()) {
/* 129 */           itemName = EnumChatFormatting.WHITE + "Bedbug";
/* 130 */         } else if (held.func_77973_b() == Items.field_151063_bx && held.func_77960_j() != 93) {
/* 131 */           itemName = EnumChatFormatting.WHITE + "Iron Golem";
/*     */         } 
/*     */       }
/* 134 */       if (cfg.v.itemAlerts_rotationItems) {
/* 135 */         if (held.func_77973_b() == Items.field_151156_bN) {
/* 136 */           itemName = EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.UNDERLINE + "Shuriken";
/* 137 */         } else if (held.func_77973_b() == Item.func_150898_a(Blocks.field_150432_aD) && held.func_77962_s()) {
/* 138 */           itemName = EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.UNDERLINE + "Ice Bridge";
/* 139 */         } else if (held.func_77973_b() == Items.field_151145_ak) {
/* 140 */           itemName = EnumChatFormatting.DARK_BLUE.toString() + EnumChatFormatting.UNDERLINE + "Bridge Zapper";
/* 141 */         } else if (held.func_77973_b() == Items.field_151136_bY) {
/* 142 */           itemName = EnumChatFormatting.GOLD.toString() + EnumChatFormatting.UNDERLINE + "Charlie the Unicorn";
/* 143 */         } else if (held.func_77973_b() == Items.field_151079_bi && held.func_77962_s()) {
/* 144 */           itemName = EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.UNDERLINE + "Time Warp Pearl";
/* 145 */         } else if (held.func_77973_b() == Items.field_151106_aX) {
/* 146 */           itemName = EnumChatFormatting.AQUA.toString() + EnumChatFormatting.UNDERLINE + "Sugar Cookie";
/* 147 */         } else if (held.func_77973_b() == Item.func_150898_a(Blocks.field_150321_G)) {
/* 148 */           itemName = EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE + "Cobweb";
/* 149 */         } else if (held.func_77973_b() == Item.func_150898_a(Blocks.field_150429_aA)) {
/* 150 */           itemName = EnumChatFormatting.DARK_GREEN.toString() + EnumChatFormatting.UNDERLINE + "Teleportation Device";
/* 151 */         } else if (held.func_77973_b() == Item.func_150898_a(Blocks.field_150335_W) && held.func_77962_s()) {
/* 152 */           itemName = EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.UNDERLINE + "Mega " + EnumChatFormatting.RED + "T" + EnumChatFormatting.WHITE + "N" + EnumChatFormatting.RED + "T";
/* 153 */         } else if (held.func_77973_b() == Items.field_151051_r) {
/* 154 */           itemName = EnumChatFormatting.GOLD.toString() + EnumChatFormatting.UNDERLINE + "Mace";
/* 155 */         } else if (held.func_77973_b() == Items.field_151126_ay && held.func_77962_s()) {
/* 156 */           itemName = EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE + "Wind Charge";
/* 157 */         } else if (held.func_77973_b() == Items.field_151097_aZ && held.func_77962_s()) {
/* 158 */           itemName = EnumChatFormatting.GREEN.toString() + EnumChatFormatting.UNDERLINE + "Enchanted Shears";
/* 159 */         } else if (held.func_77973_b() == Item.func_150898_a((Block)Blocks.field_150461_bJ) && !held.func_77962_s()) {
/* 160 */           itemName = EnumChatFormatting.RED.toString() + EnumChatFormatting.UNDERLINE + "Final Revive Beacon";
/* 161 */         } else if (held.func_77973_b() == Items.field_179562_cC) {
/* 162 */           itemName = EnumChatFormatting.AQUA.toString() + EnumChatFormatting.UNDERLINE + "Block Zapper";
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 167 */       if (itemName != null)
/*     */       {
/* 169 */         if (!hasCooldown(player.func_70005_c_(), itemName)) {
/* 170 */           alert(player, itemName);
/* 171 */           setCooldown(player.func_70005_c_(), itemName);
/*     */         }  } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void alert(EntityPlayer player, String itemName) {
/* 177 */     float distanceToEntity = player.func_70032_d((Entity)this.mc.field_71439_g);
/* 178 */     DecimalFormat df = new DecimalFormat("#");
/* 179 */     String distanceFormatted = df.format(distanceToEntity);
/* 180 */     String distance = EnumChatFormatting.GRAY + " (" + EnumChatFormatting.AQUA + distanceFormatted + "m" + EnumChatFormatting.GRAY + ")";
/* 181 */     String rawItemName = StripFormattingCodes.stripFormattingCodes(itemName).toLowerCase();
/* 182 */     String showDistance = cfg.v.itemAlertsDistanceMode.equals("All") ? distance : (((cfg.v.itemAlertsDistanceMode.equals("Important") && (rawItemName.equalsIgnoreCase("fireball") || rawItemName.equalsIgnoreCase("ender pearl") || rawItemName.equalsIgnoreCase("bridge egg") || rawItemName.equalsIgnoreCase("speed potion") || rawItemName.equalsIgnoreCase("jump potion"))) || rawItemName.equalsIgnoreCase("invis potion") || rawItemName.equalsIgnoreCase("charlie the unicorn")) ? distance : "");
/*     */     
/* 184 */     if (!hasCooldown(player.func_70005_c_(), itemName)) {
/* 185 */       Meowtils.addMessage(NameUtil.getTabDisplayName(player.func_70005_c_()) + EnumChatFormatting.GRAY + " has " + itemName + showDistance);
/* 186 */       setCooldown(player.func_70005_c_(), itemName);
/*     */     } 
/*     */     
/* 189 */     if (cfg.v.notify && cfg.v.notifyItemAlerts) {
/* 190 */       NotifyUtil.notifyParty(player.func_70005_c_() + " has " + ColorUtil.unformattedText(itemName));
/*     */     }
/*     */     
/* 193 */     if (cfg.v.itemAlertsSoundMode.equals("All")) {
/* 194 */       sound();
/* 195 */     } else if ((cfg.v.itemAlertsSoundMode.equals("Important") && rawItemName.equalsIgnoreCase("jump potion")) || rawItemName.equalsIgnoreCase("speed potion") || rawItemName.equalsIgnoreCase("invis potion") || rawItemName.equalsIgnoreCase("bridge egg") || rawItemName.equalsIgnoreCase("diamond sword") || rawItemName.equalsIgnoreCase("iron golem") || rawItemName.equalsIgnoreCase("bedbug") || rawItemName.equalsIgnoreCase("charlie the unicorn") || rawItemName.equalsIgnoreCase("diamond pickaxe") || rawItemName.equalsIgnoreCase("enchanted bow") || rawItemName.equalsIgnoreCase("milk") || rawItemName.equalsIgnoreCase("ender pearl")) {
/* 196 */       sound();
/*     */     } 
/*     */   }
/*     */   private boolean hasCooldown(String playerName, String itemName) {
/* 200 */     long ALERT_COOLDOWN = cfg.v.itemAlertsCooldown * 1000L;
/* 201 */     Map<String, Long> playerCooldowns = cooldowns.get(playerName);
/* 202 */     if (playerCooldowns == null) return false;
/*     */     
/* 204 */     Long lastTime = playerCooldowns.get(itemName);
/* 205 */     return (lastTime != null && System.currentTimeMillis() - lastTime.longValue() < ALERT_COOLDOWN);
/*     */   }
/*     */   
/*     */   private void setCooldown(String playerName, String itemName) {
/* 209 */     ((Map<String, Long>)cooldowns.computeIfAbsent(playerName, k -> new HashMap<>())).put(itemName, Long.valueOf(System.currentTimeMillis()));
/*     */   }
/*     */   
/*     */   private void sound() {
/* 213 */     PlaySound.getInstance().playPingSound();
/*     */   }
/*     */   public static void clear() {
/* 216 */     cooldowns.clear();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/bedwars/ItemAlerts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */