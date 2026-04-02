package com.example.mod.dynamicisland.animation;

/**
 * EaseOutExpo easing curve: fast start, smooth deceleration.
 * Formula: 1 - pow(2, -10 * x)
 *
 * Ported from: https://github.com/Jonqwq/DynamicIsland-System-for-mc-hacked-client
 * (MIT license, attribution retained)
 */
public class DIEaseOutExpo extends DIAnimation {

    public DIEaseOutExpo(int ms, double endPoint) {
        super(ms, endPoint);
    }

    public DIEaseOutExpo(int ms, double endPoint, DIDirection direction) {
        super(ms, endPoint, direction);
    }

    @Override
    protected double getEquation(double x) {
        return Math.abs((float) x - 1.0f) < 1.0E-4f ? 1.0 : 1.0 - Math.pow(2, -10 * x);
    }
}
