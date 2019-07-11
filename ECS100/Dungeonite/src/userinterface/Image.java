package userinterface;

import java.awt.Color;

import ecs100.UI;

public class Image {
	/**
	 * 
	 * Image object for 'block'
	 * 
	 */
	private String name, fileHeader;
	private int width, height;
	private float colorDepth;
	private float[][][] imageArray;

	public Image(String name, String fileHeader, int width, int height, float colorDepth) {

		this.name = name;
		this.fileHeader = fileHeader;
		this.width = width;
		this.height = height;
		this.colorDepth = colorDepth;
		this.imageArray = new float [width][height][3];
	}

	public String getName() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getFileHeader() {
		return fileHeader;
	}

	public void setFileHeader(String fileHeader) {
		this.fileHeader = fileHeader;
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public float getColorDepth() {
		return this.colorDepth;
	}

	public void setColorDepth(float colorDepth) {
		this.colorDepth = colorDepth;
	}

	public float[][][] getPixelArray() {
		return this.imageArray;
	}

	public void setPixelArray(float[][][] pixelsArray) {
		this.imageArray = new float[this.width][this.height][3];
		this.imageArray = pixelsArray;
	}

	public void drawImage(double x, double y, int size) {
		for (int row = 0; row < this.height; row++) {
			for (int col = 0; col < this.width; col++) {
				float r = imageArray[col][row][0];
				float g = imageArray[col][row][1];
				float b = imageArray[col][row][2];
				UI.setColor(new Color(r, g, b));
				UI.fillRect((int)(x + col) * size,(int)(y + row) * size, size, size);
			}
		}
		UI.setColor(Color.black);
	}

}
