package com.example.mod.module.modules;

import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Cape extends BaseModule {
    public static final List<String> CAPE_OPTIONS = Collections.unmodifiableList(Arrays.asList(
            "2011",
            "2012",
            "2013",
            "2015",
            "2016",
            "Experience",
            "Founder",
            "Cobalt",
            "Astolfo",
            "Moon",
            "Myau",
            "Raven"
    ));

    private String selectedCape = "2011";

    public Cape() {
        super("cape", "Renders a selectable cape on local player.", Category.RENDER, false);
    }

    public String getSelectedCape() {
        return selectedCape;
    }

    public void setSelectedCape(String selectedCape) {
        this.selectedCape = normalizeCape(selectedCape);
    }

    public static String normalizeCape(String selectedCape) {
        if (selectedCape != null) {
            String normalized = selectedCape.trim();
            for (String cape : CAPE_OPTIONS) {
                if (cape.equalsIgnoreCase(normalized)) {
                    return cape;
                }
            }
        }
        return "2011";
    }
}
