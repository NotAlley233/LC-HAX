package myau.ui.overlay.dynamicisland;

import myau.events.Render2DEvent;

public interface IslandTrigger extends Comparable<IslandTrigger> {
    void renderIsland(Render2DEvent event, float posX, float posY, float width, float height, float progress);

    float getIslandWidth();

    float getIslandHeight();

    default boolean isAvailable() {
        return true;
    }

    default int getIslandPriority() {
        return 0;
    }

    @Override
    default int compareTo(IslandTrigger o) {
        return Integer.compare(o.getIslandPriority(), getIslandPriority());
    }
}

