/*   */ package wtf.tatp.meowtils.util;
/*   */ 
/*   */ public class StripFormattingCodes {
/*   */   public static String stripFormattingCodes(String input) {
/* 5 */     return input.replaceAll("§[0-9a-fk-or]", "");
/*   */   }
/*   */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/StripFormattingCodes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */