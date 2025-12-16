package com.absolutephoenix.farminggame.game.world;

public abstract class Item {
    private final String id;

    protected Item(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract String getDisplayName();
}
