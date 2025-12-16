package com.absolutephoenix.farminggame.game;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

public class Camera {
    private final Vector2f position = new Vector2f();
    private float viewportWidth;
    private float viewportHeight;

    public Camera(float viewportWidth, float viewportHeight) {
        this.viewportWidth = viewportWidth;
        this.viewportHeight = viewportHeight;
    }

    public void follow(Vector2f target) {
        position.set(target);
    }

    public void applyOrthographic() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(position.x - viewportWidth / 2f, position.x + viewportWidth / 2f,
                position.y + viewportHeight / 2f, position.y - viewportHeight / 2f, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }

    public boolean isVisible(Sprite sprite) {
        float left = position.x - viewportWidth / 2f;
        float right = position.x + viewportWidth / 2f;
        float top = position.y - viewportHeight / 2f;
        float bottom = position.y + viewportHeight / 2f;

        return sprite.getX() + sprite.getWidth() > left &&
                sprite.getX() < right &&
                sprite.getY() + sprite.getHeight() > top &&
                sprite.getY() < bottom;
    }

    public void resize(int width, int height) {
        this.viewportWidth = width;
        this.viewportHeight = height;
    }

    public Vector2f getPosition() {
        return new Vector2f(position);
    }
}
