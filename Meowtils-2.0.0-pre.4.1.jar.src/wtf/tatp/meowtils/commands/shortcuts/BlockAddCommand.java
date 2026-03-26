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
/*    */ public class BlockAddCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 16 */     return "block";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 21 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 26 */     String arg = args[0];
/* 27 */     if (args.length < 1) {
/* 28 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /block <player>");
/*    */       return;
/*    */     } 
/* 31 */     if (arg.equalsIgnoreCase("list")) {
/* 32 */       Meowtils.sendCleanMessage("/block list");
/*    */       return;
/*    */     } 
/* 35 */     if (arg.equalsIgnoreCase("help")) {
/* 36 */       Meowtils.sendCleanMessage("/block help");
/*    */       return;
/*    */     } 
/* 39 */     if (arg.equalsIgnoreCase("add") || arg.equalsIgnoreCase("remove")) {
/* 40 */       String str = args[1];
/* 41 */       Meowtils.sendCleanMessage("/block " + arg.toLowerCase() + " " + str);
/*    */     } 
/*    */ 
/*    */     
/* 45 */     String playerName = arg;
/* 46 */     Meowtils.sendCleanMessage("/block add " + playerName);
/*    */   }
/*    */   
/*    */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/* 50 */     return CommandUtil.tabCompletePlayerNames(sender, args);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 55 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/shortcuts/BlockAddCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */