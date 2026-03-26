/*    */ package wtf.tatp.meowtils.commands.config.flagmessagecolor;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class SetFlagMessageColorCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 13 */     return "setflagmessagecolor";
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
/* 24 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /setflagmessagecolor");
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     String letter = args[0].toLowerCase();
/* 29 */     String colorName = args[1].toUpperCase();
/*    */     
/*    */     try {
/* 32 */       EnumChatFormatting.valueOf(colorName);
/*    */       
/* 34 */       switch (letter) { case "reason":
/* 35 */           cfg.v.flagMessageComponentColor = colorName; break;
/* 36 */         case "wdr": cfg.v.flagMessageButtonColor = colorName; break;
/* 37 */         case "bracket": cfg.v.flagMessageBracketColor = colorName; break;
/*    */         default:
/* 39 */           Meowtils.addMessage(EnumChatFormatting.RED + "Invalid letter.");
/*    */           return; }
/*    */ 
/*    */       
/* 43 */       cfg.save();
/* 44 */       Meowtils.addMessage(EnumChatFormatting.GREEN + "Set color for '" + letter.toUpperCase() + "' to " + colorName);
/* 45 */     } catch (IllegalArgumentException ex) {
/* 46 */       Meowtils.addMessage(EnumChatFormatting.RED + "Invalid color name.");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 52 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/config/flagmessagecolor/SetFlagMessageColorCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */