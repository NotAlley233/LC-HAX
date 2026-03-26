/*    */ package wtf.tatp.meowtils.commands;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.network.NetworkPlayerInfo;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.util.MojangNameToUUID;
/*    */ import wtf.tatp.meowtils.util.NameUtil;
/*    */ import wtf.tatp.meowtils.util.SafelistUtil;
/*    */ 
/*    */ 
/*    */ public class UnSafelistCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 23 */     return "unsafelist";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 28 */     return "";
/*    */   }
/*    */   public List<String> func_71514_a() {
/* 31 */     return Arrays.asList(new String[] { "usl", "slr", "safelistremove", "unsl" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) {
/* 36 */     if (args.length < 1) {
/* 37 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /unsafelist <player>");
/*    */       
/*    */       return;
/*    */     } 
/* 41 */     String playername = args[0];
/*    */     
/* 43 */     for (NetworkPlayerInfo netInfo : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/* 44 */       if (netInfo.func_178845_a().getName().equalsIgnoreCase(playername)) {
/* 45 */         String uuid = netInfo.func_178845_a().getId().toString();
/* 46 */         if (SafelistUtil.isSafelisted(uuid)) {
/* 47 */           SafelistUtil.removeFromSafelist(uuid);
/* 48 */           Meowtils.addMessage(EnumChatFormatting.YELLOW + "Removed " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.YELLOW + " from the safelist.");
/*    */         } else {
/* 50 */           Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is not in the safelist.");
/*    */         } 
/*    */         
/*    */         return;
/*    */       } 
/*    */     } 
/* 56 */     (new Thread(() -> {
/*    */           try {
/*    */             MojangNameToUUID lookup = new MojangNameToUUID(playername);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/*    */             String uuid = lookup.getUUID();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/*    */             Minecraft.func_71410_x().func_152344_a(());
/* 72 */           } catch (IOException e) {
/*    */             
/*    */             Minecraft.func_71410_x().func_152343_a(());
/*    */           }
/*    */         
/* 77 */         })).start();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/* 82 */     if (args.length == 1) {
/* 83 */       List<String> names = new ArrayList<>();
/* 84 */       for (NetworkPlayerInfo info : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/* 85 */         names.add(info.func_178845_a().getName());
/*    */       }
/* 87 */       return func_175762_a(args, names);
/*    */     } 
/* 89 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 94 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/UnSafelistCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */