/*     */ package wtf.tatp.meowtils.commands.meowtils;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.command.CommandBase;
/*     */ import net.minecraft.command.ICommandSender;
/*     */ import net.minecraft.event.ClickEvent;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ 
/*     */ public class MeowtilsCommand
/*     */   extends CommandBase
/*     */ {
/*     */   private static final int PAGES = 4;
/*     */   
/*     */   public String func_71517_b() {
/*  20 */     return "meow";
/*     */   }
/*     */ 
/*     */   
/*     */   public String func_71518_a(ICommandSender sender) {
/*  25 */     return "/meow [page]";
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> func_71514_a() {
/*  30 */     return Arrays.asList(new String[] { "meowtils", "mtils" });
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_71515_b(ICommandSender sender, String[] args) {
/*  35 */     Minecraft mc = Minecraft.func_71410_x();
/*  36 */     int page = 1;
/*     */     
/*  38 */     if (args.length > 0) {
/*     */       try {
/*  40 */         page = Integer.parseInt(args[0]);
/*  41 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */     
/*  44 */     if (page < 1) page = 1; 
/*  45 */     if (page > 4) page = 4;
/*     */     
/*  47 */     ChatComponentText header = new ChatComponentText("");
/*     */     
/*  49 */     if (page > 1) {
/*  50 */       ChatComponentText left = new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "                             «");
/*  51 */       left.func_150256_b().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/meow " + (page - 1)));
/*  52 */       header.func_150257_a((IChatComponent)left);
/*     */     } else {
/*  54 */       header.func_150258_a(EnumChatFormatting.DARK_PURPLE + "                             «");
/*     */     } 
/*     */     
/*  57 */     ChatComponentText label = new ChatComponentText(EnumChatFormatting.DARK_PURPLE + " Meowtils " + page + " ");
/*  58 */     header.func_150257_a((IChatComponent)label);
/*     */     
/*  60 */     if (page < 4) {
/*  61 */       ChatComponentText right = new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "»");
/*  62 */       right.func_150256_b().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/meow " + (page + 1)));
/*  63 */       header.func_150257_a((IChatComponent)right);
/*     */     } else {
/*  65 */       header.func_150258_a(EnumChatFormatting.DARK_PURPLE + "»");
/*     */     } 
/*     */     
/*  68 */     mc.field_71439_g.func_146105_b((IChatComponent)header);
/*  69 */     Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "--------------------------------------------------");
/*     */     
/*  71 */     sendPageContent(page);
/*     */     
/*  73 */     Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "--------------------------------------------------");
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendPageContent(int page) {
/*  78 */     String separator = " » ";
/*  79 */     switch (page) {
/*     */       case 1:
/*  81 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/meow <page>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Meowtils help");
/*  82 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/playcommands" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Show all short play commands");
/*  83 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/shortcuts" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Show all other shortcut commands");
/*  84 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/blacklist | /bl <player>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Add to blacklist");
/*  85 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/unblacklist | /ubl <player>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Remove from blacklist");
/*  86 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/safelist | /sl <player>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Add to safelist");
/*  87 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/unsafelist | /usl <player>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Remove from safelist");
/*     */         break;
/*     */       case 2:
/*  90 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/namemc | /nmc <ign>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Link to NameMC");
/*  91 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/plancke | /pla <ign>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Link to Plancke");
/*  92 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/rq" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Requeues last played Hypixel game");
/*  93 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/capefolder" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Opens cape folder");
/*  94 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/meowcolor" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Show all formatting codes");
/*  95 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/fakemsg <msg>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Display a fake message in chat");
/*  96 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/s <ign>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Show stats for player");
/*     */         break;
/*     */       case 3:
/*  99 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/theme" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Change custom prefix color");
/* 100 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/anticheat" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Change anticheat alert color");
/* 101 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/meowapi <key>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Set API key");
/* 102 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/autogg1 <msg>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Set 1st AutoGG message");
/* 103 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/autogg2 <msg>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Set 2nd AutoGG message");
/* 104 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/autogl <msg>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Set AutoGL message");
/* 105 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/autotext | /at <1-10> <msg|clear>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Set AutoText message");
/*     */         break;
/*     */       case 4:
/* 108 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/meowdebug" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Toggle Meowtils debug mode");
/* 109 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/meowanticheatinfo" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Extra clientside AntiCheat info");
/* 110 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/playerinfo <player>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Shows info about player");
/* 111 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/send <msg|command>" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Send message directly to server");
/* 112 */         Meowtils.addCleanMessage(EnumChatFormatting.GREEN + "/resetgui" + EnumChatFormatting.YELLOW + separator + EnumChatFormatting.DARK_GRAY + "Resets GUI positions");
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_82362_a() {
/* 119 */     return 0;
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/meowtils/MeowtilsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */