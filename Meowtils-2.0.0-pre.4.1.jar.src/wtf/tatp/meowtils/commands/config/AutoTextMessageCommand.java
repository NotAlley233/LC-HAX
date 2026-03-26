/*     */ package wtf.tatp.meowtils.commands.config;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.util.CommandUtil;
/*     */ 
/*     */ 
/*     */ public class AutoTextMessageCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public String func_71517_b() {
/*  19 */     return "autotext";
/*     */   }
/*     */   
/*     */   public List<String> func_71514_a() {
/*  23 */     return Arrays.asList(new String[] { "at" });
/*     */   }
/*     */ 
/*     */   
/*     */   public String func_71518_a(ICommandSender sender) {
/*  28 */     return "";
/*     */   }
/*     */   
/*     */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/*     */     int index;
/*  33 */     if (args.length < 2) {
/*  34 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /at or /autotext <1-10> <message|clear>");
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*     */     try {
/*  40 */       index = Integer.parseInt(args[0]) - 1;
/*  41 */     } catch (NumberFormatException e) {
/*  42 */       Meowtils.addMessage(EnumChatFormatting.RED + "Must be a number between 1-10!");
/*     */       
/*     */       return;
/*     */     } 
/*  46 */     if (index < 0 || index >= 10) {
/*  47 */       Meowtils.addMessage(EnumChatFormatting.RED + "Must be a number between 1-10!");
/*     */       
/*     */       return;
/*     */     } 
/*  51 */     if (args[1].equalsIgnoreCase("clear") && args.length == 2) {
/*  52 */       saveMessage(index, "");
/*  53 */       Meowtils.addMessage(EnumChatFormatting.GREEN + "Cleared AutoText message!");
/*     */       
/*     */       return;
/*     */     } 
/*  57 */     String autoMessage = String.join(" ", Arrays.<CharSequence>copyOfRange((CharSequence[])args, 1, args.length));
/*  58 */     saveMessage(index, autoMessage);
/*  59 */     Meowtils.addMessage(EnumChatFormatting.GREEN + "AutoText " + (index + 1) + " set to: " + EnumChatFormatting.WHITE + autoMessage);
/*     */   }
/*     */   
/*     */   private void saveMessage(int index, String message) {
/*  63 */     switch (index) {
/*     */       case 0:
/*  65 */         cfg.v.autoText0 = message;
/*     */         break;
/*     */       case 1:
/*  68 */         cfg.v.autoText1 = message;
/*     */         break;
/*     */       case 2:
/*  71 */         cfg.v.autoText2 = message;
/*     */         break;
/*     */       case 3:
/*  74 */         cfg.v.autoText3 = message;
/*     */         break;
/*     */       case 4:
/*  77 */         cfg.v.autoText4 = message;
/*     */         break;
/*     */       case 5:
/*  80 */         cfg.v.autoText5 = message;
/*     */         break;
/*     */       case 6:
/*  83 */         cfg.v.autoText6 = message;
/*     */         break;
/*     */       case 7:
/*  86 */         cfg.v.autoText7 = message;
/*     */         break;
/*     */       case 8:
/*  89 */         cfg.v.autoText8 = message;
/*     */         break;
/*     */       case 9:
/*  92 */         cfg.v.autoText9 = message;
/*     */         break;
/*     */     } 
/*  95 */     cfg.save();
/*     */   }
/*     */   
/*     */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/*  99 */     return CommandUtil.tabCompletePlayerNames(sender, args);
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_82362_a() {
/* 104 */     return 0;
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/config/AutoTextMessageCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */