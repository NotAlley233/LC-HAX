/*    */ package wtf.tatp.meowtils.handlers;
/*    */ import net.minecraftforge.event.world.WorldEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import wtf.tatp.meowtils.modules.advanced.AntiBot;
/*    */ import wtf.tatp.meowtils.modules.antisnipe.SniperWarning;
/*    */ import wtf.tatp.meowtils.modules.bedwars.BedwarsCounter;
/*    */ import wtf.tatp.meowtils.modules.bedwars.ItemAlerts;
/*    */ import wtf.tatp.meowtils.modules.bedwars.UpgradesHUD;
/*    */ import wtf.tatp.meowtils.modules.skywars.CooldownHUD;
/*    */ import wtf.tatp.meowtils.modules.skywars.EquipAlerts;
/*    */ import wtf.tatp.meowtils.modules.skywars.SkywarsCounter;
/*    */ import wtf.tatp.meowtils.modules.skywars.StrengthESP;
/*    */ import wtf.tatp.meowtils.util.DeathTracker;
/*    */ import wtf.tatp.meowtils.util.anticheat.checks.LegitScaffoldCheck;
/*    */ 
/*    */ public class MapClearer {
/*    */   @SubscribeEvent
/*    */   public void onWorldLoad(WorldEvent.Load event) {
/* 19 */     if (event.world.field_72995_K) {
/* 20 */       DeathTracker.clear();
/* 21 */       AntiBot.clear();
/* 22 */       AntiCheat.clear();
/* 23 */       ArmorAlerts.clear();
/* 24 */       ResourceTracker.clear();
/* 25 */       SniperWarning.clear();
/* 26 */       StrengthESP.clear();
/* 27 */       UpgradeAlerts.clear();
/* 28 */       ConsumeAlerts.clear();
/* 29 */       Denicker.clear();
/* 30 */       BlacklistHandler.clear();
/* 31 */       PlayerRemovedHandler.clear();
/* 32 */       LegitScaffoldCheck.clear();
/* 33 */       SkywarsAlerts.clear();
/* 34 */       CooldownHUD.clear();
/* 35 */       EquipAlerts.clear();
/* 36 */       ItemAlerts.clear();
/* 37 */       BedTracker.clear();
/* 38 */       UpgradesHUD.clear();
/* 39 */       BedwarsCounter.clear();
/* 40 */       SkywarsCounter.clear();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/handlers/MapClearer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */