package me.shadorc.spinvader.utils;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DisplayUtils {

    public static final GraphicsDevice SCREEN = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public static final DisplayMode DISPLAY_MODE = SCREEN.getDisplayMode();
    public static final Map<String, DisplayMode> RESOLUTIONS_MAP = new HashMap<>();

    static {
        Arrays.stream(SCREEN.getDisplayModes())
                .forEach(displayMode -> RESOLUTIONS_MAP.putIfAbsent(displayMode.getWidth() + "x" + displayMode.getHeight(), displayMode));
    }

}