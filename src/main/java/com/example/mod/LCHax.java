package com.example.mod;

import com.example.mod.command.CommandManager;
import com.example.mod.command.commands.HelpCommand;
import com.example.mod.command.commands.ListCommand;
import com.example.mod.command.commands.ModuleCommand;
import com.example.mod.command.commands.PrefixCommand;
import com.example.mod.command.commands.ToggleCommand;
import com.example.mod.command.commands.AntiCheatInfoCommand;
import com.example.mod.config.ConfigManager;
import com.example.mod.config.ConfigStore;
import com.example.mod.core.ModLogger;
import com.example.mod.listener.RenderGameOverlayEventListener;
import com.example.mod.module.KeyBindingManager;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.module.modules.AutoClicker;
import com.example.mod.module.modules.ArrayList;
import com.example.mod.module.modules.Cape;
import com.example.mod.module.modules.ClickGUIModule;
import com.example.mod.module.modules.PrefixModule;
import com.example.mod.module.modules.RightClicker;
import com.example.mod.module.modules.advanced.AntiBot;
import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.module.modules.advanced.Eagle;
import com.example.mod.module.modules.advanced.NoHitDelay;
import com.example.mod.module.modules.bedwars.BedTracker;
import com.example.mod.module.modules.render.DynamicIsland;
import com.example.mod.module.modules.render.GlowTest;
import com.example.mod.module.modules.render.PlayerESP;
import com.example.mod.module.modules.skywars.StrengthESP;
import com.example.mod.property.PropertyManager;
import com.example.mod.property.properties.FloatProperty;
import com.example.mod.property.properties.BooleanProperty;
import com.example.mod.property.properties.IntProperty;
import com.example.mod.property.properties.ModeProperty;
import com.example.mod.property.properties.TextProperty;
import com.example.mod.util.ChatUtil;
import net.weavemc.api.KeyboardEvent;
import net.weavemc.api.ModInitializer;
import net.weavemc.api.event.EventBus;
import org.jetbrains.annotations.NotNull;

import java.lang.instrument.Instrumentation;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.lwjgl.input.Keyboard;

