/*    */ package wtf.tatp.meowtils.commands;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class RequeueCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 13 */     return "rq";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 18 */     return "";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) {
/* 24 */     if (!cfg.v.lastPlayCommand.isEmpty()) {
/* 25 */       Meowtils.sendCleanMessage("/play " + cfg.v.lastPlayCommand);
/*    */     } else {
/* 27 */       Meowtils.addMessage(EnumChatFormatting.RED + "No previously saved /play command found.");
/*    */     } 
/*    */   }
/*    */   
/*    */   public int func_82362_a() {
/* 32 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/RequeueCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */