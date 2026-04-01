package com.example.mod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public final class ChatUtil {
    private ChatUtil() {}

    private static final char SECTION = '\u00A7';

    private static volatile String prefixText = "prefix";
    private static volatile boolean prefixEnabled = true;

    public static void setPrefixText(String text) {
        prefixText = text == null ? "" : text;
    }

    public static String getPrefixText() {
        return prefixText;
    }

    public static void setPrefixEnabled(boolean enabled) {
        prefixEnabled = enabled;
    }

    public static boolean isPrefixEnabled() {
        return prefixEnabled;
    }

    public static void info(String msg) {
        send(prefix() + msg);
    }

    public static void error(String msg) {
        send(prefix() + "§c" + msg + "§r");
    }

    public static void sendFormatted(String msg) {
        if (msg == null) return;
        // myau uses '&' color codes; support both.
        send(msg.replace('&', SECTION));
    }

    public static void sendPrefixedFormatted(String msg) {
        if (msg == null) return;
        sendFormatted(prefix() + msg);
    }

    public static String getPrefixString() {
        return prefix();
    }

    private static String prefix() {
        if (!prefixEnabled) return "";
        String p = prefixText;
        if (p == null || p.trim().isEmpty()) return "";
        String inner = RainbowUtil.rainbowOffset(p, 1, false);
        return "§f[" + inner + "§f] §r";
    }

    private static void send(String msg) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.thePlayer == null) {
            System.out.println(msg);
            return;
        }
        mc.thePlayer.addChatMessage(new ChatComponentText(msg));
    }
}
