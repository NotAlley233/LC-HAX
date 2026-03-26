/*     */ package wtf.tatp.meowtils.util;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import wtf.tatp.meowtils.mixins.MinecraftAccessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Render
/*     */ {
/*     */   public static void drawSlotBackground(int x, int y, int color) {
/* 196 */     GlStateManager.func_179090_x();
/* 197 */     GlStateManager.func_179147_l();
/* 198 */     GlStateManager.func_179140_f();
/* 199 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 200 */     WorldRenderer worldrenderer = tessellator.func_178180_c();
/* 201 */     worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
/* 202 */     worldrenderer.func_181662_b(x, (y + 16), 0.0D).func_181669_b(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).func_181675_d();
/* 203 */     worldrenderer.func_181662_b((x + 16), (y + 16), 0.0D).func_181669_b(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).func_181675_d();
/* 204 */     worldrenderer.func_181662_b((x + 16), y, 0.0D).func_181669_b(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).func_181675_d();
/* 205 */     worldrenderer.func_181662_b(x, y, 0.0D).func_181669_b(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).func_181675_d();
/* 206 */     tessellator.func_78381_a();
/* 207 */     GlStateManager.func_179098_w();
/*     */   }
/*     */   public static void draw3DBlockBox(AxisAlignedBB box, boolean fill, Color fillColor, boolean outline, Color outlineColor, double expandX, double expandY, double expandZ) {
/* 210 */     Minecraft mc = Minecraft.func_71410_x();
/* 211 */     double dx = (mc.func_175598_ae()).field_78730_l;
/* 212 */     double dy = (mc.func_175598_ae()).field_78731_m;
/* 213 */     double dz = (mc.func_175598_ae()).field_78728_n;
/*     */ 
/*     */ 
/*     */     
/* 217 */     AxisAlignedBB bb = box.func_72314_b(expandX, expandY, expandZ).func_72317_d(-dx, -dy, -dz);
/*     */     
/* 219 */     GlStateManager.func_179094_E();
/* 220 */     GlStateManager.func_179090_x();
/* 221 */     GlStateManager.func_179147_l();
/* 222 */     GlStateManager.func_179097_i();
/* 223 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*     */     
/* 225 */     if (fill) {
/* 226 */       float r = fillColor.getRed() / 255.0F;
/* 227 */       float g = fillColor.getGreen() / 255.0F;
/* 228 */       float b = fillColor.getBlue() / 255.0F;
/* 229 */       float a = fillColor.getAlpha() / 255.0F;
/* 230 */       drawFilledBox(bb, r, g, b, a);
/*     */     } 
/*     */     
/* 233 */     if (outline) {
/* 234 */       float r = outlineColor.getRed() / 255.0F;
/* 235 */       float g = outlineColor.getGreen() / 255.0F;
/* 236 */       float b = outlineColor.getBlue() / 255.0F;
/* 237 */       float a = outlineColor.getAlpha() / 255.0F;
/* 238 */       GlStateManager.func_179131_c(r, g, b, a);
/* 239 */       RenderGlobal.func_181561_a(bb);
/* 240 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     } 
/*     */     
/* 243 */     GlStateManager.func_179126_j();
/* 244 */     GlStateManager.func_179084_k();
/* 245 */     GlStateManager.func_179098_w();
/* 246 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   public static AxisAlignedBB getAdjustedBox(AxisAlignedBB original, Block block) {
/* 250 */     if (block instanceof net.minecraft.block.BlockChest) {
/* 251 */       return original.func_72314_b(-0.05D, -0.05D, -0.05D);
/*     */     }
/* 253 */     if (block instanceof net.minecraft.block.BlockBed) {
/* 254 */       return original.func_72314_b(0.0D, -0.4D, 0.0D);
/*     */     }
/* 256 */     if (block instanceof net.minecraft.block.BlockSlab) {
/* 257 */       return original.func_72314_b(0.0D, -0.25D, 0.0D);
/*     */     }
/* 259 */     return original;
/*     */   }
/*     */   
/*     */   public static void draw3DEntityBox(Entity entity, boolean fill, Color fillColor, boolean outline, Color outlineColor, double expandX, double expandY, double expandZ) {
/* 263 */     Minecraft mc = Minecraft.func_71410_x();
/* 264 */     float partialTicks = (((MinecraftAccessor)mc).getTimer()).field_74281_c;
/*     */     
/* 266 */     double dx = (mc.func_175598_ae()).field_78730_l;
/* 267 */     double dy = (mc.func_175598_ae()).field_78731_m;
/* 268 */     double dz = (mc.func_175598_ae()).field_78728_n;
/*     */     
/* 270 */     double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partialTicks;
/* 271 */     double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partialTicks;
/* 272 */     double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partialTicks;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 277 */     AxisAlignedBB bb = entity.func_174813_aQ().func_72317_d(-entity.field_70165_t, -entity.field_70163_u, -entity.field_70161_v).func_72317_d(x - dx, y - dy, z - dz).func_72314_b(expandX, expandY, expandZ);
/*     */     
/* 279 */     GlStateManager.func_179094_E();
/* 280 */     GlStateManager.func_179090_x();
/* 281 */     GlStateManager.func_179147_l();
/* 282 */     GlStateManager.func_179097_i();
/* 283 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 284 */     GlStateManager.func_179140_f();
/*     */     
/* 286 */     if (fill) {
/* 287 */       float r = fillColor.getRed() / 255.0F;
/* 288 */       float g = fillColor.getGreen() / 255.0F;
/* 289 */       float b = fillColor.getBlue() / 255.0F;
/* 290 */       float a = fillColor.getAlpha() / 255.0F;
/* 291 */       drawFilledBox(bb, r, g, b, a);
/*     */     } 
/*     */     
/* 294 */     if (outline) {
/* 295 */       float r = outlineColor.getRed() / 255.0F;
/* 296 */       float g = outlineColor.getGreen() / 255.0F;
/* 297 */       float b = outlineColor.getBlue() / 255.0F;
/* 298 */       float a = outlineColor.getAlpha() / 255.0F;
/*     */       
/* 300 */       GlStateManager.func_179131_c(r, g, b, a);
/* 301 */       GL11.glLineWidth(1.5F);
/* 302 */       drawOutlined3DEntityBox(bb);
/* 303 */       GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     } 
/*     */     
/* 306 */     GlStateManager.func_179126_j();
/* 307 */     GlStateManager.func_179098_w();
/* 308 */     GlStateManager.func_179084_k();
/* 309 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   
/*     */   private static void drawOutlined3DEntityBox(AxisAlignedBB bb) {
/* 313 */     Tessellator tess = Tessellator.func_178181_a();
/* 314 */     WorldRenderer wr = tess.func_178180_c();
/*     */     
/* 316 */     wr.func_181668_a(1, DefaultVertexFormats.field_181705_e);
/*     */ 
/*     */     
/* 319 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/* 320 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/*     */     
/* 322 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/* 323 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/*     */     
/* 325 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/* 326 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/*     */     
/* 328 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/* 329 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/*     */ 
/*     */     
/* 332 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/* 333 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/*     */     
/* 335 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/* 336 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/*     */     
/* 338 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/* 339 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/*     */     
/* 341 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/* 342 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/*     */ 
/*     */     
/* 345 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/* 346 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/*     */     
/* 348 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/* 349 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/*     */     
/* 351 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/* 352 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/*     */     
/* 354 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/* 355 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/*     */     
/* 357 */     tess.func_78381_a();
/*     */   }
/*     */   public static void draw2DEntityBox(double x, double y, double z, float width, float height, Color color, boolean fill) {
/* 360 */     Minecraft mc = Minecraft.func_71410_x();
/*     */     
/* 362 */     GlStateManager.func_179094_E();
/* 363 */     GlStateManager.func_179137_b(x, y, z);
/*     */     
/* 365 */     GlStateManager.func_179114_b(-(mc.func_175598_ae()).field_78735_i, 0.0F, 1.0F, 0.0F);
/* 366 */     GlStateManager.func_179114_b((mc.func_175598_ae()).field_78732_j, 1.0F, 0.0F, 0.0F);
/*     */     
/* 368 */     GlStateManager.func_179090_x();
/* 369 */     GlStateManager.func_179147_l();
/* 370 */     GlStateManager.func_179112_b(770, 771);
/* 371 */     GlStateManager.func_179097_i();
/*     */     
/* 373 */     float r = color.getRed() / 255.0F;
/* 374 */     float g = color.getGreen() / 255.0F;
/* 375 */     float b = color.getBlue() / 255.0F;
/* 376 */     float a = color.getAlpha() / 255.0F;
/*     */     
/* 378 */     if (fill) {
/* 379 */       GL11.glColor4f(r, g, b, a * 0.5F);
/* 380 */       GL11.glBegin(7);
/* 381 */       GL11.glVertex3d(-width, height, 0.0D);
/* 382 */       GL11.glVertex3d(width, height, 0.0D);
/* 383 */       GL11.glVertex3d(width, -height, 0.0D);
/* 384 */       GL11.glVertex3d(-width, -height, 0.0D);
/* 385 */       GL11.glEnd();
/*     */     } 
/*     */ 
/*     */     
/* 389 */     GL11.glColor4f(r, g, b, 1.0F);
/* 390 */     GL11.glLineWidth(1.5F);
/* 391 */     GL11.glBegin(2);
/* 392 */     GL11.glVertex3d(-width, height, 0.0D);
/* 393 */     GL11.glVertex3d(width, height, 0.0D);
/* 394 */     GL11.glVertex3d(width, -height, 0.0D);
/* 395 */     GL11.glVertex3d(-width, -height, 0.0D);
/* 396 */     GL11.glEnd();
/*     */     
/* 398 */     GlStateManager.func_179126_j();
/* 399 */     GlStateManager.func_179084_k();
/* 400 */     GlStateManager.func_179098_w();
/* 401 */     GlStateManager.func_179121_F();
/*     */   }
/*     */   private static void drawFilledBox(AxisAlignedBB bb, float r, float g, float b, float a) {
/* 404 */     Tessellator tess = Tessellator.func_178181_a();
/* 405 */     WorldRenderer wr = tess.func_178180_c();
/*     */     
/* 407 */     GlStateManager.func_179131_c(r, g, b, a);
/*     */     
/* 409 */     wr.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/*     */ 
/*     */     
/* 412 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/* 413 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/* 414 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/* 415 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/*     */ 
/*     */     
/* 418 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/* 419 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/* 420 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/* 421 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/*     */ 
/*     */     
/* 424 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/* 425 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/* 426 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/* 427 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/*     */     
/* 429 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/* 430 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/* 431 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/* 432 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/*     */     
/* 434 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/* 435 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/* 436 */     wr.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/* 437 */     wr.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/*     */     
/* 439 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181675_d();
/* 440 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181675_d();
/* 441 */     wr.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181675_d();
/* 442 */     wr.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181675_d();
/*     */     
/* 444 */     tess.func_78381_a();
/*     */   }
/*     */   public static void drawRectFloat(float x1, float y1, float x2, float y2, int color) {
/* 447 */     float a = (color >> 24 & 0xFF) / 255.0F;
/* 448 */     float r = (color >> 16 & 0xFF) / 255.0F;
/* 449 */     float g = (color >> 8 & 0xFF) / 255.0F;
/* 450 */     float b = (color & 0xFF) / 255.0F;
/*     */     
/* 452 */     GlStateManager.func_179147_l();
/* 453 */     GlStateManager.func_179090_x();
/* 454 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/*     */     
/* 456 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 457 */     WorldRenderer buffer = tessellator.func_178180_c();
/*     */     
/* 459 */     GlStateManager.func_179131_c(r, g, b, a);
/* 460 */     buffer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 461 */     buffer.func_181662_b(x1, y2, 0.0D).func_181675_d();
/* 462 */     buffer.func_181662_b(x2, y2, 0.0D).func_181675_d();
/* 463 */     buffer.func_181662_b(x2, y1, 0.0D).func_181675_d();
/* 464 */     buffer.func_181662_b(x1, y1, 0.0D).func_181675_d();
/* 465 */     tessellator.func_78381_a();
/*     */     
/* 467 */     GlStateManager.func_179098_w();
/* 468 */     GlStateManager.func_179084_k();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/Render.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */