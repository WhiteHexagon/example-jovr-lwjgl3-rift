package com.sunshineapps.riftexample.thirdparty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.opengl.GL11;


//DOES NOT WORK!!!!
public final class FixedTexture {
    private final int textureId;
    private final ByteBuffer buffer;

    private FixedTexture(int width, int height, byte[] data) {
        textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        
        buffer = ByteBuffer.allocateDirect(data.length).order(ByteOrder.nativeOrder());
        buffer.put(data);
        buffer.rewind();

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    }

    public FixedTexture(BuiltinTexture builtinTexture) {
        this(256, 256, generateTexture(builtinTexture));
    }
    
    private static byte[] generateTexture(BuiltinTexture builtinTexture) {
        byte[] data = new byte[256 * 256 * 4];

        switch (builtinTexture) {
            case tex_checker:
                for (int j = 0; j < 256; j++) {
                    for (int i = 0; i < 256; i++) {
                        boolean colorB = (((i / 4 >> 5) ^ (j / 4 >> 5)) & 1) == 1;
                        data[(j * 256 + i) * 4 + 0] = (byte)(colorB ? 80 : 180);
                        data[(j * 256 + i) * 4 + 1] = (byte)(colorB ? 80 : 180);
                        data[(j * 256 + i) * 4 + 2] = (byte)(colorB ? 80 : 180);
                        data[(j * 256 + i) * 4 + 3] = (byte)(colorB ? 255 : 255);
                    }
                }
                break;

            case tex_panel:
                for (int j = 0; j < 256; j++) {
                    for (int i = 0; i < 256; i++) {
                        boolean colorB = (i / 4 == 0 || j / 4 == 0);
                        data[(j * 256 + i) * 4 + 0] = (byte)(colorB ? 80 : 180);
                        data[(j * 256 + i) * 4 + 1] = (byte)(colorB ? 80 : 180);
                        data[(j * 256 + i) * 4 + 2] = (byte)(colorB ? 80 : 180);
                        data[(j * 256 + i) * 4 + 3] = (byte)(colorB ? 255 : 255);
                    }
                }
                break;

            default:
                for (int j = 0; j < 256; j++) {
                    for (int i = 0; i < 256; i++) {
                        boolean colorB = ((j / 4 & 15) == 0) || (((i / 4 & 15) == 0) && ((((i / 4 & 31) == 0) ^ (((j / 4 >> 4) & 1)== 1)) == false));
                        data[(j * 256 + i) * 4 + 0] = (byte)(colorB ? 80 : 180);
                        data[(j * 256 + i) * 4 + 1] = (byte)(colorB ? 80 : 180);
                        data[(j * 256 + i) * 4 + 2] = (byte)(colorB ? 80 : 180);
                        data[(j * 256 + i) * 4 + 3] = (byte)(colorB ? 255 : 255);
                    }
                }
        }
        return data;
    }

    public int getId() {
        return textureId;
    }

    public enum BuiltinTexture {
        tex_none,
        tex_checker,
        tex_panel,
        tex_block,
        tex_count
    }
}