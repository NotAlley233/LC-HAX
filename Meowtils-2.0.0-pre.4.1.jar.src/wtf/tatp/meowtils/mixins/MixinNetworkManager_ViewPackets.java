/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.network.NetworkManager;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ 
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({NetworkManager.class})
/*    */ public class MixinNetworkManager_ViewPackets
/*    */ {
/*    */   @Inject(method = {"channelRead0"}, at = {@At("HEAD")})
/*    */   private void onReceivePacket(ChannelHandlerContext ctx, Packet<?> packet, CallbackInfo ci) {
/* 25 */     Minecraft mc = Minecraft.func_71410_x();
/* 26 */     if (mc.field_71439_g == null || mc.field_71441_e == null)
/* 27 */       return;  if (!cfg.v.packetDebugger)
/* 28 */       return;  if (!cfg.v.packetDirection.equals("Incoming") && !cfg.v.packetDirection.equals("Both"))
/* 29 */       return;  if (cfg.v.ignorePacketSpam && showAll(packet))
/*    */       return; 
/* 31 */     Meowtils.addMessage(EnumChatFormatting.RED + "» " + EnumChatFormatting.WHITE + packet.getClass().getSimpleName());
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean showAll(Packet<?> packet) {
/* 36 */     if (packet instanceof net.minecraft.network.play.server.S14PacketEntity) return true; 
/* 37 */     if (packet instanceof net.minecraft.network.play.server.S18PacketEntityTeleport) return true; 
/* 38 */     if (packet instanceof net.minecraft.network.play.server.S08PacketPlayerPosLook) return true; 
/* 39 */     if (packet instanceof net.minecraft.network.play.server.S32PacketConfirmTransaction) return true; 
/* 40 */     if (packet instanceof net.minecraft.network.play.server.S00PacketKeepAlive) return true; 
/* 41 */     if (packet instanceof net.minecraft.network.play.server.S19PacketEntityHeadLook) return true; 
/* 42 */     if (packet instanceof net.minecraft.network.play.server.S3EPacketTeams) return true; 
/* 43 */     if (packet instanceof net.minecraft.network.play.server.S29PacketSoundEffect) return true; 
/* 44 */     if (packet instanceof net.minecraft.network.play.server.S03PacketTimeUpdate) return true; 
/* 45 */     if (packet instanceof net.minecraft.network.play.server.S1CPacketEntityMetadata) return true; 
/* 46 */     if (packet instanceof net.minecraft.network.play.server.S0BPacketAnimation) return true; 
/* 47 */     if (packet instanceof net.minecraft.network.play.server.S3CPacketUpdateScore) return true; 
/* 48 */     if (packet instanceof net.minecraft.network.play.server.S21PacketChunkData) return true; 
/* 49 */     if (packet instanceof net.minecraft.network.play.server.S38PacketPlayerListItem) return true; 
/* 50 */     if (packet instanceof net.minecraft.network.play.server.S04PacketEntityEquipment) return true; 
/* 51 */     if (packet instanceof net.minecraft.network.play.server.S20PacketEntityProperties) return true; 
/* 52 */     if (packet instanceof net.minecraft.network.play.server.S26PacketMapChunkBulk) return true; 
/* 53 */     if (packet instanceof net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter) return true; 
/* 54 */     if (packet instanceof net.minecraft.network.play.client.C03PacketPlayer) return true;
/*    */ 
/*    */     
/* 57 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinNetworkManager_ViewPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */