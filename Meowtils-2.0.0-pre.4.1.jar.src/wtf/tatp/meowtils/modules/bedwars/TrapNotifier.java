/*    */ package wtf.tatp.meowtils.modules.bedwars;
/*    */ 
/*    */ import net.minecraft.potion.Potion;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*    */ import wtf.tatp.meowtils.util.PlaySound;
/*    */ 
/*    */ public class TrapNotifier
/*    */   extends Module
/*    */ {
/*    */   private BooleanValue sound;
/*    */   private boolean notified = false;
/* 20 */   private int tickCounter = 0;
/* 21 */   private String trap = "";
/*    */   private boolean revealTrap = false;
/*    */   
/*    */   public TrapNotifier() {
/* 25 */     super("TrapNotifier", "trapNotifierKey", "trapNotifier", Module.Category.Bedwars);
/* 26 */     tooltip("Notifies you when trigger a trap.");
/* 27 */     addBoolean(this.sound = new BooleanValue("Ping sound", "trapNotifierSound"));
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 33 */     String msg = event.message.func_150260_c();
/* 34 */     if (msg.equals("Your invisibility was removed by an Reveal Trap!")) {
/* 35 */       this.revealTrap = true;
/*    */     }
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 41 */     if (this.mc.field_71439_g == null || this.mc.field_71441_e == null || event.phase != TickEvent.Phase.END)
/* 42 */       return;  if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby) {
/*    */       return;
/*    */     }
/* 45 */     this.tickCounter++;
/* 46 */     if (this.tickCounter != 20)
/* 47 */       return;  this.tickCounter = 0;
/*    */     
/* 49 */     boolean currentTrap = (this.mc.field_71439_g.func_70644_a(Potion.field_76419_f) || this.mc.field_71439_g.func_70644_a(Potion.field_76440_q) || this.revealTrap);
/*    */     
/* 51 */     if (!this.notified) {
/* 52 */       if (this.mc.field_71439_g.func_70644_a(Potion.field_76419_f)) {
/* 53 */         this.trap = "Miner Fatigue";
/* 54 */         alert();
/* 55 */       } else if (this.mc.field_71439_g.func_70644_a(Potion.field_76440_q)) {
/* 56 */         this.trap = "Blindness";
/* 57 */         alert();
/* 58 */       } else if (this.revealTrap) {
/* 59 */         this.trap = "Reveal";
/* 60 */         this.revealTrap = false;
/* 61 */         alert();
/*    */       } 
/*    */     }
/*    */     
/* 65 */     if (!currentTrap) {
/* 66 */       this.notified = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private void alert() {
/* 72 */     Meowtils.addMessage(EnumChatFormatting.RED.toString() + EnumChatFormatting.BOLD + "TRAP TRIGGERED! " + EnumChatFormatting.DARK_GRAY + "(" + EnumChatFormatting.GOLD + this.trap + EnumChatFormatting.DARK_GRAY + ")");
/* 73 */     this.notified = true;
/*    */     
/* 75 */     if (cfg.v.trapNotifierSound)
/* 76 */       PlaySound.getInstance().playAnvilBreakSound(); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/bedwars/TrapNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */