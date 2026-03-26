/*     */ package wtf.tatp.meowtils.gui.parts;
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.ColorComponent;
/*     */ import wtf.tatp.meowtils.gui.GuiScale;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.component.Component;
/*     */ import wtf.tatp.meowtils.gui.component.Frame;
/*     */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*     */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.gui.values.OpacityValue;
/*     */ import wtf.tatp.meowtils.gui.values.SaturationValue;
/*     */ import wtf.tatp.meowtils.util.ColorUtil;
/*     */ 
/*     */ public class ModulesPart extends Component {
/*     */   public Module mod;
/*     */   public Frame parent;
/*     */   public int offset;
/*     */   
/*     */   public List<Component> getSubcomponents() {
/*  34 */     return this.subcomponents;
/*     */   }
/*     */   private boolean binding; private boolean isHovered; private ArrayList<Component> subcomponents; public boolean open;
/*     */   public boolean isHovered() {
/*  38 */     return this.isHovered;
/*     */   }
/*     */   
/*     */   public ModulesPart(Module mod, Frame parent, int offset) {
/*  42 */     this.mod = mod;
/*  43 */     this.parent = parent;
/*  44 */     this.offset = offset;
/*  45 */     this.subcomponents = new ArrayList<>();
/*  46 */     this.open = false;
/*     */     
/*  48 */     int opY = offset + 14;
/*     */     
/*  50 */     Set<ColorComponent> processed = new LinkedHashSet<>();
/*     */     
/*  52 */     for (ColorValue colorValue : mod.getRgb()) processed.add(colorValue.getLink()); 
/*  53 */     for (SaturationValue satValue : mod.getSaturation()) processed.add(satValue.getLink()); 
/*  54 */     for (BrightnessValue briValue : mod.getBrightness()) processed.add(briValue.getLink()); 
/*  55 */     for (OpacityValue opaValue : mod.getOpacity()) processed.add(opaValue.getLink());
/*     */     
/*  57 */     for (ColorComponent comp : processed) {
/*     */       
/*  59 */       for (ColorValue colorValue : mod.getRgb()) {
/*  60 */         if (colorValue.getLink() == comp) {
/*  61 */           this.subcomponents.add(new ColorPart(colorValue, this, opY));
/*  62 */           opY += 12;
/*     */         } 
/*     */       } 
/*     */       
/*  66 */       for (SaturationValue satValue : mod.getSaturation()) {
/*  67 */         if (satValue.getLink() == comp) {
/*  68 */           this.subcomponents.add(new SaturationPart(satValue, this, opY));
/*  69 */           opY += 12;
/*     */         } 
/*     */       } 
/*     */       
/*  73 */       for (BrightnessValue briValue : mod.getBrightness()) {
/*  74 */         if (briValue.getLink() == comp) {
/*  75 */           this.subcomponents.add(new BrightnessPart(briValue, this, opY));
/*  76 */           opY += 12;
/*     */         } 
/*     */       } 
/*     */       
/*  80 */       for (OpacityValue opaValue : mod.getOpacity()) {
/*  81 */         if (opaValue.getLink() == comp) {
/*  82 */           this.subcomponents.add(new OpacityPart(opaValue, this, opY));
/*  83 */           opY += 12;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  88 */     if (!mod.getArrays().isEmpty()) {
/*  89 */       for (ArrayValue array : mod.getArrays()) {
/*  90 */         this.subcomponents.add(new ModePart(array, this, opY, array.getName()));
/*  91 */         opY += 12;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*  96 */     if (!mod.getValues().isEmpty()) {
/*  97 */       for (NumberValue num : mod.getValues()) {
/*  98 */         this.subcomponents.add(new SliderPart(num, this, opY));
/*  99 */         opY += 12;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 104 */     if (!mod.getBooleans().isEmpty()) {
/* 105 */       for (BooleanValue bool : mod.getBooleans()) {
/* 106 */         this.subcomponents.add(new TogglePart(bool, this, opY));
/* 107 */         opY += 12;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOff(int newOff) {
/* 114 */     this.offset = newOff;
/* 115 */     int opY = this.offset + 12;
/* 116 */     for (Component comp : this.subcomponents) {
/* 117 */       comp.setOff(opY);
/* 118 */       opY += 12;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void render() {
/* 124 */     Minecraft mc = Minecraft.func_71410_x();
/* 125 */     boolean moduleAbove = isModuleAbove();
/* 126 */     boolean moduleBelow = isModuleBelow();
/*     */     
/* 128 */     if (this.isHovered && Mouse.isButtonDown(2)) {
/* 129 */       this.binding = true;
/*     */     }
/*     */ 
/*     */     
/* 133 */     String background = (moduleAbove & moduleBelow) ? "textures/gui/module_disabled.png" : (((moduleAbove & (!this.open ? 1 : 0)) != 0) ? "textures/gui/module_disabled_bottom.png" : "textures/gui/module_disabled.png");
/* 134 */     mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", background));
/* 135 */     GlStateManager.func_179147_l();
/* 136 */     GlStateManager.func_179141_d();
/* 137 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 138 */     Gui.func_146110_a(this.parent.getX() - 1, this.parent.getY() + 1 + this.offset, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/*     */ 
/*     */     
/* 141 */     if (this.mod.getState()) {
/* 142 */       float f1 = cfg.v.meowtils_red / 255.0F;
/* 143 */       float f2 = cfg.v.meowtils_green / 255.0F;
/* 144 */       float f3 = cfg.v.meowtils_blue / 255.0F;
/* 145 */       boolean enabledAbove = isAboveEnabled();
/* 146 */       boolean enabledBelow = isBelowEnabled();
/* 147 */       String module = (enabledAbove && enabledBelow) ? "textures/gui/module_connected_both.png" : (enabledAbove ? "textures/gui/module_connected_top.png" : (enabledBelow ? "textures/gui/module_connected_bottom.png" : "textures/gui/module_not_connected.png"));
/*     */ 
/*     */       
/* 150 */       mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", module));
/* 151 */       GlStateManager.func_179131_c(f1, f2, f3, 1.0F);
/* 152 */       Gui.func_146110_a(this.parent.getX() - 1, this.parent.getY() + 1 + this.offset, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/* 153 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 154 */       GlStateManager.func_179118_c();
/* 155 */       GlStateManager.func_179084_k();
/*     */ 
/*     */       
/* 158 */       if (GuiScale.customGuiScale() < 1.0F && enabledBelow) {
/* 159 */         mc.func_110434_K().func_110577_a(new ResourceLocation("meowtils", "textures/gui/custom_scale_line.png"));
/* 160 */         GlStateManager.func_179147_l();
/* 161 */         GlStateManager.func_179141_d();
/* 162 */         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 163 */         GL11.glTexParameteri(3553, 10241, 9729);
/* 164 */         GL11.glTexParameteri(3553, 10240, 9729);
/* 165 */         Gui.func_146110_a(this.parent.getX() - 1, this.parent.getY() + 1 + this.offset, 0.0F, 0.0F, 90, 20, 90.0F, 20.0F);
/* 166 */         GL11.glTexParameteri(3553, 10241, 9728);
/* 167 */         GL11.glTexParameteri(3553, 10240, 9728);
/* 168 */         GlStateManager.func_179084_k();
/* 169 */         GlStateManager.func_179118_c();
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 175 */     int r = cfg.v.meowtils_red;
/* 176 */     int g = cfg.v.meowtils_green;
/* 177 */     int b = cfg.v.meowtils_blue;
/*     */ 
/*     */     
/* 180 */     if (ColorUtil.isColorTooBright(r, g, b) && this.mod.getState()) {
/* 181 */       int textColor = (new Color(40, 40, 40)).getRGB();
/*     */ 
/*     */       
/* 184 */       Meowtils.fontRenderer.drawStringWithLightShadow(this.binding ? "" : this.mod.getName(), (this.parent.getX() + (this.parent.getWidth() - 20) / 2 - 37), (this.parent.getY() + this.offset + 3 + 7), textColor, 6.0F);
/*     */ 
/*     */       
/* 187 */       Meowtils.fontRenderer.drawStringWithLightShadow(this.binding ? ("Bound.. " + Keyboard.getKeyName(this.mod.getKey())) : "", (this.parent.getX() + (this.parent.getWidth() - 20) / 2 - 37), (this.parent.getY() + this.offset + 3 + 7), textColor, 6.0F);
/*     */     } else {
/* 189 */       int textColor = (new Color(255, 255, 255)).getRGB();
/*     */ 
/*     */       
/* 192 */       Meowtils.fontRenderer.drawStringWithShadow(this.binding ? "" : this.mod.getName(), (this.parent.getX() + (this.parent.getWidth() - 20) / 2 - 37), (this.parent.getY() + this.offset + 3 + 7), textColor, 6.0F);
/*     */ 
/*     */       
/* 195 */       Meowtils.fontRenderer.drawStringWithShadow(this.binding ? ("Bound.. " + Keyboard.getKeyName(this.mod.getKey())) : "", (this.parent.getX() + (this.parent.getWidth() - 20) / 2 - 37), (this.parent.getY() + this.offset + 3 + 7), textColor, 6.0F);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 200 */     if (!this.subcomponents.isEmpty()) {
/* 201 */       float color = this.mod.getState() ? 1.0F : 0.5882353F;
/* 202 */       int arrowX = this.parent.getX() + 70;
/* 203 */       int arrowY = this.parent.getY() + this.offset + 6;
/*     */       
/* 205 */       ResourceLocation tex = this.open ? new ResourceLocation("meowtils", "textures/gui/module_arrow_down.png") : new ResourceLocation("meowtils", "textures/gui/module_arrow_up.png");
/*     */       
/* 207 */       mc.func_110434_K().func_110577_a(tex);
/* 208 */       GlStateManager.func_179147_l();
/* 209 */       GlStateManager.func_179141_d();
/* 210 */       GlStateManager.func_179131_c(color, color, color, 1.0F);
/* 211 */       GL11.glTexParameteri(3553, 10241, 9729);
/* 212 */       GL11.glTexParameteri(3553, 10240, 9729);
/* 213 */       Gui.func_146110_a(arrowX, arrowY, 0.0F, 0.0F, 5, 5, 5.0F, 5.0F);
/* 214 */       GL11.glTexParameteri(3553, 10241, 9728);
/* 215 */       GL11.glTexParameteri(3553, 10240, 9728);
/* 216 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 217 */       GlStateManager.func_179118_c();
/* 218 */       GlStateManager.func_179084_k();
/*     */     } 
/*     */     
/* 221 */     if (this.open && !this.subcomponents.isEmpty()) {
/* 222 */       for (Component comp : this.subcomponents) {
/* 223 */         comp.render();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 230 */     if (this.open) {
/* 231 */       return this.subcomponents.isEmpty() ? 28 : (15 + this.subcomponents.size() * 12);
/*     */     }
/* 233 */     return 15;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateComponent(int mouseX, int mouseY) {
/* 238 */     this.parent.refresh();
/* 239 */     this.isHovered = isMouseOnButton(mouseX, mouseY);
/* 240 */     if (!this.subcomponents.isEmpty()) {
/* 241 */       for (Component comp : this.subcomponents) {
/* 242 */         comp.updateComponent(mouseX, mouseY);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int button) {
/* 250 */     if (isMouseOnButton(mouseX, mouseY) && button == 2 && this.parent.open) {
/* 251 */       this.binding = !this.binding;
/*     */     }
/* 253 */     if (isMouseOnButton(mouseX, mouseY) && button == 0)
/*     */     {
/* 255 */       this.mod.setState(!this.mod.getState());
/*     */     }
/* 257 */     if (isMouseOnButton(mouseX, mouseY) && button == 1 && 
/* 258 */       !this.subcomponents.isEmpty()) {
/* 259 */       this.open = !this.open;
/* 260 */       this.parent.refresh();
/*     */     } 
/*     */     
/* 263 */     for (Component comp : this.subcomponents) {
/* 264 */       comp.mouseClicked(mouseX, mouseY, button);
/*     */     }
/* 266 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isMouseOnButton(int x, int y) {
/* 270 */     return (x > this.parent.getX() && x < this.parent.getX() + this.parent.getWidth() - 18 && y > this.parent.getY() + this.offset && y < this.parent.getY() + 16 + this.offset);
/*     */   }
/*     */   
/*     */   public void keyTyped(char typedChar, int key) {
/* 274 */     if (this.binding) {
/* 275 */       if (key == 14) {
/* 276 */         this.mod.setKey(0);
/* 277 */         this.binding = false;
/*     */         return;
/*     */       } 
/* 280 */       this.mod.setKey(key);
/* 281 */       this.binding = false;
/* 282 */       if (key == 42) {
/* 283 */         this.mod.setKey(0);
/* 284 */         this.binding = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private ModulesPart getConnected(int direction) {
/* 290 */     int index = this.parent.getComponents().indexOf(this);
/* 291 */     int connectedIndex = index + direction;
/*     */     
/* 293 */     if (connectedIndex >= 0 && connectedIndex < this.parent.getComponents().size()) {
/* 294 */       Component neighbor = this.parent.getComponents().get(connectedIndex);
/* 295 */       if (neighbor instanceof ModulesPart) {
/* 296 */         return (ModulesPart)neighbor;
/*     */       }
/*     */     } 
/* 299 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isAboveEnabled() {
/* 303 */     ModulesPart above = getConnected(-1);
/* 304 */     return (above != null && above.mod.getState());
/*     */   }
/*     */   
/*     */   private boolean isBelowEnabled() {
/* 308 */     ModulesPart below = getConnected(1);
/* 309 */     return (below != null && below.mod.getState());
/*     */   }
/*     */   
/*     */   public boolean isModuleAbove() {
/* 313 */     ModulesPart above = getConnected(-1);
/* 314 */     return (above != null);
/*     */   }
/*     */   
/*     */   public boolean isModuleBelow() {
/* 318 */     ModulesPart below = getConnected(1);
/* 319 */     return (below != null);
/*     */   }
/*     */   
/*     */   private Component getConnectedComponent(Component current, int direction) {
/* 323 */     int index = this.subcomponents.indexOf(current);
/* 324 */     int neighborIndex = index + direction;
/*     */     
/* 326 */     if (neighborIndex >= 0 && neighborIndex < this.subcomponents.size()) {
/* 327 */       return this.subcomponents.get(neighborIndex);
/*     */     }
/* 329 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isComponentAbove(Component comp) {
/* 333 */     return (getConnectedComponent(comp, -1) != null);
/*     */   }
/*     */   
/*     */   public boolean isComponentBelow(Component comp) {
/* 337 */     return (getConnectedComponent(comp, 1) != null);
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/parts/ModulesPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */