/*     */ package wtf.tatp.meowtils.gui.parts;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.gui.GuiScale;
/*     */ import wtf.tatp.meowtils.gui.component.Component;
/*     */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*     */ 
/*     */ public class ModePart
/*     */   extends Component {
/*     */   public final ArrayValue value;
/*     */   public final ModulesPart parent;
/*     */   private final String name;
/*     */   public int offset;
/*     */   private int x;
/*     */   private int y;
/*     */   private boolean hovered;
/*     */   public boolean expanded;
/*  25 */   public static ModePart expandedPart = null;
/*     */   public boolean modeExpanded() {
/*  27 */     return this.expanded;
/*     */   }
/*     */   
/*     */   public ModePart(ArrayValue value, ModulesPart parent, int offset, String name) {
/*  31 */     this.value = value;
/*  32 */     this.parent = parent;
/*  33 */     this.offset = offset;
/*  34 */     this.name = name;
/*  35 */     this.x = parent.parent.getX();
/*  36 */     this.y = parent.parent.getY() + offset;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render() {
/*  41 */     Minecraft mc = Minecraft.func_71410_x();
/*  42 */     boolean above = this.parent.isComponentAbove(this);
/*  43 */     boolean below = this.parent.isComponentBelow(this);
/*  44 */     boolean last = (!this.parent.isModuleBelow() && !this.parent.isComponentBelow(this));
/*  45 */     int x = this.parent.parent.getX() - 1;
/*  46 */     int y = this.parent.parent.getY() + 4 + this.offset;
/*     */ 
/*     */     
/*  49 */     String location = (above && below) ? "textures/gui/sub_component_background.png" : ((above && this.parent.isModuleBelow()) ? "textures/gui/sub_component_background_bottom.png" : ((above && last) ? "textures/gui/sub_component_background_last.png" : (below ? "textures/gui/sub_component_background_top.png" : (last ? "textures/gui/sub_component_background_only_last.png" : "textures/gui/sub_component_background_only.png"))));
/*  50 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", location));
/*  51 */     GlStateManager.func_179141_d();
/*  52 */     GlStateManager.func_179147_l();
/*  53 */     Gui.func_146110_a(x, y, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*  54 */     GlStateManager.func_179118_c();
/*  55 */     GlStateManager.func_179084_k();
/*     */ 
/*     */     
/*  58 */     String texture = (this.expanded && this.value.getModes().size() >= 2) ? "textures/gui/modepart/mode_top.png" : "textures/gui/modepart/mode_not_expanded.png";
/*  59 */     renderBox(x, y, this.name + " - " + this.value.getValue(), texture);
/*     */ 
/*     */     
/*  62 */     String arrow = this.expanded ? "textures/gui/modepart/mode_arrow_down.png" : "textures/gui/modepart/mode_arrow_up.png";
/*  63 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", arrow));
/*  64 */     GlStateManager.func_179141_d();
/*  65 */     GlStateManager.func_179147_l();
/*  66 */     GlStateManager.func_179124_c(255.0F, 255.0F, 255.0F);
/*  67 */     GL11.glTexParameteri(3553, 10241, 9729);
/*  68 */     GL11.glTexParameteri(3553, 10240, 9729);
/*  69 */     Gui.func_146110_a(x + 72, y + 4, 0.0F, 0.0F, 5, 5, 5.0F, 5.0F);
/*  70 */     GL11.glTexParameteri(3553, 10241, 9728);
/*  71 */     GL11.glTexParameteri(3553, 10240, 9728);
/*  72 */     GlStateManager.func_179118_c();
/*  73 */     GlStateManager.func_179084_k();
/*     */     
/*  75 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void renderExpanded() {
/*  79 */     if (!this.expanded)
/*  80 */       return;  List<String> modes = this.value.getModes();
/*  81 */     String current = this.value.getValue();
/*  82 */     List<String> displayModes = new ArrayList<>();
/*     */     
/*  84 */     for (String m : modes) { if (!m.equals(current)) displayModes.add(m);  }
/*  85 */      for (int i = 0; i < displayModes.size(); i++) {
/*  86 */       String mode = displayModes.get(i);
/*  87 */       String modePart = (displayModes.size() == 1) ? "textures/gui/modepart/mode_bottom.png" : ((i == displayModes.size() - 1) ? "textures/gui/modepart/mode_bottom.png" : "textures/gui/modepart/mode_middle.png");
/*     */       
/*  89 */       int scaleOffset = (GuiScale.customGuiScale() == 1.0F) ? 10 : 9;
/*     */       
/*  91 */       int boxY = this.parent.parent.getY() + 4 + this.offset + (i + 1) * scaleOffset;
/*  92 */       renderBox(this.parent.parent.getX() - 1, boxY, mode, modePart);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderBox(int x, int y, String text, String texture) {
/*  97 */     Minecraft mc = Minecraft.func_71410_x();
/*  98 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", texture));
/*     */     
/* 100 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 101 */     GlStateManager.func_179141_d();
/* 102 */     GlStateManager.func_179147_l();
/* 103 */     GlStateManager.func_179097_i();
/* 104 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 105 */     GL11.glTexParameteri(3553, 10240, 9729);
/* 106 */     Gui.func_146110_a(x, y, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/* 107 */     GL11.glTexParameteri(3553, 10241, 9728);
/* 108 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 109 */     GlStateManager.func_179118_c();
/* 110 */     GlStateManager.func_179084_k();
/*     */     
/* 112 */     Meowtils.fontRenderer.drawString(text, (x + 5), (y + 8), -1, 5.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOff(int newOff) {
/* 117 */     this.offset = newOff;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateComponent(int mouseX, int mouseY) {
/* 122 */     this.hovered = isMouseOnCollapsed(mouseX, mouseY);
/* 123 */     this.y = this.parent.parent.getY() + this.offset;
/* 124 */     this.x = this.parent.parent.getX();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int button) {
/* 129 */     if (button != 0 || !this.parent.open) return false; 
/* 130 */     int boxX = this.parent.parent.getX();
/* 131 */     int topY = this.parent.parent.getY() + 4 + this.offset;
/* 132 */     int boxWidth = 80;
/* 133 */     int boxHeight = 11;
/* 134 */     boolean clickHandled = false;
/*     */     
/* 136 */     if (!this.expanded) {
/*     */       
/* 138 */       if (mouseX > boxX && mouseX < boxX + boxWidth && mouseY > topY && mouseY < topY + boxHeight) {
/* 139 */         this.expanded = true;
/* 140 */         expandedPart = this;
/* 141 */         clickHandled = true;
/*     */       } 
/*     */     } else {
/*     */       
/* 145 */       if (mouseX > boxX && mouseX < boxX + boxWidth && mouseY > topY && mouseY < topY + boxHeight) {
/* 146 */         this.expanded = false;
/* 147 */         if (expandedPart == this) expandedPart = null; 
/* 148 */         clickHandled = true;
/*     */       } 
/*     */ 
/*     */       
/* 152 */       List<String> modes = this.value.getModes();
/* 153 */       String current = this.value.getValue();
/* 154 */       List<String> displayModes = new ArrayList<>();
/* 155 */       for (String m : modes) { if (!m.equals(current)) displayModes.add(m);  }
/*     */       
/* 157 */       for (int i = 0; i < displayModes.size(); i++) {
/* 158 */         int boxY = topY + (i + 1) * 10;
/* 159 */         if (mouseX > boxX && mouseX < boxX + boxWidth && mouseY > boxY && mouseY < boxY + boxHeight) {
/* 160 */           this.value.setValue(displayModes.get(i));
/* 161 */           this.expanded = false;
/* 162 */           if (expandedPart == this) expandedPart = null; 
/* 163 */           clickHandled = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     if (clickHandled) {
/* 171 */       return clickHandled;
/*     */     }
/* 173 */     return clickHandled;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isMouseOnCollapsed(int x, int y) {
/* 178 */     int boxX = this.parent.parent.getX() - 1;
/* 179 */     int boxY = this.parent.parent.getY() + 4 + this.offset;
/* 180 */     return (x > boxX && x < boxX + 80 && y > boxY && y < boxY + 14);
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/parts/ModePart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */