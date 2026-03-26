/*    */ package wtf.tatp.meowtils.modules.hypixel;
/*    */ 
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ 
/*    */ public class Notify
/*    */   extends Module {
/*    */   private BooleanValue antiCheat;
/*    */   private BooleanValue denicker;
/*    */   private BooleanValue armorAlerts;
/*    */   private BooleanValue bedTracker;
/*    */   private BooleanValue consumeAlerts;
/*    */   private BooleanValue itemAlerts;
/*    */   private BooleanValue upgradeAlerts;
/*    */   private BooleanValue notifyParty;
/*    */   
/*    */   public Notify() {
/* 19 */     super("Notify", "notifyKey", "notify", Module.Category.Hypixel);
/* 20 */     tooltip("Sends a notification to chat from selected modules.\n" + EnumChatFormatting.RED + "BETA");
/* 21 */     addBoolean(this.antiCheat = new BooleanValue("AntiCheat notify", "notifyAntiCheat"));
/* 22 */     addBoolean(this.denicker = new BooleanValue("Denicker notify", "notifyDenicker"));
/* 23 */     addBoolean(this.armorAlerts = new BooleanValue("ArmorAlerts notify", "notifyArmorAlerts"));
/* 24 */     addBoolean(this.bedTracker = new BooleanValue("BedTracker notify", "notifyBedTracker"));
/* 25 */     addBoolean(this.consumeAlerts = new BooleanValue("ConsumeAlerts notify", "notifyConsumeAlerts"));
/* 26 */     addBoolean(this.itemAlerts = new BooleanValue("ItemAlerts notify", "notifyItemAlerts"));
/* 27 */     addBoolean(this.upgradeAlerts = new BooleanValue("UpgradeAlerts notify", "notifyUpgradeAlerts"));
/* 28 */     addBoolean(this.notifyParty = new BooleanValue("PartyWarning alert", "notifyPartyWarning"));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/hypixel/Notify.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */