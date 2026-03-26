/*     */ package wtf.tatp.meowtils.modules.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.Timer;
/*     */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.modules.advanced.AntiBot;
/*     */ 
/*     */ 
/*     */ public class HealthInfo
/*     */   extends Module
/*     */ {
/*     */   private BooleanValue display;
/*     */   private NumberValue displayScale;
/*     */   private BooleanValue indicator;
/*     */   private NumberValue indicatorScale;
/*     */   private BooleanValue esp;
/*     */   private BooleanValue espWalls;
/*  36 */   private float indicatorHealth = 0.0F;
/*  37 */   private long lastIndicator = 0L;
/*     */   
/*  39 */   private String espMode = "Raven";
/*     */   
/*     */   private Field timerField;
/*     */   
/*     */   public HealthInfo() {
/*  44 */     super("HealthInfo", "healthInfoKey", "healthInfo", Module.Category.Render);
/*  45 */     tooltip("Displays various health information.");
/*  46 */     addValue(this.displayScale = new NumberValue("Display scale", 0.5D, 1.5D, 0.05D, null, "healthInfoDisplayScale", float.class));
/*  47 */     addValue(this.indicatorScale = new NumberValue("Indicator scale", 0.5D, 5.0D, 0.05D, null, "healthInfoIndicatorScale", float.class));
/*  48 */     addBoolean(this.display = new BooleanValue("Show own health", "healthInfoDisplay"));
/*  49 */     addBoolean(this.indicator = new BooleanValue("Show damage indicators", "healthInfoIndicator"));
/*  50 */     addBoolean(this.esp = new BooleanValue("Show healthbars", "healthESP"));
/*  51 */     addBoolean(this.espWalls = new BooleanValue("§7^ Healthbar through walls", "healthESPThroughWalls"));
/*     */ 
/*     */     
/*  54 */     initializeTimerField();
/*     */   }
/*     */ 
/*     */   
/*     */   private void initializeTimerField() {
/*  59 */     if (this.timerField != null)
/*     */       return;  try {
/*  61 */       this.timerField = Minecraft.class.getDeclaredField("field_71428_T");
/*  62 */       this.timerField.setAccessible(true);
/*  63 */     } catch (Exception ex) {
/*  64 */       ex.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/*  70 */     Minecraft mc = Minecraft.func_71410_x();
/*  71 */     if (mc.field_71441_e == null || mc.field_71439_g == null || event.phase != TickEvent.Phase.END)
/*  72 */       return;  if (mc.field_71462_r != null) {
/*     */       return;
/*     */     }
/*  75 */     if (cfg.v.healthInfoDisplay) {
/*  76 */       int hearts = (int)mc.field_71439_g.func_110143_aJ();
/*  77 */       int absorptionHearts = (int)mc.field_71439_g.func_110139_bj();
/*  78 */       boolean isAbsorptionActive = mc.field_71439_g.func_70644_a(Potion.field_76444_x);
/*  79 */       int health = isAbsorptionActive ? (hearts + absorptionHearts) : hearts;
/*  80 */       float scale = cfg.v.healthInfoDisplayScale;
/*     */       
/*  82 */       String heart = (health < 4) ? "❣" : "❤";
/*  83 */       EnumChatFormatting healthColor = (health < 3) ? EnumChatFormatting.DARK_RED : ((health < 6) ? EnumChatFormatting.RED : ((health < 10) ? EnumChatFormatting.YELLOW : ((health < 15) ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_GREEN)));
/*  84 */       EnumChatFormatting heartColor = isAbsorptionActive ? EnumChatFormatting.GOLD : EnumChatFormatting.RED;
/*     */       
/*  86 */       String text = healthColor + String.valueOf(health) + heartColor + heart;
/*     */       
/*  88 */       if (!cfg.v.smoothFont) {
/*  89 */         GlStateManager.func_179094_E();
/*  90 */         GlStateManager.func_179152_a(scale, scale, scale);
/*  91 */         mc.field_71466_p.func_175063_a(text, cfg.v.healthInfoDisplay_x / scale, cfg.v.healthInfoDisplay_y / scale, -1);
/*  92 */         GlStateManager.func_179121_F();
/*     */       } else {
/*  94 */         Meowtils.fontRenderer.drawScaledStringWithShadow(text, cfg.v.healthInfoDisplay_x, cfg.v.healthInfoDisplay_y, -1, scale * 10.0F);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  99 */     if (cfg.v.healthInfoIndicator && System.currentTimeMillis() - this.lastIndicator <= 2000L) {
/* 100 */       float scale = cfg.v.healthInfoIndicatorScale;
/* 101 */       EnumChatFormatting healthColor = (this.indicatorHealth < 3.0F) ? EnumChatFormatting.DARK_RED : ((this.indicatorHealth < 6.0F) ? EnumChatFormatting.RED : ((this.indicatorHealth < 10.0F) ? EnumChatFormatting.YELLOW : ((this.indicatorHealth < 15.0F) ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_GREEN)));
/* 102 */       String text = healthColor + String.valueOf(this.indicatorHealth) + " HP";
/*     */       
/* 104 */       if (!cfg.v.smoothFont) {
/* 105 */         GlStateManager.func_179094_E();
/* 106 */         GlStateManager.func_179152_a(scale, scale, scale);
/* 107 */         mc.field_71466_p.func_175063_a(text, cfg.v.healthInfoIndicator_x / scale, cfg.v.healthInfoIndicator_y / scale, -1);
/* 108 */         GlStateManager.func_179121_F();
/*     */       } else {
/* 110 */         Meowtils.fontRenderer.drawScaledStringWithShadow(text, cfg.v.healthInfoIndicator_x, cfg.v.healthInfoIndicator_y, -1, scale * 10.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 118 */     if (!cfg.v.healthInfoIndicator)
/* 119 */       return;  String msg = event.message.func_150260_c();
/*     */     
/* 121 */     Pattern pattern = Pattern.compile("is on ([0-9]+(?:\\.[0-9]+)?) HP!");
/* 122 */     Matcher matcher = pattern.matcher(msg);
/*     */     
/* 124 */     if (matcher.find() && !msg.contains(":")) {
/* 125 */       String indicatorString = matcher.group(1);
/* 126 */       this.indicatorHealth = Float.parseFloat(indicatorString);
/* 127 */       this.lastIndicator = System.currentTimeMillis();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderWorldLast(RenderWorldLastEvent event) {
/* 134 */     if (!isWorldReady())
/* 135 */       return;  if (!cfg.v.healthESP)
/*     */       return; 
/* 137 */     for (EntityPlayer player : this.mc.field_71441_e.field_73010_i) {
/* 138 */       if ((!cfg.v.antiBot || !AntiBot.isBot(player)) && 
/* 139 */         player != this.mc.field_71439_g && player.field_70725_aQ == 0) {
/* 140 */         renderHealthBar(player);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderHealthBar(EntityPlayer player) {
/* 146 */     double x = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * getRenderPartialTicks() - (this.mc.func_175598_ae()).field_78730_l;
/* 147 */     double y = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * getRenderPartialTicks() - (this.mc.func_175598_ae()).field_78731_m;
/* 148 */     double z = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * getRenderPartialTicks() - (this.mc.func_175598_ae()).field_78728_n;
/* 149 */     double healthRatio = (player.func_110143_aJ() / player.func_110138_aP());
/* 150 */     int barHeight = (int)(74.0D * healthRatio);
/* 151 */     barHeight = Math.max(0, Math.min(barHeight, 74));
/* 152 */     int ravenBarColor = (healthRatio < 0.3D) ? Color.red.getRGB() : ((healthRatio < 0.5D) ? Color.orange.getRGB() : ((healthRatio < 0.7D) ? Color.yellow.getRGB() : Color.green.getRGB()));
/*     */     
/* 154 */     if (this.espMode.equals("Raven")) {
/* 155 */       boolean depthEnabled = GL11.glIsEnabled(2929);
/* 156 */       GlStateManager.func_179094_E();
/*     */       
/* 158 */       GL11.glTranslated(x, y - 0.2D, z);
/* 159 */       GL11.glRotatef(-(this.mc.func_175598_ae()).field_78735_i, 0.0F, 1.0F, 0.0F);
/*     */       
/* 161 */       if (cfg.v.healthESPThroughWalls) {
/* 162 */         GlStateManager.func_179097_i();
/*     */       }
/*     */       
/* 165 */       GL11.glScalef(0.03F, 0.03F, 0.03F);
/*     */       
/* 167 */       Gui.func_73734_a(21, -1, 25, 75, Color.black.getRGB());
/* 168 */       Gui.func_73734_a(22, barHeight, 24, 74, Color.darkGray.getRGB());
/* 169 */       Gui.func_73734_a(22, 0, 24, barHeight, ravenBarColor);
/*     */       
/* 171 */       if (depthEnabled) {
/* 172 */         GlStateManager.func_179126_j();
/*     */       } else {
/* 174 */         GlStateManager.func_179097_i();
/*     */       } 
/*     */       
/* 177 */       GlStateManager.func_179121_F();
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
/*     */ 
/*     */   
/*     */   private float getRenderPartialTicks() {
/* 248 */     if (this.timerField == null) initializeTimerField(); 
/* 249 */     if (this.timerField != null) {
/*     */       try {
/* 251 */         Timer timer = (Timer)this.timerField.get(this.mc);
/* 252 */         return timer.field_74281_c;
/* 253 */       } catch (IllegalAccessException ex) {
/* 254 */         ex.printStackTrace();
/*     */       } 
/*     */     }
/* 257 */     return 0.0F;
/*     */   }
/*     */   
/*     */   private boolean isWorldReady() {
/* 261 */     return (this.mc.field_71439_g != null && this.mc.field_71441_e != null);
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/render/HealthInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */