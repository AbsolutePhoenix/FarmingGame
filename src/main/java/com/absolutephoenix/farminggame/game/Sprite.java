package com.absolutephoenix.farminggame.game;

import com.absolutephoenix.farminggame.game.texture.Texture;
import org.joml.Vector2f;

public class Sprite {
    private final Vector2f position;
    private final float width;
    private final float height;
    private final Texture texture;

    public Sprite(Vector2f position, Texture texture) {
        this(position, texture.getWidth(), texture.getHeight(), texture);
    }

    public Sprite(Vector2f position, float width, float height, Texture texture) {
        this.position = new Vector2f(position);
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return new Vector2f(position);
    }
}
