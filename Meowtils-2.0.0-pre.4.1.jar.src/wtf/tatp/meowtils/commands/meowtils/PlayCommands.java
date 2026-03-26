/*    */ package wtf.tatp.meowtils.commands.meowtils;
/*    */ import net.minecraft.command.CommandBase;
/*    */ import net.minecraft.command.CommandException;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ 
/*    */ public class PlayCommands extends CommandBase {
/*    */   public String func_71517_b() {
/* 10 */     return "playcommands";
/*    */   }
/*    */   
/*    */   public String func_71518_a(ICommandSender sender) {
/* 14 */     return "";
/*    */   }
/*    */   
/*    */   public void func_71515_b(ICommandSender sender, String[] args) throws CommandException {
/* 18 */     String separator = " » ";
/* 19 */     Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
/* 20 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/1s or /bw1" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play bedwars_eight_one");
/* 21 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/2s or /bw2" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play bedwars_eight_two");
/* 22 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/3s or /bw3" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play bedwars_four_three");
/* 23 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/4s or /bw4" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play bedwars_four_four");
/* 24 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + "/4v4 or /bw4v4" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play bedwars_four_two");
/* 25 */     Meowtils.addCleanMessage("");
/* 26 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /sw" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play solo_normal");
/* 27 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /sw2" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play teams_normal");
/* 28 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /si" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play solo_insane");
/* 29 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /sm" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play mini_normal");
/* 30 */     Meowtils.addCleanMessage("");
/* 31 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /mw" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play mw_standard");
/* 32 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /mwfaceoff" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play mw_face_off");
/* 33 */     Meowtils.addCleanMessage("");
/* 34 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /blitz" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play blitz_solo_normal");
/* 35 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /blitz2" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play blitz_teams_normal");
/* 36 */     Meowtils.addCleanMessage("");
/* 37 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /tntrun" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play tnt_tntrun");
/* 38 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /tnttag" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play tnt_tntag");
/* 39 */     Meowtils.addCleanMessage("");
/* 40 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /classic" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_classic_duel");
/* 41 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /swduel" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_sw_duel");
/* 42 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /uhcduel" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_uhc_duel");
/* 43 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /sumo" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_sumo_duel");
/* 44 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /bridge1" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_bridge_duel");
/* 45 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /bridge2" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_bridge_doubles");
/* 46 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /bridge3" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_bridge_threes");
/* 47 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /bridge4" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_bridge_four");
/* 48 */     Meowtils.addCleanMessage(EnumChatFormatting.YELLOW + " /blitz" + EnumChatFormatting.DARK_GRAY + separator + EnumChatFormatting.GRAY + "/play duels_blitz_duel");
/*    */     
/* 50 */     Meowtils.addCleanMessage(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.STRIKETHROUGH + "---------------------------------------------------");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int func_82362_a() {
/* 56 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/commands/meowtils/PlayCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */