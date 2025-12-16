package com.absolutephoenix.farminggame.game.world;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Registry<T> {
    private final Map<String, T> entries = new HashMap<>();

    public void register(String id, T entry) {
        entries.put(id, entry);
    }

    public Optional<T> get(String id) {
        return Optional.ofNullable(entries.get(id));
    }

    public Collection<T> all() {
        return entries.values();
    }
}
