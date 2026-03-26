package com.example.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

public class NameUtil {
    public static String getTabDisplayName(String playerName) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.theWorld.getScoreboard() == null) {
            return playerName;
        }

        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScorePlayerTeam team = scoreboard.getPlayersTeam(playerName);

        if (team != null) {
            return team.getColorPrefix() + playerName + team.getColorSuffix();
        }

        return playerName;
    }
}