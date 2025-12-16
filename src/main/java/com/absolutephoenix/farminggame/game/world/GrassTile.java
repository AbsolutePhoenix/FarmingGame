package com.absolutephoenix.farminggame.game.world;

public class GrassTile extends Tile {
    private final float width;
    private final float height;
    private final float[] color;

    public GrassTile(String id, float width, float height, float[] color) {
        super(id);
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float[] getColor() {
        return color;
    }
}
