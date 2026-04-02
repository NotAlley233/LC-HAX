package com.example.mod.dynamicisland.animation;

/**
 * Time-driven animation base class with direction support.
 * When direction is reversed mid-animation, remaining time is automatically calculated
 * so the animation reverses smoothly from wherever it currently is.
 *
 * Ported from: https://github.com/Jonqwq/DynamicIsland-System-for-mc-hacked-client
 * (MIT license, attribution retained)
 */
public abstract class DIAnimation {
    private final DIAnimTimeUtil timerUtil = new DIAnimTimeUtil();
    private int duration;
    private double endPoint;
    private DIDirection direction;

    public DIAnimation(int ms, double endPoint) {
        this(ms, endPoint, DIDirection.FORWARDS);
    }

    public DIAnimation(int ms, double endPoint, DIDirection direction) {
        this.duration = ms;
        this.endPoint = endPoint;
        this.direction = direction;
    }

    public boolean finished(DIDirection direction) {
        return this.isDone() && this.direction.equals(direction);
    }

    public double getLinearOutput() {
        return 1.0 - (double) this.timerUtil.getTime() / (double) this.duration * this.endPoint;
    }

    public void reset() {
        this.timerUtil.reset();
    }

    public boolean isDone() {
        return this.timerUtil.hasTimeElapsed(this.duration);
    }

    public void changeDirection() {
        this.setDirection(this.direction.opposite());
    }

    public boolean isForward() {
        return this.direction.forwards();
    }

    public DIDirection getDirection() {
        return direction;
    }

    public DIAnimation setDirection(DIDirection direction) {
        if (this.direction != direction) {
            this.direction = direction;
            this.timerUtil.setTime(
                    System.currentTimeMillis()
                            - ((long) this.duration - Math.min(this.duration, this.timerUtil.getTime()))
            );
        }
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(double endPoint) {
        this.endPoint = endPoint;
    }

    protected boolean correctOutput() {
        return false;
    }

    public double getOutput() {
        if (this.direction.forwards()) {
            if (this.isDone()) {
                return this.endPoint;
            }
            return this.getEquation((double) this.timerUtil.getTime() / (double) this.duration) * this.endPoint;
        }
        if (this.isDone()) {
            return 0.0;
        }
        if (this.correctOutput()) {
            double revTime = Math.min(this.duration, Math.max(0L, (long) this.duration - this.timerUtil.getTime()));
            return this.getEquation(revTime / (double) this.duration) * this.endPoint;
        }
        return (1.0 - this.getEquation((double) this.timerUtil.getTime() / (double) this.duration)) * this.endPoint;
    }

    protected abstract double getEquation(double t);
}
