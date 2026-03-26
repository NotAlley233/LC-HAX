/*     */ package wtf.tatp.meowtils.modules.bedwars;
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.NameUtil;
/*     */ import wtf.tatp.meowtils.util.NotifyUtil;
/*     */ import wtf.tatp.meowtils.util.PlaySound;
/*     */ import wtf.tatp.meowtils.util.TeamUtil;
/*     */ 
/*     */ public class ConsumeAlerts extends Module {
/*  26 */   private static final Map<UUID, Long> lastAlertTime = new HashMap<>();
/*     */   private static final long ALERT_COOLDOWN = 1600L;
/*     */   private NumberValue distance;
/*     */   private BooleanValue showDistance;
/*     */   private BooleanValue pingSound;
/*     */   private BooleanValue detectApple;
/*     */   private BooleanValue detectMilk;
/*     */   private BooleanValue detectInvis;
/*     */   private BooleanValue detectSpeed;
/*     */   private BooleanValue detectJump;
/*  36 */   private static final Map<UUID, TrackedUse> usingItems = new HashMap<>();
/*     */   public ConsumeAlerts() {
/*  38 */     super("ConsumeAlerts", "consumeAlertsKey", "consumeAlerts", Module.Category.Bedwars);
/*  39 */     tooltip("Alerts you of players gaining specific potion effects.");
/*  40 */     addValue(this.distance = new NumberValue("Max distance", 0.0D, 250.0D, 5.0D, "m", "consumeAlerts_distance", int.class));
/*  41 */     addBoolean(this.pingSound = new BooleanValue("Ping sound", "consumeAlertsSound"));
/*  42 */     addBoolean(this.showDistance = new BooleanValue("Show distance", "consumeAlertsShowDistance"));
/*  43 */     addBoolean(this.detectApple = new BooleanValue("§6Golden Apple ", "consumeAlerts_goldenapple"));
/*  44 */     addBoolean(this.detectMilk = new BooleanValue("Milk", "consumeAlerts_milk"));
/*  45 */     addBoolean(this.detectSpeed = new BooleanValue("§eSpeed Potion", "consumeAlerts_speed"));
/*  46 */     addBoolean(this.detectJump = new BooleanValue("§aJump Potion", "consumeAlerts_jump"));
/*  47 */     addBoolean(this.detectInvis = new BooleanValue("§bInvis Potion", "consumeAlerts_invis"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTick(TickEvent.ClientTickEvent event) {
/*  52 */     if (event.phase != TickEvent.Phase.END || this.mc.field_71441_e == null)
/*  53 */       return;  if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*     */       return; 
/*  55 */     for (EntityPlayer player : this.mc.field_71441_e.field_73010_i) {
/*  56 */       if (player == this.mc.field_71439_g || 
/*  57 */         TeamUtil.ignoreTeam(player.func_70005_c_()))
/*  58 */         continue;  if (TeamUtil.inSpectator())
/*     */         return; 
/*  60 */       float maxDistance = cfg.v.consumeAlerts_distance;
/*  61 */       float distanceToPlayer = player.func_70032_d((Entity)this.mc.field_71439_g);
/*  62 */       if (maxDistance > 0.0F && distanceToPlayer > maxDistance)
/*     */         continue; 
/*  64 */       UUID uuid = player.func_110124_au();
/*  65 */       ItemStack heldItem = player.func_70694_bm();
/*  66 */       boolean isUsing = player.func_71039_bw();
/*     */       
/*  68 */       TrackedUse previous = usingItems.get(uuid);
/*     */       
/*  70 */       if (isUsing && heldItem != null && isTrackedItem(heldItem.func_77973_b())) {
/*  71 */         if (previous == null || !ItemStack.func_77989_b(heldItem, previous.item))
/*  72 */           usingItems.put(uuid, new TrackedUse(heldItem.func_77946_l(), (int)System.currentTimeMillis())); 
/*     */         continue;
/*     */       } 
/*  75 */       if (previous != null) {
/*  76 */         usingItems.remove(uuid);
/*     */         
/*  78 */         if (heldItem == null || !ItemStack.func_77989_b(heldItem, previous.item)) {
/*  79 */           alert(player, previous.item);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isTrackedItem(Item item) {
/*  87 */     return (item == Items.field_151153_ao || item == Items.field_151068_bn || item == Items.field_151117_aB);
/*     */   }
/*     */   
/*     */   private void alert(EntityPlayer player, ItemStack item) {
/*  91 */     UUID uuid = player.func_110124_au();
/*  92 */     long now = System.currentTimeMillis();
/*     */     
/*  94 */     Long lastAlert = lastAlertTime.getOrDefault(uuid, Long.valueOf(0L));
/*  95 */     if (now - lastAlert.longValue() < 1600L)
/*  96 */       return;  lastAlertTime.put(uuid, Long.valueOf(now));
/*     */     
/*  98 */     String name = NameUtil.getTabDisplayName(player.func_70005_c_());
/*  99 */     float distanceToEntity = player.func_70032_d((Entity)this.mc.field_71439_g);
/* 100 */     DecimalFormat df = new DecimalFormat("#");
/* 101 */     String distanceStr = df.format(distanceToEntity);
/* 102 */     String distance = cfg.v.consumeAlertsShowDistance ? (EnumChatFormatting.GRAY + " (" + EnumChatFormatting.AQUA + distanceStr + "m" + EnumChatFormatting.GRAY + ")") : "";
/* 103 */     Item heldItem = item.func_77973_b();
/*     */     
/* 105 */     if (heldItem == Items.field_151153_ao && cfg.v.consumeAlerts_goldenapple) {
/* 106 */       Meowtils.addMessage(name + EnumChatFormatting.GRAY + " consumed " + EnumChatFormatting.GOLD + "Golden Apple" + distance);
/* 107 */       if (cfg.v.notify && cfg.v.notifyConsumeAlerts) NotifyUtil.notifyParty(player.func_70005_c_() + " consumed Golden Apple"); 
/* 108 */       ping();
/* 109 */     } else if (heldItem == Items.field_151117_aB && cfg.v.consumeAlerts_milk) {
/* 110 */       Meowtils.addMessage(name + EnumChatFormatting.GRAY + " consumed " + EnumChatFormatting.WHITE + "Milk" + distance);
/* 111 */       if (cfg.v.notify && cfg.v.notifyConsumeAlerts) NotifyUtil.notifyParty(player.func_70005_c_() + " consumed Milk"); 
/* 112 */       ping();
/* 113 */     } else if (heldItem == Items.field_151068_bn) {
/* 114 */       String lowerName = item.func_82833_r().toLowerCase();
/*     */       
/* 116 */       if (lowerName.contains("speed") && cfg.v.consumeAlerts_speed) {
/* 117 */         Meowtils.addMessage(name + EnumChatFormatting.GRAY + " consumed " + EnumChatFormatting.YELLOW + "Speed Potion" + distance);
/* 118 */         if (cfg.v.notify && cfg.v.notifyConsumeAlerts) NotifyUtil.notifyParty(player.func_70005_c_() + " consumed Speed Potion"); 
/* 119 */         ping();
/* 120 */       } else if (lowerName.contains("jump") && cfg.v.consumeAlerts_jump) {
/* 121 */         Meowtils.addMessage(name + EnumChatFormatting.GRAY + " consumed " + EnumChatFormatting.GREEN + "Jump Potion" + distance);
/* 122 */         if (cfg.v.notify && cfg.v.notifyConsumeAlerts) NotifyUtil.notifyParty(player.func_70005_c_() + " consumed Jump Potion"); 
/* 123 */         ping();
/* 124 */       } else if (lowerName.contains("invis") && cfg.v.consumeAlerts_invis) {
/* 125 */         Meowtils.addMessage(name + EnumChatFormatting.GRAY + " consumed " + EnumChatFormatting.AQUA + "Invis Potion" + distance);
/* 126 */         if (cfg.v.notify && cfg.v.notifyConsumeAlerts) NotifyUtil.notifyParty(player.func_70005_c_() + " consumed Invis Potion"); 
/* 127 */         ping();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void ping() {
/* 132 */     if (cfg.v.consumeAlertsSound)
/* 133 */       PlaySound.getInstance().playPingSoundMedium(); 
/*     */   }
/*     */   
/*     */   private static class TrackedUse
/*     */   {
/*     */     final ItemStack item;
/*     */     final int startUseDuration;
/*     */     
/*     */     TrackedUse(ItemStack item, int useDuration) {
/* 142 */       this.item = item;
/* 143 */       this.startUseDuration = useDuration;
/*     */     } }
/*     */   
/*     */   public static void clear() {
/* 147 */     usingItems.clear();
/* 148 */     lastAlertTime.clear();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/bedwars/ConsumeAlerts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */