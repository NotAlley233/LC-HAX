/*    */ package wtf.tatp.meowtils.modules.utility;
/*    */ 
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ import wtf.tatp.meowtils.DelayedTask;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ 
/*    */ public class AutoStairs
/*    */   extends Module
/*    */ {
/* 15 */   private double lastY = 0.0D;
/*    */   
/*    */   public AutoStairs() {
/* 18 */     super("AutoStairs", "autoStairsKey", "autoStairs", Module.Category.Utility);
/* 19 */     tooltip("Automatically jumps when going up stairs, for faster movement.");
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onTick(TickEvent.ClientTickEvent event) {
/* 24 */     if (event.phase != TickEvent.Phase.END || this.mc.field_71439_g == null || this.mc.field_71441_e == null)
/*    */       return; 
/* 26 */     double currentY = this.mc.field_71439_g.field_70163_u;
/*    */     
/* 28 */     if (this.mc.field_71439_g.field_70122_E && this.mc.field_71439_g.func_70051_ag()) {
/* 29 */       double heightGain = currentY - this.lastY;
/* 30 */       BlockPos below = new BlockPos(this.mc.field_71439_g.field_70165_t, this.mc.field_71439_g.field_70163_u - 0.1D, this.mc.field_71439_g.field_70161_v);
/*    */ 
/*    */       
/* 33 */       if (heightGain == 0.5D && this.mc.field_71441_e.func_180495_p(below).func_177230_c() instanceof net.minecraft.block.BlockStairs) {
/* 34 */         KeyBinding.func_74510_a(this.mc.field_71474_y.field_74314_A.func_151463_i(), true);
/* 35 */         Meowtils.debugMessage(EnumChatFormatting.YELLOW + "[AutoStairs]: " + EnumChatFormatting.RESET + "Jumped!");
/* 36 */         new DelayedTask(() -> KeyBinding.func_74510_a(this.mc.field_71474_y.field_74314_A.func_151463_i(), false), 1);
/*    */       } 
/*    */     } 
/* 39 */     this.lastY = currentY;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/utility/AutoStairs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */