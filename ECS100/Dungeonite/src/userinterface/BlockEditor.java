package userinterface;
import ecs100.UI;
import ecs100.UIFileChooser;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JColorChooser;

public class BlockEditor {

	/**************************
	 * 						  *
	 * A PPM PGM PIXEL EDITOR *
	 * 						  *
	 **************************/
	//file formating
	private String fileHeader;
	private float colorDepth = 255;
	//image vars
	private int width, height;
	//image array
	private float[][][] pixelArray;
	//tools vars
	private double downX, downY;
	private int scale = 10;
	private int tools = 2;
	private int brushSize = 1;
	private Color col = Color.black;

	/**
	 * 
	 * Constructor
	 * 
	 */
	public BlockEditor() {
		UI.initialise();
		
		UI.addButton("New Image", 			this::newImage);
		UI.addButton("Save", 				this::saveFileHandler);
		UI.addButton("Load", 				this::loadFileHandler);
		
		UI.addSlider("Zoom", 10, 30, 10, 	this::imageZoom);
		UI.addButton("Rotate", 				this::imageRotate);
		
		UI.addButton("Brush", 				this::imagePaint);
		UI.addSlider("Brush size", 1, 6, 1, this::changeBrushSize);
		UI.addButton("Choose colour", 		this::imageColor);
		UI.addButton("Get colour", 			this::getColor);
		UI.addButton("Quit editor", 		this::backToMain);
		
		UI.setMouseMotionListener(			this::mouseMotionListener);
		
		this.newImage(); //prepare vars for a new image
	}
	//back to main class
	private void backToMain() {
		new UserInterface();
	}
	//clear out the old vars and array for a new image
	private void newImage() {
		UI.clearText();
		UI.println("NEW BLOCK IMAGE");
		
		width = 30; height = 30;
		
		pixelArray= new float[width][height][3];
		
		scale = 10;
		tools = 1;
		brushSize = 1;
		col = Color.black;
		colorDepth = 255;
		
		UI.clearGraphics();
		UI.setColor(Color.black);
		UI.setLineWidth(5);
		UI.drawRect(0, 0, width * scale, height * scale);
		UI.setLineWidth(1);
		for (int row = 0; row < height; row++ ) {
			for (int col = 0; col < width; col++) {
				pixelArray[col][row][0] = 1;
				pixelArray[col][row][1] = 1;
				pixelArray[col][row][2] = 1;
			}
		}
	}
	/**
	 * 
	 * File I/O methods
	 * 
	 */
	//save pixelArray image to a file
	private void saveFileHandler() {
		try {			
			String fileName = UIFileChooser.save();
			File myFile = new File(fileName);
			PrintStream ps = new PrintStream(myFile);
			
			ps.println(fileHeader);
			ps.println(width);
			ps.println(height);
			ps.println(colorDepth);
			
			for (int row = 0; row < height; row++ ) {
				for (int col = 0; col < width; col++) {
					float pix = pixelArray[col][row][0] * colorDepth;
					float pix1 = pixelArray[col][row][1] * colorDepth;
					float pix2 = pixelArray[col][row][2] * colorDepth;
					ps.println(pix);
					ps.println(pix1);
					ps.println(pix2);
				}
			}
					
			ps.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	//load compatible image file
	private void loadFileHandler() {
		UI.setColor(new Color(/* red */ 1.0f, /* green */ 1.0f, /* blue */ 1.0f));
		UI.fillRect(0, 0, 1000, 1000);
		try {
			UI.clearPanes();
			String fileName = UIFileChooser.open("Pick an image file");
			Scanner scanner = null;
			
			File myFile = new File (fileName);
			scanner = new Scanner(myFile);
			
			fileHeader = scanner.next();
			
			width = scanner.nextInt();
			height = scanner.nextInt();
			
			pixelArray= new float[width][height][3];
			
			colorDepth = scanner.nextFloat();
			
			Image im = new Image(fileName, fileHeader, width, height, colorDepth);
				
			for (int row = 0; row < height; row++ ) {
				for (int col = 0; col < width; col++) {
					float pix = scanner.nextFloat();
					pixelArray[col][row][0] = pix / colorDepth;
					pix = scanner.nextFloat();
					pixelArray[col][row][1] = pix / colorDepth;
					pix = scanner.nextFloat();
					pixelArray[col][row][2] = pix / colorDepth;
				}
			}
				
			im.setPixelArray(pixelArray);

			scanner.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		UI.clearGraphics();
		UI.setColor(Color.black);
		
		drawImageArrayToScreen();
		
		UI.setColor(Color.black);
		UI.setLineWidth(5);
		UI.drawRect(0, 0, width * scale, height * scale);
		UI.setLineWidth(1);
	}
	//update the screen with array data
	private void drawImageArrayToScreen() {
		UI.clearGraphics();
		for (int row = 0; row < height; row++ ) {
			for (int col = 0; col < width; col++) {
				float cr = (float)(pixelArray[col][row][0]);
				float cg = (float)(pixelArray[col][row][1]);
				float cb = (float)(pixelArray[col][row][2]);
				UI.setColor(new Color(cr, cg, cb));
				UI.fillRect(col * scale, row * scale, scale, scale);
			}
		}
	}	
	//rotate image
	public void imageRotate() {
		int tmp;
		float[][][] tmpArray;
		
		UI.clearGraphics();
		tmpArray = new float[height][width][3];
		for (int y = 0; y < height; y++) {
			for (int x = width - 1; x >= 0; x--) {
				
				tmpArray[y][width - 1 - x] = pixelArray[x][y];
				
			}
		}
		pixelArray = new float[height][width][];
		tmp = width; width = height; height = tmp;
		
		pixelArray = tmpArray;
		this.drawImageArrayToScreen();
	}
	//brush tool selected
	public void imagePaint() {
		tools = 1;
	}
	//pick a colour from loaded image (eye drop like tool)
	public void getColor() {
		tools = 2;
	}
	//change brush size
	public void changeBrushSize(double v) {
		this.brushSize = (int)(v);
	}
	//built in java awt colour picker
	public void imageColor() {
		col = JColorChooser.showDialog(null, "Choose colour", Color.black);
	}
	//redraw loaded image bigger or smaller
	public void imageZoom(double v) {
		scale = (int)v;
		this.drawImageArrayToScreen();
	}
	
	private void mouseMotionListener(String action, double x, double y) {
	
	switch (tools) {
	//BRUSH TOOL
		case 1:
			if (action.equalsIgnoreCase("dragged")) {
				
				if (x < width * scale && y < height * scale &&
						x > 0 && y > 0) {
					
					int drawX = (int)(x / scale);
					int drawY = (int)(y / scale);
					
					UI.setColor(col);
					//UI.fillRect(drawX * scale, drawY * scale, brushSize * scale, brushSize * scale);
					for (int row = 0; row < brushSize; row++) {
						if (drawY + row < height) {
							for (int colm = 0; colm < brushSize; colm++) {
								if (drawX + colm < width) {
									pixelArray[drawX + colm][drawY + row][0] = (float)(col.getRed() / colorDepth);
									pixelArray[drawX + colm][drawY + row][1] = (float)(col.getGreen() / colorDepth);
									pixelArray[drawX + colm][drawY + row][2] = (float)(col.getBlue() / colorDepth);
									UI.fillRect((drawX + colm) * scale, (drawY + row) * scale, scale, scale);
								}
							}
						}
					}
				}
			}
			break;
		//Eye drop tool
		case 2:
			if (action.equalsIgnoreCase("released")) {
				if (x < width * scale && y < height * scale &&
						x > 0 && y > 0) {
					
					int drawX = (int)(x / scale);
					int drawY = (int)(y / scale);
					float r = pixelArray[drawX][drawY][0];
					float g = pixelArray[drawX][drawY][1];
					float b = pixelArray[drawX][drawY][2];
					this.col = new Color(r, g, b);
				}
			}
		}
	}
	
}