/*    */ package wtf.tatp.meowtils.commands.config.flagmessagecolor;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.event.ClickEvent;
/*    */ import net.minecraft.event.HoverEvent;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.util.Prefix;
/*    */ 
/*    */ public class CustomFlagMessageCommand extends CommandBase {
/* 19 */   private static final Map<String, String> argToConfigField = new HashMap<>();
/*    */   
/*    */   static {
/* 22 */     argToConfigField.put("Reason", "flagMessageComponentColor");
/* 23 */     argToConfigField.put("WDR", "flagMessageButtonColor");
/* 24 */     argToConfigField.put("Bracket", "flagMessageBracketColor");
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71517_b() {
/* 29 */     return "anticheatcolor";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 34 */     return "";
/*    */   }
/*    */   public List<String> func_71514_a() {
/* 37 */     return Arrays.asList(new String[] { "accolor", "acc", "flagcolor", "anticheat" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) {
/* 42 */     if (args.length > 0) {
/* 43 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /anticheatcolor");
/*    */       return;
/*    */     } 
/* 46 */     openColorSelector("Reason");
/* 47 */     openColorSelector("WDR");
/* 48 */     openColorSelector("Bracket");
/*    */   }
/*    */ 
/*    */   
/*    */   private void openColorSelector(String targetLetter) {
/* 53 */     Minecraft mc = Minecraft.func_71410_x();
/*    */     
/* 55 */     String baseText = targetLetter.toUpperCase() + ": ";
/* 56 */     ChatComponentText base = new ChatComponentText(Prefix.getPrefix() + EnumChatFormatting.WHITE + baseText);
/*    */     
/* 58 */     EnumChatFormatting[] sortedColors = { EnumChatFormatting.BLACK, EnumChatFormatting.DARK_GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.WHITE, EnumChatFormatting.DARK_RED, EnumChatFormatting.RED, EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.DARK_BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.LIGHT_PURPLE };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 77 */     for (EnumChatFormatting color : sortedColors) {
/* 78 */       if (color.func_96302_c()) {
/*    */         
/* 80 */         ChatComponentText colorComponent = new ChatComponentText(color + "⬛");
/* 81 */         colorComponent.func_150256_b().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/setflagmessagecolor " + targetLetter + " " + color
/* 82 */               .name()));
/*    */         
/* 84 */         colorComponent.func_150256_b().func_150209_a(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (IChatComponent)new ChatComponentText("Click to set " + targetLetter
/* 85 */                 .toUpperCase() + " to " + color + color.name())));
/*    */ 
/*    */         
/* 88 */         base.func_150257_a((IChatComponent)colorComponent);
/*    */       } 
/*    */     } 
/* 91 */     mc.field_71439_g.func_145747_a((IChatComponent)base);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 96 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/config/flagmessagecolor/CustomFlagMessageCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */