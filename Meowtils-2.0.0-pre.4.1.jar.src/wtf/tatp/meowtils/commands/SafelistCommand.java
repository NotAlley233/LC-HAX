/*     */ package wtf.tatp.meowtils.commands;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.util.MojangNameToUUID;
/*     */ import wtf.tatp.meowtils.util.NameUtil;
/*     */ import wtf.tatp.meowtils.util.SafelistUtil;
/*     */ 
/*     */ public class SafelistCommand
/*     */   extends CommandBase {
/*     */   public String func_71517_b() {
/*  21 */     return "safelist";
/*     */   }
/*     */ 
/*     */   
/*     */   public String func_71518_a(ICommandSender sender) {
/*  26 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> func_71514_a() {
/*  31 */     return Arrays.asList(new String[] { "sl" });
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_71515_b(ICommandSender sender, String[] args) {
/*  36 */     if (args.length == 0) {
/*  37 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /safelist <player>");
/*     */       
/*     */       return;
/*     */     } 
/*  41 */     if (args[0].equalsIgnoreCase("info")) {
/*  42 */       if (args.length < 2) {
/*  43 */         Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /safelist info <player>");
/*     */         
/*     */         return;
/*     */       } 
/*  47 */       String str = args[1];
/*     */       
/*  49 */       for (NetworkPlayerInfo netInfo : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/*  50 */         if (netInfo.func_178845_a().getName().equalsIgnoreCase(str)) {
/*  51 */           String uuid = netInfo.func_178845_a().getId().toString();
/*  52 */           if (SafelistUtil.isSafelisted(uuid)) {
/*  53 */             Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(str) + EnumChatFormatting.GREEN + " is safelisted.");
/*     */             
/*     */             return;
/*     */           } 
/*     */           break;
/*     */         } 
/*     */       } 
/*  60 */       (new Thread(() -> {
/*     */             try {
/*     */               MojangNameToUUID lookup = new MojangNameToUUID(playername);
/*     */               String uuid = lookup.getUUID();
/*  64 */               boolean safelisted = ((uuid != null && SafelistUtil.isSafelisted(uuid)) || SafelistUtil.isSafelisted(playername));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               Minecraft.func_71410_x().func_152344_a(());
/*  72 */             } catch (IOException e) {
/*     */               
/*     */               Minecraft.func_71410_x().func_152343_a(());
/*     */             }
/*     */           
/*  77 */           })).start();
/*     */       
/*     */       return;
/*     */     } 
/*  81 */     String playername = args[0];
/*     */     
/*  83 */     for (NetworkPlayerInfo netInfo : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/*  84 */       if (netInfo.func_178845_a().getName().equalsIgnoreCase(playername)) {
/*  85 */         String uuid = netInfo.func_178845_a().getId().toString();
/*  86 */         if (SafelistUtil.isSafelisted(uuid)) {
/*  87 */           Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is already safelisted.");
/*     */           return;
/*     */         } 
/*  90 */         SafelistUtil.addToSafelist(uuid);
/*  91 */         Meowtils.addMessage(EnumChatFormatting.GREEN + "Safelisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + ".");
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/*  96 */     (new Thread(() -> {
/*     */           try {
/*     */             MojangNameToUUID lookup = new MojangNameToUUID(playername);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             String uuid = lookup.getUUID();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             Minecraft.func_71410_x().func_152344_a(());
/* 117 */           } catch (IOException e) {
/*     */             
/*     */             Minecraft.func_71410_x().func_152343_a(());
/*     */           }
/*     */         
/* 122 */         })).start();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/* 127 */     if (args.length == 1) {
/* 128 */       List<String> names = new ArrayList<>();
/* 129 */       for (NetworkPlayerInfo info : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/* 130 */         names.add(info.func_178845_a().getName());
/*     */       }
/* 132 */       return func_175762_a(args, names);
/*     */     } 
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_82362_a() {
/* 139 */     return 0;
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/SafelistCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */