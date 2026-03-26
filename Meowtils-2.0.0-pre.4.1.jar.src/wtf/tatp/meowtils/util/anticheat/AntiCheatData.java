/*    */ package wtf.tatp.meowtils.util.anticheat;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import wtf.tatp.meowtils.util.anticheat.checks.KillauraBCheck;
/*    */ import wtf.tatp.meowtils.util.anticheat.checks.LegitScaffoldCheck;
/*    */ import wtf.tatp.meowtils.util.anticheat.checks.NoSlowCheck;
/*    */ 
/*    */ public class AntiCheatData {
/*  8 */   public PlayerData playerData = new PlayerData();
/*    */   
/* 10 */   public AutoBlockCheck autoBlockCheck = new AutoBlockCheck();
/* 11 */   public NoSlowCheck noSlowCheck = new NoSlowCheck();
/* 12 */   public LegitScaffoldCheck legitScaffoldCheck = new LegitScaffoldCheck();
/* 13 */   public KillauraBCheck killauraBCheck = new KillauraBCheck();
/*    */   
/*    */   public void anticheatCheck(EntityPlayer player) {
/* 16 */     this.playerData.update(player);
/*    */     
/* 18 */     this.autoBlockCheck.anticheatCheck(player);
/* 19 */     this.noSlowCheck.anticheatCheck(player);
/* 20 */     this.legitScaffoldCheck.anticheatCheck(player);
/* 21 */     this.killauraBCheck.anticheatCheck(player);
/*    */   }
/*    */   
/*    */   public boolean failedAutoBlock() {
/* 25 */     return this.autoBlockCheck.failedAutoBlock();
/*    */   }
/*    */   public boolean failedNoSlow() {
/* 28 */     return this.noSlowCheck.failedNoSlow();
/*    */   }
/*    */   public boolean failedLegitScaffold() {
/* 31 */     return this.legitScaffoldCheck.failedLegitScaffold();
/*    */   } public boolean failedKillauraB() {
/* 33 */     return this.killauraBCheck.failedKillauraB();
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/anticheat/AntiCheatData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */