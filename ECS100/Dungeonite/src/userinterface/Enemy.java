package userinterface;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ecs100.UI;

public class Enemy {
//Enemies object
	private String name;
	private String fileHeader;
	private float colorDepth = 255;
	private int w, h;
	private int health, attack;
	private int maxHealth;
	private float[][][] enemyPixelArray;
	private boolean enemyAlive = true;
	private int scoreWorth;

	public Enemy (String name, int health, int attack) {
		this.health = health;
		this.maxHealth = health;
		this.attack = attack;
		this.name = name;
		this.loadBlockWithName(name);
	}
	
	public void drawImage(double x, double y, int size) {
		for (int row = 0; row < this.h; row++) {
			for (int col = 0; col < this.w; col++) {
				float r = enemyPixelArray[col][row][0];
				float g = enemyPixelArray[col][row][1];
				float b = enemyPixelArray[col][row][2];
				UI.setColor(new Color(r, g, b));
				UI.fillRect((int)(x + col) * size,(int)(y + row) * size, size, size);
			}
		}
		UI.setColor(Color.black);
	}
	
	public int health () {
		return health;
	}
	
	public void takeHit(int hit) {
		this.health -= hit;
	}
	
	public void setScoring (int s) {
		this.scoreWorth = s;
	}
	
	public int scoring () {
		return this.scoreWorth;
	}
	
	public int getAttackPower() {
		return this.attack;
	}
	
	public boolean check() {
		if (this.health < 0) {
			enemyAlive = false;
		} else {
			enemyAlive = true;
		}
		return enemyAlive;
	}
	
	public void displayHBar(double x, double y) {
		
		int step = (int)(100 / this.maxHealth);
		
		int healthBar = this.health * step;
		
		
		for (int i = 0; i < healthBar; i+=step) {
			float r = (float)(120 + i) / 255;
			float g = (float)(i * 2) / 255;
			float b = 0;
			UI.setColor(new Color(r, g, b));
			UI.fillRect(x + i, y, step, 15);
		}
		
		UI.setColor(Color.black);
		UI.setLineWidth(3);
	}
	
	private void loadBlockWithName(String name) {
		try {
			String fileName = name;
			Scanner scanner = null;

			File myFile = new File (fileName);
			scanner = new Scanner(myFile);

			fileHeader = scanner.next();

			w = scanner.nextInt();
			h = scanner.nextInt();

			this.enemyPixelArray = new float[w][h][3];

			colorDepth = scanner.nextFloat();

			Image im = new Image(fileName, fileHeader, w, h, colorDepth);

			for (int row = 0; row < h; row++ ) {
				for (int col = 0; col < w; col++) {
					float pix = scanner.nextFloat();
					enemyPixelArray[col][row][0] = pix / colorDepth;
					pix = scanner.nextFloat();
					enemyPixelArray[col][row][1] = pix / colorDepth;
					pix = scanner.nextFloat();
					enemyPixelArray[col][row][2] = pix / colorDepth;
				}
			}
			
			scanner.close();
			
			im.setPixelArray(enemyPixelArray);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
}
