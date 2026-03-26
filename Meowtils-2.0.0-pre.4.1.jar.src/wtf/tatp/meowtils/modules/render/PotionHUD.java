/*     */ package wtf.tatp.meowtils.modules.render;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.util.PlaySound;
/*     */ 
/*     */ public class PotionHUD
/*     */   extends Module
/*     */ {
/*     */   private NumberValue hudScale;
/*     */   private NumberValue time;
/*     */   private BooleanValue sound;
/*     */   private BooleanValue invis;
/*     */   private BooleanValue jump;
/*     */   private BooleanValue speed;
/*  33 */   private final Set<Integer> alertedPotions = new HashSet<>(); private BooleanValue regen; private BooleanValue strength; private BooleanValue fireres; private BooleanValue miningfatigue; private BooleanValue hideInfinite; private ArrayValue nameMode; private ArrayValue displayMode;
/*     */   public PotionHUD() {
/*  35 */     super("PotionHUD", "potionHUDKey", "potionHUD", Module.Category.Render);
/*  36 */     tooltip("Display important potion effects on screen.");
/*  37 */     addValue(this.hudScale = new NumberValue("Scale", 0.5D, 1.5D, 0.05D, null, "potionHUDScale", float.class));
/*  38 */     addValue(this.time = new NumberValue("Alert time", 0.0D, 10.0D, 1.0D, "s", "potionHUDSeconds", int.class));
/*  39 */     addBoolean(this.sound = new BooleanValue("Expire sound", "potionHUDSound"));
/*  40 */     addBoolean(this.hideInfinite = new BooleanValue("Hide infinite effects", "potionHUDHideInfinite"));
/*  41 */     addBoolean(this.invis = new BooleanValue("§bInvisibility", "potionHUD_invis"));
/*  42 */     addBoolean(this.jump = new BooleanValue("§aJump Boost", "potionHUD_jump"));
/*  43 */     addBoolean(this.speed = new BooleanValue("§eSpeed", "potionHUD_speed"));
/*  44 */     addBoolean(this.regen = new BooleanValue("§dRegeneration", "potionHUD_regen"));
/*  45 */     addBoolean(this.strength = new BooleanValue("§4Strength", "potionHUD_strength"));
/*  46 */     addBoolean(this.fireres = new BooleanValue("§6Fire Resistance", "potionHUD_fireres"));
/*  47 */     addBoolean(this.miningfatigue = new BooleanValue("§7Mining Fatigue", "potionHUD_miningfatigue"));
/*  48 */     addArray(this.nameMode = new ArrayValue("Name", Arrays.asList(new String[] { "Full", "Short", "None" }, ), "potionHUDNameMode"));
/*  49 */     addArray(this.displayMode = new ArrayValue("Display", Arrays.asList(new String[] { "Both", "HUD", "Chat" }, ), "potionHUDMode"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/*  54 */     Minecraft mc = Minecraft.func_71410_x();
/*  55 */     if (mc.field_71441_e == null || mc.field_71439_g == null || event.phase != TickEvent.Phase.END)
/*  56 */       return;  if (mc.field_71462_r != null)
/*     */       return; 
/*  58 */     float scale = cfg.v.potionHUDScale;
/*  59 */     List<String> potionStrings = new ArrayList<>();
/*     */     
/*  61 */     mc.field_71439_g.func_70651_bq().forEach(effect -> {
/*     */           int id = effect.func_76456_a();
/*     */           
/*     */           Potion potion = Potion.field_76425_a[id];
/*     */           
/*     */           if (potion == null) {
/*     */             return;
/*     */           }
/*     */           
/*     */           if ((id != Potion.field_76441_p.field_76415_H || !cfg.v.potionHUD_invis) && (id != Potion.field_76430_j.field_76415_H || !cfg.v.potionHUD_jump) && (id != Potion.field_76424_c.field_76415_H || !cfg.v.potionHUD_speed) && (id != Potion.field_76428_l.field_76415_H || !cfg.v.potionHUD_regen) && (id != Potion.field_76420_g.field_76415_H || !cfg.v.potionHUD_strength) && (id != Potion.field_76426_n.field_76415_H || !cfg.v.potionHUD_fireres) && (id != Potion.field_76419_f.field_76415_H || !cfg.v.potionHUD_miningfatigue)) {
/*     */             return;
/*     */           }
/*     */           
/*     */           String timeLeft = Potion.func_76389_a(effect);
/*     */           
/*     */           if (timeLeft.contains("*") && cfg.v.potionHUDHideInfinite) {
/*     */             return;
/*     */           }
/*     */           
/*     */           int seconds = cfg.v.potionHUDSeconds * 20;
/*     */           
/*     */           if (effect.func_76459_b() <= seconds) {
/*     */             timeLeft = !cfg.v.potionHUDNameMode.equals("None") ? (EnumChatFormatting.RED + timeLeft) : timeLeft;
/*     */             if (!this.alertedPotions.contains(Integer.valueOf(id)) && (cfg.v.potionHUDMode.equals("Chat") || cfg.v.potionHUDMode.equals("Both"))) {
/*     */               String messageSuffix = (seconds <= 0) ? " has expired!" : " is about to expire!";
/*     */               Meowtils.addMessage(getPotionName(id, "Full").replace(":", "") + EnumChatFormatting.RED + messageSuffix);
/*     */               this.alertedPotions.add(Integer.valueOf(id));
/*     */               if (cfg.v.potionHUDSound) {
/*     */                 PlaySound.getInstance().playPingSoundMedium();
/*     */               }
/*     */             } 
/*     */           } else {
/*     */             this.alertedPotions.remove(Integer.valueOf(id));
/*     */           } 
/*     */           potionStrings.add(getPotionName(id, cfg.v.potionHUDNameMode) + " " + (cfg.v.potionHUDNameMode.equals("None") ? "" : (String)EnumChatFormatting.GRAY) + timeLeft);
/*     */         });
/*  97 */     potionStrings.sort(Comparator.<String>comparingInt(s -> mc.field_71466_p.func_78256_a(EnumChatFormatting.func_110646_a((String)s))).reversed());
/*     */     
/*  99 */     if (potionStrings.isEmpty() || cfg.v.potionHUDMode.equals("Chat"))
/*     */       return; 
/* 101 */     int y = cfg.v.potionHUD_y;
/*     */     
/* 103 */     for (String potion : potionStrings) {
/* 104 */       if (!cfg.v.smoothFont) {
/* 105 */         GlStateManager.func_179094_E();
/* 106 */         GlStateManager.func_179152_a(scale, scale, scale);
/* 107 */         mc.field_71466_p.func_175063_a(potion, cfg.v.potionHUD_x / scale, y / scale, -1);
/* 108 */         GlStateManager.func_179121_F();
/* 109 */         y += (int)(mc.field_71466_p.field_78288_b * scale + 3.0F); continue;
/*     */       } 
/* 111 */       Meowtils.fontRenderer.drawScaledStringWithShadow(potion, cfg.v.potionHUD_x, y, -1, scale * 10.0F);
/* 112 */       y += (int)(mc.field_71466_p.field_78288_b * scale + 2.0F);
/*     */     } 
/*     */   }
/*     */   private String getPotionName(int id, String mode) {
/*     */     String fullName;
/*     */     String shortName;
/*     */     EnumChatFormatting color;
/* 119 */     if (id == Potion.field_76441_p.field_76415_H) {
/* 120 */       fullName = "Invisibility";
/* 121 */       shortName = "Inv";
/* 122 */       color = EnumChatFormatting.AQUA;
/* 123 */     } else if (id == Potion.field_76430_j.field_76415_H) {
/* 124 */       fullName = "Jump Boost";
/* 125 */       shortName = "Jmp";
/* 126 */       color = EnumChatFormatting.GREEN;
/* 127 */     } else if (id == Potion.field_76424_c.field_76415_H) {
/* 128 */       fullName = "Speed";
/* 129 */       shortName = "Spd";
/* 130 */       color = EnumChatFormatting.YELLOW;
/* 131 */     } else if (id == Potion.field_76428_l.field_76415_H) {
/* 132 */       fullName = "Regeneration";
/* 133 */       shortName = "Reg";
/* 134 */       color = EnumChatFormatting.LIGHT_PURPLE;
/* 135 */     } else if (id == Potion.field_76420_g.field_76415_H) {
/* 136 */       fullName = "Strength";
/* 137 */       shortName = "Str";
/* 138 */       color = EnumChatFormatting.DARK_RED;
/* 139 */     } else if (id == Potion.field_76426_n.field_76415_H) {
/* 140 */       fullName = "Fire Resistance";
/* 141 */       shortName = "Fire";
/* 142 */       color = EnumChatFormatting.GOLD;
/* 143 */     } else if (id == Potion.field_76419_f.field_76415_H) {
/* 144 */       fullName = "Mining Fatigue";
/* 145 */       shortName = "Mine";
/* 146 */       color = EnumChatFormatting.GRAY;
/*     */     } else {
/* 148 */       return "Unknown";
/*     */     } 
/*     */     
/* 151 */     switch (mode) {
/*     */       case "Full":
/* 153 */         return color + fullName + ":";
/*     */       case "Short":
/* 155 */         return color + shortName + ":";
/*     */       case "None":
/* 157 */         return color + "";
/*     */     } 
/* 159 */     return "Unknown";
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/render/PotionHUD.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */