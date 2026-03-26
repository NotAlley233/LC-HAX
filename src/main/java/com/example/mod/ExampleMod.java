package com.example.mod;

import com.example.mod.command.CommandManager;
import com.example.mod.command.commands.HelpCommand;
import com.example.mod.command.commands.ListCommand;
import com.example.mod.command.commands.ModuleCommand;
import com.example.mod.command.commands.PrefixCommand;
import com.example.mod.command.commands.ToggleCommand;
import com.example.mod.config.ConfigManager;
import com.example.mod.config.ConfigStore;
import com.example.mod.core.ModLogger;
import com.example.mod.listener.RenderGameOverlayEventListener;
import com.example.mod.module.KeyBindingManager;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.module.modules.AutoClicker;
import com.example.mod.module.modules.ClickGUIModule;
import com.example.mod.module.modules.PrefixModule;
import com.example.mod.module.modules.advanced.AntiBot;
import com.example.mod.module.modules.bedwars.ArmorAlerts;
import com.example.mod.module.modules.bedwars.BedTracker;
import com.example.mod.module.modules.bedwars.ItemAlerts;
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

public class ExampleMod implements ModInitializer {
    @Override
    public void init() {
        System.out.println("Hello from ExampleMod!");

        EventBus.subscribe(new RenderGameOverlayEventListener());

        // ---- Command + Module system (client-side dot commands) ----
        ModuleManager moduleManager = new ModuleManager();
        // Prefix module must be registered before loading config so its enabled state is applied.
        moduleManager.register(new PrefixModule(true));
        moduleManager.register(new AutoClicker());
        moduleManager.register(new AntiBot());
        moduleManager.register(new ArmorAlerts());
        moduleManager.register(new BedTracker());
        moduleManager.register(new ItemAlerts());

        Path configPath = ConfigStore.defaultConfigPath("examplemod.json");
        ConfigStore configStore = new ConfigStore(configPath);

        // ---- myau-style Property system ----
        PropertyManager propertyManager = new PropertyManager();
        moduleManager.register(new ClickGUIModule(moduleManager, propertyManager));

        AutoClicker autoClicker = (AutoClicker) moduleManager.get("autoclicker");
        if (autoClicker != null) {
            propertyManager.register(
                    autoClicker,
                    new BooleanProperty("enabled", autoClicker::enabled, autoClicker::setEnabled),
                    new IntProperty("minCPS", autoClicker::getMinCPS, autoClicker::setMinCPS),
                    new IntProperty("maxCPS", autoClicker::getMaxCPS, autoClicker::setMaxCPS),
                    new BooleanProperty("blocks", autoClicker::isBlocks, autoClicker::setBlocks),
                    new BooleanProperty("breakBlocks", autoClicker::isBreakBlocks, autoClicker::setBreakBlocks)
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

        ArmorAlerts armorAlerts = (ArmorAlerts) moduleManager.get("armoralerts");
        if (armorAlerts != null) {
            propertyManager.register(
                    armorAlerts,
                    new BooleanProperty("enabled", armorAlerts::enabled, armorAlerts::setEnabled),
                    new BooleanProperty("armorPingSound", armorAlerts::isArmorPingSound, armorAlerts::setArmorPingSound),
                    new BooleanProperty("detectDiamondArmor", armorAlerts::isDetectDiamondArmor, armorAlerts::setDetectDiamondArmor),
                    new BooleanProperty("detectIronArmor", armorAlerts::isDetectIronArmor, armorAlerts::setDetectIronArmor),
                    new BooleanProperty("detectChainArmor", armorAlerts::isDetectChainArmor, armorAlerts::setDetectChainArmor)
            );
        }

        BedTracker bedTracker = (BedTracker) moduleManager.get("bedtracker");
        if (bedTracker != null) {
            propertyManager.register(
                    bedTracker,
                    new BooleanProperty("enabled", bedTracker::enabled, bedTracker::setEnabled),
                    new FloatProperty("hudScale", bedTracker::getHudScale, bedTracker::setHudScale),
                    new IntProperty("alertCooldownSeconds", bedTracker::getAlertCooldownSeconds, bedTracker::setAlertCooldownSeconds),
                    new IntProperty("maxDistance", bedTracker::getMaxDistance, bedTracker::setMaxDistance),
                    new BooleanProperty("pingSound", bedTracker::isPingSound, bedTracker::setPingSound),
                    new BooleanProperty("showHud", bedTracker::isShowHud, bedTracker::setShowHud),
                    new IntProperty("hudX", bedTracker::getHudX, bedTracker::setHudX),
                    new IntProperty("hudY", bedTracker::getHudY, bedTracker::setHudY),
                    new IntProperty("hudRed", bedTracker::getHudRed, bedTracker::setHudRed),
                    new IntProperty("hudGreen", bedTracker::getHudGreen, bedTracker::setHudGreen),
                    new IntProperty("hudBlue", bedTracker::getHudBlue, bedTracker::setHudBlue)
            );
        }

        ItemAlerts itemAlerts = (ItemAlerts) moduleManager.get("itemalerts");
        if (itemAlerts != null) {
            propertyManager.register(
                    itemAlerts,
                    new BooleanProperty("enabled", itemAlerts::enabled, itemAlerts::setEnabled),
                    new IntProperty("cooldownSeconds", itemAlerts::getCooldownSeconds, itemAlerts::setCooldownSeconds),
                    new ModeProperty("soundMode", itemAlerts::getSoundMode, itemAlerts::setSoundMode, "All", "Important", "None"),
                    new ModeProperty("distanceMode", itemAlerts::getDistanceMode, itemAlerts::setDistanceMode, "All", "Important", "None"),
                    new BooleanProperty("commonItems", itemAlerts::isCommonItems, itemAlerts::setCommonItems),
                    new BooleanProperty("importantItems", itemAlerts::isImportantItems, itemAlerts::setImportantItems),
                    new BooleanProperty("rotationItems", itemAlerts::isRotationItems, itemAlerts::setRotationItems),
                    new BooleanProperty("potions", itemAlerts::isPotions, itemAlerts::setPotions),
                    new BooleanProperty("pickaxes", itemAlerts::isPickaxes, itemAlerts::setPickaxes),
                    new BooleanProperty("explosives", itemAlerts::isExplosives, itemAlerts::setExplosives),
                    new BooleanProperty("bows", itemAlerts::isBows, itemAlerts::setBows),
                    new BooleanProperty("mobs", itemAlerts::isMobs, itemAlerts::setMobs)
            );
        }

        configStore.loadInto(moduleManager, propertyManager);

        CommandManager commandManager = new CommandManager(moduleManager, configStore);
        commandManager.register(new HelpCommand(commandManager));
        commandManager.register(new ListCommand(moduleManager));
        commandManager.register(new ToggleCommand(moduleManager, configStore, propertyManager));
        commandManager.register(new PrefixCommand(configStore));
        
        // This command dynamically handles settings for all registered modules based on moduleManager.all()
        commandManager.register(new ModuleCommand(moduleManager, propertyManager, ""));

        // Expose for mixins (chat interception).
        ModContext.setCommandManager(commandManager);
        ModContext.setModuleManager(moduleManager);

        // ---- New KeyBinding & Config System ----
        Path newConfigDir = Paths.get("run", ".weave", "mods", "examplemod");
        ModLogger.init(newConfigDir);
        
        KeyBindingManager keyBindingManager = new KeyBindingManager();
        ConfigManager newConfigManager = new ConfigManager(newConfigDir, moduleManager, propertyManager, keyBindingManager);
        
        // Try to load default profile if exists
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
