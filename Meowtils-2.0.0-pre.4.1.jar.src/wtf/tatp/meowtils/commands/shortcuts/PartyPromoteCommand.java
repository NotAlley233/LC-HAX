/*    */ package wtf.tatp.meowtils.commands.shortcuts;
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
/*    */ public class PartyPromoteCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 16 */     return "pp";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 21 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 26 */     if (args.length < 1) {
/* 27 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /pp <player>");
/*    */       
/*    */       return;
/*    */     } 
/* 31 */     String playerName = args[0];
/* 32 */     Meowtils.sendCleanMessage("/party promote " + playerName);
/*    */   }
/*    */   
/*    */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/* 36 */     return CommandUtil.tabCompletePlayerNames(sender, args);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 41 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/shortcuts/PartyPromoteCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */