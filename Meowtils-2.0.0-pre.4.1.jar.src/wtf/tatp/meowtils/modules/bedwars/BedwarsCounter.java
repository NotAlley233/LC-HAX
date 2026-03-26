/*     */ package wtf.tatp.meowtils.modules.bedwars;
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
/*     */ 
/*     */ public class BedwarsCounter
/*     */   extends Module
/*     */ {
/*     */   private BooleanValue kill;
/*     */   private BooleanValue finalKill;
/*     */   private BooleanValue exp;
/*     */   private NumberValue scale;
/*     */   private ColorComponent color;
/*  34 */   private static int kills = 0; private ColorValue rgb; private SaturationValue saturation; private BrightnessValue brightness; private ArrayValue mode; private BooleanValue dynamic; private BooleanValue reset;
/*  35 */   private static int finalKills = 0;
/*  36 */   private static int xp = 0;
/*     */   
/*     */   public BedwarsCounter() {
/*  39 */     super("BedwarsCounter", "bedwarsCounterKey", "bedwarsCounter", Module.Category.Bedwars);
/*  40 */     tooltip("Counts and displays different information about your bedwars game.");
/*  41 */     this.color = new ColorComponent("bedwarsCounter_red", "bedwarsCounter_green", "bedwarsCounter_blue");
/*  42 */     addColor(this.rgb = new ColorValue("Color", this.color));
/*  43 */     addSaturation(this.saturation = new SaturationValue(this.color));
/*  44 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/*  45 */     addValue(this.scale = new NumberValue("Scale", 0.5D, 1.5D, 0.05D, null, "bedwarsCounterScale", float.class));
/*  46 */     addBoolean(this.reset = new BooleanValue("Reset after game", "bedwarsCounterReset"));
/*  47 */     addBoolean(this.dynamic = new BooleanValue("Dynamic color", "bedwarsCounterDynamicColor"));
/*  48 */     addBoolean(this.kill = new BooleanValue("Count: §7Kills", "bedwarsCounterKill"));
/*  49 */     addBoolean(this.finalKill = new BooleanValue("Count: §7Final kills", "bedwarsCounterFinal"));
/*  50 */     addBoolean(this.exp = new BooleanValue("Count: §7Earned xp", "bedwarsCounterExp"));
/*  51 */     addArray(this.mode = new ArrayValue("Display", Arrays.asList(new String[] { "Horizontal", "Vertical" }, ), "bedwarsCounterMode"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/*  56 */     String msg = event.message.func_150260_c();
/*  57 */     if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*  58 */       return;  if (msg.startsWith(this.mc.field_71439_g.func_70005_c_()))
/*  59 */       return;  if (msg.contains(":")) {
/*     */       return;
/*     */     }
/*  62 */     String replaced = msg.replace(".", "");
/*  63 */     if (replaced.endsWith(this.mc.field_71439_g.func_70005_c_())) {
/*  64 */       kills++;
/*     */     }
/*     */ 
/*     */     
/*  68 */     if (msg.contains(this.mc.field_71439_g.func_70005_c_()) && msg.contains("FINAL KILL!")) {
/*  69 */       finalKills++;
/*     */     }
/*     */ 
/*     */     
/*  73 */     if (msg.contains("Bed Wars XP")) {
/*  74 */       String value = msg.replaceAll("[a-zA-Z()+\\s]", "");
/*     */       
/*  76 */       if (!value.isEmpty()) {
/*  77 */         int parsedValue = Integer.parseInt(value);
/*     */         
/*  79 */         xp += parsedValue;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/*  86 */     if (this.mc.field_71439_g == null || this.mc.field_71441_e == null || event.phase != TickEvent.Phase.END)
/*  87 */       return;  if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*  88 */       return;  if (this.mc.field_71462_r != null) {
/*     */       return;
/*     */     }
/*  91 */     EnumChatFormatting killsColor = null;
/*  92 */     EnumChatFormatting finalsColor = null;
/*  93 */     EnumChatFormatting expColor = null;
/*     */     
/*  95 */     float scale = cfg.v.bedwarsCounterScale;
/*  96 */     int x = cfg.v.bedwarsCounter_x;
/*  97 */     int y = cfg.v.bedwarsCounter_y;
/*  98 */     int color = (new Color(cfg.v.bedwarsCounter_red, cfg.v.bedwarsCounter_green, cfg.v.bedwarsCounter_blue)).getRGB();
/*     */ 
/*     */     
/* 101 */     if (cfg.v.bedwarsCounterReset && cfg.v.bedwarsCounterDynamicColor) {
/* 102 */       killsColor = (kills < 5) ? EnumChatFormatting.GREEN : ((kills < 10) ? EnumChatFormatting.YELLOW : ((kills < 15) ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
/* 103 */       finalsColor = (finalKills < 4) ? EnumChatFormatting.GREEN : ((finalKills < 6) ? EnumChatFormatting.YELLOW : ((finalKills < 8) ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
/* 104 */       expColor = (xp < 300) ? EnumChatFormatting.GREEN : ((xp < 500) ? EnumChatFormatting.YELLOW : ((xp < 750) ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
/* 105 */     } else if (cfg.v.bedwarsCounterDynamicColor) {
/* 106 */       killsColor = (kills < 50) ? EnumChatFormatting.GREEN : ((kills < 100) ? EnumChatFormatting.YELLOW : ((kills < 200) ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
/* 107 */       finalsColor = (finalKills < 30) ? EnumChatFormatting.GREEN : ((finalKills < 60) ? EnumChatFormatting.YELLOW : ((finalKills < 100) ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
/* 108 */       expColor = (xp < 2000) ? EnumChatFormatting.GREEN : ((xp < 4000) ? EnumChatFormatting.YELLOW : ((xp < 6000) ? EnumChatFormatting.RED : EnumChatFormatting.LIGHT_PURPLE));
/*     */     } 
/*     */ 
/*     */     
/* 112 */     if (cfg.v.bedwarsCounterMode.equals("Vertical")) {
/* 113 */       List<String> counterStrings = new ArrayList<>();
/*     */ 
/*     */       
/* 116 */       if (cfg.v.bedwarsCounterKill) counterStrings.add("Kills: " + (cfg.v.bedwarsCounterDynamicColor ? (String)killsColor : "") + kills); 
/* 117 */       if (cfg.v.bedwarsCounterFinal) counterStrings.add("Finals: " + (cfg.v.bedwarsCounterDynamicColor ? (String)finalsColor : "") + finalKills); 
/* 118 */       if (cfg.v.bedwarsCounterExp) counterStrings.add("EXP: " + (cfg.v.bedwarsCounterDynamicColor ? (String)expColor : "") + xp);
/*     */       
/* 120 */       for (String text : counterStrings) {
/* 121 */         if (!cfg.v.smoothFont) {
/* 122 */           GlStateManager.func_179094_E();
/* 123 */           GlStateManager.func_179152_a(scale, scale, scale);
/* 124 */           this.mc.field_71466_p.func_175063_a(text, x / scale, y / scale, color);
/* 125 */           GlStateManager.func_179121_F();
/* 126 */           y += (int)(this.mc.field_71466_p.field_78288_b * scale + 3.0F); continue;
/*     */         } 
/* 128 */         Meowtils.fontRenderer.drawScaledStringWithShadow(text, x, y, color, scale * 10.0F);
/* 129 */         y += (int)(this.mc.field_71466_p.field_78288_b * scale + 2.0F);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 134 */       String separator = EnumChatFormatting.GRAY + " | ";
/* 135 */       String killString = cfg.v.bedwarsCounterKill ? ("Kills: " + (cfg.v.bedwarsCounterDynamicColor ? (String)killsColor : "") + kills + ((cfg.v.bedwarsCounterFinal || cfg.v.bedwarsCounterExp) ? separator : "") + EnumChatFormatting.RESET) : "";
/* 136 */       String finalString = cfg.v.bedwarsCounterFinal ? ("Finals: " + (cfg.v.bedwarsCounterDynamicColor ? (String)finalsColor : "") + finalKills + (cfg.v.bedwarsCounterExp ? separator : "") + EnumChatFormatting.RESET) : "";
/* 137 */       String expString = cfg.v.bedwarsCounterExp ? ("EXP: " + (cfg.v.bedwarsCounterDynamicColor ? (String)expColor : "") + xp + EnumChatFormatting.RESET) : "";
/* 138 */       String text = killString + finalString + expString;
/* 139 */       if (!cfg.v.smoothFont) {
/* 140 */         GlStateManager.func_179094_E();
/* 141 */         GlStateManager.func_179152_a(scale, scale, scale);
/* 142 */         this.mc.field_71466_p.func_175063_a(text, x / scale, y / scale, color);
/* 143 */         GlStateManager.func_179121_F();
/*     */       } else {
/* 145 */         Meowtils.fontRenderer.drawScaledStringWithShadow(text, x, y, color, scale * 10.0F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void clear() {
/* 151 */     if (cfg.v.bedwarsCounterReset) {
/* 152 */       kills = 0;
/* 153 */       finalKills = 0;
/* 154 */       xp = 0;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/bedwars/BedwarsCounter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */