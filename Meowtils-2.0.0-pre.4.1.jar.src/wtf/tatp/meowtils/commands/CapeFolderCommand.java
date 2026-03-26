/*    */ package wtf.tatp.meowtils.commands;
/*    */ 
/*    */ import java.awt.Desktop;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ 
/*    */ public class CapeFolderCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 16 */     return "capefolder";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 21 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 26 */     File capeDirectory = new File("meowtils/custom_cape");
/*    */     
/*    */     try {
/* 29 */       if (Desktop.isDesktopSupported()) {
/* 30 */         Meowtils.addMessage(EnumChatFormatting.GREEN + "Opening cape folder.");
/* 31 */         Desktop.getDesktop().open(capeDirectory);
/*    */       } else {
/* 33 */         Meowtils.addMessage(EnumChatFormatting.RED + "Failed to open cape folder on this system!");
/* 34 */         Meowtils.addMessage(EnumChatFormatting.GRAY + "Open it manually: " + EnumChatFormatting.YELLOW + "<this minecraft instance folder>/meowtils/custom_cape/");
/*    */       } 
/* 36 */     } catch (IOException e) {
/* 37 */       Meowtils.addMessage(EnumChatFormatting.RED + "Failed to open cape folder: " + e.getMessage());
/* 38 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 44 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/CapeFolderCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */