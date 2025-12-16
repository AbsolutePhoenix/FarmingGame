package com.absolutephoenix.farminggame.game.texture;

public enum TextureType {
    TILE("textures/tile"),
    ITEM("textures/item"),
    GUI("textures/gui"),
    ENTITY("textures/entities"),
    UNKNOWN("textures/unknown");

    private final String directory;

    TextureType(String directory) {
        this.directory = directory;
    }

    public String buildPath(String textureName) {
        return directory + "/" + textureName;
    }
}
