/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import net.minecraft.item.ItemPotion;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.util.Render;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({GuiContainer.class})
/*    */ public abstract class MixinGuiContainer_ShinyPots
/*    */ {
/*    */   @Shadow
/*    */   protected abstract void func_146977_a(Slot paramSlot);
/*    */   
/*    */   @Inject(method = {"drawSlot"}, at = {@At("HEAD")})
/*    */   private void onDrawSlot(Slot slot, CallbackInfo ci) {
/* 26 */     if (!cfg.v.shinyPots)
/* 27 */       return;  if (slot == null || !slot.func_75216_d())
/*    */       return; 
/* 29 */     ItemStack stack = slot.func_75211_c();
/* 30 */     if (stack.func_77973_b() instanceof ItemPotion) {
/* 31 */       int color = ((ItemPotion)stack.func_77973_b()).func_82790_a(stack, 0);
/* 32 */       Render.drawSlotBackground(slot.field_75223_e, slot.field_75221_f, color | 0xCC000000);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinGuiContainer_ShinyPots.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */