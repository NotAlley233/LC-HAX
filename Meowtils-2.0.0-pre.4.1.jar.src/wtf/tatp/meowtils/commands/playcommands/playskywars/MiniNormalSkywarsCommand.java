/*    */ package wtf.tatp.meowtils.commands.playcommands.playskywars;
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
/*    */ public class MiniNormalSkywarsCommand
/*    */   extends CommandBase {
/*    */   private String Mode;
/*    */   
/*    */   public MiniNormalSkywarsCommand() {
/* 18 */     this.Mode = EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC + "mini skywars" + EnumChatFormatting.RESET;
/* 19 */     MinecraftForge.EVENT_BUS.register(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71517_b() {
/* 24 */     return "sm";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 29 */     return "";
/*    */   }
/*    */   public List<String> func_71514_a() {
/* 32 */     return Arrays.asList(new String[] { "mini" });
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 37 */     (Minecraft.func_71410_x()).field_71439_g.func_71165_d("/play mini_normal");
/* 38 */     Meowtils.addMessage(EnumChatFormatting.GREEN + "Sending you to a " + this.Mode + EnumChatFormatting.GREEN + " game.");
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 43 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/playcommands/playskywars/MiniNormalSkywarsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */