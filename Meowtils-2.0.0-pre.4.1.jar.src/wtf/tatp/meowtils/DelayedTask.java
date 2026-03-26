/*    */ package wtf.tatp.meowtils;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ public class DelayedTask
/*    */ {
/*    */   private final Runnable runnable;
/*    */   private int counter;
/*    */   
/*    */   public DelayedTask(Runnable task, int ticks) {
/* 14 */     this.runnable = task;
/* 15 */     this.counter = ticks;
/* 16 */     MinecraftForge.EVENT_BUS.register(this);
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 21 */     if (event.phase != TickEvent.Phase.START)
/*    */       return; 
/* 23 */     if (this.counter-- <= 0) {
/* 24 */       MinecraftForge.EVENT_BUS.unregister(this);
/*    */       try {
/* 26 */         if ((Minecraft.func_71410_x()).field_71441_e == null || (Minecraft.func_71410_x()).field_71439_g == null)
/* 27 */           return;  this.runnable.run();
/* 28 */       } catch (Throwable t) {
/* 29 */         t.printStackTrace();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/DelayedTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */