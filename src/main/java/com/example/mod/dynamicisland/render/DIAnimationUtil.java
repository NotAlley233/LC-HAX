package com.example.mod.dynamicisland.render;

/**
 * Frame-rate independent smooth animation utility.
 * Uses deltaTime so animations look identical regardless of FPS.
 *
 * Ported from: https://github.com/Jonqwq/DynamicIsland-System-for-mc-hacked-client
 * (MIT license, attribution retained)
 */
public final class DIAnimationUtil {
    private static long delta;

    private DIAnimationUtil() {}

    public static void setDelta(long delta) {
        DIAnimationUtil.delta = delta;
    }

    public static float smooth(float current, float target, float speed) {
        long deltaTime = delta;

        speed = Math.abs(target - current) * speed;

        if (deltaTime < 1L) {
            deltaTime = 1L;
        }

        final float difference = current - target;
        final float smoothing = Math.max(speed * (deltaTime / 16F), .15F);

        if (difference > speed) {
            current = Math.max(current - smoothing, target);
        } else if (difference < -speed) {
            current = Math.min(current + smoothing, target);
        } else {
            current = target;
        }

        return current;
    }

    public static double smooth(double current, double target, double speed) {
        long deltaTime = delta;

        speed = Math.abs(target - current) * speed;

        if (deltaTime < 1L) {
            deltaTime = 1L;
        }

        final double difference = current - target;
        final double smoothing = Math.max(speed * (deltaTime / 16F), .15F);

        if (difference > speed) {
            current = Math.max(current - smoothing, target);
        } else if (difference < -speed) {
            current = Math.min(current + smoothing, target);
        } else {
            current = target;
        }

        return current;
    }
}
