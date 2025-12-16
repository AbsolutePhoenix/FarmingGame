package com.absolutephoenix.farminggame.server;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameServer {
    private final int port;
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean running;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        executorService.scheduleAtFixedRate(this::tick, 0, 50, TimeUnit.MILLISECONDS);
    }

    private void tick() {
        if (!running) {
            return;
        }
        // In a real server, networking and world updates would occur here.
    }

    public void stop() {
        running = false;
        executorService.shutdownNow();
    }

    public boolean isRunning() {
        return running;
    }

    public int getPort() {
        return port;
    }
}
