package com.absolutephoenix.farminggame.game;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class SpriteBatch {
    private final List<Sprite> sprites = new ArrayList<>();

    public void add(Sprite sprite) {
        sprites.add(sprite);
    }

    public void clear() {
        sprites.clear();
    }

    public void render(Camera camera) {
        camera.applyOrthographic();
        GL11.glBegin(GL11.GL_QUADS);
        for (Sprite sprite : sprites) {
            if (!camera.isVisible(sprite)) {
                continue;
            }
            float[] color = sprite.getColor();
            GL11.glColor3f(color[0], color[1], color[2]);
            float x = sprite.getX();
            float y = sprite.getY();
            float w = sprite.getWidth();
            float h = sprite.getHeight();

            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x + w, y);
            GL11.glVertex2f(x + w, y + h);
            GL11.glVertex2f(x, y + h);
        }
        GL11.glEnd();
    }
}
