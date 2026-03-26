/*     */ package wtf.tatp.meowtils.modules.skywars;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.ColorComponent;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*     */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.gui.values.SaturationValue;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ 
/*     */ 
/*     */ public class SkywarsCounter
/*     */   extends Module
/*     */ {
/*     */   private BooleanValue kill;
/*     */   private BooleanValue exp;
/*     */   private NumberValue scale;
/*     */   private ColorComponent color;
/*     */   private ColorValue rgb;
/*  33 */   private static int kills = 0; private SaturationValue saturation; private BrightnessValue brightness; private ArrayValue mode; private BooleanValue dynamic; private BooleanValue reset;
/*  34 */   private static int xp = 0;
/*     */   
/*     */   public SkywarsCounter() {
/*  37 */     super("SkywarsCounter", "skywarsCounterKey", "skywarsCounter", Module.Category.Skywars);
/*  38 */     tooltip("Counts and displays different information about your skywars game.");
/*  39 */     this.color = new ColorComponent("skywarsCounter_red", "skywarsCounter_green", "skywarsCounter_blue");
/*  40 */     addColor(this.rgb = new ColorValue("Color", this.color));
/*  41 */     addSaturation(this.saturation = new SaturationValue(this.color));
/*  42 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/*  43 */     addValue(this.scale = new NumberValue("Scale", 0.5D, 1.5D, 0.05D, null, "skywarsCounterScale", float.class));
/*  44 */     addBoolean(this.reset = new BooleanValue("Reset after game", "skywarsCounterReset"));
/*  45 */     addBoolean(this.dynamic = new BooleanValue("Dynamic color", "skywarsCounterDynamicColor"));
/*  46 */     addBoolean(this.kill = new BooleanValue("Count: §7Kills", "skywarsCounterKill"));
/*  47 */     addBoolean(this.exp = new BooleanValue("Count: §7Earned xp", "skywarsCounterExp"));
/*  48 */     addArray(this.mode = new ArrayValue("Display", Arrays.asList(new String[] { "Horizontal", "Vertical" }, ), "skywarsCounterMode"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/*  53 */     String msg = event.message.func_150260_c();
/*  54 */     if (!GamemodeUtil.skywarsGame)
/*  55 */       return;  if (msg.startsWith(this.mc.field_71439_g.func_70005_c_()))
/*  56 */       return;  if (msg.contains(":")) {
/*     */       return;
/*     */     }
/*  59 */     String replaced = msg.replace(".", "");
/*  60 */     if (replaced.endsWith(this.mc.field_71439_g.func_70005_c_())) {
/*  61 */       kills++;
/*     */     }
/*     */ 
/*     */     
/*  65 */     if (msg.contains("SkyWars Experience")) {
/*  66 */       String value = msg.replaceAll("[a-zA-Z()+!\\s]", "");
/*     */       
/*  68 */       if (!value.isEmpty()) {
/*  69 */         int parsedValue = Integer.parseInt(value);
/*     */         
/*  71 */         xp += parsedValue;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/*  78 */     if (this.mc.field_71439_g == null || this.mc.field_71441_e == null || event.phase != TickEvent.Phase.END)
/*  79 */       return;  if (!GamemodeUtil.skywarsGame)
/*  80 */       return;  if (this.mc.field_71462_r != null) {
/*     */       return;
/*     */     }
/*  83 */     EnumChatFormatting killsColor = null;
/*  84 */     EnumChatFormatting expColor = null;
/*     */     
/*  86 */     float scale = cfg.v.skywarsCounterScale;
/*  87 */     int x = cfg.v.skywarsCounter_x;
/*  88 */     int y = cfg.v.skywarsCounter_y;
/*  89 */     int color = (new Color(cfg.v.skywarsCounter_red, cfg.v.skywarsCounter_green, cfg.v.skywarsCounter_blue)).getRGB();
/*     */ 
/*     */     
/*  92 */     if (cfg.v.skywarsCounterReset && cfg.v.skywarsCounterDynamicColor) {
/*  93 */       killsColor = (kills < 2) ? EnumChatFormatting.GREEN : ((kills < 5) ? EnumChatFormatting.YELLOW : ((kills < 10) ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
/*  94 */       expColor = (xp < 3) ? EnumChatFormatting.GREEN : ((xp < 7) ? EnumChatFormatting.YELLOW : ((xp < 15) ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
/*  95 */     } else if (cfg.v.skywarsCounterDynamicColor) {
/*  96 */       killsColor = (kills < 20) ? EnumChatFormatting.GREEN : ((kills < 50) ? EnumChatFormatting.YELLOW : ((kills < 100) ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
/*  97 */       expColor = (xp < 50) ? EnumChatFormatting.GREEN : ((xp < 100) ? EnumChatFormatting.YELLOW : ((xp < 200) ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
/*     */     } 
/*     */ 
/*     */     
/* 101 */     if (cfg.v.skywarsCounterMode.equals("Vertical")) {
/* 102 */       List<String> counterStrings = new ArrayList<>();
/*     */ 
/*     */       
/* 105 */       if (cfg.v.skywarsCounterKill) counterStrings.add("Kills: " + (cfg.v.skywarsCounterDynamicColor ? (String)killsColor : "") + kills); 
/* 106 */       if (cfg.v.skywarsCounterExp) counterStrings.add("EXP: " + (cfg.v.skywarsCounterDynamicColor ? (String)expColor : "") + xp);
/*     */       
/* 108 */       for (String text : counterStrings) {
/* 109 */         if (!cfg.v.smoothFont) {
/* 110 */           GlStateManager.func_179094_E();
/* 111 */           GlStateManager.func_179152_a(scale, scale, scale);
/* 112 */           this.mc.field_71466_p.func_175063_a(text, x / scale, y / scale, color);
/* 113 */           GlStateManager.func_179121_F();
/* 114 */           y += (int)(this.mc.field_71466_p.field_78288_b * scale + 3.0F); continue;
/*     */         } 
/* 116 */         Meowtils.fontRenderer.drawScaledStringWithShadow(text, x, y, color, scale * 10.0F);
/* 117 */         y += (int)(this.mc.field_71466_p.field_78288_b * scale + 2.0F);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 122 */       String separator = EnumChatFormatting.GRAY + " | ";
/* 123 */       String killString = cfg.v.skywarsCounterKill ? ("Kills: " + (cfg.v.skywarsCounterDynamicColor ? (String)killsColor : "") + kills + (cfg.v.skywarsCounterExp ? separator : "") + EnumChatFormatting.RESET) : "";
/* 124 */       String expString = cfg.v.skywarsCounterExp ? ("EXP: " + (cfg.v.skywarsCounterDynamicColor ? (String)expColor : "") + xp + EnumChatFormatting.RESET) : "";
/* 125 */       String text = killString + expString;
/* 126 */       if (!cfg.v.smoothFont) {
/* 127 */         GlStateManager.func_179094_E();
/* 128 */         GlStateManager.func_179152_a(scale, scale, scale);
/* 129 */         this.mc.field_71466_p.func_175063_a(text, x / scale, y / scale, color);
/* 130 */         GlStateManager.func_179121_F();
/*     */       } else {
/* 132 */         Meowtils.fontRenderer.drawScaledStringWithShadow(text, x, y, color, scale * 10.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void clear() {
/* 138 */     if (cfg.v.skywarsCounterReset) {
/* 139 */       kills = 0;
/* 140 */       xp = 0;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/skywars/SkywarsCounter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */