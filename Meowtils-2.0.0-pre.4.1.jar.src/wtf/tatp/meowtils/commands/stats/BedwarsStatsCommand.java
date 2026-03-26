/*    */ package wtf.tatp.meowtils.commands.stats;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.util.CommandUtil;
/*    */ import wtf.tatp.meowtils.util.stats.StatsFetcher;
/*    */ 
/*    */ public class BedwarsStatsCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 18 */     return "s";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 23 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> func_71514_a() {
/* 28 */     return Arrays.asList(new String[] { "statbw", "meowstatbw" });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/*    */     String player;
/* 35 */     if (args.length == 0) {
/* 36 */       if (Meowtils.mc.field_71439_g != null) {
/* 37 */         player = Meowtils.mc.field_71439_g.func_70005_c_();
/*    */       } else {
/* 39 */         Meowtils.addMessage(EnumChatFormatting.RED + "Could not determine your username.");
/*    */         return;
/*    */       } 
/*    */     } else {
/* 43 */       player = args[0];
/*    */     } 
/*    */     
/* 46 */     StatsFetcher.fetchStats(player);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/* 51 */     return CommandUtil.tabCompletePlayerNames(sender, args);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 56 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/stats/BedwarsStatsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */