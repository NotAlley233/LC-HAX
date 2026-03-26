/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ 
/*    */ 
/*    */ public class ColorUtil
/*    */ {
/*    */   public static EnumChatFormatting getColorFromString(String colorName) {
/*    */     try {
/* 11 */       return EnumChatFormatting.valueOf(colorName.toUpperCase());
/* 12 */     } catch (IllegalArgumentException e) {
/* 13 */       return EnumChatFormatting.WHITE;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static EnumChatFormatting getColorFromCode(char code) {
/* 18 */     switch (code) { case '0':
/* 19 */         return EnumChatFormatting.BLACK;
/* 20 */       case '1': return EnumChatFormatting.DARK_BLUE;
/* 21 */       case '2': return EnumChatFormatting.DARK_GREEN;
/* 22 */       case '3': return EnumChatFormatting.DARK_AQUA;
/* 23 */       case '4': return EnumChatFormatting.DARK_RED;
/* 24 */       case '5': return EnumChatFormatting.DARK_PURPLE;
/* 25 */       case '6': return EnumChatFormatting.GOLD;
/* 26 */       case '7': return EnumChatFormatting.GRAY;
/* 27 */       case '8': return EnumChatFormatting.DARK_GRAY;
/* 28 */       case '9': return EnumChatFormatting.BLUE;
/* 29 */       case 'a': return EnumChatFormatting.GREEN;
/* 30 */       case 'b': return EnumChatFormatting.AQUA;
/* 31 */       case 'c': return EnumChatFormatting.RED;
/* 32 */       case 'd': return EnumChatFormatting.LIGHT_PURPLE;
/* 33 */       case 'e': return EnumChatFormatting.YELLOW;
/* 34 */       case 'f': return EnumChatFormatting.WHITE; }
/* 35 */      return EnumChatFormatting.WHITE;
/*    */   }
/*    */   
/*    */   public static int getRGBFromFormatting(EnumChatFormatting color) {
/* 39 */     switch (color) { case BLACK:
/* 40 */         return (new Color(0, 0, 0)).getRGB();
/* 41 */       case DARK_BLUE: return (new Color(0, 0, 170)).getRGB();
/* 42 */       case DARK_GREEN: return (new Color(0, 170, 0)).getRGB();
/* 43 */       case DARK_AQUA: return (new Color(0, 170, 170)).getRGB();
/* 44 */       case DARK_RED: return (new Color(170, 0, 0)).getRGB();
/* 45 */       case DARK_PURPLE: return (new Color(170, 0, 170)).getRGB();
/* 46 */       case GOLD: return (new Color(255, 170, 0)).getRGB();
/* 47 */       case GRAY: return (new Color(170, 170, 170)).getRGB();
/* 48 */       case DARK_GRAY: return (new Color(85, 85, 85)).getRGB();
/* 49 */       case BLUE: return (new Color(85, 85, 255)).getRGB();
/* 50 */       case GREEN: return (new Color(85, 255, 85)).getRGB();
/* 51 */       case AQUA: return (new Color(85, 255, 255)).getRGB();
/* 52 */       case RED: return (new Color(255, 85, 85)).getRGB();
/* 53 */       case LIGHT_PURPLE: return (new Color(255, 85, 255)).getRGB();
/* 54 */       case YELLOW: return (new Color(255, 255, 85)).getRGB();
/* 55 */       case WHITE: return (new Color(255, 255, 255)).getRGB(); }
/* 56 */      return (new Color(255, 255, 255)).getRGB();
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isColorTooBright(int r, int g, int b) {
/* 61 */     r = Math.max(0, Math.min(255, r));
/* 62 */     g = Math.max(0, Math.min(255, g));
/* 63 */     b = Math.max(0, Math.min(255, b));
/*    */     
/* 65 */     double brightness = 0.2126D * r + 0.7152D * g + 0.0722D * b;
/*    */     
/* 67 */     return (brightness > 180.0D);
/*    */   }
/*    */   
/*    */   public static String unformattedText(String text) {
/* 71 */     if (text == null) return null; 
/* 72 */     return text.replaceAll("§.", "");
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/ColorUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */