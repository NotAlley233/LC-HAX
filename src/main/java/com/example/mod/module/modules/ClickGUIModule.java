package com.example.mod.module.modules;

import com.example.mod.gui.ClickGUIScreen;
import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.ModuleManager;
import com.example.mod.property.PropertyManager;
import net.minecraft.client.Minecraft;

public class ClickGUIModule extends BaseModule {
    private final ModuleManager moduleManager;
    private final PropertyManager propertyManager;

    public ClickGUIModule(ModuleManager moduleManager, PropertyManager propertyManager) {
        super("clickgui", "Open Click GUI.", Category.RENDER, false);
        this.moduleManager = moduleManager;
        this.propertyManager = propertyManager;
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null) {
            mc.displayGuiScreen(new ClickGUIScreen(moduleManager, propertyManager));
        }
        setEnabled(false);
    }
}
