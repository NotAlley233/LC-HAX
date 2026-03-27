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
import com.example.mod.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class BedTracker extends BaseModule implements Tickable, OverlayRenderable {
    private int maxDistance = 25;
    private int alertCooldownSeconds = 10;
    private boolean showHud = true;
    private int hudX = 10;
    private int hudY = 10;
    private boolean pingSound = true;
    private boolean forceScan = false;

    private BlockPos bedPos = null;
    private long bedScanTime = 0L;
    private long startTime = 0L;
    private boolean rangeAlert = false;
    private final Map<UUID, Long> lastAlertTimes = new HashMap<>();

    private final Consumer<ChatReceiveEvent> chatListener = this::onChatReceived;

    public BedTracker() {
        super("bedtracker", "Alerts when enemies are near your bed.", Category.UTILITY, false);
    }

    public int getMaxDistance() { return maxDistance; }
    public void setMaxDistance(int maxDistance) { this.maxDistance = maxDistance; }

    public int getAlertCooldownSeconds() { return alertCooldownSeconds; }
    public void setAlertCooldownSeconds(int alertCooldownSeconds) { this.alertCooldownSeconds = alertCooldownSeconds; }

    public boolean isShowHud() { return showHud; }
    public void setShowHud(boolean showHud) { this.showHud = showHud; }

    public int getHudX() { return hudX; }
    public void setHudX(int hudX) { this.hudX = hudX; }

    public int getHudY() { return hudY; }
    public void setHudY(int hudY) { this.hudY = hudY; }

    public boolean isPingSound() { return pingSound; }
    public void setPingSound(boolean pingSound) { this.pingSound = pingSound; }

    public boolean isForceScan() { return forceScan; }
    public void setForceScan(boolean forceScan) {
        this.forceScan = forceScan;
        if (forceScan) {
            forceScanBed();
            this.forceScan = false; // Reset immediately to act as a button
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        ChatReceiveDispatcher.subscribe(chatListener);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ChatReceiveDispatcher.unsubscribe(chatListener);
        clear();
    }

    private void onChatReceived(ChatReceiveEvent event) {
        String msg = event.getMessage();
        if (!GamemodeUtil.isBedwars()) return;

        if (!msg.contains(":")) {
            if (msg.contains("The game starts in 1 second!")) {
                bedPos = null;
                bedScanTime = System.currentTimeMillis() + 6000L;
                startTime = System.currentTimeMillis() + 7000L;
                ChatUtil.sendFormatted("&e[BedTracker] &fLocating bed in 6s...");
            } else if (msg.contains("You will respawn because you still have a bed!")) {
                bedPos = null;
                bedScanTime = System.currentTimeMillis() + 12000L;
                startTime = System.currentTimeMillis() + 13000L;
                ChatUtil.sendFormatted("&e[BedTracker] &fLocating bed in 12s...");
            }
        }

        if (msg.startsWith("BED DESTRUCTION > Your Bed")) {
            bedPos = null;
            ChatUtil.sendFormatted("&4&l⚠ Your bed was destroyed!");
        }
    }

    private void forceScanBed() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        bedPos = findNearbyBed(mc.theWorld, mc.thePlayer.getPosition(), 25);
        if (bedPos != null) {
            ChatUtil.sendFormatted("&a&l✓ &rWhitelisted your bed at &7(&a" + bedPos.getX() + ", " + bedPos.getY() + ", " + bedPos.getZ() + "&7)");
            startTime = System.currentTimeMillis(); // Allow alerts immediately
        } else {
            ChatUtil.sendFormatted("&c⚠ Error locating your bed manually. Are you near it?");
        }
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;
        
        // Gamemode status is refreshed globally in MinecraftMixin.runTick.
        if (!GamemodeUtil.isBedwars()) return;

        // Auto Scan
        if (bedPos == null && bedScanTime > 0L && System.currentTimeMillis() > bedScanTime) {
            bedPos = findNearbyBed(mc.theWorld, mc.thePlayer.getPosition(), 25);
            bedScanTime = 0L;
            if (bedPos != null) {
                ChatUtil.sendFormatted("&a&l✓ &rWhitelisted your bed at &7(&a" + bedPos.getX() + ", " + bedPos.getY() + ", " + bedPos.getZ() + "&7)");
            } else {
                ChatUtil.sendFormatted("&c⚠ Error locating your bed automatically.");
            }
        }

        // Out of range alert
        if (bedPos != null && isBedOutOfRange() && !rangeAlert) {
            ChatUtil.sendFormatted("&d&l⚠ Your bed is out of range!");
            rangeAlert = true;
        } else if (!isBedOutOfRange() && bedPos != null) {
            rangeAlert = false;
        }

        // Enemy proximity check
        if (bedPos != null && mc.theWorld != null && !TeamUtil.inSpectator()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime < 6000L) return;

            for (EntityPlayer player : mc.theWorld.playerEntities) {
                if (player == mc.thePlayer || AntiBot.isBot(player) || TeamUtil.ignoreTeam(player.getName())) continue;
                if (player.capabilities.isFlying || player.posY > 100) continue; // Basic spectator/cage check

                double distanceToBed = player.getDistance(bedPos.getX(), bedPos.getY(), bedPos.getZ());
                if (distanceToBed > maxDistance) continue;

                UUID uuid = player.getUniqueID();
                long lastAlert = lastAlertTimes.getOrDefault(uuid, 0L);

                if (currentTime - lastAlert >= alertCooldownSeconds * 1000L) {
                    String distanceColor = (distanceToBed <= 5) ? "&4" : ((distanceToBed <= 15) ? "&c" : ((distanceToBed <= 30) ? "&6" : "&e"));
                    ChatUtil.sendFormatted(NameUtil.getTabDisplayName(player.getName()) + "&f is " + distanceColor + (int)distanceToBed + "&f blocks from your bed!" + distanceColor + " ⚠");
                    
                    lastAlertTimes.put(uuid, currentTime);
                    if (pingSound) {
                        playPingSound();
                    }
                }
            }
            lastAlertTimes.keySet().removeIf(uuid -> mc.theWorld.getPlayerEntityByUUID(uuid) == null);
        }
    }

    @Override
    public void onRenderOverlay() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) return;
        if (!GamemodeUtil.isBedwars() || !showHud) return;

        String cross = "§c✗";
        String checkMark = "§a✓";
        String separator = (bedPos != null) ? "§7 | §r" : "";
        
        String bedState = (bedPos == null) ? cross : checkMark;
        String bed = "Bed: ";
        
        int dist = getDistanceToBed();
        String distanceColor = (dist < 70) ? "§a" : (isBedOutOfRange() ? "§c" : "§e");
        String distance = (bedPos == null) ? "" : ("Distance: " + distanceColor + dist);
        String doesChunkExist = (isBedOutOfRange() && bedPos != null) ? "§c ⚠" : "";
        
        String text = bed + bedState + separator + distance + doesChunkExist;
        
        RenderUtil.drawString(text, hudX, hudY, 0xFFFFFFFF);
    }

    private BlockPos findNearbyBed(World world, BlockPos center, int radius) {
        for (int x = center.getX() - radius; x <= center.getX() + radius; x++) {
            for (int y = center.getY() - radius; y <= center.getY() + radius; y++) {
                for (int z = center.getZ() - radius; z <= center.getZ() + radius; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (world.getBlockState(pos).getBlock() instanceof net.minecraft.block.BlockBed) {
                        return pos;
                    }
                }
            }
        }
        return null;
    }

    private int getDistanceToBed() {
        if (bedPos == null) return 0;
        Minecraft mc = Minecraft.getMinecraft();
        Vec3 playerPos = mc.thePlayer.getPositionVector();
        return (int) Math.sqrt(playerPos.squareDistanceTo(new Vec3(bedPos.getX() + 0.5, bedPos.getY(), bedPos.getZ() + 0.5)));
    }

    private boolean isBedOutOfRange() {
        if (bedPos == null) return true;
        Minecraft mc = Minecraft.getMinecraft();
        int chunkX = bedPos.getX() >> 4;
        int chunkZ = bedPos.getZ() >> 4;
        boolean serverOutOfRange = !mc.theWorld.getChunkProvider().chunkExists(chunkX, chunkZ);

        int renderDistanceBlocks = mc.gameSettings.renderDistanceChunks * 16;
        double dx = mc.thePlayer.posX - bedPos.getX();
        double dz = mc.thePlayer.posZ - bedPos.getZ();
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        boolean clientOutOfRange = (horizontalDistance > renderDistanceBlocks);

        return serverOutOfRange || clientOutOfRange;
    }

    private void playPingSound() {
        Minecraft mc = Minecraft.getMinecraft();
        SoundHandler soundHandler = mc.getSoundHandler();
        if (soundHandler != null) {
            PositionedSoundRecord positionedSoundRecord = PositionedSoundRecord.create(new ResourceLocation("random.orb"));
            soundHandler.playSound(positionedSoundRecord);
        }
    }

    public void clear() {
        bedPos = null;
        bedScanTime = 0L;
        startTime = 0L;
        lastAlertTimes.clear();
        rangeAlert = false;
    }
}
