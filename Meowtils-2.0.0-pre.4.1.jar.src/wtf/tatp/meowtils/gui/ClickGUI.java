/*     */ package wtf.tatp.meowtils.gui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.component.Component;
/*     */ import wtf.tatp.meowtils.gui.component.Frame;
/*     */ import wtf.tatp.meowtils.gui.parts.ModePart;
/*     */ import wtf.tatp.meowtils.gui.parts.ModulesPart;
/*     */ 
/*     */ public class ClickGUI
/*     */   extends GuiScreen {
/*  23 */   private String versionTag = "Meowtils 2.0.0-pre.4.1";
/*     */   public ArrayList<Frame> frames;
/*  25 */   private Object lastHoveredModule = null;
/*  26 */   private long hoverStartTime = 0L;
/*     */   
/*     */   public ClickGUI() {
/*  29 */     this.frames = new ArrayList<>();
/*     */     Module.Category[] values;
/*  31 */     for (int length = (values = Module.Category.values()).length, i = 0; i < length; i++) {
/*  32 */       Module.Category category = values[i];
/*  33 */       Frame frame = new Frame(category);
/*  34 */       this.frames.add(frame);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
/*  40 */     Minecraft mc = Minecraft.func_71410_x();
/*  41 */     float scale = getScale();
/*  42 */     int scaledMouseX = (int)(mouseX / scale);
/*  43 */     int scaledMouseY = (int)(mouseY / scale);
/*  44 */     ScaledResolution sr = new ScaledResolution(mc);
/*  45 */     float versionTagScale = 0.65F;
/*  46 */     int versionTagWidth = mc.field_71466_p.func_78256_a(this.versionTag);
/*  47 */     int versionTagHeight = mc.field_71466_p.field_78288_b;
/*  48 */     float versionTagX = sr.func_78326_a() - versionTagWidth * versionTagScale - 2.0F;
/*  49 */     float versionTagY = sr.func_78328_b() - versionTagHeight * versionTagScale - 2.0F;
/*  50 */     int versionTagColor = (new Color(cfg.v.meowtils_red, cfg.v.meowtils_green, cfg.v.meowtils_blue)).getRGB();
/*     */ 
/*     */     
/*  53 */     float customGuiScale = GuiScale.customGuiScale();
/*  54 */     if (GuiScale.customGuiScale() != 1.0F) {
/*  55 */       GlStateManager.func_179094_E();
/*  56 */       GL11.glTranslatef(0.5F / customGuiScale, 0.5F / customGuiScale, 0.0F);
/*  57 */       GlStateManager.func_179121_F();
/*     */     } 
/*     */ 
/*     */     
/*  61 */     Meowtils.fontRenderer.drawStringWithShadow(this.versionTag, versionTagX - 2.0F, versionTagY + 4.0F, versionTagColor, 7.0F);
/*     */ 
/*     */     
/*  64 */     int screenHeightButton = sr.func_78328_b();
/*     */     
/*  66 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/hud_button.png"));
/*     */     
/*  68 */     GlStateManager.func_179147_l();
/*  69 */     GlStateManager.func_179141_d();
/*  70 */     Gui.func_146110_a(6, screenHeightButton - 20, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  71 */     GlStateManager.func_179084_k();
/*  72 */     GlStateManager.func_179118_c();
/*     */     
/*  74 */     Meowtils.fontRenderer.drawStringWithShadow("HUD Editor", 25.0F, (screenHeightButton - 10), -1, 7.0F);
/*     */ 
/*     */     
/*  77 */     GlStateManager.func_179094_E();
/*  78 */     GlStateManager.func_179152_a(scale, scale, 1.0F);
/*  79 */     ModulesPart hovered = null;
/*  80 */     List<ModePart> overlays = new ArrayList<>();
/*     */     
/*  82 */     for (Frame frame : this.frames) {
/*  83 */       frame.renderFrame(this.field_146289_q);
/*  84 */       frame.updatePosition(scaledMouseX, scaledMouseY);
/*     */       
/*  86 */       for (Component comp : frame.getComponents()) {
/*  87 */         comp.updateComponent(scaledMouseX, scaledMouseY);
/*     */         
/*  89 */         if (comp instanceof ModulesPart) {
/*  90 */           ModulesPart part = (ModulesPart)comp;
/*  91 */           if (part.isHovered()) hovered = part;
/*     */           
/*  93 */           for (Component sub : part.getSubcomponents()) {
/*  94 */             if (sub instanceof ModePart) {
/*  95 */               ModePart mode = (ModePart)sub;
/*  96 */               if (mode.modeExpanded()) {
/*  97 */                 overlays.add(mode);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 104 */     for (ModePart overlay : overlays) {
/* 105 */       overlay.renderExpanded();
/*     */     }
/* 107 */     GlStateManager.func_179121_F();
/*     */ 
/*     */     
/* 110 */     if (hovered != null && cfg.v.guiTooltips) {
/* 111 */       Frame parentFrame = hovered.parent;
/* 112 */       if (!parentFrame.isOpen())
/*     */         return; 
/* 114 */       if (hovered.mod != this.lastHoveredModule) {
/* 115 */         this.lastHoveredModule = hovered.mod;
/* 116 */         this.hoverStartTime = System.currentTimeMillis();
/*     */       } 
/* 118 */       if (System.currentTimeMillis() - this.hoverStartTime < 250L) {
/*     */         return;
/*     */       }
/*     */       
/* 122 */       String tooltip = hovered.mod.getTooltip();
/* 123 */       if (tooltip != null && !tooltip.isEmpty()) {
/* 124 */         int mouseXScaled = Mouse.getX() * sr.func_78326_a() / mc.field_71443_c;
/* 125 */         int mouseYScaled = sr.func_78328_b() - Mouse.getY() * sr.func_78328_b() / mc.field_71440_d - 1;
/* 126 */         float guiTooltipscale = getScale() / 2.0F;
/* 127 */         int drawX = (int)((mouseXScaled + 6) / guiTooltipscale);
/* 128 */         int drawY = (int)(mouseYScaled / guiTooltipscale);
/* 129 */         int screenWidth = (int)(sr.func_78326_a() / guiTooltipscale);
/* 130 */         int screenHeight = (int)(sr.func_78328_b() / guiTooltipscale);
/* 131 */         int availableWidth = screenWidth - drawX - 8;
/* 132 */         List<String> lines = new ArrayList<>();
/*     */         
/* 134 */         GL11.glPushMatrix();
/* 135 */         GL11.glScalef(guiTooltipscale, guiTooltipscale, 1.0F);
/*     */         
/* 137 */         float fontScale = 10.5F;
/*     */         
/* 139 */         for (String manualLine : tooltip.split("\n")) {
/* 140 */           StringBuilder currentLine = new StringBuilder();
/* 141 */           for (String word : manualLine.split(" ")) {
/* 142 */             String testLine = (currentLine.length() == 0) ? word : (currentLine + " " + word);
/*     */ 
/*     */             
/* 145 */             if (Meowtils.fontRenderer.getStringWidth(testLine, fontScale) > availableWidth) {
/* 146 */               if (currentLine.length() > 0) {
/* 147 */                 lines.add(currentLine.toString());
/* 148 */                 currentLine = new StringBuilder(word);
/*     */               } else {
/* 150 */                 lines.add(word);
/* 151 */                 currentLine = new StringBuilder();
/*     */               } 
/*     */             } else {
/* 154 */               currentLine = new StringBuilder(testLine);
/*     */             } 
/*     */           } 
/* 157 */           if (currentLine.length() > 0) lines.add(currentLine.toString());
/*     */         
/*     */         } 
/* 160 */         int lineHeight = mc.field_71466_p.field_78288_b + 2;
/*     */ 
/*     */         
/* 163 */         int maxWidth = 0;
/* 164 */         for (String line : lines) {
/* 165 */           maxWidth = (int)Math.max(maxWidth, Meowtils.fontRenderer.getStringWidth(line, fontScale));
/*     */         }
/*     */         
/* 168 */         int totalHeight = lines.size() * lineHeight;
/*     */         
/* 170 */         if (drawX + maxWidth > screenWidth) drawX = screenWidth - maxWidth - 8; 
/* 171 */         if (drawY + totalHeight > screenHeight) drawY = screenHeight - totalHeight - 8; 
/* 172 */         if (drawX < 0) drawX = 0; 
/* 173 */         if (drawY < 0) drawY = 0;
/*     */         
/* 175 */         int padding = 3;
/* 176 */         Gui.func_73734_a(drawX - padding, drawY - padding, drawX + maxWidth + padding, drawY + totalHeight - 2 + padding, (new Color(14, 14, 14)).getRGB());
/* 177 */         for (int i = 0; i < lines.size(); i++) {
/* 178 */           Meowtils.fontRenderer.drawString(lines.get(i), drawX, (drawY + i * lineHeight + 7), -1, fontScale);
/*     */         }
/* 180 */         GL11.glPopMatrix();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
/* 187 */     float scale = getScale();
/* 188 */     int scaledMouseX = (int)(mouseX / scale);
/* 189 */     int scaledMouseY = (int)(mouseY / scale);
/* 190 */     hudEditorClicked(mouseX, mouseY, mouseButton);
/*     */ 
/*     */     
/* 193 */     List<ModePart> overlays = new ArrayList<>();
/* 194 */     for (Frame frame : this.frames) {
/* 195 */       for (Component comp : frame.getComponents()) {
/* 196 */         if (comp instanceof ModulesPart) {
/* 197 */           ModulesPart part = (ModulesPart)comp;
/* 198 */           for (Component sub : part.getSubcomponents()) {
/* 199 */             if (sub instanceof ModePart) {
/* 200 */               ModePart mode = (ModePart)sub;
/* 201 */               if (mode.modeExpanded()) overlays.add(mode);
/*     */             
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 209 */     for (ModePart overlay : overlays) {
/* 210 */       if (overlay.mouseClicked(scaledMouseX, scaledMouseY, mouseButton)) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 215 */     if (ModePart.expandedPart != null) {
/* 216 */       boolean clickedInsideAny = false;
/*     */       
/* 218 */       ModePart expanded = ModePart.expandedPart;
/* 219 */       int boxX = expanded.parent.parent.getX();
/* 220 */       int topY = expanded.parent.parent.getY() + 4 + expanded.offset;
/* 221 */       int boxWidth = 80;
/* 222 */       int boxHeight = 11;
/*     */ 
/*     */       
/* 225 */       int totalHeight = boxHeight;
/* 226 */       if (expanded.modeExpanded()) {
/* 227 */         int extra = (expanded.value.getModes().size() - 1) * 10;
/* 228 */         totalHeight += extra;
/*     */       } 
/*     */ 
/*     */       
/* 232 */       if (scaledMouseX > boxX && scaledMouseX < boxX + boxWidth && scaledMouseY > topY && scaledMouseY < topY + totalHeight) {
/* 233 */         clickedInsideAny = true;
/*     */       }
/*     */ 
/*     */       
/* 237 */       if (!clickedInsideAny) {
/* 238 */         expanded.expanded = false;
/* 239 */         ModePart.expandedPart = null;
/*     */       } 
/*     */     } 
/*     */     
/* 243 */     for (Frame frame : this.frames) {
/* 244 */       if (frame.isWithinHeader(scaledMouseX, scaledMouseY) && mouseButton == 0) {
/* 245 */         frame.setDrag(true);
/* 246 */         frame.dragX = scaledMouseX - frame.getX();
/* 247 */         frame.dragY = scaledMouseY - frame.getY();
/*     */       } 
/* 249 */       if (frame.isWithinHeader(scaledMouseX, scaledMouseY) && mouseButton == 1) {
/* 250 */         frame.setOpen(!frame.isOpen());
/*     */       }
/* 252 */       if (frame.isOpen() && !frame.getComponents().isEmpty()) {
/* 253 */         for (Component component : frame.getComponents()) {
/* 254 */           component.mouseClicked(scaledMouseX, scaledMouseY, mouseButton);
/*     */         }
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void hudEditorClicked(int mouseX, int mouseY, int mouseButton) {
/* 261 */     ScaledResolution sr = new ScaledResolution(this.field_146297_k);
/* 262 */     int screenHeightButton = sr.func_78328_b();
/* 263 */     int buttonX = 6;
/* 264 */     int buttonY = screenHeightButton - 20;
/* 265 */     int buttonW = 72;
/* 266 */     int buttonH = 13;
/*     */     
/* 268 */     if (mouseButton == 0 && mouseX >= buttonX && mouseX <= buttonX + buttonW && mouseY >= buttonY && mouseY <= buttonY + buttonH) {
/* 269 */       this.field_146297_k.func_147108_a(new HUDEditor());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_73869_a(char typedChar, int keyCode) {
/* 274 */     for (Frame frame : this.frames) {
/* 275 */       if (frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
/* 276 */         for (Component component : frame.getComponents()) {
/* 277 */           component.keyTyped(typedChar, keyCode);
/*     */         }
/*     */       }
/*     */     } 
/* 281 */     if (keyCode == 1) {
/* 282 */       this.field_146297_k.func_147108_a((GuiScreen)null);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_146286_b(int mouseX, int mouseY, int state) {
/* 288 */     for (Frame frame : this.frames)
/* 289 */       frame.setDrag(false); 
/*     */   }
/*     */   
/*     */   public boolean func_73868_f() {
/* 293 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private float getScale() {
/* 298 */     float effectiveScale = GuiScale.getEffectiveGuiScale();
/* 299 */     float baseScale = 3.0F;
/*     */     
/* 301 */     return baseScale / effectiveScale * GuiScale.customGuiScale();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_73866_w_() {
/* 307 */     super.func_73866_w_();
/* 308 */     if (this.field_146297_k.field_71460_t.func_147706_e() != null) {
/* 309 */       this.field_146297_k.field_71460_t.func_147706_e().func_148021_a();
/*     */     }
/*     */     try {
/* 312 */       this.field_146297_k.field_71460_t.func_175069_a(new ResourceLocation("shaders/post/blur.json"));
/* 313 */     } catch (Exception e) {
/* 314 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void func_146281_b() {
/* 319 */     super.func_146281_b();
/* 320 */     if (this.field_146297_k.field_71460_t.func_147706_e() != null)
/* 321 */       this.field_146297_k.field_71460_t.func_147706_e().func_148021_a(); 
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/ClickGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */