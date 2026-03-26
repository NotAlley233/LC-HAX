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
/*    */ public class DoublesNormalSkywarsCommand
/*    */   extends CommandBase {
/*    */   public DoublesNormalSkywarsCommand() {
/* 14 */     this.Mode = EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC + "team skywars" + EnumChatFormatting.RESET;
/* 15 */     MinecraftForge.EVENT_BUS.register(this);
/*    */   }
/*    */   String Mode;
/*    */   public String func_71517_b() {
/* 19 */     return "sw2";
/*    */   }
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 23 */     return "";
/*    */   }
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 27 */     (Minecraft.func_71410_x()).field_71439_g.func_71165_d("/play teams_normal");
/* 28 */     Meowtils.addMessage(EnumChatFormatting.GREEN + "Sending you to a " + this.Mode + EnumChatFormatting.GREEN + " game.");
/*    */   }
/*    */   
/*    */   public int func_82362_a() {
/* 32 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/playcommands/playskywars/DoublesNormalSkywarsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */