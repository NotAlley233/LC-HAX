package com.example.mod.command.commands;

import com.example.mod.command.Command;
import com.example.mod.module.Module;
import com.example.mod.module.ModuleManager;
import com.example.mod.property.Property;
import com.example.mod.property.PropertyManager;
import com.example.mod.property.properties.BooleanProperty;
import com.example.mod.util.ChatUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * myau-style: `.ModuleName [property] [value...]`
 */
public class ModuleCommand implements Command {
    private final ModuleManager moduleManager;
    private final PropertyManager propertyManager;
    private final String clientName;

    public ModuleCommand(ModuleManager moduleManager, PropertyManager propertyManager, String clientName) {
        this.moduleManager = moduleManager;
        this.propertyManager = propertyManager;
        this.clientName = clientName;
    }

    @Override
    public List<String> names() {
        return moduleManager.all().stream().map(Module::name).collect(Collectors.toList());
    }

    @Override
    public void runCommand(ArrayList<String> args) {
        // args[0] is the module name root
        Module module = moduleManager.get(args.get(0));
        if (module == null) {
            ChatUtil.sendFormatted(String.format("%sModule not found (&o%s&r)&r", clientName, args.get(0)));
            return;
        }

        if (args.size() >= 2) {
            Property<?> property = propertyManager.getProperty(module, args.get(1));
            if (property == null) {
                ChatUtil.sendFormatted(String.format("%s%s has no property &o%s&r", clientName, module.name(), args.get(1)));
                return;
            }

            boolean isBool = property instanceof BooleanProperty;
            if (args.size() < 3 && !isBool) {
                ChatUtil.sendFormatted(
                        String.format(
                                "%s%s: &o%s&r is set to %s&r (%s)&r",
                                clientName,
                                module.name(),
                                property.getName(),
                                property.formatValue(),
                                property.getValuePrompt()
                        )
                );
                return;
            }

            String newValue = args.size() < 3 ? null : String.join(" ", args.subList(2, args.size()));
            try {
                if (property.parseString(newValue)) {
                    ChatUtil.sendFormatted(
                            String.format("%s%s: &o%s&r has been set to %s&r", clientName, module.name(), property.getName(), property.formatValue())
                    );
                    return;
                }
            } catch (Exception ignored) {
            }

            ChatUtil.sendFormatted(
                    String.format("%sInvalid value for property &o%s&r (%s)&r", clientName, property.getName(), property.getValuePrompt())
            );
            return;
        }

        List<Property<?>> props = propertyManager.getProperties(module);
        if (props != null) {
            List<Property<?>> visible = props.stream().filter(Property::isVisible).collect(Collectors.toList());
            if (!visible.isEmpty()) {
                ChatUtil.sendFormatted(String.format("%s%s:&r", clientName, module.name()));
                for (Property<?> p : visible) {
                    ChatUtil.sendFormatted(String.format("&7»&r %s: %s&r", p.getName(), p.formatValue()));
                }
                return;
            }
        }

        ChatUtil.sendFormatted(String.format("%s%s has no properties&r", clientName, module.name()));
    }
}

