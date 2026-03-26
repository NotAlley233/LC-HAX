/*    */ package wtf.tatp.meowtils.modules.utility;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.handlers.KeybindHandler;
/*    */ 
/*    */ public class AutoText
/*    */   extends Module {
/*    */   public AutoText() {
/* 14 */     super("AutoText", "autoTextKey", "autoText", Module.Category.Utility);
/* 15 */     tooltip("Automatically send a message on key press.\nSet message: §5/at<1-9> §5<msg> §7or §5/autotext<1-9> §5<msg>\nSet keybind: Options -> Controls");
/*    */   }
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 19 */     Minecraft mc = Minecraft.func_71410_x();
/* 20 */     if (event.phase != TickEvent.Phase.END || mc.field_71439_g == null || mc.field_71441_e == null)
/* 21 */       return;  for (int i = 0; i < 10; i++) {
/* 22 */       if (KeybindHandler.autotextKeybinds[i].func_151468_f()) {
/* 23 */         String msg = getMessageForIndex(i);
/* 24 */         if (msg != null && !msg.isEmpty()) {
/* 25 */           Meowtils.sendCleanMessage(msg);
/*    */         } else {
/* 27 */           Meowtils.addMessage("No message set!");
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   private String getMessageForIndex(int index) {
/* 33 */     switch (index) {
/*    */       case 0:
/* 35 */         return cfg.v.autoText0;
/*    */       case 1:
/* 37 */         return cfg.v.autoText1;
/*    */       case 2:
/* 39 */         return cfg.v.autoText2;
/*    */       case 3:
/* 41 */         return cfg.v.autoText3;
/*    */       case 4:
/* 43 */         return cfg.v.autoText4;
/*    */       case 5:
/* 45 */         return cfg.v.autoText5;
/*    */       case 6:
/* 47 */         return cfg.v.autoText6;
/*    */       case 7:
/* 49 */         return cfg.v.autoText7;
/*    */       case 8:
/* 51 */         return cfg.v.autoText8;
/*    */       case 9:
/* 53 */         return cfg.v.autoText9;
/*    */     } 
/* 55 */     return "";
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/utility/AutoText.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */