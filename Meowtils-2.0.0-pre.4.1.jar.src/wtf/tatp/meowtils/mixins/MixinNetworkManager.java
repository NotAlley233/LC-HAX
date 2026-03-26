/*    */ package wtf.tatp.meowtils.mixins;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import net.minecraft.network.NetworkManager;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraftforge.common.MinecraftForge;
/*    */ import net.minecraftforge.fml.common.eventhandler.Event;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import wtf.tatp.meowtils.events.ReceivePacketEvent;
/*    */ import wtf.tatp.meowtils.events.SendPacketEvent;
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({NetworkManager.class})
/*    */ public abstract class MixinNetworkManager {
/*    */   @Inject(method = {"channelRead0"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void onPacketReceive(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
/* 21 */     ReceivePacketEvent event = new ReceivePacketEvent(packet);
/* 22 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 23 */     if (event.isCanceled())
/* 24 */       ci.cancel(); 
/*    */   }
/*    */   
/*    */   @Inject(method = {"sendPacket(Lnet/minecraft/network/Packet;)V"}, at = {@At("HEAD")}, cancellable = true)
/*    */   private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
/* 29 */     SendPacketEvent event = new SendPacketEvent(packet);
/* 30 */     MinecraftForge.EVENT_BUS.post((Event)event);
/* 31 */     if (event.isCanceled())
/* 32 */       ci.cancel(); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinNetworkManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */