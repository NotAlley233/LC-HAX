/*     */ package wtf.tatp.meowtils.gui.parts;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.component.Component;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.util.Render;
/*     */ 
/*     */ public class SliderPart extends Component {
/*     */   private boolean hovered;
/*     */   private boolean dragging = false;
/*     */   private NumberValue value;
/*     */   private ModulesPart parent;
/*     */   private int offset;
/*     */   private int x;
/*     */   private int y;
/*     */   
/*     */   public SliderPart(NumberValue value, ModulesPart modulesPart, int offset) {
/*  26 */     this.value = value;
/*  27 */     this.parent = modulesPart;
/*  28 */     this.x = modulesPart.parent.getX() + modulesPart.parent.getWidth();
/*  29 */     this.y = modulesPart.parent.getY() + modulesPart.offset;
/*  30 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render() {
/*  35 */     Minecraft mc = Minecraft.func_71410_x();
/*  36 */     int newWidth = this.parent.parent.getWidth() - 18;
/*  37 */     int drag = -2 + (int)((this.value.get() - this.value.getMin()) / (this.value.getMax() - this.value.getMin()) * (newWidth - 8));
/*  38 */     float sliderHeight = 2.5F;
/*  39 */     boolean above = this.parent.isComponentAbove(this);
/*  40 */     boolean below = this.parent.isComponentBelow(this);
/*  41 */     boolean last = (!this.parent.isModuleBelow() && !this.parent.isComponentBelow(this));
/*  42 */     int x = this.parent.parent.getX() - 1;
/*  43 */     int y = this.parent.parent.getY() + 4 + this.offset;
/*  44 */     float circleX = (this.parent.parent.getX() + 5 + drag);
/*  45 */     float circleY = (this.parent.parent.getY() + this.offset + 11) + sliderHeight / 2.0F;
/*     */ 
/*     */ 
/*     */     
/*  49 */     String location = (above & below) ? "textures/gui/sub_component_background.png" : ((above & this.parent.isModuleBelow()) ? "textures/gui/sub_component_background_bottom.png" : ((above & last) ? "textures/gui/sub_component_background_last.png" : (below ? "textures/gui/sub_component_background_top.png" : (last ? "textures/gui/sub_component_background_only_last.png" : "textures/gui/sub_component_background_only.png"))));
/*  50 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", location));
/*  51 */     GlStateManager.func_179141_d();
/*  52 */     GlStateManager.func_179147_l();
/*  53 */     Gui.func_146110_a(x, y, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  54 */     GlStateManager.func_179118_c();
/*  55 */     GlStateManager.func_179084_k();
/*     */ 
/*     */ 
/*     */     
/*  59 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/sliderpart/slider_track.png"));
/*  60 */     GlStateManager.func_179141_d();
/*  61 */     GlStateManager.func_179147_l();
/*  62 */     Gui.func_146110_a(x, y, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  63 */     GlStateManager.func_179118_c();
/*  64 */     GlStateManager.func_179084_k();
/*     */ 
/*     */ 
/*     */     
/*  68 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/sliderpart/slider_end.png"));
/*  69 */     GlStateManager.func_179141_d();
/*  70 */     GlStateManager.func_179147_l();
/*  71 */     float r = cfg.v.meowtils_red / 255.0F;
/*  72 */     float g = cfg.v.meowtils_green / 255.0F;
/*  73 */     float b = cfg.v.meowtils_blue / 255.0F;
/*  74 */     GlStateManager.func_179124_c(r, g, b);
/*  75 */     Gui.func_146110_a(x, y, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  76 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  77 */     GlStateManager.func_179118_c();
/*  78 */     GlStateManager.func_179084_k();
/*     */ 
/*     */     
/*  81 */     Render.drawRectFloat((this.parent.parent.getX() + 4), (this.parent.parent.getY() + this.offset) + 11.25F, (this.parent.parent.getX() + 5 + drag), (this.parent.parent.getY() + this.offset) + 11.5F + sliderHeight, (new Color(cfg.v.meowtils_red, cfg.v.meowtils_green, cfg.v.meowtils_blue)).getRGB());
/*     */ 
/*     */     
/*  84 */     int buttonScale = 5;
/*  85 */     float renderX = circleX - buttonScale / 2.0F;
/*  86 */     float renderY = circleY - buttonScale / 2.0F;
/*     */     
/*  88 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/sliderpart/slider_button.png"));
/*  89 */     GlStateManager.func_179141_d();
/*  90 */     GlStateManager.func_179147_l();
/*  91 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  92 */     GL11.glTexParameteri(3553, 10241, 9729);
/*  93 */     GL11.glTexParameteri(3553, 10240, 9729);
/*  94 */     Gui.func_152125_a((int)renderX + 1, (int)renderY + 1, 0.0F, 0.0F, 64, 64, buttonScale, buttonScale, 64.0F, 64.0F);
/*  95 */     GL11.glTexParameteri(3553, 10241, 9728);
/*  96 */     GL11.glTexParameteri(3553, 10240, 9728);
/*  97 */     GlStateManager.func_179084_k();
/*  98 */     GlStateManager.func_179118_c();
/*     */ 
/*     */ 
/*     */     
/* 102 */     int leftX = this.parent.parent.getX() + 2;
/* 103 */     int baseY = this.parent.parent.getY() + this.offset + 9;
/* 104 */     int rightEdge = this.parent.parent.getX() + this.parent.parent.getWidth() - 22;
/*     */ 
/*     */     
/* 107 */     String nameText = this.value.getName();
/* 108 */     Meowtils.fontRenderer.drawStringWithShadow(nameText, leftX, baseY, -1, 5.0F);
/*     */ 
/*     */     
/* 111 */     String formatted = this.value.getFormattedValue();
/* 112 */     String valueSuffix = (this.value.getValueType() != null) ? (" " + this.value.getValueType()) : "";
/* 113 */     String valueText = formatted + valueSuffix;
/*     */     
/* 115 */     int valueWidth = (int)Meowtils.fontRenderer.getStringWidth(valueText, 5.0F);
/* 116 */     int valueX = rightEdge - valueWidth;
/* 117 */     Meowtils.fontRenderer.drawStringWithShadow(valueText, valueX, baseY, -1, 5.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOff(int newOff) {
/* 122 */     this.offset = newOff;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateComponent(int mouseX, int mouseY) {
/* 127 */     this.hovered = isMouseOnSlider(mouseX, mouseY);
/* 128 */     this.y = this.parent.parent.getY() + this.offset;
/* 129 */     this.x = this.parent.parent.getX();
/*     */     
/* 131 */     int sliderX = this.parent.parent.getX() + 4;
/* 132 */     int sliderWidth = this.parent.parent.getWidth() - 18 - 8;
/*     */     
/* 134 */     if (this.dragging && Mouse.isButtonDown(0)) {
/* 135 */       double percent = (mouseX - sliderX) / sliderWidth;
/* 136 */       percent = Math.max(0.0D, Math.min(percent, 1.0D));
/* 137 */       double range = this.value.getMax() - this.value.getMin();
/* 138 */       this.value.set(this.value.getMin() + percent * range);
/* 139 */     } else if (!Mouse.isButtonDown(0)) {
/* 140 */       this.dragging = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int button) {
/* 145 */     if (button != 0 || !this.parent.open) return false;
/*     */     
/* 147 */     if (isMouseOnSlider(mouseX, mouseY)) {
/* 148 */       int sliderX = this.parent.parent.getX() + 4;
/* 149 */       int sliderWidth = this.parent.parent.getWidth() - 18 - 8;
/* 150 */       double percent = (mouseX - sliderX) / sliderWidth;
/* 151 */       percent = Math.max(0.0D, Math.min(percent, 1.0D));
/* 152 */       double range = this.value.getMax() - this.value.getMin();
/* 153 */       this.value.set(this.value.getMin() + percent * range);
/*     */       
/* 155 */       this.dragging = true;
/* 156 */       return true;
/*     */     } 
/*     */     
/* 159 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isMouseOnSlider(int mouseX, int mouseY) {
/* 163 */     int sliderX = this.parent.parent.getX() + 3;
/* 164 */     int sliderY = this.parent.parent.getY() + this.offset + 4;
/* 165 */     int sliderWidth = this.parent.parent.getWidth() - 18 - 7;
/* 166 */     int sliderHeight = 11;
/*     */     
/* 168 */     return (mouseX >= sliderX && mouseX <= sliderX + sliderWidth && mouseY >= sliderY && mouseY <= sliderY + sliderHeight);
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/parts/SliderPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */