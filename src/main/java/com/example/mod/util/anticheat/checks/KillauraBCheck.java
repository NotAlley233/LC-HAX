package com.example.mod.util.anticheat.checks;

import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KillauraBCheck {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<UUID, Integer> useItemTicks = new HashMap<>();
    private final Map<UUID, Integer> lastEatTicks = new HashMap<>();
    private final Map<UUID, Integer> violationLevels = new HashMap<>();
    private boolean failedKillauraB = false;

    public void anticheatCheck(EntityPlayer player, AntiCheat antiCheat) {
        if (!antiCheat.isDetectKillaura()) {
            failedKillauraB = false;
            return;
        }
        if (player == mc.thePlayer || player.ridingEntity != null || player.worldObj == null) {
            failedKillauraB = false;
            return;
        }

        UUID uuid = player.getUniqueID();
        long tick = player.worldObj.getTotalWorldTime();
        ItemStack heldItem = player.getHeldItem();
        boolean isUsingItem = player.isUsingItem();
        boolean isConsumable = heldItem != null && isConsumable(heldItem.getItem());
        boolean isAttacking = player.isSwingInProgress;

        int useTime = useItemTicks.getOrDefault(uuid, 0);
        if (isUsingItem && isConsumable) {
            useTime++;
            useItemTicks.put(uuid, useTime);
        } else {
            if (useTime > 0) {
                lastEatTicks.put(uuid, (int) tick);
            }
            useItemTicks.put(uuid, 0);
        }

        int lastEatTick = lastEatTicks.getOrDefault(uuid, 0);
        int sinceLastEat = (int) (tick - lastEatTick);
        if (isAttacking && useTime > 6 && sinceLastEat < 33 && isConsumable) {
            int vl = violationLevels.getOrDefault(uuid, 0) + 1;
            violationLevels.put(uuid, vl);
            if (antiCheat.isDebugMessages()) {
                ChatUtil.sendFormatted("§e[AntiCheat] §f" + player.getName()
                        + " swinging while using item"
                        + " | Use Time=" + useTime
                        + " | Last Ate=" + sinceLastEat
                        + " | vl=" + vl
                        + " | Item=" + heldItem.getItem().getUnlocalizedName());
            }
            if (vl >= 8) {
                failedKillauraB = true;
            }
        } else {
            int vl = violationLevels.getOrDefault(uuid, 0);
            if (vl > 0) {
                violationLevels.put(uuid, vl - 1);
            }
            failedKillauraB = false;
        }
    }

    public boolean failedKillauraB() {
        return failedKillauraB;
    }

    public void reset() {
        failedKillauraB = false;
        useItemTicks.clear();
        lastEatTicks.clear();
        violationLevels.clear();
    }

    private boolean isConsumable(Item item) {
        return item instanceof ItemFood || item instanceof ItemPotion || item instanceof ItemBucketMilk;
    }
}
