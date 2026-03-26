/*    */ package wtf.tatp.meowtils.commands.config.customthemecolor;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class SetThemeCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 13 */     return "settheme";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 18 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) {
/* 23 */     if (args.length != 2) {
/* 24 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /settheme <m|e|o|w> <COLOR_NAME>");
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     String letter = args[0].toLowerCase();
/* 29 */     String colorName = args[1].toUpperCase();
/*    */     
/*    */     try {
/* 32 */       EnumChatFormatting.valueOf(colorName);
/*    */       
/* 34 */       switch (letter) { case "m":
/* 35 */           cfg.v.customThemeM = colorName; break;
/* 36 */         case "e": cfg.v.customThemeE = colorName; break;
/* 37 */         case "o": cfg.v.customThemeO = colorName; break;
/* 38 */         case "w": cfg.v.customThemeW = colorName; break;
/* 39 */         case "[": cfg.v.customThemeFirstBracket = colorName; break;
/* 40 */         case "]": cfg.v.customThemeSecondBracket = colorName; break;
/*    */         default:
/* 42 */           Meowtils.addMessage(EnumChatFormatting.RED + "Invalid letter.");
/*    */           return; }
/*    */ 
/*    */       
/* 46 */       cfg.save();
/* 47 */       Meowtils.addMessage(EnumChatFormatting.GREEN + "Set color for '" + letter.toUpperCase() + "' to " + colorName);
/* 48 */     } catch (IllegalArgumentException ex) {
/* 49 */       Meowtils.addMessage(EnumChatFormatting.RED + "Invalid color name.");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 55 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/config/customthemecolor/SetThemeCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */