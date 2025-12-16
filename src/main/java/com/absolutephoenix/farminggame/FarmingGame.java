package com.absolutephoenix.farminggame;

import com.absolutephoenix.farminggame.config.GameConfiguration;
import com.absolutephoenix.farminggame.game.Game;
import com.absolutephoenix.farminggame.game.Window;
import com.absolutephoenix.farminggame.server.GameServer;

public class FarmingGame {
    public static void main(String[] args) {
        GameConfiguration configuration = GameConfiguration.fromArgs(args);
        GameServer server = new GameServer(configuration.getServerPort());

        if (configuration.isServerOnly()) {
            server.start();
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            return;
        }

        server.start();
        Window window = new Window(configuration.getInitialWidth(), configuration.getInitialHeight(), configuration.isFullscreen());
        window.create();
        window.setVsync(false);

        Game game = new Game(window, configuration, server);
        try {
            game.run();
        } finally {
            server.stop();
            window.destroy();
        }
    }
}
