package com.example.mod.module.modules.bedwars;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.Tickable;
import com.example.mod.module.modules.advanced.AntiBot;
import com.example.mod.util.ChatUtil;
import com.example.mod.util.GamemodeUtil;
import com.example.mod.util.NameUtil;
import com.example.mod.util.TeamUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ItemAlerts extends BaseModule implements Tickable {
    private final Map<String, Map<String, Long>> cooldowns = new HashMap<>();

    private int cooldownSeconds = 3;
    private String soundMode = "Important";
    private String distanceMode = "Important";
    private boolean commonItems = true;
    private boolean importantItems = true;
    private boolean rotationItems = true;
    private boolean potions = true;
    private boolean pickaxes = true;
    private boolean explosives = true;
    private boolean bows = true;
    private boolean mobs = true;

    public ItemAlerts() {
        super("itemalerts", "Alerts in chat when enemies hold specific items.", Category.BEDWARS, false);
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public void setCooldownSeconds(int cooldownSeconds) {
        this.cooldownSeconds = cooldownSeconds;
    }

    public String getSoundMode() {
        return soundMode;
    }

    public void setSoundMode(String soundMode) {
        this.soundMode = soundMode;
    }

    public String getDistanceMode() {
        return distanceMode;
    }

    public void setDistanceMode(String distanceMode) {
        this.distanceMode = distanceMode;
    }

    public boolean isCommonItems() {
        return commonItems;
    }

    public void setCommonItems(boolean commonItems) {
        this.commonItems = commonItems;
    }

    public boolean isImportantItems() {
        return importantItems;
    }

    public void setImportantItems(boolean importantItems) {
        this.importantItems = importantItems;
    }

    public boolean isRotationItems() {
        return rotationItems;
    }

    public void setRotationItems(boolean rotationItems) {
        this.rotationItems = rotationItems;
    }

    public boolean isPotions() {
        return potions;
    }

    public void setPotions(boolean potions) {
        this.potions = potions;
    }

    public boolean isPickaxes() {
        return pickaxes;
    }

    public void setPickaxes(boolean pickaxes) {
        this.pickaxes = pickaxes;
    }

    public boolean isExplosives() {
        return explosives;
    }

    public void setExplosives(boolean explosives) {
        this.explosives = explosives;
    }

    public boolean isBows() {
        return bows;
    }

    public void setBows(boolean bows) {
        this.bows = bows;
    }

    public boolean isMobs() {
        return mobs;
    }

    public void setMobs(boolean mobs) {
        this.mobs = mobs;
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        GamemodeUtil.updateGamemode();
        if (!GamemodeUtil.isBedwars()) {
            return;
        }

        TeamUtil.updateSpectatorState();
        if (TeamUtil.inSpectator()) {
            return;
        }

        for (EntityPlayer player : mc.theWorld.playerEntities) {
            if (player == null || player == mc.thePlayer || TeamUtil.ignoreTeam(player.getName()) || AntiBot.isBot(player)) {
                continue;
            }
            ItemStack held = player.getHeldItem();
            if (held == null) {
                continue;
            }
            String itemName = detectItemName(held);
            if (itemName == null) {
                continue;
            }
            if (hasCooldown(player.getName(), itemName)) {
                continue;
            }
            alert(player, itemName);
            setCooldown(player.getName(), itemName);
        }
    }

    private String detectItemName(ItemStack held) {
        Item item = held.getItem();
        if (commonItems) {
            if (item == Items.iron_sword) return EnumChatFormatting.WHITE + "Iron Sword";
            if (item == Items.golden_apple) return EnumChatFormatting.GOLD + "Golden Apple";
        }
        if (importantItems) {
            if (item == Items.diamond_sword) return EnumChatFormatting.AQUA + "Diamond Sword";
            if (item == Items.stick) return EnumChatFormatting.GOLD + "Knockback Stick";
            if (item == Items.water_bucket) return EnumChatFormatting.BLUE + "Water Bucket";
            if (item == Items.ender_pearl && !held.isItemEnchanted()) return EnumChatFormatting.DARK_PURPLE + "Ender Pearl";
            if (item == Items.egg) return EnumChatFormatting.DARK_AQUA + "Bridge Egg";
            if (item == Item.getItemFromBlock(Blocks.obsidian)) return EnumChatFormatting.DARK_GRAY + "Obsidian";
        }
        if (potions) {
            String display = held.getDisplayName().toLowerCase(Locale.ROOT);
            if (item == Items.milk_bucket) return EnumChatFormatting.WHITE + "Milk";
            if (display.contains("jump")) return EnumChatFormatting.GREEN + "Jump Potion";
            if (display.contains("speed")) return EnumChatFormatting.YELLOW + "Speed Potion";
            if (display.contains("invis")) return EnumChatFormatting.AQUA + "Invis Potion";
        }
        if (pickaxes) {
            if (item == Items.diamond_pickaxe) return EnumChatFormatting.AQUA + "Diamond Pickaxe";
            if (item == Items.golden_pickaxe) return EnumChatFormatting.GOLD + "Golden Pickaxe";
        }
        if (explosives) {
            if (item == Items.fire_charge) return EnumChatFormatting.RED + "Fireball";
            if (item == Item.getItemFromBlock(Blocks.tnt) && !held.isItemEnchanted()) {
                return EnumChatFormatting.RED + "T" + EnumChatFormatting.WHITE + "N" + EnumChatFormatting.RED + "T";
            }
        }
        if (bows) {
            if (item == Items.bow && held.isItemEnchanted()) return EnumChatFormatting.GOLD + "Enchanted Bow";
            if (item == Items.bow) return EnumChatFormatting.GOLD + "Bow";
        }
        if (mobs) {
            if (item == Items.spawn_egg && !held.isItemEnchanted()) return EnumChatFormatting.WHITE + "Bedbug";
            if (item == Items.spawn_egg && held.getMetadata() != 93) return EnumChatFormatting.WHITE + "Iron Golem";
        }
        if (rotationItems) {
            if (item == Items.snowball) return EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.UNDERLINE + "Shuriken";
            if (item == Item.getItemFromBlock(Blocks.packed_ice) && held.isItemEnchanted()) return EnumChatFormatting.DARK_AQUA.toString() + EnumChatFormatting.UNDERLINE + "Ice Bridge";
            if (item == Items.wooden_hoe) return EnumChatFormatting.DARK_BLUE.toString() + EnumChatFormatting.UNDERLINE + "Bridge Zapper";
            if (item == Items.gold_ingot) return EnumChatFormatting.GOLD.toString() + EnumChatFormatting.UNDERLINE + "Charlie the Unicorn";
            if (item == Items.ender_pearl && held.isItemEnchanted()) return EnumChatFormatting.LIGHT_PURPLE.toString() + EnumChatFormatting.UNDERLINE + "Time Warp Pearl";
            if (item == Items.cookie) return EnumChatFormatting.AQUA.toString() + EnumChatFormatting.UNDERLINE + "Sugar Cookie";
            if (item == Item.getItemFromBlock(Blocks.web)) return EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE + "Cobweb";
            if (item == Item.getItemFromBlock(Blocks.slime_block)) return EnumChatFormatting.DARK_GREEN.toString() + EnumChatFormatting.UNDERLINE + "Teleportation Device";
            if (item == Item.getItemFromBlock(Blocks.tnt) && held.isItemEnchanted()) return EnumChatFormatting.DARK_RED.toString() + EnumChatFormatting.UNDERLINE + "Mega " + EnumChatFormatting.RED + "T" + EnumChatFormatting.WHITE + "N" + EnumChatFormatting.RED + "T";
            if (item == Items.stone_sword) return EnumChatFormatting.GOLD.toString() + EnumChatFormatting.UNDERLINE + "Mace";
            if (item == Items.spawn_egg && held.isItemEnchanted()) return EnumChatFormatting.WHITE.toString() + EnumChatFormatting.UNDERLINE + "Wind Charge";
            if (item == Items.shears && held.isItemEnchanted()) return EnumChatFormatting.GREEN.toString() + EnumChatFormatting.UNDERLINE + "Enchanted Shears";
            if (item == Item.getItemFromBlock((Block) Blocks.beacon) && !held.isItemEnchanted()) return EnumChatFormatting.RED.toString() + EnumChatFormatting.UNDERLINE + "Final Revive Beacon";
            if (item == Items.prismarine_shard) return EnumChatFormatting.AQUA.toString() + EnumChatFormatting.UNDERLINE + "Block Zapper";
        }
        return null;
    }

    private void alert(EntityPlayer player, String itemName) {
        Minecraft mc = Minecraft.getMinecraft();
        float distance = player.getDistanceToEntity(mc.thePlayer);
        String distanceText = EnumChatFormatting.GRAY + " (" + EnumChatFormatting.AQUA + new DecimalFormat("#").format(distance) + "m" + EnumChatFormatting.GRAY + ")";
        String raw = stripFormatting(itemName).toLowerCase(Locale.ROOT);
        boolean importantDistance = raw.equals("fireball") || raw.equals("ender pearl") || raw.equals("bridge egg")
                || raw.equals("speed potion") || raw.equals("jump potion") || raw.equals("invis potion")
                || raw.equals("charlie the unicorn");
        String showDistance = "All".equalsIgnoreCase(distanceMode) || ("Important".equalsIgnoreCase(distanceMode) && importantDistance)
                ? distanceText : "";

        ChatUtil.info(NameUtil.getTabDisplayName(player.getName()) + EnumChatFormatting.GRAY + " has " + itemName + showDistance);

        boolean play = "All".equalsIgnoreCase(soundMode) || ("Important".equalsIgnoreCase(soundMode) && isImportantSound(raw));
        if (play && mc.thePlayer != null) {
            mc.thePlayer.playSound("random.orb", 1.0f, 1.0f);
        }
    }

    private boolean isImportantSound(String rawName) {
        return rawName.equals("jump potion")
                || rawName.equals("speed potion")
                || rawName.equals("invis potion")
                || rawName.equals("bridge egg")
                || rawName.equals("diamond sword")
                || rawName.equals("iron golem")
                || rawName.equals("bedbug")
                || rawName.equals("charlie the unicorn")
                || rawName.equals("diamond pickaxe")
                || rawName.equals("enchanted bow")
                || rawName.equals("milk")
                || rawName.equals("ender pearl");
    }

    private boolean hasCooldown(String playerName, String itemName) {
        long alertCooldown = Math.max(1, cooldownSeconds) * 1000L;
        Map<String, Long> playerCooldowns = cooldowns.get(playerName);
        if (playerCooldowns == null) {
            return false;
        }
        Long lastTime = playerCooldowns.get(itemName);
        return lastTime != null && System.currentTimeMillis() - lastTime < alertCooldown;
    }

    private void setCooldown(String playerName, String itemName) {
        cooldowns.computeIfAbsent(playerName, k -> new HashMap<>()).put(itemName, System.currentTimeMillis());
    }

    private String stripFormatting(String value) {
        return value.replaceAll("§.", "");
    }

    @Override
    protected void onDisable() {
        cooldowns.clear();
    }
}
