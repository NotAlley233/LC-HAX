/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.modules.utility.NullMove;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({KeyBinding.class})
/*    */ public abstract class MixinKeyBinding_NullMove {
/*    */   @Shadow
/*    */   private int field_74512_d;
/*    */   @Shadow
/*    */   public boolean field_74513_e;
/*    */   
/*    */   @Inject(method = {"isKeyDown"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onIsKeyDown(CallbackInfoReturnable<Boolean> cir) {
/* 27 */     GameSettings settings = (Minecraft.func_71410_x()).field_71474_y;
/* 28 */     if (!cfg.v.nullMove)
/* 29 */       return;  if ((Minecraft.func_71410_x()).field_71462_r instanceof net.minecraft.client.gui.inventory.GuiInventory)
/*    */       return; 
/* 31 */     if (this.field_74512_d == settings.field_74370_x.func_151463_i()) {
/* 32 */       if (this.field_74513_e)
/* 33 */       { if (NullMove.RIGHT_STRAFE_LAST_PRESS_TIME == 0L) {
/* 34 */           cir.setReturnValue(Boolean.valueOf(true));
/*    */           return;
/*    */         } 
/* 37 */         cir.setReturnValue(Boolean.valueOf((NullMove.RIGHT_STRAFE_LAST_PRESS_TIME <= NullMove.LEFT_STRAFE_LAST_PRESS_TIME))); }
/* 38 */       else { cir.setReturnValue(Boolean.valueOf(false)); }
/* 39 */        cir.cancel();
/*    */     
/*    */     }
/* 42 */     else if (this.field_74512_d == settings.field_74366_z.func_151463_i()) {
/* 43 */       if (this.field_74513_e)
/* 44 */       { if (NullMove.LEFT_STRAFE_LAST_PRESS_TIME == 0L) {
/* 45 */           cir.setReturnValue(Boolean.valueOf(true));
/*    */           return;
/*    */         } 
/* 48 */         cir.setReturnValue(Boolean.valueOf((NullMove.LEFT_STRAFE_LAST_PRESS_TIME <= NullMove.RIGHT_STRAFE_LAST_PRESS_TIME))); }
/* 49 */       else { cir.setReturnValue(Boolean.valueOf(false)); }
/* 50 */        cir.cancel();
/*    */     
/*    */     }
/* 53 */     else if (this.field_74512_d == settings.field_74351_w.func_151463_i()) {
/* 54 */       if (this.field_74513_e)
/* 55 */       { if (NullMove.BACKWARD_STRAFE_LAST_PRESS_TIME == 0L) {
/* 56 */           cir.setReturnValue(Boolean.valueOf(true));
/*    */           return;
/*    */         } 
/* 59 */         cir.setReturnValue(Boolean.valueOf((NullMove.BACKWARD_STRAFE_LAST_PRESS_TIME <= NullMove.FORWARD_STRAFE_LAST_PRESS_TIME))); }
/* 60 */       else { cir.setReturnValue(Boolean.valueOf(false)); }
/* 61 */        cir.cancel();
/*    */     
/*    */     }
/* 64 */     else if (this.field_74512_d == settings.field_74368_y.func_151463_i()) {
/* 65 */       if (this.field_74513_e)
/* 66 */       { if (NullMove.FORWARD_STRAFE_LAST_PRESS_TIME == 0L) {
/* 67 */           cir.setReturnValue(Boolean.valueOf(true));
/*    */           return;
/*    */         } 
/* 70 */         cir.setReturnValue(Boolean.valueOf((NullMove.FORWARD_STRAFE_LAST_PRESS_TIME <= NullMove.BACKWARD_STRAFE_LAST_PRESS_TIME))); }
/* 71 */       else { cir.setReturnValue(Boolean.valueOf(false)); }
/* 72 */        cir.cancel();
/*    */     } 
/*    */   }
/*    */   
/*    */   @Inject(method = {"setKeyBindState"}, at = {@At("HEAD")})
/*    */   private static void onSetKeyBindState(int keyCode, boolean pressed, CallbackInfo ci) {
/* 78 */     GameSettings settings = (Minecraft.func_71410_x()).field_71474_y;
/* 79 */     if (!cfg.v.nullMove)
/*    */       return; 
/* 81 */     if (keyCode == settings.field_74370_x.func_151463_i()) {
/* 82 */       NullMove.LEFT_STRAFE_LAST_PRESS_TIME = pressed ? System.nanoTime() : 0L;
/* 83 */     } else if (keyCode == settings.field_74366_z.func_151463_i()) {
/* 84 */       NullMove.RIGHT_STRAFE_LAST_PRESS_TIME = pressed ? System.nanoTime() : 0L;
/* 85 */     } else if (keyCode == settings.field_74351_w.func_151463_i()) {
/* 86 */       NullMove.FORWARD_STRAFE_LAST_PRESS_TIME = pressed ? System.nanoTime() : 0L;
/* 87 */     } else if (keyCode == settings.field_74368_y.func_151463_i()) {
/* 88 */       NullMove.BACKWARD_STRAFE_LAST_PRESS_TIME = pressed ? System.nanoTime() : 0L;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinKeyBinding_NullMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */