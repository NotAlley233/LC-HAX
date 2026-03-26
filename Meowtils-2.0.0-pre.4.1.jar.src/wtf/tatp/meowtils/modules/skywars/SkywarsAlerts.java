/*     */ package wtf.tatp.meowtils.modules.skywars;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
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
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.NameUtil;
/*     */ import wtf.tatp.meowtils.util.PlaySound;
/*     */ import wtf.tatp.meowtils.util.StripFormattingCodes;
/*     */ 
/*     */ public class SkywarsAlerts
/*     */   extends Module {
/*     */   private ArrayValue soundMode;
/*     */   private BooleanValue diamondSword;
/*     */   private BooleanValue enderPearls;
/*     */   private BooleanValue fireAspectSword;
/*     */   private BooleanValue knockbackItems;
/*     */   private NumberValue cooldown;
/*     */   private BooleanValue showDistance;
/*  35 */   private final Minecraft mc = Minecraft.func_71410_x();
/*  36 */   private static final Map<String, Map<String, Long>> cooldowns = new HashMap<>();
/*     */   
/*     */   public SkywarsAlerts() {
/*  39 */     super("SkywarsAlerts", "skywarsAlertsKey", "skywarsAlerts", Module.Category.Skywars);
/*  40 */     tooltip("Alerts you of items players have in Skywars.");
/*  41 */     addValue(this.cooldown = new NumberValue("Cooldown", 1.0D, 30.0D, 1.0D, "s", "skywarsAlertsCooldown", int.class));
/*  42 */     addArray(this.soundMode = new ArrayValue("Ping sound", Arrays.asList(new String[] { "All", "Important", "None" }, ), "skywarsAlertsSoundMode"));
/*  43 */     addBoolean(this.showDistance = new BooleanValue("Show distance", "skywarsAlertsDistance"));
/*  44 */     addBoolean(this.enderPearls = new BooleanValue("§5Ender Pearls", "skywarsAlerts_EnderPearls"));
/*  45 */     addBoolean(this.diamondSword = new BooleanValue("§bDiamond Sword", "skywarsAlerts_DiamondSword"));
/*  46 */     addBoolean(this.fireAspectSword = new BooleanValue("§cFire Sword", "skywarsAlerts_FireAspectSword"));
/*  47 */     addBoolean(this.knockbackItems = new BooleanValue("§eKnockback Items", "skywarsAlerts_KnockbackItems"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  52 */     if (event.phase != TickEvent.Phase.END)
/*  53 */       return;  if (this.mc.field_71439_g == null || this.mc.field_71441_e == null)
/*  54 */       return;  if (!GamemodeUtil.skywarsGame)
/*     */       return; 
/*  56 */     for (EntityPlayer player : this.mc.field_71441_e.field_73010_i) {
/*  57 */       if (player == null || 
/*  58 */         player == this.mc.field_71439_g)
/*  59 */         continue;  ItemStack held = player.func_70694_bm();
/*  60 */       String itemName = null;
/*  61 */       if (held == null)
/*     */         continue; 
/*  63 */       if (held.func_77973_b() == Items.field_151079_bi && held.func_77962_s() && cfg.v.skywarsAlerts_EnderPearls) {
/*  64 */         if (held.func_82840_a((EntityPlayer)this.mc.field_71439_g, false).stream().anyMatch(s -> s.contains("Teleport back"))) {
/*  65 */           itemName = EnumChatFormatting.LIGHT_PURPLE + "Time Warp Pearl";
/*     */         } else {
/*  67 */           itemName = EnumChatFormatting.DARK_PURPLE + "Corrupt Pearl";
/*     */         } 
/*  69 */       } else if (held.func_77973_b() == Items.field_151079_bi && cfg.v.skywarsAlerts_EnderPearls) {
/*  70 */         itemName = EnumChatFormatting.DARK_PURPLE + "Ender Pearl";
/*  71 */       } else if (held.func_77973_b() == Items.field_151048_u && cfg.v.skywarsAlerts_DiamondSword) {
/*  72 */         itemName = EnumChatFormatting.AQUA + "Diamond Sword";
/*  73 */       } else if (held.func_77973_b() == Items.field_151040_l && held.func_77962_s() && cfg.v.skywarsAlerts_FireAspectSword) {
/*  74 */         itemName = EnumChatFormatting.RED + "Fire Sword";
/*  75 */       } else if (held.func_77973_b() == Items.field_151112_aM && held.func_77962_s() && cfg.v.skywarsAlerts_KnockbackItems) {
/*  76 */         if (held.func_77986_q().func_74745_c() == 1) {
/*  77 */           itemName = EnumChatFormatting.GOLD + "Knockback Rod";
/*     */         }
/*  79 */       } else if (held.func_77973_b() == Items.field_151041_m && held.func_77962_s() && cfg.v.skywarsAlerts_KnockbackItems) {
/*  80 */         itemName = EnumChatFormatting.YELLOW + "Knockback Sword";
/*     */       } 
/*     */ 
/*     */       
/*  84 */       if (itemName != null)
/*     */       {
/*  86 */         if (!hasCooldown(player.func_70005_c_(), itemName)) {
/*  87 */           alert(player, itemName);
/*  88 */           setCooldown(player.func_70005_c_(), itemName);
/*     */         }  } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void alert(EntityPlayer player, String itemName) {
/*  94 */     float distanceToEntity = player.func_70032_d((Entity)this.mc.field_71439_g);
/*  95 */     DecimalFormat df = new DecimalFormat("#");
/*  96 */     String distance = df.format(distanceToEntity);
/*  97 */     String rawItemName = StripFormattingCodes.stripFormattingCodes(itemName).toLowerCase();
/*  98 */     String showDistance = cfg.v.skywarsAlertsDistance ? (EnumChatFormatting.GRAY + " (" + EnumChatFormatting.AQUA + distance + "m" + EnumChatFormatting.GRAY + ")") : "";
/*     */     
/* 100 */     if (!hasCooldown(player.func_70005_c_(), itemName)) {
/* 101 */       Meowtils.addMessage(NameUtil.getTabDisplayName(player.func_70005_c_()) + EnumChatFormatting.GRAY + " has " + itemName + showDistance);
/* 102 */       setCooldown(player.func_70005_c_(), itemName);
/*     */     } 
/*     */     
/* 105 */     if (cfg.v.skywarsAlertsSoundMode.equals("All")) {
/* 106 */       sound();
/* 107 */     } else if ((cfg.v.skywarsAlertsSoundMode.equals("Important") && rawItemName.equalsIgnoreCase("ender pearl")) || rawItemName.equalsIgnoreCase("diamond sword") || rawItemName.equalsIgnoreCase("knockback rod")) {
/* 108 */       sound();
/*     */     } 
/*     */   }
/*     */   private boolean hasCooldown(String playerName, String itemName) {
/* 112 */     long ALERT_COOLDOWN = cfg.v.skywarsAlertsCooldown * 1000L;
/* 113 */     Map<String, Long> playerCooldowns = cooldowns.get(playerName);
/* 114 */     if (playerCooldowns == null) return false;
/*     */     
/* 116 */     Long lastTime = playerCooldowns.get(itemName);
/* 117 */     return (lastTime != null && System.currentTimeMillis() - lastTime.longValue() < ALERT_COOLDOWN);
/*     */   }
/*     */   
/*     */   private void setCooldown(String playerName, String itemName) {
/* 121 */     ((Map<String, Long>)cooldowns.computeIfAbsent(playerName, k -> new HashMap<>())).put(itemName, Long.valueOf(System.currentTimeMillis()));
/*     */   }
/*     */   private void sound() {
/* 124 */     PlaySound.getInstance().playPingSound();
/*     */   }
/*     */   public static void clear() {
/* 127 */     cooldowns.clear();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/skywars/SkywarsAlerts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */