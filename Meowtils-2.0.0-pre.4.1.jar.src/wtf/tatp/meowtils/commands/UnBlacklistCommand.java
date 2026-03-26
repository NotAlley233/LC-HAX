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
/*    */ import wtf.tatp.meowtils.util.BlacklistUtil;
/*    */ import wtf.tatp.meowtils.util.MojangNameToUUID;
/*    */ import wtf.tatp.meowtils.util.NameUtil;
/*    */ 
/*    */ 
/*    */ public class UnBlacklistCommand
/*    */   extends CommandBase
/*    */ {
/*    */   public String func_71517_b() {
/* 23 */     return "unblacklist";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 28 */     return "";
/*    */   }
/*    */   public List<String> func_71514_a() {
/* 31 */     return Arrays.asList(new String[] { "ubl", "blr", "blacklistremove", "unbl" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) {
/* 36 */     if (args.length < 1) {
/* 37 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /unblacklist <player>");
/*    */       
/*    */       return;
/*    */     } 
/* 41 */     String playername = args[0];
/*    */ 
/*    */     
/* 44 */     for (NetworkPlayerInfo netInfo : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/* 45 */       if (netInfo.func_178845_a().getName().equalsIgnoreCase(playername)) {
/* 46 */         String uuid = netInfo.func_178845_a().getId().toString();
/* 47 */         if (BlacklistUtil.isBlacklistedByUUID(uuid)) {
/* 48 */           BlacklistUtil.removeFromBlacklist(uuid);
/* 49 */           Meowtils.addMessage(EnumChatFormatting.YELLOW + "Removed " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.YELLOW + " from the blacklist.");
/*    */         } else {
/* 51 */           Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is not in the blacklist.");
/*    */         } 
/*    */         
/*    */         return;
/*    */       } 
/*    */     } 
/* 57 */     (new Thread(() -> {
/*    */           try {
/*    */             MojangNameToUUID lookup = new MojangNameToUUID(playername);
/*    */ 
/*    */             
/*    */             String uuid = lookup.getUUID();
/*    */ 
/*    */             
/*    */             if (uuid != null && BlacklistUtil.isBlacklistedByUUID(uuid)) {
/*    */               BlacklistUtil.removeFromBlacklist(uuid);
/*    */ 
/*    */               
/*    */               Minecraft.func_71410_x().func_152343_a(());
/*    */             } else if (BlacklistUtil.isBlacklisted(playername)) {
/*    */               BlacklistUtil.removeFromBlacklist(playername);
/*    */               
/*    */               Minecraft.func_71410_x().func_152343_a(());
/*    */             } else {
/*    */               Minecraft.func_71410_x().func_152343_a(());
/*    */             } 
/* 77 */           } catch (IOException e) {
/*    */             
/*    */             Minecraft.func_71410_x().func_152343_a(());
/*    */           }
/*    */         
/* 82 */         })).start();
/*    */   }
/*    */ 
/*    */   
/*    */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/* 87 */     if (args.length == 1) {
/* 88 */       List<String> names = new ArrayList<>();
/* 89 */       for (NetworkPlayerInfo info : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/* 90 */         names.add(info.func_178845_a().getName());
/*    */       }
/* 92 */       return func_175762_a(args, names);
/*    */     } 
/* 94 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 99 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/UnBlacklistCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */