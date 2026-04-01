package com.example.mod.mixins;

import com.example.mod.module.modules.skywars.StrengthESP;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderManager.class)
public class RenderManagerMixin {
    @Inject(method = "doRenderEntity", at = @At("TAIL"))
    private void lchax$renderStrengthEsp(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean hideDebugBox, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null || player == mc.thePlayer) return;
        if (!StrengthESP.shouldHighlight(player)) return;
        StrengthESP module = StrengthESP.getInstance();
        if (module == null) return;

        AxisAlignedBB bb = player.getEntityBoundingBox()
                .expand(0.12D, 0.18D, 0.12D)
                .offset(-player.posX + x, -player.posY + y, -player.posZ + z);

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.color(
                module.getRed() / 255.0F,
                module.getGreen() / 255.0F,
                module.getBlue() / 255.0F,
                module.getAlpha() / 255.0F
        );
        GL11.glLineWidth(2.0F);
        RenderGlobal.drawSelectionBoundingBox(bb);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
