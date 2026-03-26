/*    */ package wtf.tatp.meowtils.commands.config.customthemecolor;
/*    */ 
/*    */ import java.util.HashMap;
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
/*    */ public class CustomThemeCommand
/*    */   extends CommandBase {
/* 18 */   private static final Map<String, String> argToConfigField = new HashMap<>();
/*    */   
/*    */   static {
/* 21 */     argToConfigField.put("m", "customThemeM");
/* 22 */     argToConfigField.put("e", "customThemeE");
/* 23 */     argToConfigField.put("o", "customThemeO");
/* 24 */     argToConfigField.put("w", "customThemeW");
/* 25 */     argToConfigField.put("[", "customThemeFirstBracket");
/* 26 */     argToConfigField.put("]", "customThemeSecondBracket");
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71517_b() {
/* 31 */     return "theme";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 36 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) {
/* 41 */     if (args.length > 0) {
/* 42 */       Meowtils.addMessage(EnumChatFormatting.RED + "Usage: /theme");
/*    */       
/*    */       return;
/*    */     } 
/* 46 */     openColorSelector("m");
/* 47 */     openColorSelector("e");
/* 48 */     openColorSelector("o");
/* 49 */     openColorSelector("w");
/* 50 */     Meowtils.addMessage("Bracket color:");
/* 51 */     openColorSelector("[");
/* 52 */     openColorSelector("]");
/*    */   }
/*    */   
/*    */   private void openColorSelector(String targetLetter) {
/* 56 */     Minecraft mc = Minecraft.func_71410_x();
/*    */     
/* 58 */     String baseText = targetLetter.toUpperCase() + ": ";
/* 59 */     ChatComponentText base = new ChatComponentText(Prefix.getPrefix() + EnumChatFormatting.WHITE + baseText);
/*    */     
/* 61 */     EnumChatFormatting[] sortedColors = { EnumChatFormatting.BLACK, EnumChatFormatting.DARK_GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.WHITE, EnumChatFormatting.DARK_RED, EnumChatFormatting.RED, EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.DARK_BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.LIGHT_PURPLE };
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
/* 80 */     for (EnumChatFormatting color : sortedColors) {
/* 81 */       if (color.func_96302_c()) {
/*    */         
/* 83 */         ChatComponentText colorComponent = new ChatComponentText(color + "⬛");
/* 84 */         colorComponent.func_150256_b().func_150241_a(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/settheme " + targetLetter + " " + color.name()));
/* 85 */         colorComponent.func_150256_b().func_150209_a(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (IChatComponent)new ChatComponentText("Click to set " + targetLetter.toUpperCase() + " to " + color + color.name())));
/*    */         
/* 87 */         base.func_150257_a((IChatComponent)colorComponent);
/*    */       } 
/*    */     } 
/* 90 */     mc.field_71439_g.func_145747_a((IChatComponent)base);
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 95 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/config/customthemecolor/CustomThemeCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */