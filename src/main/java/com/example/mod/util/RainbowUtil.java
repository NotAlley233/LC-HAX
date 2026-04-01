package com.example.mod.util;

public final class RainbowUtil {
    private static final char SECTION = '\u00A7'; // '§'
    private static final String[] COLORS = new String[] {
            "c", "6", "e", "a", "b", "9", "d"
    };

    private RainbowUtil() {}

    public static String rainbow(String input) {
        return rainbowOffset(input, 0, true);
    }

    public static String rainbowOffset(String input, int startOffset, boolean appendReset) {
        if (input == null || input.isEmpty()) return "";

        int offset = startOffset % COLORS.length;
        if (offset < 0) offset += COLORS.length;

        StringBuilder out = new StringBuilder(input.length() * 3);
        int colorIdx = offset;

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == SECTION && i + 1 < input.length()) {
                out.append(ch).append(input.charAt(i + 1));
                i++;
                continue;
            }

            if (ch == ' ') {
                out.append(' ');
                continue;
            }

            out.append(SECTION).append(COLORS[colorIdx % COLORS.length]).append(ch);
            colorIdx++;
        }

        if (appendReset) {
            out.append(SECTION).append('r');
        }
        return out.toString();
    }
}
