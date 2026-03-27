package com.example.mod.module.modules;

import com.example.mod.gui.clickgui.ClickGuiScreen;
import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.ModuleManager;
import com.example.mod.property.PropertyManager;
import com.example.mod.util.render.RenderUtil;
import net.minecraft.client.Minecraft;

public class ClickGUIModule extends BaseModule {
    private final ModuleManager moduleManager;
    private final PropertyManager propertyManager;
    private boolean productFont = false;

    public ClickGUIModule(ModuleManager moduleManager, PropertyManager propertyManager) {
        super("clickgui", "Open Click GUI.", Category.RENDER, false);
        this.moduleManager = moduleManager;
        this.propertyManager = propertyManager;
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        RenderUtil.setUseProductFont(productFont);
        if (mc != null) {
            mc.displayGuiScreen(new ClickGuiScreen(moduleManager, propertyManager));
        }
        setEnabled(false);
    }

    public boolean isProductFont() {
        return productFont;
    }

    public void setProductFont(boolean productFont) {
        this.productFont = productFont;
        RenderUtil.setUseProductFont(productFont);
    }
}
