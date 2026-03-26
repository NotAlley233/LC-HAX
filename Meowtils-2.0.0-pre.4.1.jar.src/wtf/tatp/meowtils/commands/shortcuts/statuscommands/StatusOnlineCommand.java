/*    */ package wtf.tatp.meowtils.commands.shortcuts.statuscommands;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ 
/*    */ public class StatusOnlineCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 12 */     return "online";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 17 */     return "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 23 */     Meowtils.sendCleanMessage("/status online");
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 28 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/shortcuts/statuscommands/StatusOnlineCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */