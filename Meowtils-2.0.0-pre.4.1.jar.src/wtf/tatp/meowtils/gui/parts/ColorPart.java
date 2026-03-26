/*     */ package wtf.tatp.meowtils.gui.parts;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.gui.component.Component;
/*     */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*     */ 
/*     */ public class ColorPart
/*     */   extends Component {
/*     */   private boolean hovered;
/*     */   private boolean dragging = false;
/*     */   private final ColorValue value;
/*     */   private final ModulesPart parent;
/*     */   private int offset;
/*     */   private int x;
/*     */   private int y;
/*     */   
/*     */   public ColorPart(ColorValue value, ModulesPart modulesPart, int offset) {
/*  24 */     this.value = value;
/*  25 */     this.parent = modulesPart;
/*  26 */     this.offset = offset;
/*  27 */     this.x = modulesPart.parent.getX();
/*  28 */     this.y = modulesPart.parent.getY() + offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render() {
/*  33 */     Minecraft mc = Minecraft.func_71410_x();
/*  34 */     int width = this.parent.parent.getWidth() - 18;
/*  35 */     int baseX = this.parent.parent.getX() - 1;
/*  36 */     int baseY = this.parent.parent.getY() + 4 + this.offset;
/*  37 */     boolean above = this.parent.isComponentAbove(this);
/*  38 */     boolean below = this.parent.isComponentBelow(this);
/*  39 */     boolean last = (!this.parent.isModuleBelow() && !this.parent.isComponentBelow(this));
/*  40 */     int sliderX = this.parent.parent.getX() + 3;
/*  41 */     int sliderY = this.parent.parent.getY() + this.offset + 6;
/*  42 */     int sliderWidth = width - 7;
/*  43 */     int sliderHeight = 6;
/*  44 */     float percent = (float)((this.value.get() - 0.0D) / 360.0D);
/*  45 */     float circleX = sliderX + percent * sliderWidth;
/*  46 */     float circleY = sliderY + sliderHeight / 2.0F;
/*  47 */     int buttonScale = 8;
/*     */     
/*  49 */     float renderX = circleX - buttonScale / 2.0F;
/*  50 */     float renderY = circleY - buttonScale / 2.0F;
/*     */ 
/*     */     
/*  53 */     String location = (above & below) ? "textures/gui/sub_component_background.png" : ((above & this.parent.isModuleBelow()) ? "textures/gui/sub_component_background_bottom.png" : ((above & last) ? "textures/gui/sub_component_background_last.png" : (below ? "textures/gui/sub_component_background_top.png" : (last ? "textures/gui/sub_component_background_only_last.png" : "textures/gui/sub_component_background_only.png"))));
/*  54 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", location));
/*  55 */     GlStateManager.func_179141_d();
/*  56 */     GlStateManager.func_179147_l();
/*  57 */     Gui.func_146110_a(baseX, baseY, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  58 */     GlStateManager.func_179118_c();
/*  59 */     GlStateManager.func_179084_k();
/*     */ 
/*     */     
/*  62 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/colorpart/color_track.png"));
/*  63 */     GlStateManager.func_179141_d();
/*  64 */     GlStateManager.func_179147_l();
/*  65 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  66 */     GL11.glTexParameteri(3553, 10241, 9729);
/*  67 */     GL11.glTexParameteri(3553, 10240, 9729);
/*  68 */     Gui.func_146110_a(baseX, baseY, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  69 */     GL11.glTexParameteri(3553, 10241, 9728);
/*  70 */     GL11.glTexParameteri(3553, 10240, 9728);
/*  71 */     GlStateManager.func_179084_k();
/*  72 */     GlStateManager.func_179118_c();
/*     */ 
/*     */     
/*  75 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/colorpart/color_button.png"));
/*  76 */     GlStateManager.func_179141_d();
/*  77 */     GlStateManager.func_179147_l();
/*  78 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  79 */     GL11.glTexParameteri(3553, 10241, 9729);
/*  80 */     GL11.glTexParameteri(3553, 10240, 9729);
/*  81 */     Gui.func_152125_a((int)renderX, (int)renderY + 3, 0.0F, 0.0F, 64, 64, buttonScale, buttonScale, 64.0F, 64.0F);
/*  82 */     GL11.glTexParameteri(3553, 10241, 9728);
/*  83 */     GL11.glTexParameteri(3553, 10240, 9728);
/*  84 */     GlStateManager.func_179084_k();
/*  85 */     GlStateManager.func_179118_c();
/*     */ 
/*     */     
/*  88 */     Meowtils.fontRenderer.drawStringWithShadow(this.value.getName(), (baseX + 4), (baseY + 5), -1, 4.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOff(int newOff) {
/*  93 */     this.offset = newOff;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateComponent(int mouseX, int mouseY) {
/*  98 */     this.hovered = isMouseOnSlider(mouseX, mouseY);
/*  99 */     this.y = this.parent.parent.getY() + this.offset;
/* 100 */     this.x = this.parent.parent.getX();
/*     */     
/* 102 */     int sliderX = this.parent.parent.getX() + 3;
/* 103 */     int sliderWidth = this.parent.parent.getWidth() - 18 - 7;
/*     */     
/* 105 */     if (this.dragging && Mouse.isButtonDown(0)) {
/* 106 */       float percent = (mouseX - sliderX) / sliderWidth;
/* 107 */       percent = Math.max(0.0F, Math.min(1.0F, percent));
/* 108 */       double range = 360.0D;
/* 109 */       this.value.set(0.0D + percent * range);
/* 110 */     } else if (!Mouse.isButtonDown(0)) {
/* 111 */       this.dragging = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int button) {
/* 117 */     if (button != 0 || !this.parent.open) return false;
/*     */     
/* 119 */     if (isMouseOnSlider(mouseX, mouseY)) {
/* 120 */       int sliderX = this.parent.parent.getX() + 3;
/* 121 */       int sliderWidth = this.parent.parent.getWidth() - 18 - 7;
/* 122 */       float percent = (mouseX - sliderX) / sliderWidth;
/* 123 */       percent = Math.max(0.0F, Math.min(1.0F, percent));
/* 124 */       double range = 360.0D;
/* 125 */       this.value.set(0.0D + percent * range);
/*     */       
/* 127 */       this.dragging = true;
/* 128 */       return true;
/*     */     } 
/*     */     
/* 131 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isMouseOnSlider(int mouseX, int mouseY) {
/* 135 */     int sliderX = this.parent.parent.getX() + 3;
/* 136 */     int sliderY = this.parent.parent.getY() + this.offset + 4;
/* 137 */     int sliderWidth = this.parent.parent.getWidth() - 18 - 7;
/* 138 */     int sliderHeight = 11;
/*     */     
/* 140 */     return (mouseX >= sliderX && mouseX <= sliderX + sliderWidth && mouseY >= sliderY && mouseY <= sliderY + sliderHeight);
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/parts/ColorPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */