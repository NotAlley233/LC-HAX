package com.example.mod.module.modules.advanced;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.Tickable;
import com.example.mod.util.ChatUtil;
import com.example.mod.util.NameUtil;
import com.example.mod.util.TeamUtil;
import com.example.mod.util.anticheat.AntiCheatData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AntiCheat extends BaseModule implements Tickable {
    private final Map<String, AntiCheatData> dataByPlayer = new HashMap<>();
    private final Map<String, Map<String, Integer>> violationLevels = new HashMap<>();
    private final Map<String, Integer> flagCounters = new HashMap<>();

    private int violationLevel = 3;
    private boolean detectAutoBlock = true;
    private boolean detectNoSlow = true;
    private boolean detectLegitScaffold = true;
    private boolean detectKillaura = true;
    private boolean flagPingSound = true;
    private boolean flagWDRButton = true;
    private boolean debugMessages = false;

    private Object lastWorldRef;

    public AntiCheat() {
        super("anticheat", "Detects suspicious combat behavior around you.", Category.ADVANCED, false);
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) {
            clear();
            return;
        }
        if (lastWorldRef != mc.theWorld) {
            clear();
            lastWorldRef = mc.theWorld;
        }

        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == null || player == mc.thePlayer) {
                continue;
            }
            String name = player.getName();
            if (name == null || name.trim().isEmpty()) {
                continue;
            }
            if (TeamUtil.ignoreTeam(name)) {
                continue;
            }

            AntiCheatData data = dataByPlayer.computeIfAbsent(name.toLowerCase(Locale.ROOT), k -> new AntiCheatData());
            data.anticheatCheck(player, this);

            if (data.failedAutoBlock() && incrementViolation(name, "AutoBlock")) {
                sendFlagMessage(name, "AutoBlock");
                data.autoBlockCheck.reset();
            }
            if (data.failedNoSlow() && incrementViolation(name, "NoSlow")) {
                sendFlagMessage(name, "NoSlow");
                data.noSlowCheck.reset();
            }
            if (data.failedLegitScaffold() && incrementViolation(name, "Legit Scaffold")) {
                sendFlagMessage(name, "Legit Scaffold");
                data.legitScaffoldCheck.reset();
            }
            if (data.failedKillauraB() && incrementViolation(name, "Killaura")) {
                sendFlagMessage(name, "Killaura");
                data.killauraBCheck.reset();
            }
        }
    }

    @Override
    protected void onDisable() {
        clear();
    }

    private boolean incrementViolation(String playerName, String checkType) {
        String key = playerName.toLowerCase(Locale.ROOT);
        Map<String, Integer> playerViolations = violationLevels.computeIfAbsent(key, k -> new HashMap<>());
        int newLevel = playerViolations.getOrDefault(checkType, 0) + 1;
        playerViolations.put(checkType, newLevel);
        if (newLevel >= violationLevel) {
            playerViolations.put(checkType, 0);
            return true;
        }
        return false;
    }

    private void sendFlagMessage(String playerName, String checkType) {
        String name = NameUtil.getTabDisplayName(playerName);
        String msg = name + "§7 failed §c" + checkType;

        Minecraft mc = Minecraft.getMinecraft();
        if (flagWDRButton && mc.thePlayer != null) {
            ChatComponentText message = new ChatComponentText(msg + " ");
            ChatComponentText wdrButton = new ChatComponentText("§7[§bWDR§7]");
            wdrButton.setChatStyle(new ChatStyle()
                    .setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/wdr " + playerName))
                    .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (IChatComponent) new ChatComponentText("§3点击举报该玩家"))));
            message.appendSibling(wdrButton);
            mc.thePlayer.addChatMessage(message);
        } else {
            ChatUtil.sendFormatted(msg);
        }

        if (flagPingSound && mc.thePlayer != null) {
            mc.thePlayer.playSound("note.pling", 1.0F, 1.6F);
        }

        flagCounters.put(checkType, flagCounters.getOrDefault(checkType, 0) + 1);
    }

    public void clear() {
        dataByPlayer.clear();
        violationLevels.clear();
    }

    public int getViolationLevel() {
        return violationLevel;
    }

    public void setViolationLevel(int violationLevel) {
        this.violationLevel = Math.max(1, Math.min(20, violationLevel));
    }

    public boolean isDetectAutoBlock() {
        return detectAutoBlock;
    }

    public void setDetectAutoBlock(boolean detectAutoBlock) {
        this.detectAutoBlock = detectAutoBlock;
    }

    public boolean isDetectNoSlow() {
        return detectNoSlow;
    }

    public void setDetectNoSlow(boolean detectNoSlow) {
        this.detectNoSlow = detectNoSlow;
    }

    public boolean isDetectLegitScaffold() {
        return detectLegitScaffold;
    }

    public void setDetectLegitScaffold(boolean detectLegitScaffold) {
        this.detectLegitScaffold = detectLegitScaffold;
    }

    public boolean isDetectKillaura() {
        return detectKillaura;
    }

    public void setDetectKillaura(boolean detectKillaura) {
        this.detectKillaura = detectKillaura;
    }

    public boolean isFlagPingSound() {
        return flagPingSound;
    }

    public void setFlagPingSound(boolean flagPingSound) {
        this.flagPingSound = flagPingSound;
    }

    public boolean isFlagWDRButton() {
        return flagWDRButton;
    }

    public void setFlagWDRButton(boolean flagWDRButton) {
        this.flagWDRButton = flagWDRButton;
    }

    public boolean isDebugMessages() {
        return debugMessages;
    }

    public void setDebugMessages(boolean debugMessages) {
        this.debugMessages = debugMessages;
    }

    public int getTrackedPlayers() {
        return dataByPlayer.size();
    }

    public int getFlagCount(String check) {
        return flagCounters.getOrDefault(check, 0);
    }

    public int getTotalFlags() {
        int total = 0;
        for (Integer value : flagCounters.values()) {
            total += value;
        }
        return total;
    }
}
