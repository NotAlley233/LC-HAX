/*    */ package wtf.tatp.meowtils.modules.utility;
/*    */ 
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ 
/*    */ public class Sprint extends Module {
/*    */   private boolean toggled = true;
/*    */   
/*    */   public Sprint() {
/* 12 */     super("Sprint", "sprintKey", "sprint", Module.Category.Utility);
/* 13 */     tooltip("Automatically toggles sprint for you.");
/*    */   }
/*    */   @SubscribeEvent
/*    */   public void onRenderTick(TickEvent.RenderTickEvent event) {
/* 17 */     if (this.mc.field_71439_g == null || this.mc.field_71441_e == null)
/* 18 */       return;  KeyBinding.func_74510_a(this.mc.field_71474_y.field_151444_V.func_151463_i(), (this.toggled && this.mc.field_71474_y.field_74351_w.func_151470_d()));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/utility/Sprint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */