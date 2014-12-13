package com.sunshineapps.riftexample.thirdparty;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;


public class Texture {
	public final int id;
	private final int target;

	public Texture(int target) {
		id = glGenTextures();
		this.target = target;
	}

	public Texture() {
		this(GL_TEXTURE_2D);
	}

	public void bind() {
		glBindTexture(target, id);
	}

	public void unbind() {
		unbind(target);
	}

	public static void unbind(int target) {
		glBindTexture(target, 0);
	}

	public void parameter(int pname, int value) {
		glTexParameteri(target, pname, value);
	}

	public void parameter(int pname, float value) {
		glTexParameterf(target, pname, value);
	}

	public void image2d(int internalformat, int width, int height, int format,
			int type, ByteBuffer pixels) {
		glTexImage2D(target, 0, internalformat, width, height, 0, format, type,
				pixels);
	}

	public void loadImageData(BufferedImage image, int loadTarget) {
		// Flip the image vertically
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -image.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = op.filter(image, null);
		GL11.glTexImage2D(loadTarget, 0, GL11.GL_RGBA8, image.getWidth(),
				image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
				convertImageData(image));
	}

	public static Texture loadImage(BufferedImage image, int textureType,
			int loadTarget) {
		Texture result = new Texture(textureType);
		result.bind();
		result.loadImageData(image, loadTarget);
		result.unbind();
		return result;
	}

	/**
	 * Convert the buffered image to a texture
	 */
	public static ByteBuffer convertImageData(BufferedImage bufferedImage) {
		ByteBuffer imageBuffer;
		WritableRaster raster;
		BufferedImage texImage;
		ColorModel glAlphaColorModel = new ComponentColorModel(
				ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8,
						8, 8 }, true, false, Transparency.TRANSLUCENT,
				DataBuffer.TYPE_BYTE);
		raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
				bufferedImage.getWidth(), bufferedImage.getHeight(), 4, null);
		texImage = new BufferedImage(glAlphaColorModel, raster, true,
				new Hashtable<>());
		// copy the source image into the produced image
		Graphics g = texImage.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
		g.drawImage(bufferedImage, 0, 0, null);
		// build a byte buffer from the temporary image
		// that be used by OpenGL to produce a texture.
		byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer())
				.getData();
		BufferUtils.createByteBuffer(data.length);
		imageBuffer = ByteBuffer.allocateDirect(data.length);
		imageBuffer.order(ByteOrder.nativeOrder());
		imageBuffer.put(data, 0, data.length);
		imageBuffer.flip();
		return imageBuffer;
	}
}