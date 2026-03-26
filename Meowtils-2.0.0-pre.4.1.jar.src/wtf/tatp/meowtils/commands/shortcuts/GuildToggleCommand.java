/*    */ package wtf.tatp.meowtils.commands.shortcuts;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ 
/*    */ 
/*    */ public class GuildToggleCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 13 */     return "gt";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 18 */     return "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 24 */     Meowtils.sendCleanMessage("/guild toggle");
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 29 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/shortcuts/GuildToggleCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */