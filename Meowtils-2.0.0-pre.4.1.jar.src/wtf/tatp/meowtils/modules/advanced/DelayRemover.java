/*    */ package wtf.tatp.meowtils.modules.advanced;
/*    */ 
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*    */ import wtf.tatp.meowtils.mixins.EntityLivingBaseAccessor;
/*    */ import wtf.tatp.meowtils.mixins.MinecraftAccessor;
/*    */ import wtf.tatp.meowtils.mixins.PlayerControllerMPAccessor;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DelayRemover
/*    */   extends Module
/*    */ {
/*    */   private NumberValue useDelayTicks;
/*    */   private NumberValue breakDelay;
/*    */   private BooleanValue noBreakDelay;
/*    */   private BooleanValue noUseDelay;
/*    */   private BooleanValue noHitDelay;
/*    */   private BooleanValue noJumpDelay;
/* 26 */   private int useTicks = 0;
/*    */   private boolean holdingItem = false;
/*    */   
/*    */   public DelayRemover() {
/* 30 */     super("DelayRemover", "delayRemoverKey", "delayRemover", Module.Category.Advanced);
/* 31 */     tooltip("Remove or reduce certain delays.");
/* 32 */     addValue(this.breakDelay = new NumberValue("Break delay", 0.0D, 5.0D, 1.0D, "ticks", "breakDelay", int.class));
/* 33 */     addValue(this.useDelayTicks = new NumberValue("Extra use delay", 0.0D, 4.0D, 1.0D, "ticks", "noUseDelayExtraTicks", int.class));
/* 34 */     addBoolean(this.noBreakDelay = new BooleanValue("No break delay", "noBreakDelay"));
/* 35 */     addBoolean(this.noUseDelay = new BooleanValue("No use delay", "noUseDelay"));
/* 36 */     addBoolean(this.noHitDelay = new BooleanValue("No hit delay", "noHitDelay"));
/* 37 */     addBoolean(this.noJumpDelay = new BooleanValue("No jump delay", "noJumpDelay"));
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 43 */     if (this.mc.field_71439_g == null || this.mc.field_71441_e == null || event.phase != TickEvent.Phase.END) {
/*    */       return;
/*    */     }
/* 46 */     if (this.mc.field_71442_b != null && cfg.v.noBreakDelay) {
/* 47 */       PlayerControllerMPAccessor accessor = (PlayerControllerMPAccessor)this.mc.field_71442_b;
/* 48 */       int configuredDelay = cfg.v.breakDelay;
/* 49 */       if (accessor.getBlockHitDelay() > configuredDelay) {
/* 50 */         accessor.setBlockHitDelay(configuredDelay);
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 55 */     if (cfg.v.noUseDelay) {
/* 56 */       ItemStack held = this.mc.field_71439_g.func_70694_bm();
/* 57 */       boolean validItem = (held != null && (held.func_77973_b() instanceof net.minecraft.item.ItemFood || held.func_77973_b() instanceof net.minecraft.item.ItemPotion));
/*    */       
/* 59 */       boolean holdingUse = this.mc.field_71474_y.field_74313_G.func_151470_d();
/*    */       
/* 61 */       if (holdingUse && validItem) {
/* 62 */         this.holdingItem = true;
/* 63 */         this.useTicks++;
/*    */         
/* 65 */         int consumeTicks = 32 + cfg.v.noUseDelayExtraTicks;
/*    */         
/* 67 */         if (this.useTicks >= consumeTicks) {
/*    */           
/* 69 */           KeyBinding.func_74510_a(this.mc.field_71474_y.field_74313_G.func_151463_i(), false);
/* 70 */           this.holdingItem = false;
/* 71 */           this.useTicks = 0;
/*    */         } 
/*    */       } else {
/* 74 */         this.holdingItem = false;
/* 75 */         this.useTicks = 0;
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 80 */     if (cfg.v.noHitDelay) {
/* 81 */       ((MinecraftAccessor)this.mc).setLeftClickCounter(0);
/*    */     }
/*    */ 
/*    */     
/* 85 */     if (cfg.v.noJumpDelay)
/* 86 */       ((EntityLivingBaseAccessor)this.mc.field_71439_g).setJumpTicks(0); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/advanced/DelayRemover.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */