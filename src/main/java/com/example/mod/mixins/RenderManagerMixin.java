package com.example.mod.mixins;

import com.example.mod.module.modules.render.PlayerESP;
import com.example.mod.module.modules.skywars.StrengthESP;
import com.example.mod.util.render.RenderUtil;
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
    private void lchax$renderEntityEsp(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean hideDebugBox, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (StrengthESP.shouldHighlight(player) && player != mc.thePlayer) {
            StrengthESP strengthModule = StrengthESP.getInstance();
            if (strengthModule != null) {
                renderOutlineBox(player, x, y, z, 0.1D, 0.1D,
                        strengthModule.getRed(), strengthModule.getGreen(),
                        strengthModule.getBlue(), strengthModule.getAlpha(),
                        2.0F, true, 40);
            }
        }

        if (PlayerESP.shouldRender(player)) {
            PlayerESP espModule = PlayerESP.getInstance();
            if (espModule != null) {
                renderOutlineBox(player, x, y, z, 0.1D, 0.1D,
                        espModule.getRed(), espModule.getGreen(),
                        espModule.getBlue(), espModule.getAlpha(),
                        espModule.getLineWidth(),
                        espModule.isFilledBox(), espModule.getFilledAlpha());
            }
        }
    }

    private void renderOutlineBox(EntityPlayer player, double x, double y, double z,
                                  double expandXZ, double expandY,
                                  int red, int green, int blue, int alpha,
                                  float lineWidth, boolean filled, int filledAlpha) {
        AxisAlignedBB bb = player.getEntityBoundingBox()
                .expand(expandXZ, expandY, expandXZ)
                .offset(-player.posX + x, -player.posY + y, -player.posZ + z);

        float r = red / 255.0F;
        float g = green / 255.0F;
        float b = blue / 255.0F;
        float a = alpha / 255.0F;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);

        if (filled && filledAlpha > 0) {
            GlStateManager.depthMask(false);
            RenderUtil.drawFilledBoundingBox(bb, r, g, b, filledAlpha / 255.0F);
            GlStateManager.depthMask(true);
        }

        GlStateManager.color(r, g, b, a);
        GL11.glLineWidth(lineWidth);
        RenderGlobal.drawSelectionBoundingBox(bb);

        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
