/*    */ package wtf.tatp.meowtils.commands.debug;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class DebugMessageCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 14 */     return "meowdebug";
/*    */   }
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 18 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 23 */     cfg.v.debugMessages = !cfg.v.debugMessages;
/* 24 */     String toggleMessage = cfg.v.debugMessages ? (EnumChatFormatting.GREEN.toString() + EnumChatFormatting.BOLD + "ON") : (EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + "OFF");
/*    */     
/* 26 */     cfg.save();
/* 27 */     Meowtils.addMessage("Debug messages: " + toggleMessage);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 32 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/debug/DebugMessageCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */