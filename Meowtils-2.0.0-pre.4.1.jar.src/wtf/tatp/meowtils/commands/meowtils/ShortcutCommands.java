/*    */ package wtf.tatp.meowtils.commands.meowtils;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ 
/*    */ public class ShortcutCommands extends CommandBase {
/*    */   public String func_71517_b() {
/* 10 */     return "shortcuts";
/*    */   }
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 14 */     return "";
/*    */   }
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 18 */     String separator = " » ";
/* 19 */     Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
/*    */     
/* 21 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/renick" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/nick reuse");
/* 22 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/away" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/status away");
/* 23 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/busy" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/status busy");
/* 24 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/offline" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/status offline");
/* 25 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/online" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/status online");
/* 26 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/block" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/block add");
/* 27 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/unblock" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/block remove");
/* 28 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/ca" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/chat all");
/* 29 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/cc" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/chat coop");
/* 30 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/cg" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/chat guild");
/* 31 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/co" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/chat officer");
/* 32 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/cp" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/chat party");
/* 33 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/gi" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/guild info");
/* 34 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/glo" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/guild online");
/* 35 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/gt" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/guild toggle");
/* 36 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/pd" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party demote");
/* 37 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/disband" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party disband");
/* 38 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/poffline" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party kickoffline");
/* 39 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/pp" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party promote");
/* 40 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/psa" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party settings allinvite");
/* 41 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/pt" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/party transfer");
/* 42 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/rp" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/replay");
/* 43 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/sh" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/shout");
/* 44 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/slb" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/swaplobby");
/* 45 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/ta" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/tip all");
/* 46 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/tc" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/togglechat");
/*    */     
/* 48 */     Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 53 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/meowtils/ShortcutCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */