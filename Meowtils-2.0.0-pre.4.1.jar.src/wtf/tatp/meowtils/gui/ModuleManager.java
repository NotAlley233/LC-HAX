/*    */ package wtf.tatp.meowtils.gui;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import wtf.tatp.meowtils.modules.advanced.AntiBot;
/*    */ import wtf.tatp.meowtils.modules.advanced.DelayRemover;
/*    */ import wtf.tatp.meowtils.modules.antisnipe.SniperWarning;
/*    */ import wtf.tatp.meowtils.modules.bedwars.BedwarsCounter;
/*    */ import wtf.tatp.meowtils.modules.hypixel.AutoGG;
/*    */ import wtf.tatp.meowtils.modules.render.BlockCount;
/*    */ import wtf.tatp.meowtils.modules.render.ConsumeTimer;
/*    */ import wtf.tatp.meowtils.modules.skywars.ItemHighlight;
/*    */ import wtf.tatp.meowtils.modules.skywars.NoArmorDye;
/*    */ import wtf.tatp.meowtils.modules.utility.AutoSwap;
/*    */ import wtf.tatp.meowtils.modules.utility.HotbarLock;
/*    */ import wtf.tatp.meowtils.modules.utility.NullMove;
/*    */ import wtf.tatp.meowtils.modules.utility.Sprint;
/*    */ 
/*    */ public class ModuleManager {
/* 19 */   private static final List<Module> modules = new ArrayList<>();
/*    */   
/*    */   static {
/* 22 */     register(new Module[] { (Module)new GUI(), (Module)new AntiBot(), (Module)new AntiCheat(), (Module)new AntiObfuscate(), (Module)new ArmorAlerts(), (Module)new AutoChannel(), (Module)new AutoGG(), (Module)new AutoReport(), (Module)new AutoSafelist(), (Module)new AutoTip(), (Module)new AutoWho(), (Module)new BreakProgress(), (Module)new Cape(), (Module)new ChatCleaner(), (Module)new ConsumeTimer(), (Module)new Denicker(), (Module)new FakeAutoBlock(), (Module)new HealthInfo(), (Module)new InventoryMove(), (Module)new ItemAlerts(), (Module)new ItemHighlight(), (Module)new LatencyAlerts(), (Module)new Notifications(), (Module)new NoTitles(), (Module)new NullMove(), (Module)new ConsumeAlerts(), (Module)new ResourceTracker(), (Module)new ShinyPots(), (Module)new SniperWarning(), (Module)new Sprint(), (Module)new Stats(), (Module)new StrengthESP(), (Module)new AutoClaim(), (Module)new UpgradeAlerts(), (Module)new UpgradesHUD(), (Module)new ViewClip(), (Module)new InventoryFill(), (Module)new ViewPackets(), (Module)new SkywarsAlerts(), (Module)new MiningAlerts(), (Module)new CooldownHUD(), (Module)new AutoText(), (Module)new AutoChest(), (Module)new NoArmorDye(), (Module)new EquipAlerts(), (Module)new ChestESP(), (Module)new Settings(), (Module)new PotionHUD(), (Module)new HotbarLock(), (Module)new TimeWarpDisplay(), (Module)new AutoSwap(), (Module)new BlockCount(), (Module)new BedTracker(), (Module)new InstantHurt(), (Module)new DelayRemover(), (Module)new AutoStairs(), (Module)new AntiInvis(), (Module)new TrapNotifier(), (Module)new ActionSounds(), (Module)new Teams(), (Module)new BedwarsCounter(), (Module)new Notify(), (Module)new SkywarsCounter(), (Module)new PartyWarning() });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 90 */     modules.sort(Comparator.comparing(Module::getName, String.CASE_INSENSITIVE_ORDER));
/*    */   }
/*    */   
/*    */   private static void register(Module... mods) {
/* 94 */     Collections.addAll(modules, mods);
/*    */   }
/*    */   
/*    */   public static List<Module> getModules() {
/* 98 */     return modules;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/ModuleManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */