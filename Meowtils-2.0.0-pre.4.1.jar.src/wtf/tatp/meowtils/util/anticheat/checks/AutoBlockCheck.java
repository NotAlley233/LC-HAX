/*    */ package wtf.tatp.meowtils.util.anticheat.checks;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class AutoBlockCheck {
/*  9 */   private int autoBlockTicks = 0;
/*    */   
/*    */   public void anticheatCheck(EntityPlayer player) {
/* 12 */     if (!cfg.v.detectAutoBlock)
/* 13 */       return;  if (player.field_82175_bq && player.func_70632_aY()) {
/* 14 */       this.autoBlockTicks++;
/* 15 */       if (cfg.v.debugMessages && this.autoBlockTicks > 5) {
/* 16 */         Meowtils.addMessage(EnumChatFormatting.YELLOW + "[AntiCheat]: " + EnumChatFormatting.WHITE + player.func_70005_c_() + " AutoBlock ticks: " + this.autoBlockTicks);
/*    */       }
/*    */     } else {
/* 19 */       this.autoBlockTicks = 0;
/*    */     } 
/*    */   }
/*    */   public boolean failedAutoBlock() {
/* 23 */     return (this.autoBlockTicks > 10);
/*    */   }
/*    */   public void reset() {
/* 26 */     this.autoBlockTicks = 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/anticheat/checks/AutoBlockCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */