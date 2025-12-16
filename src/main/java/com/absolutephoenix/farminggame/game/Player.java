package com.absolutephoenix.farminggame.game;

import com.absolutephoenix.farminggame.game.texture.Texture;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Player {
    private final Vector2f position;
    private final float speed;
    private final Sprite sprite;

    public Player(Vector2f startPosition, float speed, float size, Texture texture) {
        this.position = new Vector2f(startPosition);
        this.speed = speed;
        this.sprite = new Sprite(position, size, size, texture);
    }

    public void update(double deltaSeconds, Input input) {
        float distance = (float) (speed * deltaSeconds);
        if (input.isKeyDown(GLFW.GLFW_KEY_W)) {
            position.y -= distance;
        }
        if (input.isKeyDown(GLFW.GLFW_KEY_S)) {
            position.y += distance;
        }
        if (input.isKeyDown(GLFW.GLFW_KEY_A)) {
            position.x -= distance;
        }
        if (input.isKeyDown(GLFW.GLFW_KEY_D)) {
            position.x += distance;
        }
    }

    public Vector2f getPosition() {
        return position;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
