/*     */ package wtf.tatp.meowtils.modules.skywars;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityEnderPearl;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
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
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.Render;
/*     */ 
/*     */ public class TimeWarpDisplay extends Module {
/*     */   private BooleanValue others;
/*     */   private BooleanValue self;
/*     */   private NumberValue scale;
/*  36 */   private static float cooldown = 0.0F; private ColorValue rgb; private SaturationValue saturation; private BrightnessValue brightness; private ColorComponent color;
/*  37 */   private static int tickCounter = 0;
/*     */   
/*     */   private static boolean thrown = false;
/*  40 */   private final Minecraft mc = Minecraft.func_71410_x();
/*  41 */   private final Map<EntityEnderPearl, EntityPlayer> activePearls = new HashMap<>();
/*  42 */   private final List<BoxLocation> activeBoxes = new ArrayList<>();
/*     */   private static final long BOX_LIFETIME = 3000L;
/*     */   
/*     */   public TimeWarpDisplay() {
/*  46 */     super("TimeWarpDisplay", "timeWarpDisplayKey", "timeWarpDisplay", Module.Category.Skywars);
/*  47 */     tooltip("For others - Shows a box at the position a player might warp back to\nFor self - Shows a countdown on screen until you warp back");
/*  48 */     this.color = new ColorComponent("timeWarpDisplay_red", "timeWarpDisplay_green", "timeWarpDisplay_blue");
/*  49 */     addColor(this.rgb = new ColorValue("Color", this.color));
/*  50 */     addSaturation(this.saturation = new SaturationValue(this.color));
/*  51 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/*  52 */     addValue(this.scale = new NumberValue("Scale", 0.5D, 1.5D, 0.05D, null, "timeWarpDisplayScale", float.class));
/*  53 */     addBoolean(this.self = new BooleanValue("For self", "timeWarpDisplayForSelf"));
/*  54 */     addBoolean(this.others = new BooleanValue("For others", "timeWarpDisplayForOthers"));
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/*  60 */     if (this.mc.field_71441_e == null || this.mc.field_71439_g == null || event.phase != TickEvent.Phase.END)
/*  61 */       return;  if (this.mc.field_71462_r != null)
/*  62 */       return;  if (!GamemodeUtil.skywarsGame)
/*  63 */       return;  if (!thrown || !cfg.v.timeWarpDisplayForSelf)
/*     */       return; 
/*  65 */     float scale = cfg.v.timeWarpDisplayScale;
/*  66 */     int color = (new Color(cfg.v.timeWarpDisplay_red, cfg.v.timeWarpDisplay_green, cfg.v.timeWarpDisplay_blue)).getRGB();
/*     */     
/*  68 */     EnumChatFormatting cooldownColor = (cooldown >= 2.5D) ? EnumChatFormatting.DARK_GREEN : ((cooldown >= 2.0F) ? EnumChatFormatting.GREEN : ((cooldown >= 1.5D) ? EnumChatFormatting.YELLOW : ((cooldown >= 1.0F) ? EnumChatFormatting.GOLD : ((cooldown >= 0.5D) ? EnumChatFormatting.RED : EnumChatFormatting.DARK_RED))));
/*     */     
/*  70 */     if (!cfg.v.smoothFont) {
/*  71 */       GlStateManager.func_179094_E();
/*  72 */       GlStateManager.func_179152_a(scale, scale, scale);
/*  73 */       this.mc.field_71466_p.func_175063_a("Time Warp: " + cooldownColor + String.format("%.1f", new Object[] { Float.valueOf(cooldown) }).replace(",", "."), cfg.v.timeWarpDisplay_x / scale, cfg.v.timeWarpDisplay_y / scale, color);
/*  74 */       GlStateManager.func_179121_F();
/*     */     } else {
/*  76 */       Meowtils.fontRenderer.drawScaledStringWithShadow("Time Warp: " + cooldownColor + String.format("%.1f", new Object[] { Float.valueOf(cooldown) }).replace(",", "."), cfg.v.timeWarpDisplay_x, cfg.v.timeWarpDisplay_y, color, scale * 10.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTick(TickEvent.ClientTickEvent event) {
/*  82 */     if (event.phase != TickEvent.Phase.END || this.mc.field_71441_e == null)
/*     */       return; 
/*  84 */     if (thrown && cooldown > 0.0F) {
/*  85 */       tickCounter++;
/*  86 */       if (tickCounter >= 2) {
/*  87 */         cooldown -= 0.1F;
/*  88 */         tickCounter = 0;
/*     */         
/*  90 */         if (cooldown <= 0.0F) {
/*  91 */           cooldown = 0.0F;
/*  92 */           thrown = false;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  97 */     for (Entity e : this.mc.field_71441_e.field_72996_f) {
/*  98 */       if (e instanceof EntityEnderPearl && !this.activePearls.containsKey(e)) {
/*  99 */         EntityEnderPearl pearl = (EntityEnderPearl)e;
/* 100 */         EntityPlayer nearest = findClosestPlayer(pearl);
/* 101 */         if (nearest != null) {
/* 102 */           this.activePearls.put(pearl, nearest);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     Iterator<Map.Entry<EntityEnderPearl, EntityPlayer>> iterator = this.activePearls.entrySet().iterator();
/* 108 */     while (iterator.hasNext()) {
/* 109 */       Map.Entry<EntityEnderPearl, EntityPlayer> entry = iterator.next();
/* 110 */       EntityEnderPearl pearl = entry.getKey();
/* 111 */       EntityPlayer owner = entry.getValue();
/*     */       
/* 113 */       if (pearl.field_70128_L || !this.mc.field_71441_e.field_72996_f.contains(pearl)) {
/*     */         
/* 115 */         this.activeBoxes.add(new BoxLocation(owner.field_70165_t, owner.field_70163_u, owner.field_70161_v, System.currentTimeMillis()));
/* 116 */         iterator.remove();
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     this.activeBoxes.removeIf(box -> (System.currentTimeMillis() - box.spawnTime > 3000L));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 125 */     String msg = event.message.func_150260_c();
/*     */     
/* 127 */     if (msg.equals("You will be warped back in 3 seconds!")) {
/* 128 */       thrown = true;
/* 129 */       cooldown = 3.0F;
/* 130 */       tickCounter = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderWorld(RenderWorldLastEvent event) {
/* 137 */     if (!GamemodeUtil.skywarsGame)
/* 138 */       return;  if (!cfg.v.timeWarpDisplayForOthers)
/* 139 */       return;  for (BoxLocation box : this.activeBoxes) {
/*     */       
/* 141 */       Color color = new Color(cfg.v.timeWarpDisplay_red, cfg.v.timeWarpDisplay_green, cfg.v.timeWarpDisplay_blue, 150);
/*     */       
/* 143 */       double width = 0.6D;
/* 144 */       double height = 1.8D;
/*     */       
/* 146 */       double halfWidth = width / 2.0D;
/*     */       
/* 148 */       AxisAlignedBB bb = new AxisAlignedBB(box.x - halfWidth, box.y, box.z - halfWidth, box.x + halfWidth, box.y + height, box.z + halfWidth);
/*     */       
/* 150 */       Render.draw3DBlockBox(bb, true, color, false, color, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   private EntityPlayer findClosestPlayer(EntityEnderPearl pearl) {
/* 155 */     EntityPlayer closest = null;
/* 156 */     double bestDist = Double.MAX_VALUE;
/*     */     
/* 158 */     for (EntityPlayer p : this.mc.field_71441_e.field_73010_i) {
/* 159 */       double dist = p.func_70068_e((Entity)pearl);
/* 160 */       if (dist < bestDist) {
/* 161 */         bestDist = dist;
/* 162 */         closest = p;
/*     */       } 
/*     */     } 
/* 165 */     return closest;
/*     */   }
/*     */   private static class BoxLocation { public final double x;
/*     */     public final double y;
/*     */     public final double z;
/*     */     public final long spawnTime;
/*     */     
/*     */     public BoxLocation(double x, double y, double z, long spawnTime) {
/* 173 */       this.x = x;
/* 174 */       this.y = y;
/* 175 */       this.z = z;
/* 176 */       this.spawnTime = spawnTime;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/skywars/TimeWarpDisplay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */