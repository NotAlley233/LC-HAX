//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
package myau.module.modules;

import myau.module.Module;
import myau.property.properties.IntProperty;

public class SwingSpeed extends Module {
    public final IntProperty speed = new IntProperty("speed", 6, 1, 20);

    public SwingSpeed() {
        super("SwingSpeed", false, true);
    }

    public String[] getSuffix() {
        return new String[]{String.valueOf(this.speed.getValue())};
    }
}
