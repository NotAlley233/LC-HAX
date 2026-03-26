/*    */ package wtf.tatp.meowtils.util.anticheat;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class PlayerData {
/*    */   public double speed;
/*    */   public int aboveVoidTicks;
/*    */   public int fastTick;
/*    */   public int autoBlockTicks;
/*    */   public int ticksExisted;
/*    */   public int lastSneakTick;
/*    */   public double posZ;
/*    */   public int sneakTicks;
/*    */   public int noSlowTicks;
/*    */   public double posY;
/*    */   public boolean sneaking;
/*    */   public double posX;
/*    */   private int resetTick;
/*    */   
/*    */   public void update(EntityPlayer entityPlayer) {
/* 21 */     int ticksExisted = entityPlayer.field_70173_aa;
/* 22 */     this.posX = entityPlayer.field_70165_t - entityPlayer.field_70142_S;
/* 23 */     this.posY = entityPlayer.field_70163_u - entityPlayer.field_70137_T;
/* 24 */     this.posZ = entityPlayer.field_70161_v - entityPlayer.field_70136_U;
/* 25 */     this.speed = Math.max(Math.abs(this.posX), Math.abs(this.posZ));
/* 26 */     if (ticksExisted - this.resetTick >= 20) {
/* 27 */       this.fastTick = 0;
/* 28 */       this.resetTick = ticksExisted;
/*    */     } 
/*    */     
/* 31 */     if (this.speed >= 0.3D) {
/* 32 */       this.fastTick++;
/* 33 */       this.ticksExisted = ticksExisted;
/*    */     } else {
/* 35 */       this.fastTick = 0;
/*    */     } 
/* 37 */     if (Math.abs(this.posY) >= 0.1D) {
/* 38 */       this.aboveVoidTicks = ticksExisted;
/*    */     }
/* 40 */     if (entityPlayer.func_70093_af()) {
/* 41 */       this.lastSneakTick = ticksExisted;
/*    */     }
/* 43 */     if (entityPlayer.field_82175_bq && entityPlayer.func_70632_aY()) {
/* 44 */       this.autoBlockTicks++;
/*    */     } else {
/* 46 */       this.autoBlockTicks = 0;
/*    */     } 
/* 48 */     if (entityPlayer.func_70051_ag() && entityPlayer.func_71039_bw()) {
/* 49 */       this.noSlowTicks++;
/*    */     } else {
/* 51 */       this.noSlowTicks = 0;
/*    */     } 
/* 53 */     if (entityPlayer.field_70125_A >= 70.0F && entityPlayer.func_70694_bm() != null && entityPlayer.func_70694_bm().func_77973_b() instanceof net.minecraft.item.ItemBlock) {
/* 54 */       if (entityPlayer.field_110158_av == 1) {
/* 55 */         if (!this.sneaking && entityPlayer.func_70093_af()) {
/* 56 */           this.sneakTicks++;
/*    */         } else {
/* 58 */           this.sneakTicks = 0;
/*    */         } 
/*    */       }
/*    */     } else {
/* 62 */       this.sneakTicks = 0;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/anticheat/PlayerData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */