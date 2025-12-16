package com.absolutephoenix.farminggame.game;

import com.absolutephoenix.farminggame.config.GameConfiguration;
import com.absolutephoenix.farminggame.game.texture.Texture;
import com.absolutephoenix.farminggame.game.texture.TextureLoader;
import com.absolutephoenix.farminggame.game.texture.TextureType;
import com.absolutephoenix.farminggame.game.world.GrassTile;
import com.absolutephoenix.farminggame.game.world.Registry;
import com.absolutephoenix.farminggame.game.world.Tile;
import com.absolutephoenix.farminggame.server.GameServer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBEasyFont;

import java.util.ArrayList;
import java.util.List;
import java.nio.ByteBuffer;

public class Game {
    private static final double TARGET_UPS = 60.0;
    private static final double METRICS_INTERVAL = 1_000_000_000.0;

    private final Window window;
    private final Input input;
    private final Camera camera;
    private final SpriteBatch spriteBatch;
    private final Player player;
    private final Registry<Tile> tileRegistry = new Registry<>();
    private final List<Sprite> worldSprites = new ArrayList<>();
    private final GameServer gameServer;
    private final Double targetFps;
    private final Texture defaultTexture;
    private final ByteBuffer textBuffer = BufferUtils.createByteBuffer(2048);

    private int fps;
    private int ups;
    private int frameCounter;
    private int updateCounter;
    private double metricsAccumulator;

    public Game(Window window, GameConfiguration configuration, GameServer gameServer) {
        this.window = window;
        this.input = new Input();
        this.camera = new Camera(window.getWidth(), window.getHeight());
        this.spriteBatch = new SpriteBatch();
        this.defaultTexture = TextureLoader.loadTexture(
                TextureLoader.buildLocation("farminggame", TextureType.UNKNOWN, "default.png")
        );
        this.player = new Player(new Vector2f(0, 0), 200f, 32f, defaultTexture);
        this.gameServer = gameServer;
        this.targetFps = configuration.getTargetFps().orElse(null);
        if (!this.gameServer.isRunning()) {
            this.gameServer.start();
        }
        setupInput();
        seedWorld();
    }

    private void setupInput() {
        GLFW.glfwSetKeyCallback(window.getHandle(), input.createKeyCallback(window.getHandle()));
    }

    private void seedWorld() {
        GrassTile grassTile = new GrassTile("grass", defaultTexture);
        tileRegistry.register(grassTile.getId(), grassTile);
        for (int x = -10; x <= 10; x++) {
            for (int y = -10; y <= 10; y++) {
                Sprite sprite = new Sprite(new Vector2f(x * grassTile.getWidth(), y * grassTile.getHeight()),
                        grassTile.getWidth(), grassTile.getHeight(), grassTile.getTexture());
                worldSprites.add(sprite);
            }
        }
    }

    public void run() {
        double updateInterval = 1_000_000_000.0 / TARGET_UPS;
        double renderInterval = targetFps == null ? 0 : 1_000_000_000.0 / targetFps;

        double lastTime = System.nanoTime();
        double updateAccumulator = 0;
        double renderAccumulator = 0;

        while (!window.shouldClose()) {
            double now = System.nanoTime();
            double delta = now - lastTime;
            lastTime = now;
            updateAccumulator += delta;
            renderAccumulator += delta;
            metricsAccumulator += delta;

            while (updateAccumulator >= updateInterval) {
                update(updateInterval / 1_000_000_000.0);
                updateAccumulator -= updateInterval;
            }

            boolean shouldRender = targetFps == null || renderAccumulator >= renderInterval;
            if (shouldRender) {
                render();
                renderAccumulator = 0;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }

            if (metricsAccumulator >= METRICS_INTERVAL) {
                fps = frameCounter;
                ups = updateCounter;
                frameCounter = 0;
                updateCounter = 0;
                metricsAccumulator -= METRICS_INTERVAL;
            }
        }
    }

    private void update(double deltaSeconds) {
        updateCounter++;
        player.update(deltaSeconds, input);
        camera.follow(player.getPosition());
        if (window.isResized()) {
            camera.resize(window.getWidth(), window.getHeight());
            window.setResized(false);
        }
        if (input.consumeKeyPress(GLFW.GLFW_KEY_F11)) {
            window.toggleFullscreen();
        }
        input.endFrame();
    }

    private void render() {
        frameCounter++;
        GL11.glClearColor(0.1f, 0.1f, 0.2f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        spriteBatch.clear();
        spriteBatch.add(player.getSprite());
        worldSprites.forEach(spriteBatch::add);
        spriteBatch.render(camera);

        renderOverlay();

        window.update();
    }

    private void renderOverlay() {
        String metricsText = String.format("FPS: %d | UPS: %d", fps, ups);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, window.getWidth(), window.getHeight(), 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        textBuffer.clear();
        int quads = STBEasyFont.stb_easy_font_print(10, 10, metricsText, null, textBuffer);

        GL11.glColor3f(1f, 1f, 1f);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glVertexPointer(2, GL11.GL_FLOAT, 16, textBuffer);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, quads * 4);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
    }
}
