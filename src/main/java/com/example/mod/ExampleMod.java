package com.example.mod;

import com.example.mod.command.CommandManager;
import com.example.mod.command.commands.HelpCommand;
import com.example.mod.command.commands.ListCommand;
import com.example.mod.command.commands.ModuleCommand;
import com.example.mod.command.commands.PrefixCommand;
import com.example.mod.command.commands.ToggleCommand;
import com.example.mod.config.ConfigStore;
import com.example.mod.listener.RenderGameOverlayEventListener;
import com.example.mod.module.ModuleManager;
import com.example.mod.module.modules.ModuleA;
import com.example.mod.module.modules.PrefixModule;
import com.example.mod.property.PropertyManager;
import com.example.mod.property.properties.BooleanProperty;
import com.example.mod.property.properties.TextProperty;
import com.example.mod.util.ChatUtil;
import net.weavemc.api.KeyboardEvent;
import net.weavemc.api.ModInitializer;
import net.weavemc.api.event.EventBus;
import org.jetbrains.annotations.NotNull;

import java.lang.instrument.Instrumentation;
import java.nio.file.Path;

public class ExampleMod implements ModInitializer {
    @Override
    public void init() {
        System.out.println("Hello from ExampleMod!");

        EventBus.subscribe(new RenderGameOverlayEventListener());

        // ---- Command + Module system (client-side dot commands) ----
        ModuleManager moduleManager = new ModuleManager();
        moduleManager.register(new ModuleA());
        // Prefix module must be registered before loading config so its enabled state is applied.
        moduleManager.register(new PrefixModule(true));

        Path configPath = ConfigStore.defaultConfigPath("examplemod.properties");
        ConfigStore configStore = new ConfigStore(configPath);
        configStore.loadInto(moduleManager);
        ChatUtil.setPrefixText(configStore.getPrefixText());

        // ---- myau-style Property system ----
        PropertyManager propertyManager = new PropertyManager();

        ModuleA moduleA = (ModuleA) moduleManager.get("a");
        if (moduleA != null) {
            propertyManager.register(
                    moduleA,
                    new BooleanProperty("enabled", moduleA::enabled, moduleA::setEnabled)
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
                                configStore.setPrefixText(t);
                            },
                            "text"
                    )
            );
        }

        CommandManager commandManager = new CommandManager(moduleManager, configStore);
        commandManager.register(new HelpCommand(commandManager));
        commandManager.register(new ListCommand(moduleManager));
        commandManager.register(new ToggleCommand(moduleManager, configStore));
        commandManager.register(new PrefixCommand(configStore));
        commandManager.register(new ModuleCommand(moduleManager, propertyManager, ""));

        // Expose for mixins (chat interception).
        ModContext.setCommandManager(commandManager);

        EventBus.subscribe(KeyboardEvent.class, (e) -> {
            int keyCode = e.getKeyCode();
            System.out.println("Key pressed: " + keyCode);
        });
    }

    @Override public void preInit(@NotNull Instrumentation instrumentation) {
        System.out.println("This message is printed before Minecraft initialises");
    }
}