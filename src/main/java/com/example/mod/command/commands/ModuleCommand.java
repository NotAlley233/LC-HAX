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

    public ModuleCommand(ModuleManager moduleManager, PropertyManager propertyManager) {
        this.moduleManager = moduleManager;
        this.propertyManager = propertyManager;
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
            ChatUtil.sendPrefixedFormatted(String.format("&cModule not found (&o&e%s&c)&r", args.get(0)));
            return;
        }

        if (args.size() >= 2) {
            Property<?> property = propertyManager.getProperty(module, args.get(1));
            if (property == null) {
                ChatUtil.sendPrefixedFormatted(String.format("&b%s&c has no property &o&e%s&r", module.name(), args.get(1)));
                return;
            }

            boolean isBool = property instanceof BooleanProperty;
            if (args.size() < 3 && !isBool) {
                ChatUtil.sendPrefixedFormatted(
                        String.format(
                                "&b%s&7: &o&e%s&r &7is set to &a%s&r &8(&7%s&8)&r",
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
                    ChatUtil.sendPrefixedFormatted(
                            String.format("&b%s&7: &o&e%s&r &7has been set to &a%s&r", module.name(), property.getName(), property.formatValue())
                    );
                    return;
                }
            } catch (Exception ignored) {
            }

            ChatUtil.sendPrefixedFormatted(
                    String.format("&cInvalid value for property &o&e%s&r &8(&7%s&8)&r", property.getName(), property.getValuePrompt())
            );
            return;
        }

        List<Property<?>> props = propertyManager.getProperties(module);
        if (props != null) {
            List<Property<?>> visible = props.stream().filter(Property::isVisible).collect(Collectors.toList());
            if (!visible.isEmpty()) {
                ChatUtil.sendPrefixedFormatted(String.format("&b%s&7:&r", module.name()));
                for (Property<?> p : visible) {
                    ChatUtil.sendPrefixedFormatted(String.format("&7»&r &e%s&7: &a%s&r", p.getName(), p.formatValue()));
                }
                return;
            }
        }

        ChatUtil.sendPrefixedFormatted(String.format("&e%s &7has no properties&r", module.name()));
    }
}
