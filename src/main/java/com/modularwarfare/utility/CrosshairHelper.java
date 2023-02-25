package com.modularwarfare.utility;

import java.lang.reflect.Field;

/**
 * Author: MrCrayfish
 */
public class CrosshairHelper {
    private static Boolean loaded = null;

    public static boolean isLoaded() {
        if (loaded == null) {
            try {
                Class.forName("sparkless101.crosshairmod.crosshair.Crosshair");
                loaded = true;
            } catch (ClassNotFoundException e) {
                loaded = false;
            }
        }
        return loaded;
    }
}