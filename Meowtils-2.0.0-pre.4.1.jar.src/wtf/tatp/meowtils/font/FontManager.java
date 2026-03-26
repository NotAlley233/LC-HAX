/*    */ package wtf.tatp.meowtils.font;
/*    */ 
/*    */ import java.awt.Font;
/*    */ import java.awt.GraphicsEnvironment;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class FontManager
/*    */ {
/*    */   public static Font loadTTF(String path, float size) {
/* 10 */     try (InputStream is = FontManager.class.getResourceAsStream(path)) {
/* 11 */       Font font = Font.createFont(0, is).deriveFont(size);
/* 12 */       GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
/* 13 */       ge.registerFont(font);
/* 14 */       return font;
/* 15 */     } catch (Exception e) {
/* 16 */       e.printStackTrace();
/* 17 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/font/FontManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */