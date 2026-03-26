package com.example.mod.module.modules.bedwars;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.Tickable;
import com.example.mod.module.modules.advanced.AntiBot;
import com.example.mod.util.ChatUtil;
import com.example.mod.util.GamemodeUtil;
import com.example.mod.util.NameUtil;
import com.example.mod.util.TeamUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorAlerts extends BaseModule implements Tickable {
    private boolean armorPingSound = true;
    private boolean detectDiamondArmor = true;
    private boolean detectIronArmor = true;
    private boolean detectChainArmor = true;

    private int tickCounter = 0;
    private final Map<String, String> currentPlayerArmorMap = new HashMap<>();

    public ArmorAlerts() {
        super("armoralerts", "Alerts you when players buy an armor upgrade.", Category.BEDWARS, false);
    }

    public boolean isArmorPingSound() { return armorPingSound; }
    public void setArmorPingSound(boolean armorPingSound) { this.armorPingSound = armorPingSound; }

    public boolean isDetectDiamondArmor() { return detectDiamondArmor; }
    public void setDetectDiamondArmor(boolean detectDiamondArmor) { this.detectDiamondArmor = detectDiamondArmor; }

    public boolean isDetectIronArmor() { return detectIronArmor; }
    public void setDetectIronArmor(boolean detectIronArmor) { this.detectIronArmor = detectIronArmor; }

    public boolean isDetectChainArmor() { return detectChainArmor; }
    public void setDetectChainArmor(boolean detectChainArmor) { this.detectChainArmor = detectChainArmor; }

    @Override
    public void onTick() {
        // GamemodeUtil.updateGamemode() will be called globally or can be called here
        GamemodeUtil.updateGamemode();
        if (!GamemodeUtil.isBedwars()) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        // Process every 1 second (20 ticks)
        if (++tickCounter % 20 != 0) return;

        TeamUtil.updateSpectatorState();
        if (TeamUtil.inSpectator()) return;

        List<EntityPlayer> playerList = mc.theWorld.playerEntities;
        for (EntityPlayer player : playerList) {
            processArmor(player);
        }
    }

    private void processArmor(EntityPlayer player) {
        String playerName = player.getName();

        if (TeamUtil.ignoreSelf(playerName)) return;
        if (TeamUtil.ignoreTeam(playerName)) return;
        if (AntiBot.isBot(player)) return;

        ItemStack[] armorInventory = player.inventory.armorInventory;
        ItemStack leggingsStack = armorInventory[1]; // Leggings are typically used for armor checks in BW

        if (leggingsStack != null && leggingsStack.getItem() instanceof ItemArmor) {
            ItemArmor armor = (ItemArmor) leggingsStack.getItem();
            if (armor.armorType == 2) { // 2 = leggings in 1.8.9
                String armorType = armor.getArmorMaterial().name();

                if (!currentPlayerArmorMap.containsKey(playerName)) {
                    currentPlayerArmorMap.put(playerName, armorType);
                }
                
                String currentArmorType = currentPlayerArmorMap.get(playerName);

                if (detectDiamondArmor && !currentArmorType.equals(armorType) && armorType.equals("DIAMOND")) {
                    alertPurchase(playerName, "Diamond Armor", "§b", armorType);
                } else if (detectIronArmor && !currentArmorType.equals(armorType) && armorType.equals("IRON")) {
                    alertPurchase(playerName, "Iron Armor", "§f", armorType);
                } else if (detectChainArmor && !currentArmorType.equals(armorType) && armorType.equals("CHAIN")) {
                    alertPurchase(playerName, "Chainmail Armor", "§8", armorType);
                }
            }
        }
    }

    private void alertPurchase(String playerName, String armorName, String colorCode, String newArmorType) {
        String displayName = NameUtil.getTabDisplayName(playerName);
        ChatUtil.info(displayName + "§7 purchased " + colorCode + armorName);
        currentPlayerArmorMap.put(playerName, newArmorType);
        
        if (armorPingSound) {
            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0f, 1.0f);
        }
    }

    @Override
    protected void onDisable() {
        currentPlayerArmorMap.clear();
        tickCounter = 0;
    }
}