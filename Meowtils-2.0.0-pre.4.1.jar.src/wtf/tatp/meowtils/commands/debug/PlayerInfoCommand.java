/*     */ package wtf.tatp.meowtils.commands.debug;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.network.NetworkPlayerInfo;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.CommandException;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.util.CommandUtil;
/*     */ 
/*     */ 
/*     */ public class PlayerInfoCommand
/*     */   extends CommandBase
/*     */ {
/*     */   public String func_71517_b() {
/*  23 */     return "playerinfo";
/*     */   }
/*     */ 
/*     */   
/*     */   public String func_71518_a(ICommandSender sender) {
/*  28 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> func_71514_a() {
/*  33 */     return Arrays.asList(new String[] { "info", "pi", "playeri", "pinfo" });
/*     */   }
/*     */   
/*     */   private String state(boolean value) {
/*  37 */     return value ? (EnumChatFormatting.GREEN + "true") : (EnumChatFormatting.RED + "false");
/*     */   }
/*     */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/*     */     String playerName;
/*  41 */     Minecraft mc = Minecraft.func_71410_x();
/*     */ 
/*     */     
/*  44 */     if (args.length == 0) {
/*  45 */       if (mc.field_71439_g != null) {
/*  46 */         playerName = mc.field_71439_g.func_70005_c_();
/*     */       } else {
/*  48 */         Meowtils.addMessage(EnumChatFormatting.RED + "Could not determine your username.");
/*     */         return;
/*     */       } 
/*     */     } else {
/*  52 */       playerName = args[0];
/*     */     } 
/*     */     
/*  55 */     EntityPlayer player = mc.field_71441_e.func_72924_a(playerName);
/*     */     
/*  57 */     if (player == null) {
/*  58 */       Meowtils.addMessage(EnumChatFormatting.RED + "Player not found in world.");
/*     */       
/*     */       return;
/*     */     } 
/*  62 */     NetworkPlayerInfo info = mc.func_147114_u().func_175102_a(player.func_110124_au());
/*     */     
/*  64 */     if (info == null) {
/*  65 */       Meowtils.addMessage(EnumChatFormatting.RED + "Could not retrieve tablist info for " + playerName);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  70 */     Meowtils.addMessage(EnumChatFormatting.GOLD + "Player Info: " + player.func_70005_c_());
/*     */ 
/*     */     
/*  73 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Name: " + EnumChatFormatting.WHITE + player.func_70005_c_());
/*  74 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "UUID: " + EnumChatFormatting.WHITE + player.func_110124_au());
/*     */ 
/*     */     
/*  77 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Skin Texture: " + EnumChatFormatting.WHITE + info.func_178837_g());
/*  78 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Cape Texture: " + EnumChatFormatting.WHITE + info.func_178861_h());
/*     */ 
/*     */     
/*  81 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Position: " + EnumChatFormatting.WHITE + String.format("%.2f, %.2f, %.2f", new Object[] { Double.valueOf(player.field_70165_t), Double.valueOf(player.field_70163_u), Double.valueOf(player.field_70161_v) }));
/*  82 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Rotation: " + EnumChatFormatting.WHITE + String.format("%.2f, %.2f", new Object[] { Float.valueOf(player.field_70177_z), Float.valueOf(player.field_70125_A) }));
/*  83 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "On Ground: " + state(player.field_70122_E));
/*  84 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Sneaking: " + state(player.func_70093_af()));
/*     */ 
/*     */     
/*  87 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Health: " + EnumChatFormatting.WHITE + player.func_110143_aJ());
/*  88 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Absorption: " + EnumChatFormatting.WHITE + player.func_110139_bj());
/*  89 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Dead: " + state(player.field_70128_L));
/*     */ 
/*     */     
/*  92 */     ItemStack held = player.func_70694_bm();
/*  93 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Held Item: " + EnumChatFormatting.WHITE + ((held != null) ? held.func_82833_r() : "None"));
/*  94 */     if (held != null && held.func_77942_o()) {
/*  95 */       Meowtils.addMessage(EnumChatFormatting.GRAY + "  NBT: " + held.func_77978_p().toString());
/*     */     }
/*     */ 
/*     */     
/*  99 */     for (ItemStack armorPiece : player.field_71071_by.field_70460_b) {
/* 100 */       if (armorPiece != null) {
/* 101 */         Meowtils.addMessage(EnumChatFormatting.YELLOW + "Armor: " + EnumChatFormatting.WHITE + armorPiece.func_82833_r());
/* 102 */         if (armorPiece.func_77942_o()) {
/* 103 */           Meowtils.addMessage(EnumChatFormatting.GRAY + "  NBT: " + armorPiece.func_77978_p().toString());
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 109 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Ping: " + EnumChatFormatting.WHITE + info.func_178853_c() + "ms");
/*     */ 
/*     */     
/* 112 */     IChatComponent displayName = info.func_178854_k();
/* 113 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Scoreboard Name: " + EnumChatFormatting.WHITE + ((displayName != null) ? displayName.func_150254_d() : player.func_70005_c_()));
/*     */ 
/*     */     
/* 116 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Gamemode: " + EnumChatFormatting.WHITE + info.func_178848_b());
/*     */ 
/*     */     
/* 119 */     Meowtils.addMessage(EnumChatFormatting.YELLOW + "Tablist Visible: " + state(info.func_178845_a().isComplete()));
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
/* 124 */     return CommandUtil.tabCompletePlayerNames(sender, args);
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_82362_a() {
/* 129 */     return 0;
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/debug/PlayerInfoCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */