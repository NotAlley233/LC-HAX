/*    */ package wtf.tatp.meowtils.modules.meowtils;
/*    */ 
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ 
/*    */ public class Teams
/*    */   extends Module {
/*    */   private BooleanValue ignoreTeam;
/*    */   private BooleanValue ignoreSpectator;
/*    */   
/*    */   public Teams() {
/* 12 */     super("Teams", null, null, Module.Category.Meowtils, true);
/* 13 */     tooltip("Makes certain modules ignore teammates.\n-Disable while spectator: Disables certain modules while you are a spectator");
/* 14 */     addBoolean(this.ignoreTeam = new BooleanValue("Ignore team", "ignoreTeam"));
/* 15 */     addBoolean(this.ignoreSpectator = new BooleanValue("Disable while spectator", "ignoreTeamSpectator"));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/meowtils/Teams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */