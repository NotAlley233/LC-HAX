/*     */ package wtf.tatp.meowtils.modules.bedwars;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.ColorComponent;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*     */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.gui.values.SaturationValue;
/*     */ import wtf.tatp.meowtils.modules.advanced.AntiBot;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.NameUtil;
/*     */ import wtf.tatp.meowtils.util.NotifyUtil;
/*     */ import wtf.tatp.meowtils.util.PlaySound;
/*     */ import wtf.tatp.meowtils.util.TeamUtil;
/*     */ 
/*     */ 
/*     */ public class BedTracker
/*     */   extends Module
/*     */ {
/*     */   private ColorValue rgb;
/*     */   private SaturationValue saturation;
/*     */   private BrightnessValue brightness;
/*     */   private ColorComponent color;
/*  40 */   private static BlockPos bedPos = null; private NumberValue scale; private NumberValue cooldown; private NumberValue maxDistance; private BooleanValue sound; private BooleanValue hud;
/*  41 */   private static long bedScanTime = 0L;
/*  42 */   private static long startTime = 0L;
/*     */   
/*     */   private static boolean rangeAlert = false;
/*  45 */   private static final Map<UUID, Long> lastAlertTimes = new HashMap<>();
/*     */   
/*     */   public BedTracker() {
/*  48 */     super("BedTracker", "bedTrackerKey", "bedTracker", Module.Category.Bedwars);
/*  49 */     tooltip("Alerts when enemy players are near your bed and shows distance to your bed on screen.\nShows warning icon after distance if bed is out of render.");
/*  50 */     this.color = new ColorComponent("bedTracker_red", "bedTracker_green", "bedTracker_blue");
/*  51 */     addColor(this.rgb = new ColorValue("Text color", this.color));
/*  52 */     addSaturation(this.saturation = new SaturationValue(this.color));
/*  53 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/*  54 */     addValue(this.scale = new NumberValue("Scale", 0.5D, 1.5D, 0.05D, null, "bedTrackerScale", float.class));
/*  55 */     addValue(this.cooldown = new NumberValue("Alert frequency", 5.0D, 30.0D, 1.0D, "s", "bedTrackerAlertCooldown", int.class));
/*  56 */     addValue(this.maxDistance = new NumberValue("Max distance", 10.0D, 100.0D, 5.0D, "m", "bedTrackerMaxDistance", int.class));
/*  57 */     addBoolean(this.sound = new BooleanValue("Ping sound", "bedTrackerSound"));
/*  58 */     addBoolean(this.hud = new BooleanValue("Show HUD", "bedTrackerHUD"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/*  63 */     String msg = event.message.func_150260_c();
/*  64 */     if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     if (!msg.contains(":")) {
/*  72 */       if (msg.contains("The game starts in 1 second!")) {
/*  73 */         bedPos = null;
/*  74 */         bedScanTime = System.currentTimeMillis() + 6000L;
/*  75 */         startTime = System.currentTimeMillis() + 7000L;
/*  76 */       } else if (msg.contains("You will respawn because you still have a bed!")) {
/*  77 */         bedPos = null;
/*  78 */         bedScanTime = System.currentTimeMillis() + 12000L;
/*  79 */         startTime = System.currentTimeMillis() + 13000L;
/*     */       } 
/*  81 */       Meowtils.debugMessage(EnumChatFormatting.YELLOW + "[BedTracker]" + EnumChatFormatting.WHITE + "Locating bed..");
/*     */     } 
/*     */     
/*  84 */     if (msg.startsWith("BED DESTRUCTION > Your Bed")) {
/*  85 */       bedPos = null;
/*  86 */       Meowtils.addMessage(EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.BOLD + "⚠ Your bed was destroyed!");
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  92 */     if (event.phase != TickEvent.Phase.END || this.mc.field_71439_g == null || this.mc.field_71441_e == null)
/*  93 */       return;  if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*     */       return; 
/*  95 */     if (bedPos == null && bedScanTime > 0L && System.currentTimeMillis() > bedScanTime) {
/*  96 */       bedPos = findNearbyBed((World)this.mc.field_71441_e, this.mc.field_71439_g.func_180425_c(), 25);
/*  97 */       bedScanTime = 0L;
/*     */       
/*  99 */       if (bedPos != null) {
/* 100 */         Meowtils.addMessage(EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "✓ " + EnumChatFormatting.RESET + "Whitelisted your bed at" + EnumChatFormatting.GRAY + " (" + EnumChatFormatting.GREEN + bedPos.func_177958_n() + ", " + bedPos.func_177956_o() + ", " + bedPos.func_177952_p() + EnumChatFormatting.GRAY + ")");
/*     */       } else {
/* 102 */         Meowtils.addMessage(EnumChatFormatting.RED + "⚠ Error locating your bed.");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 107 */     if (bedPos != null && isBedOutOfRange() && !rangeAlert) {
/* 108 */       Meowtils.addMessage(EnumChatFormatting.BOLD.toString() + EnumChatFormatting.LIGHT_PURPLE + "⚠ Your bed is out of range!");
/* 109 */       rangeAlert = true;
/* 110 */     } else if (!isBedOutOfRange() && bedPos != null) {
/* 111 */       rangeAlert = false;
/*     */     } 
/*     */ 
/*     */     
/* 115 */     if (bedPos != null && this.mc.field_71441_e != null && !TeamUtil.inSpectator()) {
/* 116 */       long currentTime = System.currentTimeMillis();
/* 117 */       if (currentTime - startTime < 6000L)
/*     */         return; 
/* 119 */       for (EntityPlayer player : this.mc.field_71441_e.field_73010_i) {
/*     */         
/* 121 */         if (player == this.mc.field_71439_g || 
/* 122 */           AntiBot.isBot(player) || 
/* 123 */           TeamUtil.ignoreTeam(player.func_70005_c_())) {
/*     */           continue;
/*     */         }
/* 126 */         if (player.field_71075_bZ.field_75100_b || 
/* 127 */           player.field_70173_aa < 100)
/*     */           continue; 
/* 129 */         int distanceToBed = (int)player.func_70011_f(bedPos.func_177958_n(), bedPos.func_177956_o(), bedPos.func_177952_p());
/* 130 */         if (distanceToBed > cfg.v.bedTrackerMaxDistance)
/*     */           continue; 
/* 132 */         UUID uuid = player.func_110124_au();
/* 133 */         long lastAlert = ((Long)lastAlertTimes.getOrDefault(uuid, Long.valueOf(0L))).longValue();
/*     */ 
/*     */         
/* 136 */         if (currentTime - lastAlert >= cfg.v.bedTrackerAlertCooldown * 1000L) {
/* 137 */           String distanceColor = (distanceToBed <= 5) ? EnumChatFormatting.DARK_RED.toString() : ((distanceToBed <= 15) ? EnumChatFormatting.RED.toString() : ((distanceToBed <= 30) ? EnumChatFormatting.GOLD.toString() : ((distanceToBed <= 40) ? EnumChatFormatting.YELLOW.toString() : EnumChatFormatting.GREEN.toString())));
/*     */           
/* 139 */           Meowtils.addMessage(NameUtil.getTabDisplayName(player.func_70005_c_()) + EnumChatFormatting.WHITE + " is " + distanceColor + distanceToBed + EnumChatFormatting.WHITE + " blocks from your bed!" + distanceColor + " ⚠");
/*     */           
/* 141 */           if (cfg.v.notify && cfg.v.notifyBedTracker) {
/* 142 */             NotifyUtil.notifyParty(player.func_70005_c_() + " is " + distanceToBed + " blocks from your bed! ⚠");
/*     */           }
/*     */           
/* 145 */           lastAlertTimes.put(uuid, Long.valueOf(currentTime));
/* 146 */           if (cfg.v.bedTrackerSound) {
/* 147 */             PlaySound.getInstance().playMeowSound();
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 152 */       lastAlertTimes.keySet().removeIf(uuid -> (this.mc.field_71441_e.func_152378_a(uuid) == null));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/* 159 */     if (this.mc.field_71439_g == null || this.mc.field_71441_e == null || event.phase != TickEvent.Phase.END)
/* 160 */       return;  if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/* 161 */       return;  if (this.mc.field_71462_r != null)
/* 162 */       return;  if (!cfg.v.bedTrackerHUD)
/*     */       return; 
/* 164 */     float scale = cfg.v.bedTrackerScale;
/* 165 */     String cross = EnumChatFormatting.RED + "✗";
/* 166 */     String checkMark = EnumChatFormatting.GREEN + "✓";
/* 167 */     String separator = (bedPos != null) ? (EnumChatFormatting.GRAY + " | " + EnumChatFormatting.RESET) : "";
/* 168 */     int color = (new Color(cfg.v.bedTracker_red, cfg.v.bedTracker_green, cfg.v.bedTracker_blue)).getRGB();
/*     */     
/* 170 */     String bedState = (bedPos == null) ? cross : checkMark;
/* 171 */     String bed = "Bed: ";
/* 172 */     EnumChatFormatting distanceColor = (getDistanceToBed() < 70) ? EnumChatFormatting.GREEN : (isBedOutOfRange() ? EnumChatFormatting.RED : EnumChatFormatting.YELLOW);
/* 173 */     String distance = (bedPos == null) ? "" : ("Distance: " + distanceColor + getDistanceToBed());
/* 174 */     String doesChunkExist = (isBedOutOfRange() && bedPos != null) ? (EnumChatFormatting.RED + " ⚠") : "";
/* 175 */     String text = bed + bedState + separator + distance + doesChunkExist;
/*     */     
/* 177 */     if (!cfg.v.smoothFont) {
/* 178 */       GlStateManager.func_179094_E();
/* 179 */       GlStateManager.func_179152_a(scale, scale, scale);
/* 180 */       this.mc.field_71466_p.func_175063_a(text, cfg.v.bedTracker_x / scale, cfg.v.bedTracker_y / scale, color);
/* 181 */       GlStateManager.func_179121_F();
/*     */     } else {
/* 183 */       Meowtils.fontRenderer.drawScaledStringWithShadow(text, cfg.v.bedTracker_x, cfg.v.bedTracker_y, color, scale * 10.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private BlockPos findNearbyBed(World world, BlockPos center, int radius) {
/* 188 */     for (int x = center.func_177958_n() - radius; x <= center.func_177958_n() + radius; x++) {
/* 189 */       for (int y = center.func_177956_o() - radius; y <= center.func_177956_o() + radius; y++) {
/* 190 */         for (int z = center.func_177952_p() - radius; z <= center.func_177952_p() + radius; z++) {
/* 191 */           BlockPos pos = new BlockPos(x, y, z);
/* 192 */           if (world.func_180495_p(pos).func_177230_c() instanceof net.minecraft.block.BlockBed) {
/* 193 */             return pos;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 198 */     return null;
/*     */   }
/*     */   
/*     */   private int getDistanceToBed() {
/* 202 */     if (bedPos == null) return 0; 
/* 203 */     Vec3 playerPos = this.mc.field_71439_g.func_174791_d();
/* 204 */     return (int)Math.sqrt(playerPos.func_72436_e(new Vec3(bedPos.func_177958_n() + 0.5D, bedPos.func_177956_o(), bedPos.func_177952_p() + 0.5D)));
/*     */   }
/*     */   
/*     */   private boolean isBedOutOfRange() {
/* 208 */     if (bedPos == null || this.mc.field_71439_g == null || this.mc.field_71441_e == null) return true;
/*     */ 
/*     */     
/* 211 */     int chunkX = bedPos.func_177958_n() >> 4;
/* 212 */     int chunkZ = bedPos.func_177952_p() >> 4;
/* 213 */     boolean serverOutOfRange = !this.mc.field_71441_e.func_72863_F().func_73149_a(chunkX, chunkZ);
/*     */ 
/*     */     
/* 216 */     int renderDistanceBlocks = this.mc.field_71474_y.field_151451_c * 16;
/* 217 */     double dx = this.mc.field_71439_g.field_70165_t - bedPos.func_177958_n();
/* 218 */     double dz = this.mc.field_71439_g.field_70161_v - bedPos.func_177952_p();
/* 219 */     double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
/* 220 */     boolean clientOutOfRange = (horizontalDistance > renderDistanceBlocks);
/*     */ 
/*     */     
/* 223 */     return (serverOutOfRange || clientOutOfRange);
/*     */   }
/*     */   
/*     */   public static void clear() {
/* 227 */     bedPos = null;
/* 228 */     bedScanTime = 0L;
/* 229 */     startTime = 0L;
/* 230 */     lastAlertTimes.clear();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/bedwars/BedTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */