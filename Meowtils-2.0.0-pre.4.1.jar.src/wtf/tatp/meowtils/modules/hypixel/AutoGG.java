/*    */ package wtf.tatp.meowtils.modules.hypixel;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import wtf.tatp.meowtils.DelayedTask;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*    */ 
/*    */ public class AutoGG
/*    */   extends Module {
/*    */   private NumberValue firstMessageDelay;
/*    */   private NumberValue secondMessageDelay;
/*    */   private BooleanValue secondMessageToggle;
/*    */   private BooleanValue allowInAnyChat;
/*    */   private NumberValue autoGLDelay;
/*    */   private BooleanValue autoGL;
/* 21 */   private static final String[] TRIGGERS = new String[] { "1st Killer -", "1st Place -", "Winner:", "- Damage Dealt -", "Winning Team -", "1st -", "Winners:", "Winning Team:", " won the game!", "Top Seeker:", "1st Place:", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners -", "Sumo Duel -" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AutoGG() {
/* 30 */     super("AutoGG", "autoGGKey", "autoGG", Module.Category.Hypixel);
/* 31 */     tooltip("Automatically sends a message after game end or before game start.\n§5/autogg<1-2> §7| §5/autogl §ffor setting custom messages.");
/* 32 */     addValue(this.firstMessageDelay = new NumberValue("#1 message delay", 0.0D, 1000.0D, 50.0D, "ms", "firstMessageDelay", int.class));
/* 33 */     addValue(this.secondMessageDelay = new NumberValue("#2 message delay", 0.0D, 1000.0D, 50.0D, "ms", "secondMessageDelay", int.class));
/* 34 */     addValue(this.autoGLDelay = new NumberValue("Send GL at", 1.0D, 15.0D, 1.0D, "s", "autoGLDelay", int.class));
/* 35 */     addBoolean(this.secondMessageToggle = new BooleanValue("Send 2nd message", "secondMessageToggle"));
/* 36 */     addBoolean(this.allowInAnyChat = new BooleanValue("Allow in any chat", "allowInAnyChat"));
/* 37 */     addBoolean(this.autoGL = new BooleanValue("Auto GL", "autoGL"));
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onChatReceived(ClientChatReceivedEvent event) {
/* 43 */     String message = event.message.func_150260_c();
/* 44 */     String sendAnyChat = cfg.v.allowInAnyChat ? "" : "/achat ";
/* 45 */     String end = (cfg.v.autoGLDelay == 1) ? " second!" : " seconds!";
/*    */     
/* 47 */     if (message.contains(":"))
/* 48 */       return;  if (Arrays.<String>stream(TRIGGERS).anyMatch(message::contains)) {
/* 49 */       int firstDelay = cfg.v.firstMessageDelay / 50;
/* 50 */       int secondDelay = cfg.v.secondMessageDelay / 50;
/*    */       
/* 52 */       new DelayedTask(() -> Meowtils.sendCleanMessage(sendAnyChat + cfg.v.firstMessage), firstDelay);
/*    */       
/* 54 */       if (cfg.v.secondMessageToggle) {
/* 55 */         new DelayedTask(() -> Meowtils.sendCleanMessage(sendAnyChat + cfg.v.secondMessage), secondDelay);
/*    */       }
/*    */     } 
/*    */     
/* 59 */     if (message.contains("The game starts in " + cfg.v.autoGLDelay + end) && !message.contains(":") && cfg.v.autoGL)
/* 60 */       Meowtils.sendCleanMessage("/ac " + cfg.v.autoGLMessage); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/hypixel/AutoGG.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */