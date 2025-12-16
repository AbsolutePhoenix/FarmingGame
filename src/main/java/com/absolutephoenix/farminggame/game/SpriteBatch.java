package com.absolutephoenix.farminggame.game;

import com.absolutephoenix.farminggame.game.texture.Texture;
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
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        int boundTexture = -1;
        GL11.glBegin(GL11.GL_QUADS);
        for (Sprite sprite : sprites) {
            if (!camera.isVisible(sprite)) {
                continue;
            }
            Texture texture = sprite.getTexture();
            if (texture != null && texture.getId() != boundTexture) {
                GL11.glEnd();
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
                GL11.glBegin(GL11.GL_QUADS);
                boundTexture = texture.getId();
            }

            GL11.glColor3f(1f, 1f, 1f);
            float x = sprite.getX();
            float y = sprite.getY();
            float w = sprite.getWidth();
            float h = sprite.getHeight();

            GL11.glTexCoord2f(0f, 0f);
            GL11.glVertex2f(x, y);
            GL11.glTexCoord2f(1f, 0f);
            GL11.glVertex2f(x + w, y);
            GL11.glTexCoord2f(1f, 1f);
            GL11.glVertex2f(x + w, y + h);
            GL11.glTexCoord2f(0f, 1f);
            GL11.glVertex2f(x, y + h);
        }
        GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }
}
