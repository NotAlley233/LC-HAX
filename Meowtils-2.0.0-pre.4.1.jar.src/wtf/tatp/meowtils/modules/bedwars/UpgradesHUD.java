/*     */ package wtf.tatp.meowtils.modules.bedwars;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.EnumChatFormatting;
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
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ 
/*     */ public class UpgradesHUD
/*     */   extends Module
/*     */ {
/*     */   private NumberValue scale;
/*     */   private BooleanValue showSharpness;
/*     */   private BooleanValue showProtection;
/*     */   private BooleanValue showTrap;
/*     */   private BooleanValue showFeatherFalling;
/*  32 */   private static int sharpnessLevel = 0; private BooleanValue showIronForge; private BooleanValue showHealPool; private ColorValue rgb; private SaturationValue saturation; private BrightnessValue brightness; private ColorComponent color;
/*  33 */   private static int sharpnessLevelCached = 0;
/*  34 */   private static int protectionLevel = 0;
/*  35 */   private static int protectionLevelCached = 0;
/*  36 */   private static String trapName = null;
/*  37 */   private static String trapNameCached = null;
/*  38 */   private static int featherFallingLevel = 0;
/*  39 */   private static int featherFallingLevelCached = 0;
/*     */   private static boolean healPoolEnabled = false;
/*     */   private static boolean healPoolEnabledCached = false;
/*  42 */   private static int ironForgeLevel = 0;
/*  43 */   private static int ironForgeLevelCached = 0;
/*     */   public static final String modulename = "UpgradesHUD";
/*     */   
/*     */   public UpgradesHUD() {
/*  47 */     super("UpgradesHUD", "upgradesHUDKey", "upgradesHUD", Module.Category.Bedwars);
/*  48 */     tooltip("Displays team upgrades on your screen. Only works with \"English\" language selected on Hypixel.");
/*  49 */     this.color = new ColorComponent("upgradesHUD_red", "upgradesHUD_green", "upgradesHUD_blue");
/*  50 */     addColor(this.rgb = new ColorValue("Text color", this.color));
/*  51 */     addSaturation(this.saturation = new SaturationValue(this.color));
/*  52 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/*  53 */     addValue(this.scale = new NumberValue("Scale", 0.5D, 1.5D, 0.05D, null, "upgradesHUDScale", float.class));
/*  54 */     addBoolean(this.showSharpness = new BooleanValue("Show §bSharpness", "upgradesHUD_sharpness"));
/*  55 */     addBoolean(this.showProtection = new BooleanValue("Show §3Protection", "upgradesHUD_protection"));
/*  56 */     addBoolean(this.showTrap = new BooleanValue("Show §9Traps", "upgradesHUD_trap"));
/*  57 */     addBoolean(this.showFeatherFalling = new BooleanValue("Show §aFeather Falling", "upgradesHUD_featherfalling"));
/*  58 */     addBoolean(this.showHealPool = new BooleanValue("Show §dHeal Pool", "upgradesHUD_healpool"));
/*  59 */     addBoolean(this.showIronForge = new BooleanValue("Show §7Iron Forge", "upgradesHUD_ironforge"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/*  64 */     if (this.mc.field_71439_g == null || this.mc.field_71441_e == null)
/*  65 */       return;  if (this.mc.field_71462_r != null)
/*  66 */       return;  if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*     */       return; 
/*  68 */     int x = cfg.v.upgradesHUD_x;
/*  69 */     int y = cfg.v.upgradesHUD_y;
/*  70 */     float scale = cfg.v.upgradesHUDScale;
/*     */     
/*  72 */     String upgradeFalse = EnumChatFormatting.RED + "✗";
/*  73 */     Map<String, String> upgrades = new LinkedHashMap<>();
/*     */ 
/*     */     
/*  76 */     if (cfg.v.upgradesHUD_sharpness) upgrades.put("Sharpness:", (sharpnessLevel > 0) ? (EnumChatFormatting.GREEN + String.valueOf(sharpnessLevel)) : upgradeFalse); 
/*  77 */     if (cfg.v.upgradesHUD_protection) upgrades.put("Protection:", (protectionLevel > 0) ? (EnumChatFormatting.GREEN + String.valueOf(protectionLevel)) : upgradeFalse); 
/*  78 */     if (cfg.v.upgradesHUD_trap) upgrades.put("Trap:", (trapName != null) ? (EnumChatFormatting.GREEN + trapName) : upgradeFalse); 
/*  79 */     if (cfg.v.upgradesHUD_featherfalling) upgrades.put("Feather Falling:", (featherFallingLevel > 0) ? (EnumChatFormatting.GREEN + String.valueOf(featherFallingLevel)) : upgradeFalse); 
/*  80 */     if (cfg.v.upgradesHUD_healpool) upgrades.put("Heal Pool:", healPoolEnabled ? (EnumChatFormatting.GREEN + "✓") : upgradeFalse); 
/*  81 */     if (cfg.v.upgradesHUD_ironforge) upgrades.put("Iron Forge:", (ironForgeLevel > 0) ? (EnumChatFormatting.GREEN + String.valueOf(ironForgeLevel)) : upgradeFalse);
/*     */ 
/*     */     
/*  84 */     List<String> lines = (List<String>)upgrades.entrySet().stream().map(entry -> (String)entry.getKey() + " " + (String)entry.getValue()).sorted(Comparator.comparingInt(s -> this.mc.field_71466_p.func_78256_a(EnumChatFormatting.func_110646_a((String)s))).reversed()).collect(Collectors.toList());
/*     */ 
/*     */     
/*  87 */     for (String line : lines) {
/*  88 */       if (!cfg.v.smoothFont) {
/*  89 */         GlStateManager.func_179094_E();
/*  90 */         GlStateManager.func_179152_a(scale, scale, scale);
/*  91 */         this.mc.field_71466_p.func_175063_a(line, x / scale, y / scale, -1);
/*  92 */         GlStateManager.func_179121_F();
/*  93 */         y += (int)(this.mc.field_71466_p.field_78288_b * scale + 3.0F); continue;
/*     */       } 
/*  95 */       Meowtils.fontRenderer.drawScaledStringWithShadow(line, x, y, -1, scale * 10.0F);
/*  96 */       y += (int)(this.mc.field_71466_p.field_78288_b * scale + 2.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 103 */     String msg = event.message.func_150260_c();
/* 104 */     if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
/*     */       return;
/*     */     }
/* 107 */     if (msg.equals("You will respawn because you still have a bed!")) {
/* 108 */       sharpnessLevel = sharpnessLevelCached;
/* 109 */       protectionLevel = protectionLevelCached;
/* 110 */       trapName = trapNameCached;
/* 111 */       featherFallingLevel = featherFallingLevelCached;
/* 112 */       healPoolEnabled = healPoolEnabledCached;
/* 113 */       ironForgeLevel = ironForgeLevelCached;
/*     */     } 
/*     */ 
/*     */     
/* 117 */     if (msg.contains("purchased") && !msg.contains(":")) {
/*     */       
/* 119 */       if (msg.contains("Sharpened Swords")) {
/* 120 */         if (msg.contains("II")) {
/* 121 */           sharpnessLevel = 2;
/* 122 */           sharpnessLevelCached = 2;
/*     */         } else {
/* 124 */           sharpnessLevel = 1;
/* 125 */           sharpnessLevelCached = 1;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 130 */       if (msg.contains("Reinforced Armor")) {
/* 131 */         if (msg.contains("IV")) {
/* 132 */           protectionLevel = 4;
/* 133 */           protectionLevelCached = 4;
/* 134 */         } else if (msg.contains("III")) {
/* 135 */           protectionLevel = 3;
/* 136 */           protectionLevelCached = 3;
/* 137 */         } else if (msg.contains("II")) {
/* 138 */           protectionLevel = 2;
/* 139 */           protectionLevelCached = 2;
/* 140 */         } else if (msg.contains("I")) {
/* 141 */           protectionLevel = 1;
/* 142 */           protectionLevelCached = 1;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 147 */       if (msg.contains("Trap")) {
/* 148 */         if (msg.contains("Miner Fatigue")) {
/* 149 */           trapName = "Miner Fatigue";
/* 150 */           trapNameCached = "Miner Fatigue";
/* 151 */         } else if (msg.contains("Blindness")) {
/* 152 */           trapName = "Blindness";
/* 153 */           trapNameCached = "Blindness";
/* 154 */         } else if (msg.contains("Reveal")) {
/* 155 */           trapName = "Reveal";
/* 156 */           trapNameCached = "Reveal";
/* 157 */         } else if (msg.contains("Counter-Offensive")) {
/* 158 */           trapName = "Counter-Offensive";
/* 159 */           trapNameCached = "Counter-Offensive";
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 164 */       if (msg.contains("Cushioned Boots")) {
/* 165 */         if (msg.contains("II")) {
/* 166 */           featherFallingLevel = 2;
/* 167 */           featherFallingLevelCached = 2;
/* 168 */         } else if (msg.contains("I")) {
/* 169 */           featherFallingLevel = 1;
/* 170 */           featherFallingLevelCached = 1;
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 175 */       if (msg.contains("Heal Pool")) {
/* 176 */         healPoolEnabled = true;
/* 177 */         healPoolEnabledCached = true;
/*     */       } 
/*     */ 
/*     */       
/* 181 */       if (msg.contains("Iron Forge")) {
/* 182 */         if (msg.contains("IV")) {
/* 183 */           ironForgeLevel = 4;
/* 184 */           ironForgeLevelCached = 4;
/* 185 */         } else if (msg.contains("III")) {
/* 186 */           ironForgeLevel = 3;
/* 187 */           ironForgeLevelCached = 3;
/* 188 */         } else if (msg.contains("II")) {
/* 189 */           ironForgeLevel = 2;
/* 190 */           ironForgeLevelCached = 2;
/* 191 */         } else if (msg.contains("I")) {
/* 192 */           ironForgeLevel = 1;
/* 193 */           ironForgeLevelCached = 1;
/*     */         } 
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 199 */     if (msg.contains("Trap was set off!") || msg.contains("Your Bed was destroyed")) {
/* 200 */       trapName = null;
/* 201 */       trapNameCached = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void clear() {
/* 207 */     sharpnessLevel = 0;
/* 208 */     protectionLevel = 0;
/* 209 */     trapName = null;
/* 210 */     featherFallingLevel = 0;
/* 211 */     healPoolEnabled = false;
/* 212 */     ironForgeLevel = 0;
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/bedwars/UpgradesHUD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */