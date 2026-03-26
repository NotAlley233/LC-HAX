/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.modules.utility.HotbarLock;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({KeyBinding.class})
/*    */ public class MixinKeyBinding_HotbarLock
/*    */ {
/*    */   @Shadow
/*    */   private boolean field_74513_e;
/*    */   @Shadow
/*    */   private int field_151474_i;
/*    */   
/*    */   @Inject(method = {"isPressed"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onIsPressed(CallbackInfoReturnable<Boolean> cir) {
/* 26 */     KeyBinding self = (KeyBinding)this;
/* 27 */     Minecraft mc = Minecraft.func_71410_x();
/* 28 */     if (mc.field_71439_g == null)
/* 29 */       return;  if (mc.field_71462_r != null)
/* 30 */       return;  if (!cfg.v.hotbarLock)
/*    */       return; 
/* 32 */     if (self == mc.field_71474_y.field_74316_C) {
/* 33 */       boolean cancel = false;
/*    */       
/* 35 */       if (cfg.v.hotbarLockMode.equals("Manual") && HotbarLock.isSlotLocked(mc.field_71439_g.field_71071_by.field_70461_c + 1)) {
/* 36 */         cancel = true;
/*    */       }
/*    */       
/* 39 */       if (cfg.v.hotbarLockMode.equals("Swords") && mc.field_71439_g.func_70694_bm() != null && mc.field_71439_g.func_70694_bm().func_77973_b() instanceof net.minecraft.item.ItemSword) {
/* 40 */         cancel = true;
/*    */       }
/*    */       
/* 43 */       if (cancel) {
/* 44 */         this.field_74513_e = false;
/* 45 */         this.field_151474_i = 0;
/*    */         
/* 47 */         cir.setReturnValue(Boolean.valueOf(false));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinKeyBinding_HotbarLock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */