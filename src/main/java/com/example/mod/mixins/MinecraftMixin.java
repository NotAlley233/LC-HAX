package com.example.mod.mixins;

import net.minecraft.client.Minecraft;
import com.example.mod.ModContext;
import com.example.mod.module.ModuleManager;
import com.example.mod.module.Module;
import com.example.mod.module.Tickable;
import com.example.mod.module.modules.advanced.NoHitDelay;
import com.example.mod.util.GamemodeUtil;
import com.example.mod.util.TeamUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow
    private int leftClickCounter;

    @Inject(method = "startGame", at = @At("HEAD"))
    public void onStartGame(CallbackInfo ci) {
        System.out.println("Mixin Test");
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    public void onRunTick(CallbackInfo ci) {
        GamemodeUtil.updateGamemode();
        TeamUtil.updateSpectatorState();
        ModuleManager moduleManager = ModContext.getModuleManager();
        if (moduleManager != null) {
            for (Module m : moduleManager.all()) {
                if (m.enabled() && m instanceof Tickable) {
                    ((Tickable) m).onTick();
                }
            }
        }
    }

    @Inject(method = "clickMouse", at = @At("HEAD"))
    public void onClickMouse(CallbackInfo ci) {
        ModuleManager moduleManager = ModContext.getModuleManager();
        if (moduleManager == null) return;
        Module module = moduleManager.get("nohitdelay");
        if (module instanceof NoHitDelay && module.enabled()) {
            leftClickCounter = 0;
        }
    }
}
