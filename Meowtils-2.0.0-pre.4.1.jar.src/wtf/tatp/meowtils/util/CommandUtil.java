/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ 
/*    */ 
/*    */ public class CommandUtil
/*    */ {
/*    */   public static List<String> tabCompletePlayerNames(ICommandSender sender, String[] args) {
/* 14 */     List<String> names = new ArrayList<>();
/* 15 */     for (NetworkPlayerInfo info : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/* 16 */       names.add(info.func_178845_a().getName());
/*    */     }
/*    */     
/* 19 */     return CommandBase.func_175762_a(args, names);
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/CommandUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */