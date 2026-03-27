package com.example.mod.mixins;

import com.example.mod.ModContext;
import com.example.mod.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.entity.EntityPlayerSP;

@Mixin(EntityPlayerSP.class)
public abstract class ChatCommandMixin {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void lchax$onSendChatMessage(String message, CallbackInfo ci) {
        CommandManager mgr = ModContext.getCommandManager();
        if (mgr == null) return;

        if (!mgr.isTypingCommand(message)) return;

        ci.cancel();
        mgr.handleCommand(message);
    }
}
