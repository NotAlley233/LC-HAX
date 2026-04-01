package com.example.mod.gui.clickgui.theme;

import java.awt.Color;

public final class MaterialTheme {
    public static final Color PRIMARY_COLOR = new Color(110, 80, 255);
    public static final Color TEXT_COLOR = new Color(240, 240, 240);
    public static final Color TEXT_COLOR_SECONDARY = new Color(160, 160, 170);
    public static final Color OUTLINE_COLOR = new Color(80, 80, 90, 100);

    public static final Color PANEL_BG = new Color(18, 18, 22);
    public static final Color SIDEBAR_BG = new Color(24, 24, 30);
    public static final Color HEADER_BG = new Color(22, 22, 28);
    public static final Color CONTENT_BG = new Color(20, 20, 26);
    public static final Color ACTIVE_TAB = new Color(110, 80, 255, 30);
    public static final Color MODULE_HOVER = new Color(255, 255, 255, 12);
    public static final Color SEARCH_BG = new Color(30, 30, 38);
    public static final Color DIVIDER = new Color(255, 255, 255, 10);
    public static final Color SURFACE_CONTAINER_HIGH = new Color(45, 45, 50, 220);
    public static final Color SETTINGS_BG = new Color(14, 14, 18, 180);
    public static final Color SLIDER_TRACK = new Color(40, 40, 48);
    public static final Color SWITCH_OFF = new Color(55, 55, 62);
    public static final Color DROPDOWN_BG = new Color(26, 26, 32);
    public static final Color DROPDOWN_HOVER = new Color(255, 255, 255, 8);

    public static final float CORNER_RADIUS_PANEL = 10.0f;
    public static final float CORNER_RADIUS_FRAME = 8.0f;
    public static final float CORNER_RADIUS_ITEM = 5.0f;
    public static final float CORNER_RADIUS_TAB = 6.0f;
    public static final float CORNER_RADIUS_SEARCH = 5.0f;

    public static int getRGB(Color color) {
        return color.getRGB();
    }

    public static int getRGBWithAlpha(Color color, int alpha) {
        alpha = Math.max(0, Math.min(255, alpha));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }

    public static int blendColor(int from, int to, float t) {
        float clamped = Math.max(0f, Math.min(1f, t));
        int fa = (from >> 24) & 255, fr = (from >> 16) & 255, fg = (from >> 8) & 255, fb = from & 255;
        int ta = (to >> 24) & 255, tr = (to >> 16) & 255, tg = (to >> 8) & 255, tb = to & 255;
        int a = (int) (fa + (ta - fa) * clamped);
        int r = (int) (fr + (tr - fr) * clamped);
        int g = (int) (fg + (tg - fg) * clamped);
        int b = (int) (fb + (tb - fb) * clamped);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private MaterialTheme() {}
}
