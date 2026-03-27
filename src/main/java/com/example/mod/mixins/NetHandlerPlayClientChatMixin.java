package com.example.mod.mixins;

import com.example.mod.event.ChatReceiveDispatcher;
import net.minecraft.network.play.server.S02PacketChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.network.NetHandlerPlayClient.class)
public class NetHandlerPlayClientChatMixin {
    @Inject(method = "handleChat", at = @At("TAIL"))
    private void lchax$onHandleChat(S02PacketChat packet, CallbackInfo ci) {
        if (packet == null || packet.getChatComponent() == null) {
            return;
        }
        ChatReceiveDispatcher.publish(packet.getChatComponent().getUnformattedText());
    }
}
