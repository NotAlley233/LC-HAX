package com.example.mod.module.modules;

import com.example.mod.gui.clickgui.ClickGuiScreen;
import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.KeyBindingManager;
import com.example.mod.module.ModuleManager;
import com.example.mod.property.PropertyManager;
import com.example.mod.util.render.RenderUtil;
import net.minecraft.client.Minecraft;

public class ClickGUIModule extends BaseModule {
    private final ModuleManager moduleManager;
    private final PropertyManager propertyManager;
    private final KeyBindingManager keyBindingManager;
    private boolean productFont = false;
    /** Panel outer glow in ClickGUI (optional). */
    private boolean panelGlow = false;

    public ClickGUIModule(ModuleManager moduleManager, PropertyManager propertyManager, KeyBindingManager keyBindingManager) {
        super("clickgui", "Open Click GUI.", Category.RENDER, false);
        this.moduleManager = moduleManager;
        this.propertyManager = propertyManager;
        this.keyBindingManager = keyBindingManager;
    }

    @Override
    protected void onEnable() {
        Minecraft mc = Minecraft.getMinecraft();
        RenderUtil.setUseProductFont(productFont);
        if (mc != null) {
            mc.displayGuiScreen(new ClickGuiScreen(moduleManager, propertyManager, keyBindingManager));
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

    public boolean isPanelGlow() {
        return panelGlow;
    }

    public void setPanelGlow(boolean panelGlow) {
        this.panelGlow = panelGlow;
    }
}