public class LCHax implements ModInitializer {
    @Override
    public void init() {
        System.out.println("Hello from LC-HAX!");

        EventBus.subscribe(new RenderGameOverlayEventListener());

        ModuleManager moduleManager = new ModuleManager();
        AntiCheat antiCheatModule = new AntiCheat();
        moduleManager.register(new PrefixModule(true));
        moduleManager.register(new AutoClicker());
        moduleManager.register(new RightClicker());
        moduleManager.register(antiCheatModule);
        moduleManager.register(new AntiBot());
        moduleManager.register(new Eagle());
        moduleManager.register(new NoHitDelay());
        moduleManager.register(new BedTracker());
        moduleManager.register(new StrengthESP());
        moduleManager.register(new PlayerESP());
        moduleManager.register(new DynamicIsland());
        moduleManager.register(new GlowTest());
        moduleManager.register(new Cape());
        moduleManager.register(new ArrayList());

        Path configPath = ConfigStore.defaultConfigPath("lchax.json");
        ConfigStore configStore = new ConfigStore(configPath);

        KeyBindingManager keyBindingManager = new KeyBindingManager();
        PropertyManager propertyManager = new PropertyManager();
        moduleManager.register(new ClickGUIModule(moduleManager, propertyManager, keyBindingManager));
        ClickGUIModule clickGUIModule = (ClickGUIModule) moduleManager.get("clickgui");
        if (clickGUIModule != null) {
            propertyManager.register(
                    clickGUIModule,
                    new BooleanProperty("enabled", clickGUIModule::enabled, clickGUIModule::setEnabled),
                    new BooleanProperty("productFont", clickGUIModule::isProductFont, clickGUIModule::setProductFont),
                    new BooleanProperty("panelGlow", clickGUIModule::isPanelGlow, clickGUIModule::setPanelGlow)
            );
        }

        AutoClicker autoClicker = (AutoClicker) moduleManager.get("autoclicker");
        if (autoClicker != null) {
            propertyManager.register(
                    autoClicker,
                    new BooleanProperty("enabled", autoClicker::enabled, autoClicker::setEnabled),
                    new IntProperty("minCPS", autoClicker::getMinCPS, autoClicker::setMinCPS, 1, 50),
                    new IntProperty("maxCPS", autoClicker::getMaxCPS, autoClicker::setMaxCPS, 1, 50),
                    new BooleanProperty("breakBlocks", autoClicker::isBreakBlocks, autoClicker::setBreakBlocks)
            );
        }

        RightClicker rightClicker = (RightClicker) moduleManager.get("rightclicker");
        if (rightClicker != null) {
            propertyManager.register(
                    rightClicker,
                    new BooleanProperty("enabled", rightClicker::enabled, rightClicker::setEnabled),
                    new IntProperty("minCPS", rightClicker::getMinCPS, rightClicker::setMinCPS, 1, 50),
                    new IntProperty("maxCPS", rightClicker::getMaxCPS, rightClicker::setMaxCPS, 1, 50),
                    new IntProperty("startDelay", rightClicker::getStartDelay, rightClicker::setStartDelay, 0, 1000)
            );
        }

        PrefixModule prefixModule = (PrefixModule) moduleManager.get("prefix");
        if (prefixModule != null) {
            propertyManager.register(
                    prefixModule,
                    new BooleanProperty("enabled", prefixModule::enabled, (v) -> prefixModule.setEnabled(v)),
                    new TextProperty(
                            "text",
                            ChatUtil::getPrefixText,
                            (t) -> {
                                ChatUtil.setPrefixText(t);
                            },
                            "text"
                    )
            );
        }

        AntiBot antiBot = (AntiBot) moduleManager.get("antibot");
        if (antiBot != null) {
            propertyManager.register(
                    antiBot,
                    new BooleanProperty("enabled", antiBot::enabled, antiBot::setEnabled),
                    new TextProperty("mode", antiBot::getMode, antiBot::setMode, "mode")
            );
        }

        AntiCheat antiCheat = (AntiCheat) moduleManager.get("anticheat");
        if (antiCheat != null) {
            propertyManager.register(
                    antiCheat,
                    new BooleanProperty("enabled", antiCheat::enabled, antiCheat::setEnabled),
                    new BooleanProperty("detectNoSlow", antiCheat::isDetectNoSlow, antiCheat::setDetectNoSlow),
                    new IntProperty("vlNoSlow", antiCheat::getVlNoSlow, antiCheat::setVlNoSlow, 5, 30),
                    new IntProperty("cdNoSlowMs", antiCheat::getCooldownNoSlowMs, antiCheat::setCooldownNoSlowMs, 0, 3000),
                    new BooleanProperty("soundNoSlow", antiCheat::isSoundNoSlow, antiCheat::setSoundNoSlow),
                    new BooleanProperty("detectAutoBlock", antiCheat::isDetectAutoBlock, antiCheat::setDetectAutoBlock),
                    new IntProperty("vlAutoBlock", antiCheat::getVlAutoBlock, antiCheat::setVlAutoBlock, 5, 30),
                    new IntProperty("cdAutoBlockMs", antiCheat::getCooldownAutoBlockMs, antiCheat::setCooldownAutoBlockMs, 0, 3000),
                    new BooleanProperty("soundAutoBlock", antiCheat::isSoundAutoBlock, antiCheat::setSoundAutoBlock),
                    new BooleanProperty("detectEagle", antiCheat::isDetectEagle, antiCheat::setDetectEagle),
                    new IntProperty("vlEagle", antiCheat::getVlEagle, antiCheat::setVlEagle, 5, 30),
                    new IntProperty("cdEagleMs", antiCheat::getCooldownEagleMs, antiCheat::setCooldownEagleMs, 0, 3000),
                    new BooleanProperty("soundEagle", antiCheat::isSoundEagle, antiCheat::setSoundEagle),
                    new BooleanProperty("detectScaffold", antiCheat::isDetectScaffold, antiCheat::setDetectScaffold),
                    new IntProperty("vlScaffold", antiCheat::getVlScaffold, antiCheat::setVlScaffold, 5, 30),
                    new IntProperty("cdScaffoldMs", antiCheat::getCooldownScaffoldMs, antiCheat::setCooldownScaffoldMs, 0, 3000),
                    new BooleanProperty("soundScaffold", antiCheat::isSoundScaffold, antiCheat::setSoundScaffold),
                    new BooleanProperty("detectTower", antiCheat::isDetectTower, antiCheat::setDetectTower),
                    new IntProperty("vlTower", antiCheat::getVlTower, antiCheat::setVlTower, 5, 30),
                    new IntProperty("cdTowerMs", antiCheat::getCooldownTowerMs, antiCheat::setCooldownTowerMs, 0, 3000),
                    new BooleanProperty("soundTower", antiCheat::isSoundTower, antiCheat::setSoundTower),
                    new BooleanProperty("detectKillaura", antiCheat::isDetectKillaura, antiCheat::setDetectKillaura),
                    new IntProperty("vlKillaura", antiCheat::getVlKillaura, antiCheat::setVlKillaura, 5, 30),
                    new IntProperty("cdKillauraMs", antiCheat::getCooldownKillauraMs, antiCheat::setCooldownKillauraMs, 0, 3000),
                    new BooleanProperty("soundKillaura", antiCheat::isSoundKillaura, antiCheat::setSoundKillaura),
                    new BooleanProperty("flagWDRButton", antiCheat::isFlagWDRButton, antiCheat::setFlagWDRButton),
                    new BooleanProperty("debugMessages", antiCheat::isDebugMessages, antiCheat::setDebugMessages)
            );
        }

        Eagle eagle = (Eagle) moduleManager.get("eagle");
        if (eagle != null) {
            propertyManager.register(
                    eagle,
                    new BooleanProperty("enabled", eagle::enabled, eagle::setEnabled),
                    new BooleanProperty("blocksOnly", eagle::isBlocksOnly, eagle::setBlocksOnly),
                    new BooleanProperty("pitchCheck", eagle::isPitchCheck, eagle::setPitchCheck),
                    new IntProperty("pitchThreshold", eagle::getPitchThreshold, eagle::setPitchThreshold, 0, 90)
            );
        }

        BedTracker bedTracker = (BedTracker) moduleManager.get("bedtracker");
        if (bedTracker != null) {
            propertyManager.register(
                    bedTracker,
                    new BooleanProperty("enabled", bedTracker::enabled, bedTracker::setEnabled),
                    new IntProperty("maxDistance", bedTracker::getMaxDistance, bedTracker::setMaxDistance, 10, 100),
                    new IntProperty("alertCooldown", bedTracker::getAlertCooldownSeconds, bedTracker::setAlertCooldownSeconds, 5, 30),
                    new BooleanProperty("showHud", bedTracker::isShowHud, bedTracker::setShowHud),
                    new IntProperty("hudX", bedTracker::getHudX, bedTracker::setHudX, 0, 1000),
                    new IntProperty("hudY", bedTracker::getHudY, bedTracker::setHudY, 0, 1000),
                    new BooleanProperty("pingSound", bedTracker::isPingSound, bedTracker::setPingSound),
                    new BooleanProperty("forceScan (test)", bedTracker::isForceScan, bedTracker::setForceScan)
            );
        }

        StrengthESP strengthESP = (StrengthESP) moduleManager.get("strengthesp");
        if (strengthESP != null) {
            propertyManager.register(
                    strengthESP,
                    new BooleanProperty("enabled", strengthESP::enabled, strengthESP::setEnabled),
                    new IntProperty("durationMs", strengthESP::getDurationMs, strengthESP::setDurationMs, 1000, 10000),
                    new IntProperty("red", strengthESP::getRed, strengthESP::setRed, 0, 255),
                    new IntProperty("green", strengthESP::getGreen, strengthESP::setGreen, 0, 255),
                    new IntProperty("blue", strengthESP::getBlue, strengthESP::setBlue, 0, 255),
                    new IntProperty("alpha", strengthESP::getAlpha, strengthESP::setAlpha, 0, 255)
            );
        }

        PlayerESP playerESP = (PlayerESP) moduleManager.get("playeresp");
        if (playerESP != null) {
            propertyManager.register(
                    playerESP,
                    new BooleanProperty("enabled", playerESP::enabled, playerESP::setEnabled),
                    new IntProperty("red", playerESP::getRed, playerESP::setRed, 0, 255),
                    new IntProperty("green", playerESP::getGreen, playerESP::setGreen, 0, 255),
                    new IntProperty("blue", playerESP::getBlue, playerESP::setBlue, 0, 255),
                    new IntProperty("alpha", playerESP::getAlpha, playerESP::setAlpha, 0, 255),
                    new BooleanProperty("showTeammates", playerESP::isShowTeammates, playerESP::setShowTeammates),
                    new BooleanProperty("filledBox", playerESP::isFilledBox, playerESP::setFilledBox),
                    new IntProperty("filledAlpha", playerESP::getFilledAlpha, playerESP::setFilledAlpha, 0, 120),
                    new FloatProperty("lineWidth", playerESP::getLineWidth, playerESP::setLineWidth, 0.5F, 5.0F)
            );
        }

        DynamicIsland dynamicIsland = (DynamicIsland) moduleManager.get("dynamicisland");
        if (dynamicIsland != null) {
            propertyManager.register(
                    dynamicIsland,
                    new BooleanProperty("enabled", dynamicIsland::enabled, dynamicIsland::setEnabled),
                    new IntProperty("offsetY", dynamicIsland::getOffsetY, dynamicIsland::setOffsetY, 0, 250),
                    new IntProperty("durationMs", dynamicIsland::getDurationMs, dynamicIsland::setDurationMs, 800, 8000),
                    new IntProperty("maxVisible", dynamicIsland::getMaxVisible, dynamicIsland::setMaxVisible, 1, 4),
                    new IntProperty("bgAlpha", dynamicIsland::getBgAlpha, dynamicIsland::setBgAlpha, 0, 255),
                    new BooleanProperty("shadowEnabled", dynamicIsland::isShadowEnabled, dynamicIsland::setShadowEnabled),
                    new TextProperty("customText", dynamicIsland::getCustomText, dynamicIsland::setCustomText, "customText"),
                    new ModeProperty(
                            "idleTextMode",
                            dynamicIsland::getIdleTextMode,
                            dynamicIsland::setIdleTextMode,
                            "custom_only",
                            "custom_switch"
                    )
            );
        }

        Cape cape = (Cape) moduleManager.get("cape");
        if (cape != null) {
            propertyManager.register(
                    cape,
                    new BooleanProperty("enabled", cape::enabled, cape::setEnabled),
                    new ModeProperty(
                            "selectedCape",
                            cape::getSelectedCape,
                            cape::setSelectedCape,
                            Cape.CAPE_OPTIONS.toArray(new String[0])
                    )
            );
        }

        ArrayList arrayList = (ArrayList) moduleManager.get("arraylist");
        if (arrayList != null) {
            propertyManager.register(
                    arrayList,
                    new BooleanProperty("enabled", arrayList::enabled, arrayList::setEnabled),
                    new ModeProperty("colorMode", arrayList::getColorMode, arrayList::setColorMode, "Static", "Rainbow", "Category"),
                    new IntProperty("bgOpacity", arrayList::getBgOpacity, arrayList::setBgOpacity, 0, 255),
                    new IntProperty("yOffset", arrayList::getYOffset, arrayList::setYOffset, 0, 500)
            );
        }

        GlowTest glowTest = (GlowTest) moduleManager.get("glowtest");
        if (glowTest != null) {
            propertyManager.register(
                    glowTest,
                    new BooleanProperty("enabled", glowTest::enabled, glowTest::setEnabled),
                    new IntProperty("panelX", glowTest::getPanelX, glowTest::setPanelX, 0, 2000),
                    new IntProperty("panelY", glowTest::getPanelY, glowTest::setPanelY, 0, 2000),
                    new IntProperty("panelWidth", glowTest::getPanelWidth, glowTest::setPanelWidth, 32, 600),
                    new IntProperty("panelHeight", glowTest::getPanelHeight, glowTest::setPanelHeight, 24, 600),
                    new FloatProperty("cornerRadius", glowTest::getCornerRadius, glowTest::setCornerRadius, 0.0f, 64.0f),
                    new FloatProperty("glowBlurRadius", glowTest::getGlowBlurRadius, glowTest::setGlowBlurRadius, 1.0f, 128.0f),
                    new FloatProperty("glowExposure", glowTest::getGlowExposure, glowTest::setGlowExposure, 0.1f, 16.0f),
                    new IntProperty("glowRed", glowTest::getGlowRed, glowTest::setGlowRed, 0, 255),
                    new IntProperty("glowGreen", glowTest::getGlowGreen, glowTest::setGlowGreen, 0, 255),
                    new IntProperty("glowBlue", glowTest::getGlowBlue, glowTest::setGlowBlue, 0, 255),
                    new IntProperty("panelBgAlpha", glowTest::getPanelBgAlpha, glowTest::setPanelBgAlpha, 0, 255)
            );
        }

        configStore.loadInto(moduleManager, propertyManager);

        CommandManager commandManager = new CommandManager(moduleManager, configStore);
        commandManager.register(new HelpCommand(commandManager));
        commandManager.register(new ListCommand(moduleManager));
        commandManager.register(new ToggleCommand(moduleManager, configStore, propertyManager));
        commandManager.register(new PrefixCommand(configStore));
        commandManager.register(new ModuleCommand(moduleManager, propertyManager));
        commandManager.register(new AntiCheatInfoCommand(antiCheatModule));

        ModContext.setCommandManager(commandManager);
        ModContext.setModuleManager(moduleManager);

        Path newConfigDir = Paths.get("run", ".weave", "mods", "lchax");
        ModLogger.init(newConfigDir);

        ConfigManager newConfigManager = new ConfigManager(newConfigDir, moduleManager, propertyManager, keyBindingManager);

        try {
            newConfigManager.load("default").get();
        } catch (Exception e) {
            ModLogger.info("No default profile found or failed to load, starting fresh.");
        }
        if (keyBindingManager.getBoundKey("clickgui") == null) {
            keyBindingManager.bind("clickgui", Keyboard.KEY_RSHIFT, false);
        }

        commandManager.register(new com.example.mod.command.commands.BindCommand(moduleManager, keyBindingManager));
        commandManager.register(new com.example.mod.command.commands.ConfigCommand(newConfigManager));

        EventBus.subscribe(KeyboardEvent.class, (e) -> {
            if (e.getKeyState()) {
                int keyCode = e.getKeyCode();
                String boundModule = keyBindingManager.getBoundModule(keyCode);
                if (boundModule != null) {
                    Module m = moduleManager.get(boundModule);
                    if (m != null) {
                        m.setEnabled(!m.enabled());
                        newConfigManager.save("default");
                    }
                }
            }
        });
    }

    @Override public void preInit(@NotNull Instrumentation instrumentation) {
        System.out.println("This message is printed before Minecraft initialises");
    }
}
