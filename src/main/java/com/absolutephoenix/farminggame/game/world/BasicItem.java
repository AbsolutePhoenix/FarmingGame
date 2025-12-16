package com.absolutephoenix.farminggame.game.world;

public class BasicItem extends Item {
    private final String displayName;

    public BasicItem(String id, String displayName) {
        super(id);
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
