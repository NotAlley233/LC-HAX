package com.example.mod.gui;

import com.example.mod.gui.components.ModuleButton;
import com.example.mod.gui.components.Panel;
import com.example.mod.gui.components.settings.BooleanComponent;
import com.example.mod.gui.components.settings.SliderComponent;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.property.Property;
import com.example.mod.property.PropertyManager;
import com.example.mod.property.properties.BooleanProperty;
import com.example.mod.property.properties.IntProperty;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClickGUIScreen extends GuiScreen {
    private final List<Panel> panels = new ArrayList<>();

    public ClickGUIScreen(ModuleManager moduleManager, PropertyManager propertyManager) {
        Panel panel = new Panel("Modules", 20, 20, 120);
        List<Module> modules = new ArrayList<>(moduleManager.all());
        modules.sort(Comparator.comparing(Module::name, String.CASE_INSENSITIVE_ORDER));
        for (Module m : modules) {
            ModuleButton button = new ModuleButton(m);
            List<Property<?>> properties = propertyManager.getProperties(m);
            if (properties != null) {
                for (Property<?> prop : properties) {
                    if (prop.getName().equalsIgnoreCase("enabled")) continue;
                    if (prop instanceof BooleanProperty) {
                        button.addSetting(new BooleanComponent((BooleanProperty) prop));
                    } else if (prop instanceof IntProperty) {
                        button.addSetting(new SliderComponent((IntProperty) prop));
                    }
                }
            }
            panel.addButton(button);
        }
        panels.add(panel);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Draw background tint
        this.drawDefaultBackground();

        for (Panel panel : panels) {
            panel.drawScreen(mouseX, mouseY, partialTicks);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Panel panel : panels) {
            panel.mouseClicked(mouseX, mouseY, mouseButton);
        }
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for (Panel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        for (Panel panel : panels) {
            panel.keyTyped(typedChar, keyCode);
        }
        try {
            super.keyTyped(typedChar, keyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
