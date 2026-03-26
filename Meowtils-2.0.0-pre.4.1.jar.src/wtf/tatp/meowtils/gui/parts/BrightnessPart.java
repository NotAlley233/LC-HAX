/*     */ package wtf.tatp.meowtils.gui.parts;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.gui.component.Component;
/*     */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*     */ 
/*     */ public class BrightnessPart
/*     */   extends Component {
/*     */   private boolean hovered;
/*     */   private boolean dragging = false;
/*     */   private final BrightnessValue value;
/*     */   private final ModulesPart parent;
/*     */   private int offset;
/*     */   private int x;
/*     */   private int y;
/*     */   
/*     */   public BrightnessPart(BrightnessValue value, ModulesPart modulesPart, int offset) {
/*  23 */     this.value = value;
/*  24 */     this.parent = modulesPart;
/*  25 */     this.offset = offset;
/*  26 */     this.x = modulesPart.parent.getX();
/*  27 */     this.y = modulesPart.parent.getY() + offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render() {
/*  32 */     Minecraft mc = Minecraft.func_71410_x();
/*  33 */     int width = this.parent.parent.getWidth() - 18;
/*  34 */     int baseX = this.parent.parent.getX() - 1;
/*  35 */     int baseY = this.parent.parent.getY() + 4 + this.offset;
/*  36 */     boolean above = this.parent.isComponentAbove(this);
/*  37 */     boolean below = this.parent.isComponentBelow(this);
/*  38 */     boolean last = (!this.parent.isModuleBelow() && !this.parent.isComponentBelow(this));
/*  39 */     int sliderX = this.parent.parent.getX() + 3;
/*  40 */     int sliderY = this.parent.parent.getY() + this.offset + 6;
/*  41 */     int sliderWidth = width - 7;
/*  42 */     int sliderHeight = 6;
/*  43 */     float percent = (float)((this.value.get() - 0.0D) / 100.0D);
/*  44 */     float circleX = sliderX + percent * sliderWidth;
/*  45 */     float circleY = sliderY + sliderHeight / 2.0F;
/*  46 */     int buttonScale = 8;
/*     */     
/*  48 */     float renderX = circleX - buttonScale / 2.0F;
/*  49 */     float renderY = circleY - buttonScale / 2.0F;
/*     */ 
/*     */     
/*  52 */     String location = (above & below) ? "textures/gui/sub_component_background.png" : ((above & this.parent.isModuleBelow()) ? "textures/gui/sub_component_background_bottom.png" : ((above & last) ? "textures/gui/sub_component_background_last.png" : (below ? "textures/gui/sub_component_background_top.png" : (last ? "textures/gui/sub_component_background_only_last.png" : "textures/gui/sub_component_background_only.png"))));
/*  53 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", location));
/*  54 */     GlStateManager.func_179141_d();
/*  55 */     GlStateManager.func_179147_l();
/*  56 */     Gui.func_146110_a(baseX, baseY, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  57 */     GlStateManager.func_179118_c();
/*  58 */     GlStateManager.func_179084_k();
/*     */ 
/*     */     
/*  61 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/colorpart/blank_track.png"));
/*  62 */     int rgb = this.value.getLink().getPureHueRGB();
/*  63 */     int r = rgb >> 16 & 0xFF;
/*  64 */     int g = rgb >> 8 & 0xFF;
/*  65 */     int b = rgb & 0xFF;
/*  66 */     GlStateManager.func_179141_d();
/*  67 */     GlStateManager.func_179147_l();
/*  68 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  69 */     GL11.glTexParameteri(3553, 10241, 9729);
/*  70 */     GL11.glTexParameteri(3553, 10240, 9729);
/*  71 */     GlStateManager.func_179131_c(r / 255.0F, g / 255.0F, b / 255.0F, 1.0F);
/*  72 */     Gui.func_146110_a(baseX, baseY, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  73 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  74 */     GL11.glTexParameteri(3553, 10241, 9728);
/*  75 */     GL11.glTexParameteri(3553, 10240, 9728);
/*  76 */     GlStateManager.func_179084_k();
/*  77 */     GlStateManager.func_179118_c();
/*     */ 
/*     */     
/*  80 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/colorpart/brightness_fade.png"));
/*  81 */     GlStateManager.func_179141_d();
/*  82 */     GlStateManager.func_179147_l();
/*  83 */     GL11.glTexParameteri(3553, 10241, 9729);
/*  84 */     GL11.glTexParameteri(3553, 10240, 9729);
/*  85 */     Gui.func_146110_a(baseX, baseY, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  86 */     GL11.glTexParameteri(3553, 10241, 9728);
/*  87 */     GL11.glTexParameteri(3553, 10240, 9728);
/*  88 */     GlStateManager.func_179084_k();
/*  89 */     GlStateManager.func_179118_c();
/*     */ 
/*     */     
/*  92 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/colorpart/track_overlay.png"));
/*  93 */     GlStateManager.func_179141_d();
/*  94 */     GlStateManager.func_179147_l();
/*  95 */     GL11.glTexParameteri(3553, 10241, 9729);
/*  96 */     GL11.glTexParameteri(3553, 10240, 9729);
/*  97 */     Gui.func_146110_a(baseX, baseY, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  98 */     GL11.glTexParameteri(3553, 10241, 9728);
/*  99 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 100 */     GlStateManager.func_179084_k();
/* 101 */     GlStateManager.func_179118_c();
/*     */ 
/*     */     
/* 104 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/colorpart/color_button.png"));
/* 105 */     GlStateManager.func_179141_d();
/* 106 */     GlStateManager.func_179147_l();
/* 107 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 108 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 109 */     GL11.glTexParameteri(3553, 10240, 9729);
/* 110 */     Gui.func_152125_a((int)renderX, (int)renderY + 3, 0.0F, 0.0F, 64, 64, buttonScale, buttonScale, 64.0F, 64.0F);
/* 111 */     GL11.glTexParameteri(3553, 10241, 9728);
/* 112 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 113 */     GlStateManager.func_179084_k();
/* 114 */     GlStateManager.func_179118_c();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOff(int newOff) {
/* 119 */     this.offset = newOff;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateComponent(int mouseX, int mouseY) {
/* 124 */     this.hovered = isMouseOnSlider(mouseX, mouseY);
/* 125 */     this.y = this.parent.parent.getY() + this.offset;
/* 126 */     this.x = this.parent.parent.getX();
/*     */     
/* 128 */     int sliderX = this.parent.parent.getX() + 3;
/* 129 */     int sliderWidth = this.parent.parent.getWidth() - 18 - 7;
/*     */     
/* 131 */     if (this.dragging && Mouse.isButtonDown(0)) {
/* 132 */       float percent = (mouseX - sliderX) / sliderWidth;
/* 133 */       percent = Math.max(0.0F, Math.min(1.0F, percent));
/* 134 */       double range = 100.0D;
/* 135 */       this.value.set(0.0D + percent * range);
/* 136 */     } else if (!Mouse.isButtonDown(0)) {
/* 137 */       this.dragging = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int button) {
/* 143 */     if (button != 0 || !this.parent.open) return false;
/*     */     
/* 145 */     if (isMouseOnSlider(mouseX, mouseY)) {
/* 146 */       int sliderX = this.parent.parent.getX() + 3;
/* 147 */       int sliderWidth = this.parent.parent.getWidth() - 18 - 7;
/* 148 */       float percent = (mouseX - sliderX) / sliderWidth;
/* 149 */       percent = Math.max(0.0F, Math.min(1.0F, percent));
/* 150 */       double range = 100.0D;
/* 151 */       this.value.set(0.0D + percent * range);
/*     */       
/* 153 */       this.dragging = true;
/* 154 */       return true;
/*     */     } 
/*     */     
/* 157 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isMouseOnSlider(int mouseX, int mouseY) {
/* 161 */     int sliderX = this.parent.parent.getX() + 3;
/* 162 */     int sliderY = this.parent.parent.getY() + this.offset + 4;
/* 163 */     int sliderWidth = this.parent.parent.getWidth() - 18 - 7;
/* 164 */     int sliderHeight = 11;
/*     */     
/* 166 */     return (mouseX >= sliderX && mouseX <= sliderX + sliderWidth && mouseY >= sliderY && mouseY <= sliderY + sliderHeight);
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/parts/BrightnessPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */