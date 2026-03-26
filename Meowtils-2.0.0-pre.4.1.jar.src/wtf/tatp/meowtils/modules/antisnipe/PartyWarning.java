/*    */ package wtf.tatp.meowtils.modules.antisnipe;
/*    */ 
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.DelayedTask;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*    */ import wtf.tatp.meowtils.util.NotifyUtil;
/*    */ import wtf.tatp.meowtils.util.PlaySound;
/*    */ 
/*    */ public class PartyWarning
/*    */   extends Module
/*    */ {
/*    */   private BooleanValue sound;
/*    */   private BooleanValue bedwars2;
/*    */   private BooleanValue bedwars3;
/*    */   private BooleanValue bedwars4;
/* 23 */   private int tickCounter = 0;
/* 24 */   private int messageCounter = 0;
/*    */   private boolean alerted = false;
/*    */   private boolean counting = false;
/*    */   
/*    */   public PartyWarning() {
/* 29 */     super("PartyWarning", "partyWarningKey", "partyWarning", Module.Category.Antisnipe);
/* 30 */     tooltip("Alerts you when a potential party joins your pre-game lobby.");
/* 31 */     addBoolean(this.sound = new BooleanValue("Ping sound", "partyWarningSound"));
/* 32 */     addBoolean(this.bedwars2 = new BooleanValue("Bedwars 2s", "partyWarning_bedwars2"));
/* 33 */     addBoolean(this.bedwars3 = new BooleanValue("Bedwars 3s", "partyWarning_bedwars3"));
/* 34 */     addBoolean(this.bedwars4 = new BooleanValue("Bedwars 4s", "partyWarning_bedwars4"));
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 39 */     if (this.mc.field_71439_g == null || this.mc.field_71441_e == null || event.phase != TickEvent.Phase.END)
/* 40 */       return;  if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*    */       return; 
/* 42 */     if (this.counting) {
/* 43 */       this.tickCounter--;
/* 44 */       if (this.tickCounter <= 0) {
/* 45 */         this.counting = false;
/* 46 */         this.alerted = false;
/* 47 */         this.messageCounter = 0;
/* 48 */         this.tickCounter = 0;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 55 */     String msg = event.message.func_150260_c();
/* 56 */     if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/* 57 */       return;  if (msg.contains(":"))
/* 58 */       return;  if (!msg.contains("has joined"))
/*    */       return; 
/* 60 */     int joinCount = getBedwarsMode();
/*    */     
/* 62 */     if (!this.counting) {
/* 63 */       this.counting = true;
/* 64 */       this.tickCounter = 20;
/* 65 */       this.messageCounter = 1;
/* 66 */       this.alerted = false;
/*    */     } else {
/* 68 */       this.messageCounter++;
/* 69 */       this.tickCounter = 20;
/*    */     } 
/*    */     
/* 72 */     if (this.messageCounter >= joinCount && !this.alerted && joinCount > 0) {
/*    */ 
/*    */       
/* 75 */       new DelayedTask(() -> { Meowtils.addMessage(EnumChatFormatting.RED + "Warning: " + EnumChatFormatting.BLUE + "Suspected party joined! " + EnumChatFormatting.DARK_GRAY + "(" + EnumChatFormatting.DARK_RED + joinCount + EnumChatFormatting.DARK_GRAY + ")"); if (cfg.v.partyWarningSound) PlaySound.getInstance().playPingSoundDeep();  if (cfg.v.notify && cfg.v.notifyPartyWarning) NotifyUtil.notifyParty("Warning: Suspected party joined! (" + joinCount + ")");  }1);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 84 */       this.alerted = true;
/*    */     } 
/*    */   }
/*    */   
/*    */   private int getBedwarsMode() {
/* 89 */     if (GamemodeUtil.bedwars4 && cfg.v.partyWarning_bedwars4) return 4; 
/* 90 */     if (GamemodeUtil.bedwars3 && cfg.v.partyWarning_bedwars3) return 3; 
/* 91 */     if (GamemodeUtil.bedwars2 && cfg.v.partyWarning_bedwars2) return 2; 
/* 92 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/antisnipe/PartyWarning.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */