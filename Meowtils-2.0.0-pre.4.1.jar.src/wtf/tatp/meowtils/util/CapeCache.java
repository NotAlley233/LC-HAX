/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import javax.imageio.ImageIO;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class CapeCache {
/* 12 */   private static String lastCape = null;
/* 13 */   private static ResourceLocation cachedCape = null;
/*    */   
/*    */   public static ResourceLocation getCape(String selectedCape) {
/* 16 */     if (!selectedCape.equalsIgnoreCase(lastCape)) {
/* 17 */       lastCape = selectedCape;
/* 18 */       cachedCape = loadCape(selectedCape);
/*    */     } 
/* 20 */     return cachedCape;
/*    */   }
/*    */   
/*    */   private static ResourceLocation loadCape(String selectedCape) {
/* 24 */     if ("Custom".equalsIgnoreCase(selectedCape)) {
/* 25 */       File customCapeFile = new File((Minecraft.func_71410_x()).field_71412_D, "meowtils/custom_cape/custom.png");
/* 26 */       if (customCapeFile.exists()) {
/*    */         try {
/* 28 */           BufferedImage image = ImageIO.read(customCapeFile);
/* 29 */           DynamicTexture dynamicTexture = new DynamicTexture(image);
/* 30 */           return Minecraft.func_71410_x().func_110434_K().func_110578_a("custom_cape", dynamicTexture);
/* 31 */         } catch (IOException e) {
/* 32 */           e.printStackTrace();
/*    */         } 
/*    */       }
/* 35 */       return null;
/*    */     } 
/* 37 */     return new ResourceLocation("meowtils", "textures/cape/" + selectedCape + ".png");
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/CapeCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */