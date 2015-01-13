package com.sunshineapps.riftexample.thirdparty;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author gbarbieri
 */
public class FixedTexture {
    private final int textureId;
    private final ByteBuffer buffer;

    private FixedTexture(Vec2i sizei, byte[] data) {
        textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        
        buffer = ByteBuffer.allocateDirect(data.length).order(ByteOrder.nativeOrder());
        buffer.put(data);
        buffer.rewind();

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, sizei.x, sizei.y, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    }

    public static FixedTexture createBuiltinTexture(BuiltinTexture builtinTexture) {

        byte[] data = new byte[256 * 256 * 4];

        switch (builtinTexture) {

            case tex_checker:

                Vec4 a = new Vec4(180f, 180f, 180f, 255f);

                Vec4 b = new Vec4(80f, 80f, 80f, 255f);

                for (int j = 0; j < 256; j++) {

                    for (int i = 0; i < 256; i++) {

                        Vec4 color = (((i / 4 >> 5) ^ (j / 4 >> 5)) & 1) == 1 ? b : a;

                        data[(j * 256 + i) * 4] = (byte) color.x;
                        data[(j * 256 + i) * 4 + 1] = (byte) color.y;
                        data[(j * 256 + i) * 4 + 2] = (byte) color.z;
                        data[(j * 256 + i) * 4 + 3] = (byte) color.w;
                    }
                }
                break;

            case tex_panel:

                a = new Vec4(80f, 80f, 80f, 255f);

                b = new Vec4(180f, 180f, 180f, 255f);

                for (int j = 0; j < 256; j++) {

                    for (int i = 0; i < 256; i++) {

                        Vec4 color = (i / 4 == 0 || j / 4 == 0) ? a : b;

                        data[(j * 256 + i) * 4] = (byte) color.x;
                        data[(j * 256 + i) * 4 + 1] = (byte) color.y;
                        data[(j * 256 + i) * 4 + 2] = (byte) color.z;
                        data[(j * 256 + i) * 4 + 3] = (byte) color.w;
                    }
                }
                break;

            default:

                a = new Vec4(60f, 60f, 60f, 255f);

                b = new Vec4(180f, 180f, 180f, 255f);

                for (int j = 0; j < 256; j++) {

                    for (int i = 0; i < 256; i++) {

                        boolean c = ((j / 4 & 15) == 0) || (((i / 4 & 15) == 0) && ((((i / 4 & 31) == 0) ^ (((j / 4 >> 4) & 1)== 1)) == false));
                        
                        Vec4 color = c ? a : b;
//                        boolean c = ((j / 4 & 15) == 0) || ((i / 4 & 15) == 0);
//                        int d = ((i / 4 & 31) == 0) ? 0 : 1;
//                        d = d ^ (j / 4 >> 4);
//                        boolean e = c && (d == 1);
//                        Vec4 color = e ? a : b;

                        data[(j * 256 + i) * 4] = (byte) color.x;
                        data[(j * 256 + i) * 4 + 1] = (byte) color.y;
                        data[(j * 256 + i) * 4 + 2] = (byte) color.z;
                        data[(j * 256 + i) * 4 + 3] = (byte) color.w;
                    }
                }
        }
        return new FixedTexture(new Vec2i(256, 256), data);
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