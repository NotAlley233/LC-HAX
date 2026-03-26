package com.example.mod.mixins;

import net.minecraft.client.Minecraft;
import com.example.mod.ModContext;
import com.example.mod.module.ModuleManager;
import com.example.mod.module.Module;
import com.example.mod.module.Tickable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "startGame", at = @At("HEAD"))
    public void onStartGame(CallbackInfo ci) {
        System.out.println("Mixin Test");
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    public void onRunTick(CallbackInfo ci) {
        ModuleManager moduleManager = ModContext.getModuleManager();
        if (moduleManager != null) {
            for (Module m : moduleManager.all()) {
                if (m.enabled() && m instanceof Tickable) {
                    ((Tickable) m).onTick();
                }
            }
        }
    }
}
