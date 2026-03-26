package com.example.mod.module.modules.advanced;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.Tickable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class AntiBot extends BaseModule implements Tickable {
    private static final Map<UUID, Long> tabJoinTimes = new HashMap<>();
    private static final Set<UUID> currentTabList = new HashSet<>();

    private String mode = "Hypixel"; // Hypixel, Universal

    public AntiBot() {
        super("antibot", "Excludes bots from modules.", Category.ADVANCED, true);
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public static void updateTabList() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.getNetHandler() == null) return;
        
        Set<UUID> newTabList = new HashSet<>();
        for (NetworkPlayerInfo info : mc.getNetHandler().getPlayerInfoMap()) {
            newTabList.add(info.getGameProfile().getId());
        }

        long now = System.currentTimeMillis();

        for (UUID id : newTabList) {
            tabJoinTimes.putIfAbsent(id, now);
        }

        tabJoinTimes.keySet().removeIf(id -> !newTabList.contains(id));

        currentTabList.clear();
        currentTabList.addAll(newTabList);
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if ("Universal".equalsIgnoreCase(this.mode)) {
            updateTabList();
        }
    }

    public static boolean isBot(EntityPlayer player) {
        if (player == null) return true;

        // AntiBot is assumed to be running its logic through an instance config,
        // but since we need a static check for other modules without hard dependency passing:
        // We will fetch the mode dynamically or just apply strict rules.
        com.example.mod.module.Module mod = com.example.mod.ModContext.getModuleManager().get("antibot");
        if (mod == null || !mod.enabled()) {
            return false;
        }

        String currentMode = ((AntiBot) mod).getMode();
        UUID id = player.getUniqueID();

        if ("Universal".equalsIgnoreCase(currentMode)) {
            if (!currentTabList.contains(id)) return true;

            Long joinTime = tabJoinTimes.get(id);
            if (joinTime == null) return true;

            long timeInTab = System.currentTimeMillis() - joinTime;
            return (timeInTab < 10000L); // Needs to be in tab for 10s to be verified
        }
        
        if ("Hypixel".equalsIgnoreCase(currentMode)) {
            if (id.version() != 1 && id.version() != 4) {
                return true;
            }
            return false;
        }

        return true;
    }

    public static void clear() {
        currentTabList.clear();
        tabJoinTimes.clear();
    }
}