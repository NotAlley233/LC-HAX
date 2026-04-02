package com.example.mod.module.modules.advanced;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.Tickable;
import com.example.mod.util.ChatUtil;
import com.example.mod.util.NameUtil;
import com.example.mod.util.TeamUtil;
import com.example.mod.util.anticheat.AntiCheatCheckIds;
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
    private final Map<String, Integer> flagCounters = new HashMap<>();

    private boolean detectNoSlow = true;
    private boolean detectAutoBlock = true;
    private boolean detectEagle = true;
    private boolean detectScaffold = false;
    private boolean detectTower = false;
    private boolean detectKillaura = false;

    private int vlNoSlow = 10;
    private int vlAutoBlock = 10;
    private int vlEagle = 5;
    private int vlScaffold = 15;
    private int vlTower = 10;
    private int vlKillaura = 10;

    private int cdNoSlow = 2000;
    private int cdAutoBlock = 2000;
    private int cdEagle = 2000;
    private int cdScaffold = 2000;
    private int cdTower = 2000;
    private int cdKillaura = 2000;

    private boolean soundNoSlow = true;
    private boolean soundAutoBlock = true;
    private boolean soundEagle = true;
    private boolean soundScaffold = true;
    private boolean soundTower = true;
    private boolean soundKillaura = true;

    private boolean flagWDRButton = true;
    private boolean debugMessages = false;

    private Object lastWorldRef;

    public AntiCheat() {
        super("anticheat", "Detects suspicious combat behavior (Starfish-style checks).", Category.ADVANCED, false);
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

        long nowMs = System.currentTimeMillis();

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
            data.runChecks(player, this, nowMs);

            tryAlert(name, data, AntiCheatCheckIds.NO_SLOW_A, detectNoSlow, vlNoSlow, cdNoSlow, soundNoSlow, nowMs);
            tryAlert(name, data, AntiCheatCheckIds.AUTO_BLOCK_A, detectAutoBlock, vlAutoBlock, cdAutoBlock, soundAutoBlock, nowMs);
            tryAlert(name, data, AntiCheatCheckIds.EAGLE_A, detectEagle, vlEagle, cdEagle, soundEagle, nowMs);
            tryAlert(name, data, AntiCheatCheckIds.SCAFFOLD_A, detectScaffold, vlScaffold, cdScaffold, soundScaffold, nowMs);
            tryAlert(name, data, AntiCheatCheckIds.TOWER_A, detectTower, vlTower, cdTower, soundTower, nowMs);
            tryAlert(name, data, AntiCheatCheckIds.KILLAURA_B, detectKillaura, vlKillaura, cdKillaura, soundKillaura, nowMs);
        }
    }

    private void tryAlert(String playerName, AntiCheatData data, String checkId,
                          boolean enabled, int vl, int cooldownMs, boolean sound, long nowMs) {
        if (!enabled) {
            return;
        }
        if (!data.starfishState.shouldAlert(checkId, vl, cooldownMs, nowMs)) {
            return;
        }
        int vlShown = data.starfishState.violations.getOrDefault(checkId, 0);
        sendFlagMessage(playerName, checkId, vlShown, sound);
        data.starfishState.markAlert(checkId, nowMs);
        resetCheckStateAfterFlag(data, checkId);
    }

    private void resetCheckStateAfterFlag(AntiCheatData data, String checkId) {
        if (AntiCheatCheckIds.AUTO_BLOCK_A.equals(checkId)) {
            data.autoBlockACheck.reset();
        }
        if (AntiCheatCheckIds.KILLAURA_B.equals(checkId)) {
            data.killauraBCheck.reset();
        }
    }

    @Override
    protected void onDisable() {
        clear();
    }

    private void sendFlagMessage(String playerName, String checkId, int vl, boolean playSound) {
        String name = NameUtil.getTabDisplayName(playerName);
        String msg = name + "§7 flagged §5" + checkId + " §8(§7VL: " + vl + "§8)";

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

        if (playSound && mc.thePlayer != null) {
            mc.thePlayer.playSound("note.pling", 1.0F, 1.6F);
        }

        flagCounters.put(checkId, flagCounters.getOrDefault(checkId, 0) + 1);
    }

    public void clear() {
        dataByPlayer.clear();
    }

    public static int clampVl(int v) {
        int[] allowed = {5, 10, 15, 20, 30};
        return nearest(allowed, v);
    }

    public static int clampCooldownMs(int v) {
        int[] allowed = {0, 1000, 2000, 3000};
        return nearest(allowed, v);
    }

    private static int nearest(int[] allowed, int v) {
        int best = allowed[0];
        int bestDiff = Math.abs(v - best);
        for (int x : allowed) {
            int d = Math.abs(v - x);
            if (d < bestDiff) {
                best = x;
                bestDiff = d;
            }
        }
        return best;
    }

    public boolean isDetectNoSlow() {
        return detectNoSlow;
    }

    public void setDetectNoSlow(boolean detectNoSlow) {
        this.detectNoSlow = detectNoSlow;
    }

    public boolean isDetectAutoBlock() {
        return detectAutoBlock;
    }

    public void setDetectAutoBlock(boolean detectAutoBlock) {
        this.detectAutoBlock = detectAutoBlock;
    }

    public boolean isDetectEagle() {
        return detectEagle;
    }

    public void setDetectEagle(boolean detectEagle) {
        this.detectEagle = detectEagle;
    }

    public boolean isDetectScaffold() {
        return detectScaffold;
    }

    public void setDetectScaffold(boolean detectScaffold) {
        this.detectScaffold = detectScaffold;
    }

    public boolean isDetectTower() {
        return detectTower;
    }

    public void setDetectTower(boolean detectTower) {
        this.detectTower = detectTower;
    }

    public boolean isDetectKillaura() {
        return detectKillaura;
    }

    public void setDetectKillaura(boolean detectKillaura) {
        this.detectKillaura = detectKillaura;
    }

    public int getVlNoSlow() {
        return vlNoSlow;
    }

    public void setVlNoSlow(int vlNoSlow) {
        this.vlNoSlow = clampVl(vlNoSlow);
    }

    public int getVlAutoBlock() {
        return vlAutoBlock;
    }

    public void setVlAutoBlock(int vlAutoBlock) {
        this.vlAutoBlock = clampVl(vlAutoBlock);
    }

    public int getVlEagle() {
        return vlEagle;
    }

    public void setVlEagle(int vlEagle) {
        this.vlEagle = clampVl(vlEagle);
    }

    public int getVlScaffold() {
        return vlScaffold;
    }

    public void setVlScaffold(int vlScaffold) {
        this.vlScaffold = clampVl(vlScaffold);
    }

    public int getVlTower() {
        return vlTower;
    }

    public void setVlTower(int vlTower) {
        this.vlTower = clampVl(vlTower);
    }

    public int getVlKillaura() {
        return vlKillaura;
    }

    public void setVlKillaura(int vlKillaura) {
        this.vlKillaura = clampVl(vlKillaura);
    }

    public int getCooldownNoSlowMs() {
        return cdNoSlow;
    }

    public void setCooldownNoSlowMs(int cdNoSlow) {
        this.cdNoSlow = clampCooldownMs(cdNoSlow);
    }

    public int getCooldownAutoBlockMs() {
        return cdAutoBlock;
    }

    public void setCooldownAutoBlockMs(int cdAutoBlock) {
        this.cdAutoBlock = clampCooldownMs(cdAutoBlock);
    }

    public int getCooldownEagleMs() {
        return cdEagle;
    }

    public void setCooldownEagleMs(int cdEagle) {
        this.cdEagle = clampCooldownMs(cdEagle);
    }

    public int getCooldownScaffoldMs() {
        return cdScaffold;
    }

    public void setCooldownScaffoldMs(int cdScaffold) {
        this.cdScaffold = clampCooldownMs(cdScaffold);
    }

    public int getCooldownTowerMs() {
        return cdTower;
    }

    public void setCooldownTowerMs(int cdTower) {
        this.cdTower = clampCooldownMs(cdTower);
    }

    public int getCooldownKillauraMs() {
        return cdKillaura;
    }

    public void setCooldownKillauraMs(int cdKillaura) {
        this.cdKillaura = clampCooldownMs(cdKillaura);
    }

    public boolean isSoundNoSlow() {
        return soundNoSlow;
    }

    public void setSoundNoSlow(boolean soundNoSlow) {
        this.soundNoSlow = soundNoSlow;
    }

    public boolean isSoundAutoBlock() {
        return soundAutoBlock;
    }

    public void setSoundAutoBlock(boolean soundAutoBlock) {
        this.soundAutoBlock = soundAutoBlock;
    }

    public boolean isSoundEagle() {
        return soundEagle;
    }

    public void setSoundEagle(boolean soundEagle) {
        this.soundEagle = soundEagle;
    }

    public boolean isSoundScaffold() {
        return soundScaffold;
    }

    public void setSoundScaffold(boolean soundScaffold) {
        this.soundScaffold = soundScaffold;
    }

    public boolean isSoundTower() {
        return soundTower;
    }

    public void setSoundTower(boolean soundTower) {
        this.soundTower = soundTower;
    }

    public boolean isSoundKillaura() {
        return soundKillaura;
    }

    public void setSoundKillaura(boolean soundKillaura) {
        this.soundKillaura = soundKillaura;
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

    public int getFlagCount(String checkId) {
        return flagCounters.getOrDefault(checkId, 0);
    }

    public int getTotalFlags() {
        int total = 0;
        for (Integer value : flagCounters.values()) {
            total += value;
        }
        return total;
    }
}
