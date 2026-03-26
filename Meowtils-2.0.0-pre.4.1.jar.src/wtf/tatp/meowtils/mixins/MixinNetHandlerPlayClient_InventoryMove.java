/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.Queue;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.C0EPacketClickWindow;
/*    */ import net.minecraft.network.play.client.C16PacketClientStatus;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Unique;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import wtf.tatp.meowtils.DelayedTask;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.modules.advanced.InventoryMove;
/*    */ 
/*    */ 
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({NetHandlerPlayClient.class})
/*    */ public class MixinNetHandlerPlayClient_InventoryMove
/*    */ {
/*    */   @Unique
/* 28 */   private final Queue<C0EPacketClickWindow> delayedClick = new LinkedList<>();
/*    */ 
/*    */   
/*    */   @Inject(method = {"addToSendQueue"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
/* 33 */     if (InventoryMove.isInSurvivalInventory() && packet instanceof net.minecraft.network.play.client.C0DPacketCloseWindow && cfg.v.inventoryMove && !InventoryMove.clickedItem) {
/* 34 */       ci.cancel();
/*    */     }
/* 36 */     if (packet instanceof net.minecraft.network.play.client.C0DPacketCloseWindow) {
/* 37 */       InventoryMove.clickedItem = false;
/*    */     }
/* 39 */     if (packet instanceof C16PacketClientStatus && ((C16PacketClientStatus)packet).func_149435_c() == C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT && cfg.v.inventoryMove && !InventoryMove.clickedItem) {
/* 40 */       ci.cancel();
/*    */     }
/* 42 */     if (packet instanceof C0EPacketClickWindow && !InventoryMove.clickedItem && cfg.v.inventoryMove && InventoryMove.isInSurvivalInventory()) {
/* 43 */       ci.cancel();
/* 44 */       InventoryMove.clickedItem = true;
/* 45 */       Minecraft.func_71410_x().func_147114_u().func_147297_a((Packet)new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
/* 46 */       this.delayedClick.add((C0EPacketClickWindow)packet);
/* 47 */       new DelayedTask(() -> { if (!this.delayedClick.isEmpty()) { C0EPacketClickWindow click = this.delayedClick.poll(); Minecraft.func_71410_x().func_147114_u().func_147297_a((Packet)click); }  }cfg.v.inventoryMoveClickDelay);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinNetHandlerPlayClient_InventoryMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */