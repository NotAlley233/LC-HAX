package com.example.mod.module.modules.bedwars;

import com.example.mod.event.ChatReceiveDispatcher;
import com.example.mod.event.ChatReceiveEvent;
import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.OverlayRenderable;
import com.example.mod.module.Tickable;
import com.example.mod.module.modules.advanced.AntiBot;
import com.example.mod.util.ChatUtil;
import com.example.mod.util.GamemodeUtil;
import com.example.mod.util.NameUtil;
import com.example.mod.util.TeamUtil;
import net.minecraft.block.BlockBed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class BedTracker extends BaseModule implements Tickable, OverlayRenderable {
    private BlockPos bedPos = null;
    private long bedScanTime = 0L;
    private long startTime = 0L;
    private boolean rangeAlert = false;
    private final Map<UUID, Long> lastAlertTimes = new HashMap<>();
    private final Consumer<ChatReceiveEvent> chatListener = this::onChatReceived;

    private float hudScale = 1.0f;
    private int alertCooldownSeconds = 5;
    private int maxDistance = 40;
    private boolean pingSound = true;
    private boolean showHud = true;
    private int hudX = 5;
    private int hudY = 70;
    private int hudRed = 255;
    private int hudGreen = 255;
    private int hudBlue = 255;

    public BedTracker() {
        super("bedtracker", "Alerts when enemies are near your bed and shows bed distance.", Category.BEDWARS, false);
    }

    public float getHudScale() {
        return hudScale;
    }

    public void setHudScale(float hudScale) {
        this.hudScale = hudScale;
    }

    public int getAlertCooldownSeconds() {
        return alertCooldownSeconds;
    }

    public void setAlertCooldownSeconds(int alertCooldownSeconds) {
        this.alertCooldownSeconds = alertCooldownSeconds;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public boolean isPingSound() {
        return pingSound;
    }

    public void setPingSound(boolean pingSound) {
        this.pingSound = pingSound;
    }

    public boolean isShowHud() {
        return showHud;
    }

    public void setShowHud(boolean showHud) {
        this.showHud = showHud;
    }

    public int getHudX() {
        return hudX;
    }

    public void setHudX(int hudX) {
        this.hudX = hudX;
    }

    public int getHudY() {
        return hudY;
    }

    public void setHudY(int hudY) {
        this.hudY = hudY;
    }

    public int getHudRed() {
        return hudRed;
    }

    public void setHudRed(int hudRed) {
        this.hudRed = hudRed;
    }

    public int getHudGreen() {
        return hudGreen;
    }

    public void setHudGreen(int hudGreen) {
        this.hudGreen = hudGreen;
    }

    public int getHudBlue() {
        return hudBlue;
    }

    public void setHudBlue(int hudBlue) {
        this.hudBlue = hudBlue;
    }

    @Override
    protected void onEnable() {
        ChatReceiveDispatcher.subscribe(chatListener);
    }

    @Override
    protected void onDisable() {
        ChatReceiveDispatcher.unsubscribe(chatListener);
        clear();
    }

    @Override
    public void onTick() {
        GamemodeUtil.updateGamemode();
        if (!GamemodeUtil.isBedwars()) {
            clear();
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) {
            clear();
            return;
        }

        TeamUtil.updateSpectatorState();
        if (bedPos == null && bedScanTime > 0L && System.currentTimeMillis() > bedScanTime) {
            bedPos = findNearbyBed(mc.thePlayer.getPosition(), 25);
            bedScanTime = 0L;
            if (bedPos != null) {
                ChatUtil.info(EnumChatFormatting.GREEN + "✓ " + EnumChatFormatting.RESET + "Whitelisted your bed at" +
                        EnumChatFormatting.GRAY + " (" + EnumChatFormatting.GREEN + bedPos.getX() + ", " + bedPos.getY() + ", " + bedPos.getZ() + EnumChatFormatting.GRAY + ")");
            } else {
                ChatUtil.info(EnumChatFormatting.RED + "⚠ Error locating your bed.");
            }
        }

        if (bedPos != null && isBedOutOfRange() && !rangeAlert) {
            ChatUtil.info(EnumChatFormatting.LIGHT_PURPLE + "" + EnumChatFormatting.BOLD + "⚠ Your bed is out of range!");
            rangeAlert = true;
        } else if (bedPos != null && !isBedOutOfRange()) {
            rangeAlert = false;
        }

        if (bedPos == null || TeamUtil.inSpectator()) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime < 6000L) {
            return;
        }

        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == mc.thePlayer || AntiBot.isBot(player) || TeamUtil.ignoreTeam(player.getName())) {
                continue;
            }
            if (player.capabilities.isFlying || player.ticksExisted < 100) {
                continue;
            }
            int distanceToBed = (int) player.getDistance(bedPos.getX(), bedPos.getY(), bedPos.getZ());
            if (distanceToBed > maxDistance) {
                continue;
            }
            UUID uuid = player.getUniqueID();
            long lastAlert = lastAlertTimes.getOrDefault(uuid, 0L);
            if (currentTime - lastAlert < alertCooldownSeconds * 1000L) {
                continue;
            }
            String distanceColor = distanceToBed <= 5 ? EnumChatFormatting.DARK_RED.toString()
                    : distanceToBed <= 15 ? EnumChatFormatting.RED.toString()
                    : distanceToBed <= 30 ? EnumChatFormatting.GOLD.toString()
                    : distanceToBed <= 40 ? EnumChatFormatting.YELLOW.toString()
                    : EnumChatFormatting.GREEN.toString();
            ChatUtil.info(NameUtil.getTabDisplayName(player.getName()) + EnumChatFormatting.WHITE + " is " +
                    distanceColor + distanceToBed + EnumChatFormatting.WHITE + " blocks from your bed! " + distanceColor + "⚠");
            lastAlertTimes.put(uuid, currentTime);
            if (pingSound && mc.thePlayer != null) {
                mc.thePlayer.playSound("random.orb", 1.0f, 1.0f);
            }
        }

        lastAlertTimes.keySet().removeIf(uuid -> mc.theWorld.getPlayerEntityByUUID(uuid) == null);
    }

    @Override
    public void onRenderOverlay() {
        Minecraft mc = Minecraft.getMinecraft();
        if (!showHud || mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) {
            return;
        }
        if (!GamemodeUtil.isBedwars()) {
            return;
        }
        String cross = EnumChatFormatting.RED + "✗";
        String check = EnumChatFormatting.GREEN + "✓";
        String separator = bedPos != null ? EnumChatFormatting.GRAY + " | " + EnumChatFormatting.RESET : "";
        EnumChatFormatting distanceColor = getDistanceToBed() < 70 ? EnumChatFormatting.GREEN : isBedOutOfRange() ? EnumChatFormatting.RED : EnumChatFormatting.YELLOW;
        String distance = bedPos == null ? "" : ("Distance: " + distanceColor + getDistanceToBed());
        String out = isBedOutOfRange() && bedPos != null ? EnumChatFormatting.RED + " ⚠" : "";
        String text = "Bed: " + (bedPos == null ? cross : check) + separator + distance + out;
        int color = (hudRed << 16) | (hudGreen << 8) | hudBlue;
        GlStateManager.pushMatrix();
        GlStateManager.scale(hudScale, hudScale, hudScale);
        mc.fontRendererObj.drawStringWithShadow(text, hudX / hudScale, hudY / hudScale, color);
        GlStateManager.popMatrix();
    }

    private void onChatReceived(ChatReceiveEvent event) {
        String msg = event.getMessage();
        if (!GamemodeUtil.isBedwars()) {
            return;
        }
        if (!msg.contains(":")) {
            if (msg.contains("The game starts in 1 second!")) {
                bedPos = null;
                bedScanTime = System.currentTimeMillis() + 6000L;
                startTime = System.currentTimeMillis() + 7000L;
            } else if (msg.contains("You will respawn because you still have a bed!")) {
                bedPos = null;
                bedScanTime = System.currentTimeMillis() + 12000L;
                startTime = System.currentTimeMillis() + 13000L;
            }
        }
        if (msg.startsWith("BED DESTRUCTION > Your Bed")) {
            bedPos = null;
            ChatUtil.info(EnumChatFormatting.DARK_RED + "" + EnumChatFormatting.BOLD + "⚠ Your bed was destroyed!");
        }
    }

    private BlockPos findNearbyBed(BlockPos center, int radius) {
        Minecraft mc = Minecraft.getMinecraft();
        for (int x = center.getX() - radius; x <= center.getX() + radius; x++) {
            for (int y = center.getY() - radius; y <= center.getY() + radius; y++) {
                for (int z = center.getZ() - radius; z <= center.getZ() + radius; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (mc.theWorld.getBlockState(pos).getBlock() instanceof BlockBed) {
                        return pos;
                    }
                }
            }
        }
        return null;
    }

    private int getDistanceToBed() {
        Minecraft mc = Minecraft.getMinecraft();
        if (bedPos == null || mc.thePlayer == null) {
            return 0;
        }
        Vec3 playerPos = mc.thePlayer.getPositionVector();
        return (int) Math.sqrt(playerPos.squareDistanceTo(new Vec3(bedPos.getX() + 0.5D, bedPos.getY(), bedPos.getZ() + 0.5D)));
    }

    private boolean isBedOutOfRange() {
        Minecraft mc = Minecraft.getMinecraft();
        if (bedPos == null || mc.thePlayer == null || mc.theWorld == null) {
            return true;
        }
        int chunkX = bedPos.getX() >> 4;
        int chunkZ = bedPos.getZ() >> 4;
        boolean serverOutOfRange = !mc.theWorld.getChunkProvider().chunkExists(chunkX, chunkZ);
        int renderDistanceBlocks = mc.gameSettings.renderDistanceChunks * 16;
        double dx = mc.thePlayer.posX - bedPos.getX();
        double dz = mc.thePlayer.posZ - bedPos.getZ();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        boolean clientOutOfRange = horizontalDistance > renderDistanceBlocks;
        return serverOutOfRange || clientOutOfRange;
    }

    public void clear() {
        bedPos = null;
        bedScanTime = 0L;
        startTime = 0L;
        rangeAlert = false;
        lastAlertTimes.clear();
    }
}
