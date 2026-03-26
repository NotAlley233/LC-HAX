/*     */ package wtf.tatp.meowtils.gui.parts;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.component.Component;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ 
/*     */ public class TogglePart
/*     */   extends Component {
/*     */   private boolean hovered;
/*     */   private BooleanValue op;
/*     */   private ModulesPart parent;
/*     */   private int offset;
/*     */   private int x;
/*     */   private int y;
/*     */   
/*     */   public TogglePart(BooleanValue option, ModulesPart modulesPart, int offset) {
/*  23 */     this.op = option;
/*  24 */     this.parent = modulesPart;
/*  25 */     this.x = modulesPart.parent.getX() + modulesPart.parent.getWidth();
/*  26 */     this.y = modulesPart.parent.getY() + modulesPart.offset;
/*  27 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render() {
/*  32 */     Minecraft mc = Minecraft.func_71410_x();
/*     */     
/*  34 */     boolean above = this.parent.isComponentAbove(this);
/*  35 */     boolean below = this.parent.isComponentBelow(this);
/*  36 */     boolean last = (!this.parent.isModuleBelow() && !this.parent.isComponentBelow(this));
/*  37 */     int x = this.parent.parent.getX() - 1;
/*  38 */     int y = this.parent.parent.getY() + 4 + this.offset;
/*  39 */     int boolean_x = this.parent.parent.getX() + 68;
/*  40 */     int boolean_y = this.parent.parent.getY() + 4 + this.offset;
/*     */     
/*  42 */     String location = (above & below) ? "textures/gui/sub_component_background.png" : ((above & this.parent.isModuleBelow()) ? "textures/gui/sub_component_background_bottom.png" : ((above & last) ? "textures/gui/sub_component_background_last.png" : (below ? "textures/gui/sub_component_background_top.png" : (last ? "textures/gui/sub_component_background_only_last.png" : "textures/gui/sub_component_background_only.png"))));
/*     */     
/*  44 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", location));
/*     */     
/*  46 */     GlStateManager.func_179141_d();
/*  47 */     GlStateManager.func_179147_l();
/*  48 */     Gui.func_146110_a(x, y, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  49 */     GlStateManager.func_179118_c();
/*  50 */     GlStateManager.func_179084_k();
/*     */     
/*  52 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/boolean_disabled.png"));
/*     */     
/*  54 */     GlStateManager.func_179141_d();
/*  55 */     GlStateManager.func_179147_l();
/*  56 */     GL11.glTexParameteri(3553, 10241, 9729);
/*  57 */     GL11.glTexParameteri(3553, 10240, 9729);
/*  58 */     Gui.func_152125_a(boolean_x, boolean_y, 0.0F, 0.0F, 128, 128, 12, 12, 128.0F, 128.0F);
/*  59 */     GL11.glTexParameteri(3553, 10241, 9728);
/*  60 */     GL11.glTexParameteri(3553, 10240, 9728);
/*  61 */     GlStateManager.func_179118_c();
/*  62 */     GlStateManager.func_179084_k();
/*     */     
/*  64 */     if (this.op.getState()) {
/*     */       
/*  66 */       mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/boolean_enabled.png"));
/*     */       
/*  68 */       GlStateManager.func_179141_d();
/*  69 */       GlStateManager.func_179147_l();
/*  70 */       GL11.glTexParameteri(3553, 10241, 9729);
/*  71 */       GL11.glTexParameteri(3553, 10240, 9729);
/*     */       
/*  73 */       float r = cfg.v.meowtils_red / 255.0F;
/*  74 */       float g = cfg.v.meowtils_green / 255.0F;
/*  75 */       float b = cfg.v.meowtils_blue / 255.0F;
/*  76 */       GlStateManager.func_179124_c(r, g, b);
/*     */       
/*  78 */       Gui.func_152125_a(boolean_x, boolean_y, 0.0F, 0.0F, 128, 128, 12, 12, 128.0F, 128.0F);
/*  79 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/*  81 */       mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/boolean_enabled_button.png"));
/*     */       
/*  83 */       Gui.func_152125_a(boolean_x, boolean_y, 0.0F, 0.0F, 128, 128, 12, 12, 128.0F, 128.0F);
/*  84 */       GL11.glTexParameteri(3553, 10241, 9728);
/*  85 */       GL11.glTexParameteri(3553, 10240, 9728);
/*  86 */       GlStateManager.func_179118_c();
/*  87 */       GlStateManager.func_179084_k();
/*     */     } 
/*     */ 
/*     */     
/*  91 */     Meowtils.fontRenderer.drawStringWithShadow(this.op.getName(), (this.parent.parent.getX() + 2), (this.parent.parent.getY() + 12 + this.offset), -1, 5.0F);
/*  92 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOff(int newOff) {
/*  97 */     this.offset = newOff;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateComponent(int mouseX, int mouseY) {
/* 102 */     this.hovered = isMouseOnButton(mouseX, mouseY);
/* 103 */     this.y = this.parent.parent.getY() + this.offset;
/* 104 */     this.x = this.parent.parent.getX();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int button) {
/* 109 */     if (isMouseOnButton(mouseX + 2, mouseY - 3) && button == 0 && this.parent.open) {
/* 110 */       this.op.toggle();
/*     */     }
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isMouseOnButton(int x, int y) {
/* 116 */     return (x > this.x && x < this.x + 84 && y > this.y && y < this.y + 13);
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/parts/TogglePart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */