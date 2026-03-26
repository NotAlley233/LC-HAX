/*     */ package wtf.tatp.meowtils.gui.component;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.parts.ModulesPart;
/*     */ import wtf.tatp.meowtils.util.Wrapper;
/*     */ 
/*     */ public class Frame
/*     */ {
/*     */   public ArrayList<Component> components;
/*     */   public Module.Category category;
/*     */   public boolean open;
/*     */   private int width;
/*     */   private int y;
/*     */   private int x;
/*     */   private int barHeight;
/*     */   private boolean isDragging;
/*     */   public int dragX;
/*     */   public int dragY;
/*     */   private boolean wasDragging = false;
/*     */   
/*     */   public Frame(Module.Category cat) {
/*  33 */     this.components = new ArrayList<>();
/*  34 */     this.category = cat;
/*  35 */     this.width = 85;
/*  36 */     this.barHeight = 11;
/*  37 */     this.dragX = 0;
/*     */     
/*  39 */     loadFromConfig();
/*     */     
/*  41 */     int tY = this.barHeight;
/*  42 */     for (Module mod : Module.getCategoryModules(this.category)) {
/*  43 */       if (mod.getName().equalsIgnoreCase("ClickGUI"))
/*  44 */         continue;  this.components.add(new ModulesPart(mod, this, tY));
/*  45 */       tY += 12;
/*     */     } 
/*     */     
/*  48 */     this.isDragging = false;
/*     */   }
/*     */   
/*  51 */   public ArrayList<Component> getComponents() { return this.components; }
/*  52 */   public int getX() { return this.x; }
/*  53 */   public int getY() { return this.y; } public int getWidth() {
/*  54 */     return this.width;
/*     */   }
/*  56 */   public void setX(int newX) { this.x = newX; }
/*  57 */   public void setY(int newY) { this.y = newY; } public void setDrag(boolean drag) {
/*  58 */     this.isDragging = drag;
/*     */   } public boolean isOpen() {
/*  60 */     return this.open;
/*     */   } public void setOpen(boolean open) {
/*  62 */     this.open = open;
/*  63 */     saveToConfig();
/*     */   }
/*     */   
/*     */   public void updatePosition(int mouseX, int mouseY) {
/*  67 */     if (this.isDragging) {
/*  68 */       setX(mouseX - this.dragX);
/*  69 */       setY(mouseY - this.dragY);
/*     */     } 
/*     */     
/*  72 */     if (this.wasDragging && !this.isDragging) {
/*  73 */       saveToConfig();
/*     */     }
/*  75 */     this.wasDragging = this.isDragging;
/*     */   }
/*     */   
/*     */   public boolean isWithinHeader(int x, int y) {
/*  79 */     return (x >= this.x - 1 && x <= this.x + this.width - 19 && y >= this.y - 3 && y <= this.y + this.barHeight + 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderFrame(FontRenderer fontRenderer) {
/*  84 */     Minecraft mc = Minecraft.func_71410_x();
/*  85 */     this.width = 100;
/*     */ 
/*     */     
/*  88 */     ResourceLocation category = new ResourceLocation("meowtils", this.open ? "textures/gui/category_expanded.png" : "textures/gui/category_not_expanded.png");
/*  89 */     mc.func_110434_K().func_110577_a(category);
/*  90 */     GlStateManager.func_179147_l();
/*  91 */     GlStateManager.func_179141_d();
/*  92 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  93 */     Gui.func_146110_a(this.x - 1, this.y - 3, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  94 */     GlStateManager.func_179118_c();
/*  95 */     GlStateManager.func_179084_k();
/*     */ 
/*     */     
/*  98 */     GL11.glPushMatrix();
/*  99 */     int centeredX = this.x;
/* 100 */     Color categoryColor = new Color(255, 255, 255);
/* 101 */     Meowtils.fontRenderer.drawStringWithShadow(this.category.name(), (centeredX + 15), (this.y + 9), categoryColor.getRGB(), 9.0F);
/*     */ 
/*     */     
/* 104 */     ResourceLocation arrow = new ResourceLocation("meowtils", this.open ? "textures/gui/arrow_up.png" : "textures/gui/arrow_down.png");
/*     */     
/* 106 */     mc.func_110434_K().func_110577_a(arrow);
/* 107 */     GlStateManager.func_179147_l();
/* 108 */     GlStateManager.func_179141_d();
/* 109 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 110 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 111 */     GL11.glTexParameteri(3553, 10240, 9729);
/* 112 */     Gui.func_146110_a(this.x + 70, this.y + (this.open ? 2 : 3), 0.0F, 0.0F, 5, 5, 5.0F, 5.0F);
/* 113 */     GL11.glTexParameteri(3553, 10241, 9728);
/* 114 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 115 */     GL11.glPopMatrix();
/*     */     
/* 117 */     if (this.open && !this.components.isEmpty()) {
/* 118 */       for (Component component : this.components) {
/* 119 */         GlStateManager.func_179094_E();
/* 120 */         component.render();
/* 121 */         GlStateManager.func_179121_F();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 126 */     String categoryName = this.category.name().toLowerCase();
/* 127 */     ResourceLocation icon = new ResourceLocation("meowtils", "textures/gui/icons/" + categoryName + ".png");
/*     */     try {
/* 129 */       Wrapper.getMinecraft().func_110442_L().func_110536_a(icon);
/* 130 */     } catch (IOException e) {
/* 131 */       icon = new ResourceLocation("meowtils", "textures/gui/icons/default.png");
/*     */     } 
/*     */     
/* 134 */     GL11.glPushAttrib(1048575);
/* 135 */     Wrapper.getMinecraft().func_110434_K().func_110577_a(icon);
/* 136 */     GL11.glEnable(3042);
/* 137 */     GL11.glBlendFunc(770, 771);
/* 138 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/* 139 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 140 */     GL11.glTexParameteri(3553, 10240, 9729);
/* 141 */     Gui.func_152125_a(this.x + 3, this.y + 1, 0.0F, 0.0F, 26, 26, 9, 9, 26.0F, 26.0F);
/* 142 */     GL11.glTexParameteri(3553, 10241, 9728);
/* 143 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 144 */     GL11.glPopAttrib();
/*     */   }
/*     */   
/*     */   public void refresh() {
/* 148 */     int off = this.barHeight;
/* 149 */     for (Component comp : this.components) {
/* 150 */       comp.setOff(off);
/* 151 */       off += comp.getHeight();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadFromConfig() {
/* 157 */     switch (this.category) {
/*     */       case Meowtils:
/* 159 */         this.x = cfg.v.meowtilsCategoryX;
/* 160 */         this.y = cfg.v.meowtilsCategoryY;
/* 161 */         this.open = cfg.v.meowtilsCategoryExpanded;
/*     */         return;
/*     */       case Hypixel:
/* 164 */         this.x = cfg.v.hypixelCategoryX;
/* 165 */         this.y = cfg.v.hypixelCategoryY;
/* 166 */         this.open = cfg.v.hypixelCategoryExpanded;
/*     */         return;
/*     */       case Bedwars:
/* 169 */         this.x = cfg.v.bedwarsCategoryX;
/* 170 */         this.y = cfg.v.bedwarsCategoryY;
/* 171 */         this.open = cfg.v.bedwarsCategoryExpanded;
/*     */         return;
/*     */       case Skywars:
/* 174 */         this.x = cfg.v.skywarsCategoryX;
/* 175 */         this.y = cfg.v.skywarsCategoryY;
/* 176 */         this.open = cfg.v.skywarsCategoryExpanded;
/*     */         return;
/*     */       case Render:
/* 179 */         this.x = cfg.v.renderCategoryX;
/* 180 */         this.y = cfg.v.renderCategoryY;
/* 181 */         this.open = cfg.v.renderCategoryExpanded;
/*     */         return;
/*     */       case Antisnipe:
/* 184 */         this.x = cfg.v.antisnipeCategoryX;
/* 185 */         this.y = cfg.v.antisnipeCategoryY;
/* 186 */         this.open = cfg.v.antisnipeCategoryExpanded;
/*     */         return;
/*     */       case Utility:
/* 189 */         this.x = cfg.v.utilityCategoryX;
/* 190 */         this.y = cfg.v.utilityCategoryY;
/* 191 */         this.open = cfg.v.utilityCategoryExpanded;
/*     */         return;
/*     */       case Advanced:
/* 194 */         this.x = cfg.v.advancedCategoryX;
/* 195 */         this.y = cfg.v.advancedCategoryY;
/* 196 */         this.open = cfg.v.advancedCategoryExpanded;
/*     */         return;
/*     */     } 
/* 199 */     this.x = 5;
/* 200 */     this.y = 5;
/* 201 */     this.open = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void saveToConfig() {
/* 207 */     switch (this.category) {
/*     */       case Meowtils:
/* 209 */         cfg.v.meowtilsCategoryX = this.x;
/* 210 */         cfg.v.meowtilsCategoryY = this.y;
/* 211 */         cfg.v.meowtilsCategoryExpanded = this.open;
/*     */         break;
/*     */       case Hypixel:
/* 214 */         cfg.v.hypixelCategoryX = this.x;
/* 215 */         cfg.v.hypixelCategoryY = this.y;
/* 216 */         cfg.v.hypixelCategoryExpanded = this.open;
/*     */         break;
/*     */       case Skywars:
/* 219 */         cfg.v.skywarsCategoryX = this.x;
/* 220 */         cfg.v.skywarsCategoryY = this.y;
/* 221 */         cfg.v.skywarsCategoryExpanded = this.open;
/*     */         break;
/*     */       case Bedwars:
/* 224 */         cfg.v.bedwarsCategoryX = this.x;
/* 225 */         cfg.v.bedwarsCategoryY = this.y;
/* 226 */         cfg.v.bedwarsCategoryExpanded = this.open;
/*     */         break;
/*     */       case Render:
/* 229 */         cfg.v.renderCategoryX = this.x;
/* 230 */         cfg.v.renderCategoryY = this.y;
/* 231 */         cfg.v.renderCategoryExpanded = this.open;
/*     */         break;
/*     */       case Antisnipe:
/* 234 */         cfg.v.antisnipeCategoryX = this.x;
/* 235 */         cfg.v.antisnipeCategoryY = this.y;
/* 236 */         cfg.v.antisnipeCategoryExpanded = this.open;
/*     */         break;
/*     */       case Utility:
/* 239 */         cfg.v.utilityCategoryX = this.x;
/* 240 */         cfg.v.utilityCategoryY = this.y;
/* 241 */         cfg.v.utilityCategoryExpanded = this.open;
/*     */         break;
/*     */       case Advanced:
/* 244 */         cfg.v.advancedCategoryX = this.x;
/* 245 */         cfg.v.advancedCategoryY = this.y;
/* 246 */         cfg.v.advancedCategoryExpanded = this.open;
/*     */         break;
/*     */     } 
/* 249 */     cfg.save();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/component/Frame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */