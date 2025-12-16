package com.absolutephoenix.farminggame.game;

import org.joml.Vector2f;

public class Sprite {
    private final Vector2f position;
    private final float width;
    private final float height;
    private final float[] color;

    public Sprite(Vector2f position, float width, float height, float[] color) {
        this.position = new Vector2f(position);
        this.width = width;
        this.height = height;
        this.color = color;
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

    public float[] getColor() {
        return color;
    }

    public Vector2f getPosition() {
        return new Vector2f(position);
    }
}
