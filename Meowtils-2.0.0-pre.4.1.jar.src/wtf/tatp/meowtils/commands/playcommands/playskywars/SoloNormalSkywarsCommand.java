/*    */ package wtf.tatp.meowtils.commands.playcommands.playskywars;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ 
/*    */ public class SoloNormalSkywarsCommand extends CommandBase {
/*    */   private String Mode;
/*    */   
/*    */   public SoloNormalSkywarsCommand() {
/* 15 */     this.Mode = EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC + "solo skywars" + EnumChatFormatting.RESET;
/* 16 */     MinecraftForge.EVENT_BUS.register(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71517_b() {
/* 21 */     return "sw";
/*    */   }
/*    */ 
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 26 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 31 */     (Minecraft.func_71410_x()).field_71439_g.func_71165_d("/play solo_normal");
/* 32 */     Meowtils.addMessage(EnumChatFormatting.GREEN + "Sending you to a " + this.Mode + EnumChatFormatting.GREEN + " game.");
/*    */   }
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 37 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/playcommands/playskywars/SoloNormalSkywarsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */