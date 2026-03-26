/*    */ package wtf.tatp.meowtils.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.util.CommandUtil;
/*    */ 
/*    */ 
/*    */ public class SendCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 17 */     return "send";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 22 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 27 */     if (args.length < 1) {
/* 28 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /send <msg | command>");
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     String sendMessage = String.join(" ", (CharSequence[])args);
/* 33 */     Meowtils.sendCleanMessage(sendMessage);
/*    */   }
/*    */   
/*    */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/* 37 */     return CommandUtil.tabCompletePlayerNames(sender, args);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 42 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/SendCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */