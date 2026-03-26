package com.example.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public class TeamUtil {
    private static boolean spectator = false;
    private static long spectatorTime = 0L;

    public static boolean inSpectator() {
        return spectator;
    }

    public static void updateSpectatorState() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;
        
        long now = System.currentTimeMillis();
        if (mc.thePlayer.capabilities.isFlying) { // basic spectator check fallback
            spectator = true;
            spectatorTime = now;
        } else {
            spectator = (now - spectatorTime < 8000L);
        }
    }

    public static boolean ignoreSelf(String playerName) {
        Minecraft mc = Minecraft.getMinecraft();
        return (mc.thePlayer != null && playerName != null && playerName.equalsIgnoreCase(mc.thePlayer.getName()));
    }

    public static boolean ignoreTeam(String playerName) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || playerName == null) return false;

        EntityPlayer target = mc.theWorld.playerEntities.stream()
                .filter(p -> p.getName().equalsIgnoreCase(playerName))
                .findFirst()
                .orElse(null);

        if (target == null) return false;
        if (target.capabilities.isFlying) return true; // ignore spectators

        try {
            if (mc.thePlayer.isOnSameTeam((EntityLivingBase) target)) return true;

            String selfFormatted = mc.thePlayer.getDisplayName().getFormattedText();
            String targetFormatted = target.getDisplayName().getFormattedText();

            String selfColor = getMostFrequentColor(selfFormatted);
            String targetColor = getMostFrequentColor(targetFormatted);

            if (selfColor != null && selfColor.equals(targetColor)) {
                return true;
            }
        } catch (Exception ignored) {}

        return false;
    }

    private static String getMostFrequentColor(String text) {
        if (text == null) return null;

        Map<String, Integer> colorCounts = new HashMap<>();

        for (int i = 0; i < text.length() - 1; i++) {
            char c = text.charAt(i);
            if (c == '§') {
                char code = text.charAt(i + 1);
                if ((code >= '0' && code <= '9') || (code >= 'a' && code <= 'f')) {
                    String currentColor = "§" + code;
                    colorCounts.put(currentColor, colorCounts.getOrDefault(currentColor, 0) + 1);
                }
            }
        }

        return colorCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}