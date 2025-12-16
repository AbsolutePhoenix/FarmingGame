package com.absolutephoenix.farminggame.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.HashSet;
import java.util.Set;

public class Input {
    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Set<Integer> pressedThisFrame = new HashSet<>();

    public GLFWKeyCallbackI createKeyCallback(long windowHandle) {
        return (window, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS) {
                pressedKeys.add(key);
                pressedThisFrame.add(key);
                if (key == GLFW.GLFW_KEY_ESCAPE) {
                    GLFW.glfwSetWindowShouldClose(windowHandle, true);
                }
            } else if (action == GLFW.GLFW_RELEASE) {
                pressedKeys.remove(key);
            }
        };
    }

    public boolean isKeyDown(int key) {
        return pressedKeys.contains(key);
    }

    public boolean consumeKeyPress(int key) {
        return pressedThisFrame.remove(key);
    }

    public void endFrame() {
        pressedThisFrame.clear();
    }
}
