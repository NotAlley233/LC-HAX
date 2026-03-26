/*    */ package wtf.tatp.meowtils.mixins;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
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
/*    */ 
/*    */ @SideOnly(Side.CLIENT)
/*    */ @Mixin({NetHandlerPlayClient.class})
/*    */ public class MixinNetHandlerPlayClient_ViewPackets
/*    */ {
/*    */   @Inject(method = {"addToSendQueue"}, at = {@At("HEAD")})
/*    */   private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
/* 25 */     Minecraft mc = Minecraft.func_71410_x();
/* 26 */     if (mc.field_71439_g == null || mc.field_71441_e == null)
/* 27 */       return;  if (!cfg.v.packetDebugger)
/* 28 */       return;  if (!cfg.v.packetDirection.equals("Outgoing") && !cfg.v.packetDirection.equals("Both"))
/* 29 */       return;  if (cfg.v.ignorePacketSpam && showAll(packet))
/*    */       return; 
/* 31 */     Meowtils.addMessage(EnumChatFormatting.GREEN + "« " + EnumChatFormatting.WHITE + packet.getClass().getSimpleName());
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean showAll(Packet<?> packet) {
/* 36 */     if (packet instanceof net.minecraft.network.play.client.C03PacketPlayer) return true; 
/* 37 */     if (packet instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction) return true; 
/* 38 */     if (packet instanceof net.minecraft.network.play.client.C00PacketKeepAlive) return true;
/*    */     
/* 40 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinNetHandlerPlayClient_ViewPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */