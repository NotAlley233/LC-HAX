/*    */ package wtf.tatp.meowtils.util.anticheat.checks;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.potion.Potion;
/*    */ import net.minecraft.potion.PotionEffect;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class NoSlowCheck {
/*    */   private double lastPosX;
/* 11 */   private int noSlowTicks = 0; private double lastPosZ;
/*    */   
/*    */   public void anticheatCheck(EntityPlayer player) {
/* 14 */     if (!cfg.v.detectNoSlow)
/*    */       return; 
/* 16 */     double deltaX = player.field_70165_t - this.lastPosX;
/* 17 */     double deltaZ = player.field_70161_v - this.lastPosZ;
/* 18 */     double speed = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
/*    */     
/* 20 */     if (player.func_70051_ag() && player.func_71039_bw() && !player.func_70115_ae()) {
/* 21 */       double baseThreshold = 0.05D;
/*    */       
/* 23 */       PotionEffect speedEffect = player.func_70660_b(Potion.field_76424_c);
/* 24 */       if (speedEffect != null) {
/* 25 */         int amplifier = speedEffect.func_76458_c();
/* 26 */         baseThreshold *= 1.0D + 0.2D * (amplifier + 1);
/*    */       } 
/*    */       
/* 29 */       if (speed > baseThreshold) {
/* 30 */         this.noSlowTicks++;
/* 31 */         if (cfg.v.debugMessages && this.noSlowTicks > 5) {
/* 32 */           Meowtils.addMessage(EnumChatFormatting.YELLOW + "[AntiCheat]: " + EnumChatFormatting.WHITE + player.func_70005_c_() + " NoSlow ticks: " + this.noSlowTicks + " | Speed: " + speed + " | Threshold: " + baseThreshold);
/*    */         }
/*    */       } else {
/* 35 */         this.noSlowTicks = 0;
/*    */       } 
/*    */     } else {
/* 38 */       this.noSlowTicks = 0;
/*    */     } 
/*    */   }
/*    */   public boolean failedNoSlow() {
/* 42 */     return (this.noSlowTicks > 20);
/*    */   }
/*    */   public void reset() {
/* 45 */     this.noSlowTicks = 0;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/anticheat/checks/NoSlowCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */