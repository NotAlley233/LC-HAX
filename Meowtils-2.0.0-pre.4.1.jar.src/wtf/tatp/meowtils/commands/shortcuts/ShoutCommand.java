/*    */ package wtf.tatp.meowtils.commands.shortcuts;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.util.CommandUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ShoutCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 20 */     return "sh";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 25 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 30 */     if (args.length < 1) {
/* 31 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /sh <msg>");
/*    */       
/*    */       return;
/*    */     } 
/* 35 */     String shoutMessage = String.join(" ", (CharSequence[])args);
/* 36 */     (Minecraft.func_71410_x()).field_71439_g.func_71165_d("/shout " + shoutMessage);
/*    */   }
/*    */   
/*    */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/* 40 */     return CommandUtil.tabCompletePlayerNames(sender, args);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 45 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/shortcuts/ShoutCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */