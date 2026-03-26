/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class Prefix
/*    */ {
/*    */   public static String getPrefix() {
/*  9 */     String prefixcase = cfg.v.lowerCasePrefix ? "m" : "M";
/* 10 */     String boldprefixcase = cfg.v.lowerCasePrefix ? (EnumChatFormatting.BOLD + "m") : (EnumChatFormatting.BOLD + "M");
/* 11 */     if (cfg.v.meowtilsPrefixMode.equals("Default")) {
/* 12 */       return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.BLUE + prefixcase + EnumChatFormatting.DARK_AQUA + "e" + EnumChatFormatting.AQUA + "o" + EnumChatFormatting.WHITE + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 19 */     if (cfg.v.meowtilsPrefixMode.equals("Myau")) {
/* 20 */       return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.RED + prefixcase + EnumChatFormatting.GOLD + "e" + EnumChatFormatting.YELLOW + "o" + EnumChatFormatting.GREEN + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 27 */     if (cfg.v.meowtilsPrefixMode.equals("Fire")) {
/* 28 */       return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.YELLOW + prefixcase + EnumChatFormatting.GOLD + "e" + EnumChatFormatting.RED + "o" + EnumChatFormatting.DARK_RED + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     if (cfg.v.meowtilsPrefixMode.equals("Nebula")) {
/* 36 */       return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.DARK_RED + prefixcase + EnumChatFormatting.RED + "e" + EnumChatFormatting.LIGHT_PURPLE + "o" + EnumChatFormatting.DARK_PURPLE + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 43 */     if (cfg.v.meowtilsPrefixMode.equals("Air")) {
/* 44 */       return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.AQUA + prefixcase + EnumChatFormatting.WHITE + "e" + EnumChatFormatting.GRAY + "o" + EnumChatFormatting.DARK_GRAY + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 51 */     if (cfg.v.meowtilsPrefixMode.equals("Custom")) {
/* 52 */       return ColorUtil.getColorFromString(cfg.v.customThemeFirstBracket) + "[" + 
/* 53 */         ColorUtil.getColorFromString(cfg.v.customThemeM) + prefixcase + 
/* 54 */         ColorUtil.getColorFromString(cfg.v.customThemeE) + "e" + 
/* 55 */         ColorUtil.getColorFromString(cfg.v.customThemeO) + "o" + 
/* 56 */         ColorUtil.getColorFromString(cfg.v.customThemeW) + "w" + 
/* 57 */         ColorUtil.getColorFromString(cfg.v.customThemeSecondBracket) + "] " + EnumChatFormatting.WHITE;
/*    */     }
/* 59 */     if (cfg.v.meowtilsPrefixMode.equals("Short")) {
/* 60 */       return ColorUtil.getColorFromString(cfg.v.customThemeFirstBracket) + "[" + 
/* 61 */         ColorUtil.getColorFromString(cfg.v.customThemeM) + boldprefixcase + 
/* 62 */         ColorUtil.getColorFromString(cfg.v.customThemeSecondBracket) + "] " + EnumChatFormatting.WHITE;
/*    */     }
/*    */ 
/*    */     
/* 66 */     return EnumChatFormatting.GRAY + "[" + EnumChatFormatting.BLUE + prefixcase + EnumChatFormatting.DARK_AQUA + "e" + EnumChatFormatting.AQUA + "o" + EnumChatFormatting.WHITE + "w" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.WHITE;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/Prefix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */