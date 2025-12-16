package com.absolutephoenix.farminggame.game.world;

public abstract class Tile {
    private final String id;

    protected Tile(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract float getWidth();

    public abstract float getHeight();

    public abstract float[] getColor();
}
