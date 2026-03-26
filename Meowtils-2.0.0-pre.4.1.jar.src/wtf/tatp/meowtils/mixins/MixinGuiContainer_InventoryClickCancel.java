/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.inventory.GuiContainer;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import wtf.tatp.meowtils.DelayedTask;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.events.DelayedClick;
/*    */ import wtf.tatp.meowtils.modules.advanced.InventoryMove;
/*    */ import wtf.tatp.meowtils.modules.bedwars.AutoClaim;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({GuiContainer.class})
/*    */ public abstract class MixinGuiContainer_InventoryClickCancel {
/*    */   private boolean scheduledClick;
/* 25 */   private int clickDelay = cfg.v.inventoryMoveClickDelay;
/*    */   
/*    */   @Inject(method = {"handleMouseClick"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onHandleMouseClick(Slot slot, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
/* 29 */     if (!cfg.v.inventoryMove)
/* 30 */       return;  if (!InventoryMove.isInSurvivalInventory())
/* 31 */       return;  if (this.scheduledClick) {
/* 32 */       ci.cancel();
/*    */     }
/* 34 */     if (!InventoryMove.clickedItem) {
/* 35 */       ci.cancel();
/* 36 */       InventoryMove.clickedItem = true;
/* 37 */       this.scheduledClick = true;
/* 38 */       GuiContainer gui = (GuiContainer)this;
/* 39 */       Minecraft.func_71410_x().func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
/*    */       
/* 41 */       new DelayedTask(() -> this.scheduledClick = false, this.clickDelay);
/*    */ 
/*    */ 
/*    */       
/* 45 */       DelayedClick.windowClick(gui.field_147002_h.field_75152_c, slotId, clickedButton, clickType, this.clickDelay, (EntityPlayer)(Minecraft.func_71410_x()).field_71439_g);
/*    */     } 
/* 47 */     if (cfg.v.autoClaim && AutoClaim.autoClaimPending)
/* 48 */       ci.cancel(); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinGuiContainer_InventoryClickCancel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */