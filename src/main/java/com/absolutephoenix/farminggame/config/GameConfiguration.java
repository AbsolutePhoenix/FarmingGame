package com.absolutephoenix.farminggame.config;

import java.util.Optional;

public class GameConfiguration {
    private final int initialWidth;
    private final int initialHeight;
    private final boolean fullscreen;
    private final Double targetFps;
    private final boolean serverOnly;
    private final int serverPort;

    private GameConfiguration(int initialWidth, int initialHeight, boolean fullscreen, Double targetFps, boolean serverOnly, int serverPort) {
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
        this.fullscreen = fullscreen;
        this.targetFps = targetFps;
        this.serverOnly = serverOnly;
        this.serverPort = serverPort;
    }

    public static GameConfiguration fromArgs(String[] args) {
        int width = 1280;
        int height = 720;
        boolean fullscreen = false;
        Double targetFps = 60.0;
        boolean serverOnly = false;
        int port = 25565;

        for (String arg : args) {
            if ("-server".equalsIgnoreCase(arg)) {
                serverOnly = true;
            } else if (arg.startsWith("-fps=")) {
                targetFps = parseFps(arg.substring("-fps=".length()));
            } else if ("-uncapped".equalsIgnoreCase(arg)) {
                targetFps = null;
            } else if (arg.startsWith("-size=")) {
                String[] parts = arg.substring("-size=".length()).split("x");
                if (parts.length == 2) {
                    try {
                        width = Integer.parseInt(parts[0]);
                        height = Integer.parseInt(parts[1]);
                    } catch (NumberFormatException ignored) {
                        // Keep defaults.
                    }
                }
            } else if (arg.startsWith("-port=")) {
                try {
                    port = Integer.parseInt(arg.substring("-port=".length()));
                } catch (NumberFormatException ignored) {
                    // Keep defaults.
                }
            } else if ("-fullscreen".equalsIgnoreCase(arg)) {
                fullscreen = true;
            }
        }

        return new GameConfiguration(width, height, fullscreen, targetFps, serverOnly, port);
    }

    private static Double parseFps(String value) {
        try {
            double fps = Double.parseDouble(value);
            return fps > 0 ? fps : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public int getInitialWidth() {
        return initialWidth;
    }

    public int getInitialHeight() {
        return initialHeight;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public Optional<Double> getTargetFps() {
        return Optional.ofNullable(targetFps);
    }

    public boolean isServerOnly() {
        return serverOnly;
    }

    public int getServerPort() {
        return serverPort;
    }
}
