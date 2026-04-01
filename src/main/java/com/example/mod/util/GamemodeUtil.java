package com.example.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

public class GamemodeUtil {
    private static boolean inBedwars = false;
    private static boolean inSkywars = false;

    public static boolean isBedwars() {
        return inBedwars;
    }

    public static boolean isSkywars() {
        return inSkywars;
    }

    public static void updateGamemode() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.theWorld.getScoreboard() == null) {
            inBedwars = false;
            inSkywars = false;
            return;
        }

        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);

        if (objective != null) {
            String title = objective.getDisplayName();
            String strippedTitle = title.replaceAll("§.", "").toLowerCase();
            inBedwars = strippedTitle.contains("bed wars") || strippedTitle.contains("bedwars");
            inSkywars = strippedTitle.contains("skywars") || strippedTitle.contains("sky wars");
        } else {
            inBedwars = false;
            inSkywars = false;
        }
    }
}
