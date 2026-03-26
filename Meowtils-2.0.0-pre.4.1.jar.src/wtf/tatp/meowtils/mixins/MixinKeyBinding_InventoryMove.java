/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.modules.advanced.InventoryMove;
/*    */ 
/*    */ 
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({KeyBinding.class})
/*    */ public class MixinKeyBinding_InventoryMove
/*    */ {
/*    */   @Inject(method = {"isKeyDown"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onIsKeyDown(CallbackInfoReturnable<Boolean> cir) {
/* 22 */     KeyBinding self = (KeyBinding)this;
/*    */     
/* 24 */     if (cfg.v.inventoryMove && InventoryMove.isMovementKey(self) && InventoryMove.isInSurvivalInventory() && !InventoryMove.clickedItem)
/* 25 */       cir.setReturnValue(Boolean.valueOf(Keyboard.isKeyDown(self.func_151463_i()))); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinKeyBinding_InventoryMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */