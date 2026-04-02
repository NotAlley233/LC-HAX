package com.example.mod.listener;

import com.example.mod.ModContext;
import com.example.mod.dynamicisland.render.DIAnimationUtil;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.module.OverlayRenderable;
import net.weavemc.api.RenderGameOverlayEvent;
import net.weavemc.api.event.SubscribeEvent;

public class RenderGameOverlayEventListener {
    private long lastFrameTime = System.currentTimeMillis();

    @SubscribeEvent
    public void onEvent(RenderGameOverlayEvent event) {
        long now = System.currentTimeMillis();
        DIAnimationUtil.setDelta(now - lastFrameTime);
        lastFrameTime = now;

        ModuleManager moduleManager = ModContext.getModuleManager();
        if (moduleManager == null) return;
        for (Module module : moduleManager.all()) {
            if (module.enabled() && module instanceof OverlayRenderable) {
                ((OverlayRenderable) module).onRenderOverlay();
            }
        }
    }
}
