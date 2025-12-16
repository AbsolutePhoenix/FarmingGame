package com.absolutephoenix.farminggame.game.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.stb.STBImage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TextureLoader {
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(
            "farminggame",
            TextureType.UNKNOWN.buildPath("default.png")
    );
    private static final Map<ResourceLocation, Texture> CACHE = new HashMap<>();

    private TextureLoader() {
    }

    public static Texture getUnknownTexture() {
        return loadTexture(DEFAULT_TEXTURE);
    }

    public static Texture loadTexture(ResourceLocation location) {
        Objects.requireNonNull(location, "location");
        return CACHE.computeIfAbsent(location, TextureLoader::loadInternal);
    }

    public static ResourceLocation buildLocation(String namespace, TextureType type, String textureName) {
        return new ResourceLocation(namespace, type.buildPath(textureName));
    }

    private static Texture loadInternal(ResourceLocation location) {
        ByteBuffer imageBuffer = readResource(location.getFullPath());
        if (imageBuffer == null) {
            if (!location.equals(DEFAULT_TEXTURE)) {
                return loadTexture(DEFAULT_TEXTURE);
            }
            throw new IllegalStateException("Missing default texture: " + location.getFullPath());
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer components = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, width, height, components, 4);
            if (image == null) {
                if (!location.equals(DEFAULT_TEXTURE)) {
                    return loadTexture(DEFAULT_TEXTURE);
                }
                throw new IllegalStateException("Failed to load default texture: " + STBImage.stbi_failure_reason());
            }

            int textureId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width.get(0), height.get(0), 0,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

            STBImage.stbi_image_free(image);
            return new Texture(textureId, width.get(0), height.get(0));
        }
    }

    private static ByteBuffer readResource(String resourcePath) {
        try (InputStream stream = TextureLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null) {
                return null;
            }
            byte[] data = stream.readAllBytes();
            ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
            buffer.put(data);
            buffer.flip();
            return buffer;
        } catch (IOException e) {
            return null;
        }
    }
}
