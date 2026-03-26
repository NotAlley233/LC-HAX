/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.modules.advanced.InventoryFill;
/*    */ import wtf.tatp.meowtils.util.Render;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({GuiContainer.class})
/*    */ public abstract class MixinGuiContainer_InventoryFill
/*    */ {
/*    */   @Inject(method = {"drawSlot"}, at = {@At("HEAD")})
/*    */   private void onDrawSlot(Slot slot, CallbackInfo ci) {
/* 22 */     if (!cfg.v.inventoryFillRenderClicked)
/* 23 */       return;  if (slot == null)
/* 24 */       return;  if (InventoryFill.clickedSlots.contains(Integer.valueOf(slot.field_75222_d)))
/* 25 */       Render.drawSlotBackground(slot.field_75223_e, slot.field_75221_f, (new Color(255, 0, 255, 255)).getRGB()); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinGuiContainer_InventoryFill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */