/*    */ package wtf.tatp.meowtils.commands;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ 
/*    */ public class ColorCodeCommand
/*    */   extends CommandBase {
/*    */   public String func_71517_b() {
/* 12 */     return "meowcolor";
/*    */   }
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 16 */     return "";
/*    */   }
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 20 */     Meowtils.addCleanMessage(EnumChatFormatting.GRAY + "" + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
/* 21 */     Meowtils.addCleanMessage(EnumChatFormatting.BLACK + "&0 >> BLACK");
/* 22 */     Meowtils.addCleanMessage(EnumChatFormatting.DARK_BLUE + "&1 >> DARK_BLUE");
/* 23 */     Meowtils.addCleanMessage(EnumChatFormatting.DARK_GREEN + "&2 >> DARK_GREEN");
/* 24 */     Meowtils.addCleanMessage(EnumChatFormatting.DARK_AQUA + "&3 >> DARK_AQUA");
/* 25 */     Meowtils.addCleanMessage(EnumChatFormatting.DARK_RED + "&4 >> DARK_RED");
/* 26 */     Meowtils.addCleanMessage(EnumChatFormatting.DARK_PURPLE + "&5 >> DARK_PURPLE");
/* 27 */     Meowtils.addCleanMessage(EnumChatFormatting.GOLD + "&6 >> GOLD");
/* 28 */     Meowtils.addCleanMessage(EnumChatFormatting.GRAY + "&7 >> GRAY");
/* 29 */     Meowtils.addCleanMessage(EnumChatFormatting.DARK_GRAY + "&8 >> DARK_GRAY");
/* 30 */     Meowtils.addCleanMessage(EnumChatFormatting.BLUE + "&9 >> BLUE");
/* 31 */     Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "&a >> GREEN");
/* 32 */     Meowtils.addCleanMessage(EnumChatFormatting.AQUA + "&b >> AQUA");
/* 33 */     Meowtils.addCleanMessage(EnumChatFormatting.RED + "&c >> RED");
/* 34 */     Meowtils.addCleanMessage(EnumChatFormatting.LIGHT_PURPLE + "&d >> LIGHT_PURPLE");
/* 35 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "&e >> YELLOW");
/* 36 */     Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&f >> WHITE");
/* 37 */     Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.UNDERLINE + "Formatting codes:");
/* 38 */     Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&k >> " + EnumChatFormatting.OBFUSCATED + "OBFUSCATED");
/* 39 */     Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&m >> " + EnumChatFormatting.STRIKETHROUGH + "STRIKETHROUGH");
/* 40 */     Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&o >> " + EnumChatFormatting.ITALIC + "ITALIC");
/* 41 */     Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&l >> " + EnumChatFormatting.BOLD + "BOLD");
/* 42 */     Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&n >> " + EnumChatFormatting.UNDERLINE + "UNDERLINE");
/* 43 */     Meowtils.addCleanMessage(EnumChatFormatting.WHITE + "&r >> RESET");
/* 44 */     Meowtils.addCleanMessage(EnumChatFormatting.GRAY + "" + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
/*    */   }
/*    */   public int func_82362_a() {
/* 47 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/ColorCodeCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */