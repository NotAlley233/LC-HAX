/*    */ package wtf.tatp.meowtils.commands.playcommands.playbedwars;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ 
/*    */ public class FourTwosBedwarsCommand extends CommandBase {
/*    */   String Mode;
/*    */   
/*    */   public FourTwosBedwarsCommand() {
/* 17 */     this.Mode = EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC + "4v4 bedwars" + EnumChatFormatting.RESET;
/* 18 */     MinecraftForge.EVENT_BUS.register(this);
/*    */   }
/*    */   
/*    */   public String func_71517_b() {
/* 22 */     return "4v4";
/*    */   }
/*    */   public List<String> func_71514_a() {
/* 25 */     return Arrays.asList(new String[] { "bw4v4" });
/*    */   }
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 29 */     return "";
/*    */   }
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 33 */     (Minecraft.func_71410_x()).field_71439_g.func_71165_d("/play bedwars_two_four");
/* 34 */     Meowtils.addMessage(EnumChatFormatting.GREEN + "Sending you to a " + this.Mode + EnumChatFormatting.GREEN + " game.");
/*    */   }
/*    */   
/*    */   public int func_82362_a() {
/* 38 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/playcommands/playbedwars/FourTwosBedwarsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */