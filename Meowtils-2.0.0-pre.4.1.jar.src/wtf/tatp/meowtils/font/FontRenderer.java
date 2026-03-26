/*     */ package wtf.tatp.meowtils.font;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.RenderingHints;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.GlyphVector;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.util.ColorUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FontRenderer
/*     */ {
/*     */   private final Font font;
/*  33 */   private final int textureSize = 1024;
/*  34 */   private final Map<Character, CharacterInfo> charMap = new HashMap<>();
/*     */   private final ResourceLocation textureLocation;
/*     */   private final float originalFontSize;
/*  37 */   private final net.minecraft.client.gui.FontRenderer mcFontRenderer = (Minecraft.func_71410_x()).field_71466_p;
/*     */   
/*     */   private static final int PADDING = 2;
/*     */ 
/*     */   
/*     */   public FontRenderer(Font font) {
/*  43 */     this.font = font;
/*  44 */     this.originalFontSize = font.getSize2D();
/*  45 */     DynamicTexture texture = bakeFontAtlas();
/*  46 */     this.textureLocation = Minecraft.func_71410_x().func_110434_K().func_110578_a("font_renderer", texture);
/*     */   }
/*     */   
/*     */   private DynamicTexture bakeFontAtlas() {
/*  50 */     BufferedImage atlas = new BufferedImage(1024, 1024, 2);
/*  51 */     Graphics2D g = atlas.createGraphics();
/*     */     
/*  53 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*  54 */     g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
/*  55 */     g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
/*  56 */     g.setFont(this.font);
/*  57 */     g.setColor(Color.WHITE);
/*     */     
/*  59 */     FontRenderContext frc = g.getFontRenderContext();
/*     */     
/*  61 */     int x = 2;
/*  62 */     int y = 2;
/*  63 */     int lineHeight = 0;
/*     */     
/*     */     char c;
/*  66 */     for (c = ' '; c < ''; c = (char)(c + 1)) {
/*     */       
/*  68 */       GlyphVector gv = this.font.createGlyphVector(frc, String.valueOf(c));
/*  69 */       Rectangle bounds = gv.getPixelBounds(null, 0.0F, 0.0F);
/*  70 */       float advance = gv.getGlyphMetrics(0).getAdvanceX();
/*     */ 
/*     */       
/*  73 */       if (c == ' ') {
/*  74 */         this.charMap.put(Character.valueOf(c), new CharacterInfo(0, 0, 0, 0, 0.0F, 0.0F, advance));
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*  79 */       else if (bounds.width > 0 && bounds.height > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  84 */         if (x + bounds.width + 2 >= 1024) {
/*  85 */           x = 2;
/*  86 */           y += lineHeight + 2;
/*  87 */           lineHeight = 0;
/*     */         } 
/*     */ 
/*     */         
/*  91 */         g.drawGlyphVector(gv, (x - bounds.x), (y - bounds.y));
/*     */ 
/*     */         
/*  94 */         if (bounds.height > lineHeight) {
/*  95 */           lineHeight = bounds.height;
/*     */         }
/*     */         
/*  98 */         this.charMap.put(Character.valueOf(c), new CharacterInfo(x, y, bounds.width, bounds.height, bounds.x, bounds.y, advance));
/*     */         
/* 100 */         x += bounds.width + 2;
/*     */       } 
/*     */     } 
/* 103 */     g.dispose();
/* 104 */     return new DynamicTexture(atlas);
/*     */   }
/*     */   
/*     */   public void drawString(String text, float x, float y, int color, float size) {
/* 108 */     drawColoredText(text, x, y, color, size, false);
/*     */   }
/*     */   
/*     */   public void drawStringWithShadow(String text, float x, float y, int color, float size) {
/* 112 */     if (text == null || text.isEmpty())
/* 113 */       return;  float shadowOffset = 0.4F;
/*     */ 
/*     */     
/* 116 */     drawColoredText(text, x + shadowOffset, y + shadowOffset, color, size, true);
/*     */ 
/*     */     
/* 119 */     drawColoredText(text, x, y, color, size, false);
/*     */   }
/*     */   
/*     */   public void drawStringWithLightShadow(String text, float x, float y, int color, float size) {
/* 123 */     if (text == null || text.isEmpty())
/* 124 */       return;  float shadowOffset = 0.1F;
/*     */ 
/*     */     
/* 127 */     drawColoredText(text, x + shadowOffset, y + shadowOffset, color, size, true);
/*     */ 
/*     */     
/* 130 */     drawColoredText(text, x, y, color, size, false);
/*     */   }
/*     */   
/*     */   public void drawScaledString(String text, float x, float y, int color, float size) {
/* 134 */     drawColoredText(text, x, y + 7.5F * size / 10.0F, color, size, false);
/*     */   }
/*     */   
/*     */   public void drawScaledStringWithShadow(String text, float x, float y, int color, float size) {
/* 138 */     if (text == null || text.isEmpty())
/* 139 */       return;  float shadowOffset = 0.4F;
/* 140 */     drawColoredText(text, x + shadowOffset, y + shadowOffset + 7.5F * size / 10.0F, color, size, true);
/* 141 */     drawColoredText(text, x, y + 7.5F * size / 10.0F, color, size, false);
/*     */   }
/*     */   
/*     */   public void drawScaledStringWithLightShadow(String text, float x, float y, int color, float size) {
/* 145 */     if (text == null || text.isEmpty())
/* 146 */       return;  float shadowOffset = 0.1F;
/* 147 */     drawColoredText(text, x + shadowOffset, y + shadowOffset + 7.5F * size / 10.0F, color, size, true);
/* 148 */     drawColoredText(text, x, y + 7.5F * size / 10.0F, color, size, false);
/*     */   }
/*     */   
/*     */   public float getStringWidth(String text, float size) {
/* 152 */     if (text == null || text.isEmpty()) {
/* 153 */       return 0.0F;
/*     */     }
/*     */     
/* 156 */     float totalWidth = 0.0F;
/* 157 */     float customScale = size / this.originalFontSize;
/* 158 */     float mcScale = size / 10.0F;
/*     */     
/* 160 */     for (int i = 0; i < text.length(); i++) {
/* 161 */       char c = text.charAt(i);
/* 162 */       if (c == '§' && i + 1 < text.length()) {
/* 163 */         i++;
/*     */       }
/*     */       else {
/*     */         
/* 167 */         CharacterInfo info = this.charMap.get(Character.valueOf(c));
/* 168 */         if (info != null) {
/* 169 */           totalWidth += info.advance;
/*     */         } else {
/*     */           
/* 172 */           float mcPixelWidth = this.mcFontRenderer.func_78263_a(c) * mcScale;
/* 173 */           totalWidth += mcPixelWidth / customScale;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 178 */     return totalWidth * customScale;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class CharacterInfo
/*     */   {
/*     */     final int textureX;
/*     */     
/*     */     final int textureY;
/*     */     
/*     */     final int width;
/*     */     
/*     */     final int height;
/*     */     
/*     */     final float offsetX;
/*     */     final float offsetY;
/*     */     final float advance;
/*     */     
/*     */     CharacterInfo(int textureX, int textureY, int width, int height, float offsetX, float offsetY, float advance) {
/* 197 */       this.textureX = textureX;
/* 198 */       this.textureY = textureY;
/* 199 */       this.width = width;
/* 200 */       this.height = height;
/* 201 */       this.offsetX = offsetX;
/* 202 */       this.offsetY = offsetY;
/* 203 */       this.advance = advance;
/*     */     } }
/*     */   
/*     */   private void drawColoredText(String text, float x, float y, int baseColor, float size, boolean shadow) {
/* 207 */     if (text == null || text.isEmpty())
/*     */       return; 
/* 209 */     float shadowMultiplier = shadow ? 0.25F : 1.0F;
/*     */     
/* 211 */     GlStateManager.func_179094_E();
/* 212 */     GlStateManager.func_179147_l();
/* 213 */     GlStateManager.func_179112_b(770, 771);
/* 214 */     GlStateManager.func_179098_w();
/* 215 */     GlStateManager.func_179109_b(x, y, 0.0F);
/*     */     
/* 217 */     float scale = size / this.originalFontSize;
/* 218 */     GlStateManager.func_179152_a(scale, scale, 1.0F);
/*     */     
/* 220 */     Minecraft.func_71410_x().func_110434_K().func_110577_a(this.textureLocation);
/* 221 */     GL11.glTexParameteri(3553, 10241, 9729);
/* 222 */     GL11.glTexParameteri(3553, 10240, 9729);
/*     */     
/* 224 */     int currentColor = baseColor;
/* 225 */     float r = (currentColor >> 16 & 0xFF) / 255.0F;
/* 226 */     float g = (currentColor >> 8 & 0xFF) / 255.0F;
/* 227 */     float b = (currentColor & 0xFF) / 255.0F;
/* 228 */     float a = (currentColor >> 24 & 0xFF) / 255.0F;
/*     */     
/* 230 */     if (shadow) {
/* 231 */       r *= shadowMultiplier;
/* 232 */       g *= shadowMultiplier;
/* 233 */       b *= shadowMultiplier;
/*     */     } 
/*     */     
/* 236 */     GlStateManager.func_179131_c(r, g, b, a);
/*     */     
/* 238 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 239 */     WorldRenderer worldRenderer = tessellator.func_178180_c();
/*     */     
/* 241 */     float cursorX = 0.0F;
/* 242 */     int baseAlpha = baseColor >> 24 & 0xFF;
/*     */     
/* 244 */     for (int i = 0; i < text.length(); i++) {
/* 245 */       char c = text.charAt(i);
/*     */       
/* 247 */       if (c == '§' && i + 1 < text.length()) {
/* 248 */         char code = Character.toLowerCase(text.charAt(i + 1));
/* 249 */         i++;
/*     */ 
/*     */         
/* 252 */         if (code == 'r') {
/* 253 */           currentColor = baseColor;
/*     */         } else {
/*     */           
/* 256 */           boolean isColorCode = ((code >= '0' && code <= '9') || (code >= 'a' && code <= 'f'));
/* 257 */           if (isColorCode) {
/*     */             
/* 259 */             EnumChatFormatting format = ColorUtil.getColorFromCode(code);
/* 260 */             int rgb = ColorUtil.getRGBFromFormatting(format);
/*     */             
/* 262 */             if ((rgb >> 24 & 0xFF) == 0) {
/* 263 */               currentColor = baseAlpha << 24 | rgb & 0xFFFFFF;
/*     */             } else {
/* 265 */               currentColor = rgb;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 270 */         float rr = (currentColor >> 16 & 0xFF) / 255.0F;
/* 271 */         float gg = (currentColor >> 8 & 0xFF) / 255.0F;
/* 272 */         float bb = (currentColor & 0xFF) / 255.0F;
/* 273 */         float aa = (currentColor >> 24 & 0xFF) / 255.0F;
/*     */         
/* 275 */         if (shadow) {
/* 276 */           rr *= shadowMultiplier;
/* 277 */           gg *= shadowMultiplier;
/* 278 */           bb *= shadowMultiplier;
/*     */         } 
/*     */         
/* 281 */         GlStateManager.func_179131_c(rr, gg, bb, aa);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 286 */         CharacterInfo info = this.charMap.get(Character.valueOf(c));
/* 287 */         if (info == null) {
/*     */           
/* 289 */           int finalColor = currentColor;
/* 290 */           if (shadow) {
/* 291 */             int alpha = finalColor >> 24 & 0xFF;
/* 292 */             int red = (int)((finalColor >> 16 & 0xFF) * shadowMultiplier);
/* 293 */             int green = (int)((finalColor >> 8 & 0xFF) * shadowMultiplier);
/* 294 */             int blue = (int)((finalColor & 0xFF) * shadowMultiplier);
/* 295 */             finalColor = alpha << 24 | red << 16 | green << 8 | blue;
/*     */           } 
/*     */ 
/*     */           
/* 299 */           float mcScale = size / 10.0F;
/* 300 */           float myScale = size / this.originalFontSize;
/* 301 */           float relativeScale = mcScale / myScale;
/*     */           
/* 303 */           GlStateManager.func_179094_E();
/* 304 */           GlStateManager.func_179152_a(relativeScale, relativeScale, 1.0F);
/*     */           
/* 306 */           this.mcFontRenderer.func_175065_a(String.valueOf(c), cursorX / relativeScale, -8.0F, finalColor, false);
/* 307 */           GlStateManager.func_179121_F();
/*     */ 
/*     */           
/* 310 */           Minecraft.func_71410_x().func_110434_K().func_110577_a(this.textureLocation);
/* 311 */           GlStateManager.func_179131_c(r, g, b, a);
/*     */           
/* 313 */           cursorX += this.mcFontRenderer.func_78263_a(c) * mcScale / myScale;
/*     */         }
/*     */         else {
/*     */           
/* 317 */           if (info.width > 0 && info.height > 0) {
/* 318 */             float u1 = info.textureX / 1024.0F;
/* 319 */             float v1 = info.textureY / 1024.0F;
/* 320 */             float u2 = (info.textureX + info.width) / 1024.0F;
/* 321 */             float v2 = (info.textureY + info.height) / 1024.0F;
/*     */             
/* 323 */             float quadX = cursorX + info.offsetX;
/* 324 */             float quadY = info.offsetY;
/*     */             
/* 326 */             worldRenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
/* 327 */             worldRenderer.func_181662_b(quadX, (quadY + info.height), 0.0D).func_181673_a(u1, v2).func_181675_d();
/* 328 */             worldRenderer.func_181662_b((quadX + info.width), (quadY + info.height), 0.0D).func_181673_a(u2, v2).func_181675_d();
/* 329 */             worldRenderer.func_181662_b((quadX + info.width), quadY, 0.0D).func_181673_a(u2, v1).func_181675_d();
/* 330 */             worldRenderer.func_181662_b(quadX, quadY, 0.0D).func_181673_a(u1, v1).func_181675_d();
/* 331 */             tessellator.func_78381_a();
/*     */           } 
/*     */           
/* 334 */           cursorX += info.advance;
/*     */         } 
/*     */       } 
/* 337 */     }  GL11.glTexParameteri(3553, 10241, 9728);
/* 338 */     GL11.glTexParameteri(3553, 10240, 9728);
/* 339 */     GlStateManager.func_179084_k();
/* 340 */     GlStateManager.func_179121_F();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/font/FontRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */