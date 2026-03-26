/*     */ package wtf.tatp.meowtils.gui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HUDEditor
/*     */   extends GuiScreen
/*     */ {
/*  25 */   private final Minecraft mc = Minecraft.func_71410_x();
/*  26 */   private final List<HudElement> elements = new ArrayList<>();
/*  27 */   private HudElement dragging = null; private int dragOffsetX;
/*     */   private int dragOffsetY;
/*     */   
/*     */   public HUDEditor() {
/*  31 */     this.elements.add(new HudElement("UpgradesHUD", "Trap: §aMiner Fatigue\nFeather Falling: §a1\nSharpness: §c✗\nProtection: §a4\nIron Forge: §a2\nHeal Pool: §a✓", cfg.v.upgradesHUD_x, cfg.v.upgradesHUD_y, (int)(this.mc.field_71466_p.func_78256_a("Trap: Miner Fatigue") * cfg.v.upgradesHUDScale), (int)(this.mc.field_71466_p.field_78288_b * cfg.v.upgradesHUDScale) * 8 + 14, cfg.v.upgradesHUDScale));
/*  32 */     this.elements.add(new HudElement("HealthInfo (Display)", "§220§c❤", cfg.v.healthInfoDisplay_x, cfg.v.healthInfoDisplay_y, (int)(this.mc.field_71466_p.func_78256_a("20❤") * cfg.v.healthInfoDisplayScale), (int)(this.mc.field_71466_p.field_78288_b * cfg.v.healthInfoDisplayScale), cfg.v.healthInfoDisplayScale));
/*  33 */     this.elements.add(new HudElement("HealthInfo (Indicator)", "14 HP", cfg.v.healthInfoIndicator_x, cfg.v.healthInfoIndicator_y, (int)(this.mc.field_71466_p.func_78256_a("14 HP") * cfg.v.healthInfoIndicatorScale), (int)(this.mc.field_71466_p.field_78288_b * cfg.v.healthInfoIndicatorScale), cfg.v.healthInfoIndicatorScale));
/*  34 */     this.elements.add(new HudElement("ConsumeTimer", "1.6", cfg.v.consumeTimerXPos, cfg.v.consumeTimerYPos, (int)(this.mc.field_71466_p.func_78256_a("1.6") * cfg.v.consumeTimerScale), (int)(this.mc.field_71466_p.field_78288_b * cfg.v.consumeTimerScale), cfg.v.consumeTimerScale));
/*  35 */     this.elements.add(new HudElement("CooldownHUD", "§5End Lord: §aReady", cfg.v.cooldownHUDxPos, cfg.v.cooldownHUDyPos, (int)(this.mc.field_71466_p.func_78256_a("End Lord: Ready") * cfg.v.cooldownHUDScale), (int)(this.mc.field_71466_p.field_78288_b * cfg.v.cooldownHUDScale), cfg.v.cooldownHUDScale));
/*  36 */     this.elements.add(new HudElement("PotionHUD", "§6Fire Resistance: §71:00\n§7Mining Fatigue: 1:00\n§dRegeneration: §71:00\n§aJump Boost: §71:00\n§bInvisibility: §71:00\n§4Strength: §71:00\n§eSpeed: §71:00", cfg.v.potionHUD_x, cfg.v.potionHUD_y, (int)(this.mc.field_71466_p.func_78256_a("Fire Resistance: 1:00") * cfg.v.potionHUDScale), (int)(this.mc.field_71466_p.field_78288_b * cfg.v.potionHUDScale) * 8 + 14, cfg.v.potionHUDScale));
/*  37 */     this.elements.add(new HudElement("TimeWarpDisplay", "§5Time Warp: §a3.0", cfg.v.timeWarpDisplay_x, cfg.v.timeWarpDisplay_y, (int)(this.mc.field_71466_p.func_78256_a("Time Warp: 3.0") * cfg.v.timeWarpDisplayScale), (int)(this.mc.field_71466_p.field_78288_b * cfg.v.timeWarpDisplayScale), cfg.v.timeWarpDisplayScale));
/*  38 */     this.elements.add(new HudElement("BlockCount", "64", cfg.v.blockCount_x, cfg.v.blockCount_y, (int)(this.mc.field_71466_p.func_78256_a("64") * cfg.v.blockCountScale), (int)(this.mc.field_71466_p.field_78288_b * cfg.v.blockCountScale), cfg.v.blockCountScale));
/*  39 */     this.elements.add(new HudElement("BedTracker", "Bed: §a✓ §7 | §fDistance: §c150 ⚠", cfg.v.bedTracker_x, cfg.v.bedTracker_y, (int)(this.mc.field_71466_p.func_78256_a("Bed: ✓ | Distance: 150 ⚠") * cfg.v.bedTrackerScale), (int)(this.mc.field_71466_p.field_78288_b * cfg.v.bedTrackerScale), cfg.v.bedTrackerScale));
/*  40 */     this.elements.add(new HudElement("BedwarsCounter", cfg.v.bedwarsCounterMode.equals("Vertical") ? "Kills: §a3\nFinals: §e7\nEXP: §a150" : "Kills: 0 §7| §fFinals: 0 §7| §fEXP: 0", cfg.v.bedwarsCounter_x, cfg.v.bedwarsCounter_y, (int)(this.mc.field_71466_p.func_78256_a(cfg.v.bedwarsCounterMode.equals("Vertical") ? "Finals: 5" : "Kills: 0 | Finals: 0 | EXP: 0") * cfg.v.bedwarsCounterScale), cfg.v.bedwarsCounterMode.equals("Vertical") ? ((int)(this.mc.field_71466_p.field_78288_b * cfg.v.bedwarsCounterScale) * 4) : (int)(this.mc.field_71466_p.field_78288_b * cfg.v.bedwarsCounterScale), cfg.v.bedwarsCounterScale));
/*  41 */     this.elements.add(new HudElement("SkywarsCounter", cfg.v.skywarsCounterMode.equals("Vertical") ? "Kills: §a3\nEXP: 15" : "Kills: 0 §7| §fEXP: 15", cfg.v.skywarsCounter_x, cfg.v.skywarsCounter_y, (int)(this.mc.field_71466_p.func_78256_a(cfg.v.skywarsCounterMode.equals("Vertical") ? "Kills: 5" : "Kills: 0 | EXP: 15") * cfg.v.skywarsCounterScale), cfg.v.skywarsCounterMode.equals("Vertical") ? ((int)(this.mc.field_71466_p.field_78288_b * cfg.v.skywarsCounterScale) * 3) : (int)(this.mc.field_71466_p.field_78288_b * cfg.v.skywarsCounterScale), cfg.v.skywarsCounterScale));
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
/*  46 */     func_146276_q_();
/*     */     
/*  48 */     if (this.dragging != null) {
/*  49 */       this.dragging.x = mouseX - this.dragOffsetX;
/*  50 */       this.dragging.y = mouseY - this.dragOffsetY;
/*     */     } 
/*     */     
/*  53 */     HudElement hovered = null;
/*     */ 
/*     */     
/*  56 */     for (HudElement element : this.elements) {
/*  57 */       element.drawSample();
/*  58 */       if (element.isMouseOver(mouseX, mouseY)) {
/*  59 */         hovered = element;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  64 */     if (hovered != null) {
/*  65 */       hovered.drawBox();
/*     */     }
/*     */ 
/*     */     
/*  69 */     HudElement showId = (this.dragging != null) ? this.dragging : hovered;
/*  70 */     if (showId != null)
/*     */     {
/*  72 */       Meowtils.fontRenderer.drawScaledStringWithShadow(showId.id, (mouseX + 6), (mouseY + 6), -1, 6.0F);
/*     */     }
/*     */     
/*  75 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  80 */     super.func_73864_a(mouseX, mouseY, mouseButton);
/*     */     
/*  82 */     if (mouseButton == 0) {
/*  83 */       for (HudElement element : this.elements) {
/*  84 */         if (element.isMouseOver(mouseX, mouseY)) {
/*  85 */           this.dragging = element;
/*  86 */           this.dragOffsetX = mouseX - element.x;
/*  87 */           this.dragOffsetY = mouseY - element.y;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_146286_b(int mouseX, int mouseY, int state) {
/*  96 */     super.func_146286_b(mouseX, mouseY, state);
/*     */     
/*  98 */     if (state == 0 && this.dragging != null) {
/*  99 */       switch (this.dragging.id) {
/*     */         case "UpgradesHUD":
/* 101 */           cfg.v.upgradesHUD_x = this.dragging.x;
/* 102 */           cfg.v.upgradesHUD_y = this.dragging.y;
/*     */           break;
/*     */         case "HealthInfo (Display)":
/* 105 */           cfg.v.healthInfoDisplay_x = this.dragging.x;
/* 106 */           cfg.v.healthInfoDisplay_y = this.dragging.y;
/*     */           break;
/*     */         case "HealthInfo (Indicator)":
/* 109 */           cfg.v.healthInfoIndicator_x = this.dragging.x;
/* 110 */           cfg.v.healthInfoIndicator_y = this.dragging.y;
/*     */           break;
/*     */         case "ConsumeTimer":
/* 113 */           cfg.v.consumeTimerXPos = this.dragging.x;
/* 114 */           cfg.v.consumeTimerYPos = this.dragging.y;
/*     */           break;
/*     */         case "CooldownHUD":
/* 117 */           cfg.v.cooldownHUDxPos = this.dragging.x;
/* 118 */           cfg.v.cooldownHUDyPos = this.dragging.y;
/*     */           break;
/*     */         case "PotionHUD":
/* 121 */           cfg.v.potionHUD_x = this.dragging.x;
/* 122 */           cfg.v.potionHUD_y = this.dragging.y;
/*     */           break;
/*     */         case "TimeWarpDisplay":
/* 125 */           cfg.v.timeWarpDisplay_x = this.dragging.x;
/* 126 */           cfg.v.timeWarpDisplay_y = this.dragging.y;
/*     */           break;
/*     */         case "BlockCount":
/* 129 */           cfg.v.blockCount_x = this.dragging.x;
/* 130 */           cfg.v.blockCount_y = this.dragging.y;
/*     */           break;
/*     */         case "BedTracker":
/* 133 */           cfg.v.bedTracker_x = this.dragging.x;
/* 134 */           cfg.v.bedTracker_y = this.dragging.y;
/*     */           break;
/*     */         case "BedwarsCounter":
/* 137 */           cfg.v.bedwarsCounter_x = this.dragging.x;
/* 138 */           cfg.v.bedwarsCounter_y = this.dragging.y;
/*     */           break;
/*     */         case "SkywarsCounter":
/* 141 */           cfg.v.skywarsCounter_x = this.dragging.x;
/* 142 */           cfg.v.skywarsCounter_y = this.dragging.y;
/*     */           break;
/*     */       } 
/* 145 */       cfg.save();
/* 146 */       this.dragging = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_73868_f() {
/* 152 */     return false;
/*     */   }
/*     */   
/*     */   private static class HudElement
/*     */   {
/*     */     final String id;
/*     */     final String sample;
/*     */     final float scale;
/*     */     
/*     */     HudElement(String id, String sample, int x, int y, int w, int h, float scale) {
/* 162 */       this.id = id;
/* 163 */       this.sample = (sample == null) ? "" : sample;
/* 164 */       this.x = x;
/* 165 */       this.y = y;
/* 166 */       this.w = w;
/* 167 */       this.h = h;
/* 168 */       this.scale = (scale <= 0.0F) ? 1.0F : scale;
/*     */     }
/*     */     int x; int y; int w; int h;
/*     */     void drawSample() {
/* 172 */       Minecraft mc = Minecraft.func_71410_x();
/* 173 */       String[] lines = this.sample.split("\n");
/* 174 */       int currentY = this.y;
/*     */       
/* 176 */       for (String line : lines) {
/*     */         
/* 178 */         if (!cfg.v.smoothFont) {
/* 179 */           GlStateManager.func_179094_E();
/* 180 */           GlStateManager.func_179152_a(this.scale, this.scale, this.scale);
/* 181 */           mc.field_71466_p.func_175063_a(line, this.x / this.scale, currentY / this.scale, -1);
/* 182 */           GlStateManager.func_179121_F();
/*     */           
/* 184 */           currentY += (int)(mc.field_71466_p.field_78288_b * this.scale + 3.0F);
/*     */         } else {
/*     */           
/* 187 */           Meowtils.fontRenderer.drawScaledStringWithShadow(line, this.x, currentY, -1, this.scale * 10.0F);
/*     */           
/* 189 */           currentY += (int)(mc.field_71466_p.field_78288_b * this.scale + 2.0F);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     void drawBox() {
/* 195 */       Gui.func_73734_a(this.x - 1, this.y - 1, this.x + this.w + 1, this.y + this.h + 1, (new Color(0, 0, 0, 90)).getRGB());
/* 196 */       Gui.func_73734_a(this.x, this.y, this.x + this.w, this.y + this.h, (new Color(80, 80, 80, 30)).getRGB());
/*     */     }
/*     */     
/*     */     boolean isMouseOver(int mouseX, int mouseY) {
/* 200 */       return (mouseX >= this.x && mouseX <= this.x + this.w && mouseY >= this.y && mouseY <= this.y + this.h);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/HUDEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */