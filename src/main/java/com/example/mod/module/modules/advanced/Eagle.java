package com.example.mod.module.modules.advanced;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.Tickable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;

public class Eagle extends BaseModule implements Tickable {
    private boolean blocksOnly = true;
    private boolean pitchCheck = true;
    private int pitchThreshold = 60;
    private boolean isSneaking = false;

    public Eagle() {
        super("eagle", "Automatically sneaks at the edge of blocks.", Category.ADVANCED, false);
    }

    public boolean isBlocksOnly() { return blocksOnly; }
    public void setBlocksOnly(boolean blocksOnly) { this.blocksOnly = blocksOnly; }

    public boolean isPitchCheck() { return pitchCheck; }
    public void setPitchCheck(boolean pitchCheck) { this.pitchCheck = pitchCheck; }

    public int getPitchThreshold() { return pitchThreshold; }
    public void setPitchThreshold(int pitchThreshold) { this.pitchThreshold = pitchThreshold; }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        // If the user is manually holding the sneak key, let them control it
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
            isSneaking = false;
            return;
        }

        if (blocksOnly) {
            ItemStack heldItem = mc.thePlayer.getHeldItem();
            if (heldItem == null || !(heldItem.getItem() instanceof ItemBlock)) {
                unSneakIfNeeded(mc);
                return;
            }
        }

        // We should only eagle when we are on the ground
        if (!mc.thePlayer.onGround) {
            unSneakIfNeeded(mc);
            return;
        }

        // Pitch check (Myau style): Only sneak if looking down sufficiently
        if (pitchCheck && mc.thePlayer.rotationPitch < pitchThreshold) {
            unSneakIfNeeded(mc);
            return;
        }

        // Check the block directly under the player's position
        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
        Block blockUnder = mc.theWorld.getBlockState(playerPos).getBlock();

        // If the block is air, we are at an edge, so we sneak
        if (blockUnder instanceof BlockAir) {
            if (!isSneaking) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
                isSneaking = true;
            }
        } else {
            // Otherwise we are safe, release sneak
            unSneakIfNeeded(mc);
        }
    }

    private void unSneakIfNeeded(Minecraft mc) {
        if (isSneaking) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
            isSneaking = false;
        }
    }

    @Override
    public void onDisable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.gameSettings != null) {
            unSneakIfNeeded(mc);
        }
        super.onDisable();
    }
}
