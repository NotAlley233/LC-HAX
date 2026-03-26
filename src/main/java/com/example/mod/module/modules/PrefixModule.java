package com.example.mod.module.modules;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.util.ChatUtil;

public class PrefixModule extends BaseModule {
    public PrefixModule(boolean defaultEnabled) {
        super("prefix", "Toggle the rainbow prefix for mod messages.", Category.UTILITY, defaultEnabled);
        // Ensure ChatUtil matches initial state.
        ChatUtil.setPrefixEnabled(defaultEnabled);
    }

    @Override
    protected void onEnable() {
        ChatUtil.setPrefixEnabled(true);
    }

    @Override
    protected void onDisable() {
        ChatUtil.setPrefixEnabled(false);
    }
}

