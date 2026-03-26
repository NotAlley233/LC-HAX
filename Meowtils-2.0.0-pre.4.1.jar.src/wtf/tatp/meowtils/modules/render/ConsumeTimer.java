/*     */ package wtf.tatp.meowtils.modules.render;
/*     */ import java.awt.Color;
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.ColorComponent;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*     */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.gui.values.SaturationValue;
/*     */ import wtf.tatp.meowtils.util.ColorUtil;
/*     */ 
/*     */ public class ConsumeTimer extends Module {
/*  22 */   private int useTicksLeft = -1; private boolean usingItem = false;
/*  23 */   private int lastUseDuration = 0;
/*  24 */   private int maxUseDuration = 32;
/*     */   private BooleanValue dynamic;
/*     */   private ColorValue rgb;
/*     */   private SaturationValue saturation;
/*     */   private BrightnessValue brightness;
/*     */   private ColorComponent color;
/*     */   private NumberValue scale;
/*     */   private ArrayValue mode;
/*     */   
/*     */   public ConsumeTimer() {
/*  34 */     super("ConsumeTimer", "consumeTimerKey", "consumeTimer", Module.Category.Render);
/*  35 */     tooltip("Displays a countdown of how much time left to consume an item.");
/*  36 */     this.color = new ColorComponent("consumeTimerRed", "consumeTimerGreen", "consumeTimerBlue");
/*  37 */     addColor(this.rgb = new ColorValue("Text color", this.color));
/*  38 */     addSaturation(this.saturation = new SaturationValue(this.color));
/*  39 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/*  40 */     addValue(this.scale = new NumberValue("Scale", 0.5D, 1.5D, 0.05D, null, "consumeTimerScale", float.class));
/*  41 */     addArray(this.mode = new ArrayValue("Mode", Arrays.asList(new String[] { "Ticks", "Seconds" }, ), "consumeTimerMode"));
/*  42 */     addBoolean(this.dynamic = new BooleanValue("Dynamic color", "consumeTimerDynamicColor"));
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onClientTick(TickEvent.ClientTickEvent event) {
/*  46 */     Minecraft mc = Minecraft.func_71410_x();
/*  47 */     if (event.phase != TickEvent.Phase.END)
/*  48 */       return;  if (mc.field_71439_g == null)
/*     */       return; 
/*  50 */     if (mc.field_71439_g.func_71039_bw()) {
/*  51 */       int duration = mc.field_71439_g.func_71057_bx();
/*  52 */       ItemStack item = mc.field_71439_g.func_71011_bu();
/*     */       
/*  54 */       if (item != null) {
/*  55 */         int maxDuration = item.func_77988_m();
/*     */         
/*  57 */         if (maxDuration <= 32) {
/*     */           
/*  59 */           if (!this.usingItem || duration < this.lastUseDuration) {
/*  60 */             this.usingItem = true;
/*  61 */             this.maxUseDuration = maxDuration;
/*  62 */             this.useTicksLeft = maxDuration - duration;
/*  63 */           } else if (this.usingItem) {
/*  64 */             this.useTicksLeft = this.maxUseDuration - duration;
/*     */           } 
/*  66 */           this.lastUseDuration = duration;
/*     */         } else {
/*  68 */           this.usingItem = false;
/*  69 */           this.useTicksLeft = -1;
/*  70 */           this.lastUseDuration = 0;
/*     */         } 
/*     */       } 
/*     */     } else {
/*  74 */       this.usingItem = false;
/*  75 */       this.useTicksLeft = -1;
/*  76 */       this.lastUseDuration = 0;
/*     */     } 
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/*  81 */     Minecraft mc = Minecraft.func_71410_x();
/*  82 */     if (mc.field_71441_e == null || mc.field_71439_g == null || event.phase != TickEvent.Phase.END)
/*  83 */       return;  float scale = cfg.v.consumeTimerScale;
/*  84 */     int r = cfg.v.consumeTimerRed;
/*  85 */     int g = cfg.v.consumeTimerGreen;
/*  86 */     int b = cfg.v.consumeTimerBlue;
/*  87 */     int color = !cfg.v.consumeTimerDynamicColor ? (new Color(r, g, b)).getRGB() : ((this.useTicksLeft < 6) ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.DARK_RED) : ((this.useTicksLeft < 12) ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.RED) : ((this.useTicksLeft < 18) ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.GOLD) : ((this.useTicksLeft < 24) ? ColorUtil.getRGBFromFormatting(EnumChatFormatting.YELLOW) : (new Color(cfg.v.consumeTimerRed, cfg.v.consumeTimerGreen, cfg.v.consumeTimerBlue)).getRGB()))));
/*     */     
/*  89 */     if (this.useTicksLeft > 0 && this.usingItem) {
/*     */       String text;
/*     */       
/*  92 */       if (cfg.v.consumeTimerMode.equals("Ticks")) {
/*  93 */         text = String.valueOf(this.useTicksLeft);
/*  94 */       } else if (cfg.v.consumeTimerMode.equals("Seconds")) {
/*  95 */         double secondsLeft = this.useTicksLeft / 20.0D;
/*  96 */         text = String.format("%.1f", new Object[] { Double.valueOf(secondsLeft) }).replace(",", ".");
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */       
/* 101 */       if (!cfg.v.smoothFont) {
/* 102 */         GlStateManager.func_179094_E();
/* 103 */         GlStateManager.func_179152_a(scale, scale, scale);
/* 104 */         mc.field_71466_p.func_175063_a(text, cfg.v.consumeTimerXPos / scale, cfg.v.consumeTimerYPos / scale, color);
/* 105 */         GlStateManager.func_179121_F();
/*     */       } else {
/* 107 */         Meowtils.fontRenderer.drawScaledStringWithShadow(text, cfg.v.consumeTimerXPos, cfg.v.consumeTimerYPos, color, scale * 10.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/render/ConsumeTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */