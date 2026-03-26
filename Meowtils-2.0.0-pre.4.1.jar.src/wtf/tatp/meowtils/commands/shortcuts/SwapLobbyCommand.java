/*    */ package wtf.tatp.meowtils.commands.shortcuts;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ 
/*    */ public class SwapLobbyCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 13 */     return "slb";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 18 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 23 */     if (args.length < 1) {
/* 24 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /slb <Lobby number>");
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     String lobby = args[0];
/* 29 */     Meowtils.sendCleanMessage("/swaplobby " + lobby);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 34 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/shortcuts/SwapLobbyCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */