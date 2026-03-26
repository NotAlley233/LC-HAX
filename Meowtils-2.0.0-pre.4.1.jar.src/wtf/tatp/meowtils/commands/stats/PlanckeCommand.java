/*    */ package wtf.tatp.meowtils.commands.stats;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.event.ClickEvent;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.util.CommandUtil;
/*    */ import wtf.tatp.meowtils.util.Prefix;
/*    */ 
/*    */ public class PlanckeCommand
/*    */   extends CommandBase {
/*    */   public String func_71517_b() {
/* 20 */     return "plancke";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 25 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> func_71514_a() {
/* 30 */     return Arrays.asList(new String[] { "pla" });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/*    */     String player;
/* 37 */     if (args.length == 0) {
/* 38 */       if (Meowtils.mc.field_71439_g != null) {
/* 39 */         player = Meowtils.mc.field_71439_g.func_70005_c_();
/*    */       } else {
/* 41 */         Meowtils.addMessage(EnumChatFormatting.RED + "Could not determine your username.");
/*    */         return;
/*    */       } 
/*    */     } else {
/* 45 */       player = args[0];
/*    */     } 
/* 47 */     ChatComponentText message = new ChatComponentText(Prefix.getPrefix() + EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD + player + "'s stats on Plancke");
/* 48 */     message.func_150255_a(message.func_150256_b().func_150241_a(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://plancke.io/hypixel/player/stats/" + player)).func_150228_d(Boolean.valueOf(true)));
/*    */     
/* 50 */     Meowtils.mc.field_71439_g.func_145747_a((IChatComponent)message);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/* 55 */     return CommandUtil.tabCompletePlayerNames(sender, args);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 60 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/stats/PlanckeCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */