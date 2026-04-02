package com.example.mod.util.anticheat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

/**
 * Item categories aligned with Starfish anticheat-0-2-1.js isHoldingConsumable / sword / bow.
 */
public final class AntiCheatItems {

    private AntiCheatItems() {
    }

    public static boolean isHoldingBow(ItemStack held) {
        return held != null && held.getItem() instanceof ItemBow;
    }

    public static boolean isHoldingSword(ItemStack held) {
        return held != null && held.getItem() instanceof ItemSword;
    }

    public static boolean isHoldingConsumable(ItemStack held) {
        if (held == null) {
            return false;
        }
        Item i = held.getItem();
        return i instanceof ItemFood || i instanceof ItemPotion || i instanceof ItemBucketMilk;
    }

    /**
     * Using an item that applies slowdown (eat, bow draw, sword block).
     */
    public static boolean isUsingSlowdownItem(net.minecraft.entity.player.EntityPlayer p) {
        if (!p.isUsingItem()) {
            return false;
        }
        ItemStack held = p.getHeldItem();
        return isHoldingConsumable(held) || isHoldingBow(held) || isHoldingSword(held);
    }
}
