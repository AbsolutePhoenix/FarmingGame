package com.absolutephoenix.farminggame.game.world;

import com.absolutephoenix.farminggame.game.texture.Texture;

public abstract class Tile {
    private final String id;
    private final Texture texture;

    protected Tile(String id, Texture texture) {
        this.id = id;
        this.texture = texture;
    }

    public String getId() {
        return id;
    }

    public float getWidth() {
        return texture.getWidth();
    }

    public float getHeight() {
        return texture.getHeight();
    }

    public Texture getTexture() {
        return texture;
    }
}
