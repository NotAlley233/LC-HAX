/*    */ package wtf.tatp.meowtils.modules.meowtils;
/*    */ 
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ 
/*    */ public class Notifications extends Module {
/*    */   private BooleanValue failedQueueMessage;
/*    */   private BooleanValue requeueFeedbackMessage;
/*    */   private BooleanValue playerRemovedMessage;
/*    */   private BooleanValue toggleNotification;
/*    */   private BooleanValue warnBlacklistedPlayer;
/*    */   
/*    */   public Notifications() {
/* 14 */     super("Notifications", null, null, Module.Category.Meowtils, true);
/* 15 */     tooltip("Select which notifications to send in chat.");
/* 16 */     addBoolean(this.toggleNotification = new BooleanValue("Module Toggle", "toggleNotifications"));
/* 17 */     addBoolean(this.warnBlacklistedPlayer = new BooleanValue("Blacklisted Player", "warnBlacklistedPlayer"));
/* 18 */     addBoolean(this.playerRemovedMessage = new BooleanValue("Reveal Banned Player", "playerRemovedMessage"));
/* 19 */     addBoolean(this.requeueFeedbackMessage = new BooleanValue("Requeue Feedback", "requeueSavedCommandMessage"));
/* 20 */     addBoolean(this.failedQueueMessage = new BooleanValue("Failed to Queue", "failedQueueMessage"));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/meowtils/Notifications.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */