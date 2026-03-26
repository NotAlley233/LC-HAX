package com.example.mod.util;

public final class RainbowUtil {
    private static final char SECTION = '\u00A7'; // '§'
    private static final String[] COLORS = new String[] {
            "c", "6", "e", "a", "b", "9", "d"
    };

    private RainbowUtil() {}

    public static String rainbow(String input) {
        if (input == null || input.isEmpty()) return "";

        StringBuilder out = new StringBuilder(input.length() * 3);
        int colorIdx = 0;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            // Preserve existing formatting codes (e.g. "§a")
            if (ch == SECTION && i + 1 < input.length()) {
                out.append(ch).append(input.charAt(i + 1));
                i++;
                continue;
            }

            // Don't color spaces; keeps gradient aligned to visible chars.
            if (ch == ' ') {
                out.append(' ');
                continue;
            }

            out.append(SECTION).append(COLORS[colorIdx % COLORS.length]).append(ch);
            colorIdx++;
        }

        // Reset formatting after prefix so it doesn't bleed into message body.
        out.append(SECTION).append('r');
        return out.toString();
    }
}

