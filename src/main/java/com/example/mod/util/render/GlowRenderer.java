package com.example.mod.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

/**
 * Off-screen mask + separable glow blur using {@link ShaderUtil}'s built-in {@code "glow"} program,
 * then composites to the current (main) framebuffer.
 */
public final class GlowRenderer {

    private static ShaderUtil glowShader;
    private static boolean glowShaderFailed;

    private static Framebuffer maskFbo;
    private static Framebuffer hFbo;
    private static Framebuffer outFbo;
    private static int cachedW;
    private static int cachedH;

    private static final float[] WEIGHT_SCRATCH = new float[256];

    private GlowRenderer() {}

    private static ShaderUtil getGlowShader() {
        if (glowShaderFailed) {
            return null;
        }
        if (glowShader == null) {
            try {
                glowShader = new ShaderUtil("glow");
            } catch (Exception e) {
                glowShaderFailed = true;
                return null;
            }
        }
        return glowShader;
    }

    private static void disposeFramebuffers() {
        if (maskFbo != null) {
            maskFbo.deleteFramebuffer();
            maskFbo = null;
        }
        if (hFbo != null) {
            hFbo.deleteFramebuffer();
            hFbo = null;
        }
        if (outFbo != null) {
            outFbo.deleteFramebuffer();
            outFbo = null;
        }
    }

    private static void ensureFramebuffers(Minecraft mc) {
        int w = mc.displayWidth;
        int h = mc.displayHeight;
        if (w <= 0 || h <= 0) {
            return;
        }
        if (maskFbo != null && cachedW == w && cachedH == h) {
            return;
        }
        disposeFramebuffers();
        cachedW = w;
        cachedH = h;
        maskFbo = new Framebuffer(w, h, false);
        hFbo = new Framebuffer(w, h, false);
        outFbo = new Framebuffer(w, h, false);
    }

    /**
     * Normalized separable Gaussian weights for {@code weights[int(r)]}, {@code r} from 1 to {@code radius}.
     */
    public static float[] computeGaussianWeights(int radius, float sigma) {
        float[] w = new float[256];
        if (radius < 1) {
            radius = 1;
        }
        if (radius > 255) {
            radius = 255;
        }
        float sigmaSq = 2.0f * sigma * sigma;
        float sum = 0.0f;
        w[0] = 1.0f;
        sum += w[0];
        for (int i = 1; i <= radius; i++) {
            float g = (float) Math.exp(-(i * i) / sigmaSq);
            w[i] = g;
            sum += 2.0f * g;
        }
        for (int i = 0; i <= radius; i++) {
            w[i] /= sum;
        }
        return w;
    }

    private static void copyWeights(float[] src, int radius) {
        for (int i = 0; i < 256; i++) {
            WEIGHT_SCRATCH[i] = (i <= radius) ? src[i] : 0.0f;
        }
    }

    private static void setupGuiProjection(Minecraft mc) {
        ScaledResolution sr = new ScaledResolution(mc);
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, sr.getScaledWidth_double(), sr.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
    }

    private static void bindTextureUnits(int textureInId, int textureToCheckId) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(textureInId);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(textureToCheckId);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    private static void resetTextureUnit0() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
    }

    /**
     * Raw GL attrib push/pop does not sync {@link GlStateManager}'s shadow state and does not save matrix
     * stacks; that can break the next world pass. Reset program, secondary texture unit, and main FBO.
     */
    private static void restorePostGlow(Minecraft mc) {
        GL20.glUseProgram(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GlStateManager.bindTexture(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        mc.getFramebuffer().bindFramebuffer(true);
    }

    private static void applyGlowPass(
            Minecraft mc,
            ShaderUtil shader,
            Framebuffer dest,
            int texIn,
            int texCheck,
            float fw,
            float fh,
            float dirX,
            float dirY,
            float glowR,
            float glowG,
            float glowB,
            float exposure,
            float radiusPx,
            boolean avoidTexture
    ) {
        dest.bindFramebuffer(true);
        GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT);
        setupGuiProjection(mc);

        shader.init();
        bindTextureUnits(texIn, texCheck);
        shader.setUniformi("textureIn", 0);
        shader.setUniformi("textureToCheck", 1);
        shader.setUniformf("texelSize", 1.0f / fw, 1.0f / fh);
        shader.setUniformf("direction", dirX, dirY);
        shader.setUniformf("color", glowR, glowG, glowB);
        shader.setUniformi("avoidTexture", avoidTexture ? 1 : 0);
        shader.setUniformf("exposure", exposure);
        shader.setUniformf("radius", radiusPx);
        shader.setUniform1fv("weights", WEIGHT_SCRATCH, 256);
        ShaderUtil.drawQuads();
        shader.unload();
        resetTextureUnit0();
    }

    private static void drawFramebufferToScreen(Framebuffer fb, Minecraft mc) {
        ScaledResolution sr = new ScaledResolution(mc);
        GlStateManager.enableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.bindTexture(fb.framebufferTexture);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0.0, 1.0);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glTexCoord2d(0.0, 0.0);
        GL11.glVertex2d(0.0, sr.getScaledHeight());
        GL11.glTexCoord2d(1.0, 0.0);
        GL11.glVertex2d(sr.getScaledWidth(), sr.getScaledHeight());
        GL11.glTexCoord2d(1.0, 1.0);
        GL11.glVertex2d(sr.getScaledWidth(), 0.0);
        GL11.glEnd();
    }

    /**
     * Renders {@code maskDrawer} into an off-screen mask, runs glow blur, composites to the main framebuffer, then restores binding.
     *
     * @param maskDrawer draw only the alpha mask (e.g. white rounded rect) in scaled GUI coordinates
     * @param blurRadius blur sample radius in pixels (clamped 1–255 for weight table)
     * @param exposure   shader exposure term
     * @param glowR      glow color red 0–1
     * @param glowG      green 0–1
     * @param glowB      blue 0–1
     */
    public static void drawOuterGlow(
            Runnable maskDrawer,
            float blurRadius,
            float exposure,
            float glowR,
            float glowG,
            float glowB
    ) {
        ShaderUtil shader = getGlowShader();
        if (shader == null || maskDrawer == null) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        ensureFramebuffers(mc);
        if (maskFbo == null || hFbo == null || outFbo == null) {
            return;
        }

        float radiusPx = Math.max(1.0f, Math.min(255.0f, blurRadius));
        int ri = (int) Math.ceil(radiusPx);
        float sigma = Math.max(0.5f, blurRadius * 0.5f);
        float[] weights = computeGaussianWeights(ri, sigma);
        copyWeights(weights, ri);

        float fw = (float) maskFbo.framebufferWidth;
        float fh = (float) maskFbo.framebufferHeight;
        int maskTex = maskFbo.framebufferTexture;
        int hTex = hFbo.framebufferTexture;

        GlStateManager.pushAttrib();

        maskFbo.bindFramebuffer(true);
        GlStateManager.clearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GlStateManager.clear(GL11.GL_COLOR_BUFFER_BIT);
        setupGuiProjection(mc);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        maskDrawer.run();

        applyGlowPass(mc, shader, hFbo, maskTex, maskTex, fw, fh, 1.0f, 0.0f, glowR, glowG, glowB, exposure, radiusPx, false);
        applyGlowPass(mc, shader, outFbo, hTex, maskTex, fw, fh, 0.0f, 1.0f, glowR, glowG, glowB, exposure, radiusPx, true);

        mc.getFramebuffer().bindFramebuffer(false);
        setupGuiProjection(mc);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        drawFramebufferToScreen(outFbo, mc);

        GlStateManager.popAttrib();
        restorePostGlow(mc);
    }
}
