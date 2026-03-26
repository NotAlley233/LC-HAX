/*    */ package wtf.tatp.meowtils.util;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.scoreboard.Score;
/*    */ import net.minecraft.scoreboard.ScoreObjective;
/*    */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*    */ import net.minecraft.scoreboard.Scoreboard;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ public class GamemodeUtil {
/* 12 */   Minecraft mc = Minecraft.func_71410_x();
/*    */   
/*    */   public static boolean skywarsGame = false;
/*    */   
/*    */   public static boolean skywarsMiniGame = false;
/*    */   public static boolean bedwarsGame = false;
/*    */   public static boolean replay = false;
/*    */   public static boolean bedwarsLobby = false;
/*    */   public static boolean skywarsLobby = false;
/*    */   public static boolean duelsLobby = false;
/*    */   public static boolean limbo = false;
/*    */   public static boolean hypixel = false;
/*    */   public static boolean megaWalls = false;
/*    */   public static boolean bedwars2 = false;
/*    */   public static boolean bedwars3 = false;
/*    */   public static boolean bedwars4 = false;
/* 28 */   public static String scoreboardTitle = "";
/* 29 */   public static List<String> scoreboardLines = new ArrayList<>();
/* 30 */   private int tickCounter = 0;
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 34 */     if (event.phase != TickEvent.Phase.START)
/* 35 */       return;  if (!Meowtils.nullCheck())
/* 36 */       return;  boolean onJoin = (this.mc.field_71439_g.field_70173_aa <= 100);
/* 37 */     boolean shouldUpdate = (onJoin || this.tickCounter++ % 20 == 0);
/* 38 */     if (this.mc.field_71441_e == null || this.mc.field_71439_g == null) {
/* 39 */       skywarsGame = false;
/* 40 */       skywarsMiniGame = false;
/* 41 */       bedwarsGame = false;
/* 42 */       replay = false;
/* 43 */       bedwarsLobby = false;
/* 44 */       skywarsLobby = false;
/* 45 */       duelsLobby = false;
/* 46 */       limbo = false;
/* 47 */       hypixel = false;
/* 48 */       megaWalls = false;
/* 49 */       bedwars2 = false;
/* 50 */       bedwars3 = false;
/* 51 */       bedwars4 = false;
/*    */       
/* 53 */       scoreboardTitle = "";
/* 54 */       scoreboardLines.clear();
/*    */       
/*    */       return;
/*    */     } 
/* 58 */     Scoreboard scoreboard = this.mc.field_71441_e.func_96441_U();
/* 59 */     ScoreObjective sidebar = scoreboard.func_96539_a(1);
/* 60 */     if (sidebar == null)
/*    */       return; 
/* 62 */     scoreboardTitle = sidebar.func_96678_d().replaceAll("§.", "").toLowerCase();
/* 63 */     scoreboardLines = getSidebarLines(scoreboard, sidebar);
/*    */     
/* 65 */     skywarsGame = (scoreboardTitle.contains("skywars") && scoreboardLines.stream().anyMatch(line -> (line.contains("mode: normal") || line.contains("mode: insane"))));
/* 66 */     skywarsMiniGame = (scoreboardTitle.contains("skywars") && scoreboardLines.stream().anyMatch(line -> line.contains("mode: mini")));
/* 67 */     replay = (scoreboardTitle.contains("replay") || scoreboardTitle.contains("atlas"));
/* 68 */     bedwarsLobby = (scoreboardTitle.contains("bed wars") && scoreboardLines.stream().anyMatch(line -> line.contains("level:")));
/* 69 */     skywarsLobby = (scoreboardTitle.contains("skywars") && scoreboardLines.stream().anyMatch(line -> line.contains("your level:")));
/* 70 */     duelsLobby = (scoreboardTitle.contains("duels") && scoreboardLines.stream().anyMatch(line -> line.contains("tokens:")));
/* 71 */     bedwarsGame = scoreboardTitle.contains("bed wars");
/* 72 */     hypixel = scoreboardLines.stream().anyMatch(line -> line.contains("hypixel"));
/* 73 */     megaWalls = scoreboardTitle.contains("mega walls");
/*    */     
/* 75 */     bedwars2 = scoreboardLines.stream().anyMatch(line -> line.contains("mode: doubles"));
/* 76 */     bedwars3 = scoreboardLines.stream().anyMatch(line -> line.contains("mode: 3v3v3v3"));
/* 77 */     bedwars4 = scoreboardLines.stream().anyMatch(line -> line.contains("mode: 4v4v4v4"));
/*    */   }
/*    */ 
/*    */   
/*    */   private List<String> getSidebarLines(Scoreboard scoreboard, ScoreObjective objective) {
/* 82 */     List<Score> scores = new ArrayList<>(scoreboard.func_96528_e());
/*    */     
/* 84 */     scores.sort(Comparator.comparingInt(Score::func_96652_c));
/*    */     
/* 86 */     List<String> lines = new ArrayList<>();
/*    */     
/* 88 */     for (Score score : scores) {
/* 89 */       if (score.func_96645_d() != objective)
/*    */         continue; 
/* 91 */       String name = score.func_96653_e();
/* 92 */       ScorePlayerTeam team = scoreboard.func_96509_i(name);
/* 93 */       String line = ScorePlayerTeam.func_96667_a((Team)team, name);
/*    */       
/* 95 */       line = line.replaceAll("§.", "").toLowerCase();
/* 96 */       lines.add(line);
/*    */     } 
/*    */     
/* 99 */     return lines;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/GamemodeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */