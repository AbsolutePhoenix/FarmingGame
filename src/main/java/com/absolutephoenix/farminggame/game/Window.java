package com.absolutephoenix.farminggame.game;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Window {
    private long handle;
    private int width;
    private int height;
    private boolean fullscreen;
    private boolean resized;
    private int windowedWidth;
    private int windowedHeight;
    private boolean vsync;

    public Window(int width, int height, boolean fullscreen) {
        this.width = width;
        this.height = height;
        this.windowedWidth = width;
        this.windowedHeight = height;
        this.fullscreen = fullscreen;
        this.vsync = false;
    }

    public void create() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_ANY_PROFILE);

        long monitor = fullscreen ? GLFW.glfwGetPrimaryMonitor() : 0;
        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if (fullscreen && vidmode != null) {
            width = vidmode.width();
            height = vidmode.height();
        }

        handle = GLFW.glfwCreateWindow(width, height, "Farming Game", monitor, 0);
        if (handle == 0) {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        GLFW.glfwMakeContextCurrent(handle);
        GL.createCapabilities();
        GLFW.glfwSwapInterval(vsync ? 1 : 0);

        centerWindow(vidmode);

        GLFW.glfwSetFramebufferSizeCallback(handle, framebufferSizeCallback());

        GLFW.glfwShowWindow(handle);
        GL11.glViewport(0, 0, width, height);
    }

    private void centerWindow(GLFWVidMode vidmode) {
        if (!fullscreen && vidmode != null) {
            GLFW.glfwSetWindowPos(
                    handle,
                    (vidmode.width() - width) / 2,
                    (vidmode.height() - height) / 2
            );
        }
    }

    private GLFWFramebufferSizeCallback framebufferSizeCallback() {
        return new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int newWidth, int newHeight) {
                width = newWidth;
                height = newHeight;
                resized = true;
                GL11.glViewport(0, 0, newWidth, newHeight);
            }
        };
    }

    public void toggleFullscreen() {
        fullscreen = !fullscreen;
        if (fullscreen) {
            windowedWidth = width;
            windowedHeight = height;
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
            if (vidmode != null) {
                GLFW.glfwSetWindowMonitor(handle, GLFW.glfwGetPrimaryMonitor(), 0, 0, vidmode.width(), vidmode.height(), vidmode.refreshRate());
            }
        } else {
            GLFW.glfwSetWindowMonitor(handle, 0, 100, 100, windowedWidth, windowedHeight, GLFW.GLFW_DONT_CARE);
        }
        resized = true;
    }

    public void setVsync(boolean enabled) {
        this.vsync = enabled;
        GLFW.glfwSwapInterval(enabled ? 1 : 0);
    }

    public void update() {
        GLFW.glfwSwapBuffers(handle);
        GLFW.glfwPollEvents();
    }

    public void destroy() {
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
    }

    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    public long getHandle() {
        return handle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isResized() {
        return resized;
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }
}
