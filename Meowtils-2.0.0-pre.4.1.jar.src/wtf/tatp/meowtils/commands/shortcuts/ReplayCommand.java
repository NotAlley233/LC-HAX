/*    */ package wtf.tatp.meowtils.commands.shortcuts;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ 
/*    */ 
/*    */ public class ReplayCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 15 */     return "rp";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 20 */     return "";
/*    */   } public List<String> func_71514_a() {
/* 22 */     return Arrays.asList(new String[] { "rpl" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 27 */     Meowtils.sendCleanMessage("/replay");
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 32 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/shortcuts/ReplayCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */