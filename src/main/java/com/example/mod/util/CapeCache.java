package com.example.mod.util;

import com.example.mod.module.modules.Cape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class CapeCache {
    private static final String DEFAULT_CAPE = "2011";
    private static final Map<String, ResourceLocation> CACHE = new HashMap<>();

    private CapeCache() {
    }

    public static ResourceLocation getCape(String selectedCape) {
        String normalized = Cape.normalizeCape(selectedCape);
        ResourceLocation selected = getOrLoad(normalized);
        if (selected != null) {
            return selected;
        }
        return getOrLoad(DEFAULT_CAPE);
    }

    private static ResourceLocation getOrLoad(String capeName) {
        String normalized = Cape.normalizeCape(capeName);
        String cacheKey = normalized.toLowerCase(Locale.ROOT);
        ResourceLocation cached = CACHE.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        ResourceLocation loaded = loadDynamicCape(normalized);
        if (loaded != null) {
            CACHE.put(cacheKey, loaded);
        }
        return loaded;
    }

    private static ResourceLocation loadDynamicCape(String capeName) {
        String normalized = Cape.normalizeCape(capeName);
        String lower = normalized.toLowerCase(Locale.ROOT);
        String[] candidates = new String[] {
                "assets/lchax/textures/cape/" + lower + ".png",
                "assets/lchax/textures/cape/" + normalized + ".png"
        };
        for (String candidate : candidates) {
            ResourceLocation location = loadFromClasspath(candidate, lower);
            if (location != null) {
                return location;
            }
        }
        return null;
    }

    private static ResourceLocation loadFromClasspath(String resourcePath, String dynamicKey) {
        try (InputStream input = CapeCache.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                return null;
            }
            BufferedImage image = ImageIO.read(input);
            if (image == null) {
                return null;
            }
            DynamicTexture texture = new DynamicTexture(image);
            return Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("lchax_cape_" + dynamicKey, texture);
        } catch (Exception ignored) {
            return null;
        }
    }
}
