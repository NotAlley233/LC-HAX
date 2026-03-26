/*     */ package wtf.tatp.meowtils.commands;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.util.BlacklistUtil;
/*     */ import wtf.tatp.meowtils.util.MojangNameToUUID;
/*     */ import wtf.tatp.meowtils.util.NameUtil;
/*     */ 
/*     */ public class BlacklistCommand
/*     */   extends CommandBase {
/*     */   public String func_71517_b() {
/*  23 */     return "blacklist";
/*     */   }
/*     */ 
/*     */   
/*     */   public String func_71518_a(ICommandSender sender) {
/*  28 */     return "";
/*     */   }
/*     */   public List<String> func_71514_a() {
/*  31 */     return Arrays.asList(new String[] { "bl" });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void func_71515_b(ICommandSender sender, String[] args) {
/*  37 */     if (args.length == 0) {
/*  38 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /blacklist <player> <reason>");
/*     */       
/*     */       return;
/*     */     } 
/*  42 */     if (args[0].equalsIgnoreCase("info")) {
/*  43 */       if (args.length < 2) {
/*  44 */         Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /blacklist info <player>");
/*     */         
/*     */         return;
/*     */       } 
/*  48 */       String str = args[1];
/*     */       
/*  50 */       for (NetworkPlayerInfo netInfo : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/*  51 */         if (netInfo.func_178845_a().getName().equalsIgnoreCase(str)) {
/*  52 */           String uuid = netInfo.func_178845_a().getId().toString();
/*  53 */           if (BlacklistUtil.isBlacklistedByUUID(uuid)) {
/*  54 */             String entry = BlacklistUtil.getEntry(uuid);
/*  55 */             sendBlacklistInfo(sender, str, entry);
/*     */             
/*     */             return;
/*     */           } 
/*     */           break;
/*     */         } 
/*     */       } 
/*  62 */       (new Thread(() -> {
/*     */             try {
/*     */               MojangNameToUUID lookup = new MojangNameToUUID(playername);
/*     */ 
/*     */               
/*     */               String uuid = lookup.getUUID();
/*     */ 
/*     */               
/*     */               if (uuid != null && BlacklistUtil.isBlacklistedByUUID(uuid)) {
/*     */                 String entry = BlacklistUtil.getEntry(uuid);
/*     */                 
/*     */                 Minecraft.func_71410_x().func_152344_a(());
/*     */               } else if (BlacklistUtil.isBlacklisted(playername)) {
/*     */                 String entry = BlacklistUtil.getEntry(playername);
/*     */                 
/*     */                 Minecraft.func_71410_x().func_152344_a(());
/*     */               } else {
/*     */                 Minecraft.func_71410_x().func_152343_a(());
/*     */               } 
/*  81 */             } catch (IOException e) {
/*     */               
/*     */               Minecraft.func_71410_x().func_152343_a(());
/*     */             }
/*     */           
/*  86 */           })).start();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  91 */     String playername = args[0];
/*  92 */     String reason = (args.length > 1) ? reasonPreserve(Arrays.<String>copyOfRange(args, 1, args.length)) : "cheating";
/*     */ 
/*     */     
/*  95 */     for (NetworkPlayerInfo netInfo : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/*  96 */       if (netInfo.func_178845_a().getName().equalsIgnoreCase(playername)) {
/*  97 */         String uuid = netInfo.func_178845_a().getId().toString();
/*  98 */         if (BlacklistUtil.isBlacklistedByUUID(uuid)) {
/*  99 */           Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is already in the blacklist!");
/*     */           return;
/*     */         } 
/* 102 */         BlacklistUtil.addToBlacklist(uuid, reason);
/* 103 */         Meowtils.addMessage(EnumChatFormatting.GREEN + "Blacklisted " + EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " for: " + BlacklistUtil.getFormattedReason(reason));
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 108 */     (new Thread(() -> {
/*     */           try {
/*     */             MojangNameToUUID lookup = new MojangNameToUUID(playername);
/*     */ 
/*     */ 
/*     */             
/*     */             String uuid = lookup.getUUID();
/*     */ 
/*     */ 
/*     */             
/*     */             if (uuid != null) {
/*     */               if (BlacklistUtil.isBlacklistedByUUID(uuid)) {
/*     */                 Minecraft.func_71410_x().func_152343_a(());
/*     */               } else {
/*     */                 BlacklistUtil.addToBlacklist(uuid, reason);
/*     */ 
/*     */                 
/*     */                 Minecraft.func_71410_x().func_152343_a(());
/*     */               } 
/*     */             } else if (BlacklistUtil.isBlacklisted(playername)) {
/*     */               Minecraft.func_71410_x().func_152343_a(());
/*     */             } else {
/*     */               BlacklistUtil.addToBlacklist(playername, reason);
/*     */ 
/*     */               
/*     */               Minecraft.func_71410_x().func_152343_a(());
/*     */             } 
/* 135 */           } catch (IOException e) {
/*     */             
/*     */             Minecraft.func_71410_x().func_152343_a(());
/*     */           }
/*     */         
/* 140 */         })).start();
/*     */   }
/*     */   private void sendBlacklistInfo(ICommandSender sender, String playername, String entry) {
/*     */     long timeMillis;
/* 144 */     if (entry == null) {
/* 145 */       Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.GREEN + " is not in the blacklist.");
/*     */       return;
/*     */     } 
/* 148 */     String[] split = entry.split(" ", 2);
/* 149 */     String timestamp = split[0];
/* 150 */     String reason = (split.length > 1) ? split[1] : "No reason provided";
/*     */ 
/*     */     
/*     */     try {
/* 154 */       timeMillis = Long.parseLong(timestamp);
/* 155 */     } catch (NumberFormatException e) {
/* 156 */       Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is blacklisted (couldn't find timestamp) for: " + BlacklistUtil.getFormattedReason(reason));
/*     */       
/*     */       return;
/*     */     } 
/* 160 */     String formattedTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date(timeMillis));
/* 161 */     Meowtils.addMessage(EnumChatFormatting.RESET + NameUtil.getTabDisplayName(playername) + EnumChatFormatting.RED + " is blacklisted since " + EnumChatFormatting.GRAY + formattedTime + EnumChatFormatting.RED + " for: " + BlacklistUtil.getFormattedReason(reason));
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/* 166 */     if (args.length == 1) {
/* 167 */       List<String> names = new ArrayList<>();
/* 168 */       for (NetworkPlayerInfo info : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
/* 169 */         names.add(info.func_178845_a().getName());
/*     */       }
/* 171 */       return func_175762_a(args, names);
/*     */     } 
/* 173 */     return null;
/*     */   }
/*     */   public int func_82362_a() {
/* 176 */     return 0;
/*     */   }
/*     */   
/*     */   private String reasonPreserve(String[] reasonParts) {
/* 180 */     List<String> result = new ArrayList<>();
/* 181 */     for (int i = 0; i < reasonParts.length; i++) {
/* 182 */       String current = reasonParts[i].toLowerCase();
/* 183 */       if (i < reasonParts.length - 1) {
/* 184 */         String combined = current + " " + reasonParts[i + 1].toLowerCase();
/* 185 */         if (BlacklistUtil.NON_BLATANT.contains(combined) || BlacklistUtil.BLATANT.contains(combined)) {
/* 186 */           result.add(combined);
/* 187 */           i++;
/*     */           continue;
/*     */         } 
/*     */       } 
/* 191 */       result.add(current); continue;
/*     */     } 
/* 193 */     return String.join(" | ", (Iterable)result);
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/BlacklistCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */