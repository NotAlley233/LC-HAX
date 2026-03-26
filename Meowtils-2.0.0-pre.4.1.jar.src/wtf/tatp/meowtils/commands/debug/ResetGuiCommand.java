/*    */ package wtf.tatp.meowtils.commands.debug;
/*    */ 
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class ResetGuiCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 14 */     return "resetgui";
/*    */   }
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 18 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 23 */     cfg.v.meowtilsCategoryX = 5;
/* 24 */     cfg.v.meowtilsCategoryY = 5;
/* 25 */     cfg.v.hypixelCategoryX = 90;
/* 26 */     cfg.v.hypixelCategoryY = 5;
/* 27 */     cfg.v.skywarsCategoryX = 175;
/* 28 */     cfg.v.skywarsCategoryY = 5;
/* 29 */     cfg.v.bedwarsCategoryX = 260;
/* 30 */     cfg.v.bedwarsCategoryY = 5;
/* 31 */     cfg.v.renderCategoryX = 345;
/* 32 */     cfg.v.renderCategoryY = 5;
/* 33 */     cfg.v.antisnipeCategoryX = 430;
/* 34 */     cfg.v.antisnipeCategoryY = 5;
/* 35 */     cfg.v.utilityCategoryX = 515;
/* 36 */     cfg.v.utilityCategoryY = 5;
/* 37 */     cfg.v.advancedCategoryX = 515;
/* 38 */     cfg.v.advancedCategoryY = 160;
/*    */     
/* 40 */     cfg.v.meowtilsCategoryExpanded = false;
/* 41 */     cfg.v.hypixelCategoryExpanded = false;
/* 42 */     cfg.v.skywarsCategoryExpanded = false;
/* 43 */     cfg.v.bedwarsCategoryExpanded = false;
/* 44 */     cfg.v.renderCategoryExpanded = false;
/* 45 */     cfg.v.antisnipeCategoryExpanded = false;
/* 46 */     cfg.v.utilityCategoryExpanded = false;
/* 47 */     cfg.v.advancedCategoryExpanded = false;
/*    */     
/* 49 */     cfg.save();
/*    */     
/* 51 */     Meowtils.addMessage(EnumChatFormatting.GREEN + "Reset GUI positions." + EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.ITALIC + " Requires restart.");
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 56 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/debug/ResetGuiCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */