package com.example.mod.mixins;

import com.example.mod.ModContext;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.module.modules.Cape;
import com.example.mod.util.CapeCache;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerCapeMixin {
    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    private void onGetLocationCape(CallbackInfoReturnable<ResourceLocation> cir) {
        if (!(((Object) this) instanceof EntityPlayerSP)) return;
        ModuleManager moduleManager = ModContext.getModuleManager();
        if (moduleManager == null) return;
        Module module = moduleManager.get("cape");
        if (!(module instanceof Cape) || !module.enabled()) return;
        Cape capeModule = (Cape) module;
        cir.setReturnValue(CapeCache.getCape(capeModule.getSelectedCape()));
    }
}
