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
import com.example.mod.module.modules.Cape;
import com.example.mod.module.modules.ClickGUIModule;
import com.example.mod.module.modules.PrefixModule;
import com.example.mod.module.modules.RightClicker;
import com.example.mod.module.modules.advanced.AntiBot;
import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.module.modules.advanced.Eagle;
import com.example.mod.module.modules.advanced.NoHitDelay;
import com.example.mod.module.modules.bedwars.BedTracker;
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
        moduleManager.register(new Cape());

        Path configPath = ConfigStore.defaultConfigPath("lchax.json");
        ConfigStore configStore = new ConfigStore(configPath);

        PropertyManager propertyManager = new PropertyManager();
        moduleManager.register(new ClickGUIModule(moduleManager, propertyManager));
        ClickGUIModule clickGUIModule = (ClickGUIModule) moduleManager.get("clickgui");
        if (clickGUIModule != null) {
            propertyManager.register(
                    clickGUIModule,
                    new BooleanProperty("enabled", clickGUIModule::enabled, clickGUIModule::setEnabled),
                    new BooleanProperty("productFont", clickGUIModule::isProductFont, clickGUIModule::setProductFont)
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
                    new IntProperty("violationLevel", antiCheat::getViolationLevel, antiCheat::setViolationLevel, 1, 20),
                    new BooleanProperty("detectAutoBlock", antiCheat::isDetectAutoBlock, antiCheat::setDetectAutoBlock),
                    new BooleanProperty("detectNoSlow", antiCheat::isDetectNoSlow, antiCheat::setDetectNoSlow),
                    new BooleanProperty("detectLegitScaffold", antiCheat::isDetectLegitScaffold, antiCheat::setDetectLegitScaffold),
                    new BooleanProperty("detectKillaura", antiCheat::isDetectKillaura, antiCheat::setDetectKillaura),
                    new BooleanProperty("flagPingSound", antiCheat::isFlagPingSound, antiCheat::setFlagPingSound),
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

        configStore.loadInto(moduleManager, propertyManager);

        CommandManager commandManager = new CommandManager(moduleManager, configStore);
        commandManager.register(new HelpCommand(commandManager));
        commandManager.register(new ListCommand(moduleManager));
        commandManager.register(new ToggleCommand(moduleManager, configStore, propertyManager));
        commandManager.register(new PrefixCommand(configStore));
        commandManager.register(new ModuleCommand(moduleManager, propertyManager, ""));
        commandManager.register(new AntiCheatInfoCommand(antiCheatModule));

        ModContext.setCommandManager(commandManager);
        ModContext.setModuleManager(moduleManager);

        Path newConfigDir = Paths.get("run", ".weave", "mods", "lchax");
        ModLogger.init(newConfigDir);

        KeyBindingManager keyBindingManager = new KeyBindingManager();
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
