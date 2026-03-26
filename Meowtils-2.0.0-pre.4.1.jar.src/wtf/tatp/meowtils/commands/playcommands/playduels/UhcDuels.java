/*    */ package wtf.tatp.meowtils.commands.playcommands.playduels;
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
/*    */ public class UhcDuels extends CommandBase {
/*    */   String Mode;
/*    */   
/*    */   public UhcDuels() {
/* 17 */     this.Mode = EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.ITALIC + "uhc duels" + EnumChatFormatting.RESET;
/* 18 */     MinecraftForge.EVENT_BUS.register(this);
/*    */   }
/*    */   
/*    */   public String func_71517_b() {
/* 22 */     return "uhcduel";
/*    */   } public List<String> func_71514_a() {
/* 24 */     return Arrays.asList(new String[] { "uhc" });
/*    */   }
/*    */   public String func_71518_a(ICommandSender sender) {
/* 27 */     return "";
/*    */   }
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 31 */     (Minecraft.func_71410_x()).field_71439_g.func_71165_d("/play duels_uhc_duel");
/* 32 */     Meowtils.addMessage(EnumChatFormatting.GREEN + "Sending you to a " + this.Mode + EnumChatFormatting.GREEN + " game.");
/*    */   }
/*    */   
/*    */   public int func_82362_a() {
/* 36 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/playcommands/playduels/UhcDuels.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */