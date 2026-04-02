package com.example.mod.util.anticheat.checks;

import com.example.mod.module.modules.advanced.AntiCheat;
import com.example.mod.util.ChatUtil;
import com.example.mod.util.anticheat.AntiCheatCheckIds;
import com.example.mod.util.anticheat.StarfishStylePlayerState;
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

/**
 * Eat/use consumable while swinging (not in Starfish); optional, default off.
 */
public class KillauraBCheck {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<UUID, Integer> useItemTicks = new HashMap<>();
    private final Map<UUID, Integer> lastEatTicks = new HashMap<>();

    public void check(EntityPlayer player, AntiCheat antiCheat, StarfishStylePlayerState state) {
        if (!antiCheat.isDetectKillaura()) {
            return;
        }
        if (player == mc.thePlayer || player.ridingEntity != null || player.worldObj == null) {
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
            state.addViolation(AntiCheatCheckIds.KILLAURA_B, 1);
            if (antiCheat.isDebugMessages()) {
                ChatUtil.sendFormatted("§e[AntiCheat] §f" + player.getName()
                        + " swinging while using item | Use Time=" + useTime
                        + " | Last Ate=" + sinceLastEat
                        + " | Item=" + heldItem.getItem().getUnlocalizedName());
            }
        } else {
            state.reduceViolation(AntiCheatCheckIds.KILLAURA_B, 1);
        }
    }

    public void reset() {
        useItemTicks.clear();
        lastEatTicks.clear();
    }

    private boolean isConsumable(Item item) {
        return item instanceof ItemFood || item instanceof ItemPotion || item instanceof ItemBucketMilk;
    }
}
