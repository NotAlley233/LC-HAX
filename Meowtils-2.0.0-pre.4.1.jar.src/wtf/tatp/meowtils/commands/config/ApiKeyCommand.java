/*    */ package wtf.tatp.meowtils.commands.config;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class ApiKeyCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 16 */     return "meowapi";
/*    */   }
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 20 */     return "";
/*    */   } public List<String> func_71514_a() {
/* 22 */     return Arrays.asList(new String[] { "meowapikey", "mapikey" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 27 */     if (args.length == 0) {
/* 28 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /meowapi <key>");
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     String newMessage = String.join(" ", (CharSequence[])args);
/*    */     
/* 34 */     cfg.v.apiKey = newMessage;
/*    */     
/* 36 */     cfg.save();
/* 37 */     Meowtils.addMessage(EnumChatFormatting.GREEN + "Set API key.");
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 42 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/config/ApiKeyCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */