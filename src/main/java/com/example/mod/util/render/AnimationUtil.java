package com.example.mod.util.render;

public class AnimationUtil {
    
    /**
     * Calculates a linear interpolation between current and target value.
     * Use delta time to make it frame-rate independent.
     *
     * @param current The current value.
     * @param target The target value.
     * @param speed The speed of the animation.
     * @return The interpolated value.
     */
    public static float lerp(float current, float target, float speed) {
        return current + (target - current) * speed;
    }
    
    /**
     * Calculates a linear interpolation for double values.
     */
    public static double lerp(double current, double target, double speed) {
        return current + (target - current) * speed;
    }

    /**
     * Easing out exponential function for smoother UI transitions.
     */
    public static float easeOutExpo(float t) {
        return t == 1.0f ? 1.0f : 1.0f - (float) Math.pow(2, -10 * t);
    }
}