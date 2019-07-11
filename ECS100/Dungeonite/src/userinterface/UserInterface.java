package userinterface;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import ecs100.UI;
import ecs100.UIFileChooser;
import userinterface.Image;

public class UserInterface {
	
	/**
	 * 
	 * THE MAIN MENU 
	 * 
	 * >Future expansion:
	 * 
	 * >>Player:
	 * 		different sprite for movement direction
	 * 		inventory system
	 * >>Enemy:
	 * 		moving enemy
	 * 		simple AI
	 * >>Events
	 * 		triggering a simple conversation bubble
	 * >>Door and keys
	 * 		interactive block that can be open or trigger an event
	 * >>Boss fight
	 * 
	 * >REFINEMENT:
	 * program can't read keystate correctly after randomEnemy method ended
	 * need to change the window and get back
	 * create a Main Game Loop that will handle the game
	 * read the keys using java built in key listener
	 * using java graphic method to render the game faster 
	 * 
	 */
	
	//Arrays
	public Element [][] playArea;
	public Element [][] playAreaItems;
	public Element [][] screenArea;
	//Player vars
	public Element playerBody;
	private Element oldBlock;
	private int playerX = 0, playerY = 0;
	private int oldPlX = 0, oldPlY = 0;
	public int playerHealth = 100;
	public int playerMagic = 5;
	//PlayArea vars
	public int width;
	public int height;
	private int screenW = 12;
	private int screenH = 12;
	private int screenIndex = 0;
	//Blocks var
	private double size = 30;
	private float colorDepth = 255;
	float[][][] pixelArray;
	ArrayList<Image> images = new ArrayList<Image>();
	private Color c = Color.black;
	private int activeIndex = 0;
	private String fileHeader = "";
	//Gameplay vars
	private String[] levels;
	public int score = 0;
	private int level = 0;
	public boolean isAlive = false, isWon = false, isEnemy = false;
	//Random Enemy vars
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	Enemy activeEnemy;
	Enemy activePlayer;
	
	public UserInterface() {
		UI.initialise();
		
		UI.addButton("Block editor", 	this::blockEdit);
		UI.addButton("Level editor", 	this::levelEdit);		
		UI.addButton("Start a level", 	this::newGame);
		UI.addButton("Custom game", 	this::customGame);
		//UI.addButton("ranEnemy", this::ranEnemy);
		UI.addButton("Exit game", 		UI	::quit);
		UI.setKeyListener(				this::playerMove);
		//UI.setMouseListener(this::doMouse);
		//this.checkRoutine();
		this.setupEnemies(); //instantiate enemy objects
	}
	
	/**
	 *
	 * Calling other classes
	 * 
	 */
	//Class to create 'block' to use in the level editor (pixel editor)
	public void blockEdit() {
		new BlockEditor();
	}
	//Class to create level that can be save and loaded and played
	public void levelEdit() {
		new LevelEditor();
	}
	//debugging
	public void ranEnemy() {
		new RandomEnemy(null);
	}
	
	/**
	 * 
	 * MAIN MENU METHODS
	 * 
	 */
	//THE GAME START HERE
	public void newGame() {
		width = 24; height = 12;
		screenIndex = 0;
		isAlive = true;	
		images.clear();
		level = 0;
		
		levels = new String[4];
		
		levels[0] = "level1.level";
		levels[1] = "level2.level";
		levels[2] = "level3.level";
		levels[3] = "level4.level";
		
		this.nextLevel();
	}
	//instruction section
	public void instructions() {
		UI.println("Make your way to the black box to finish a level");
		UI.println("Collect apples along the way for points");
		UI.println("Press 'Start Game' to load the next level");
	}
	//USE TO TEST LEVEL and DEBUGGING
	public void customGame() {
		screenArea = new Element[screenW][screenH];
		screenIndex = 0;
		images.clear();
		String fileName = UIFileChooser.open("Pick a custom LEVEL file");
		levels = new String[1];
		levels[0] = fileName;
		this.doLoad(fileName);
		isAlive = true;
	}
	//check for next level
	public void nextLevel() {
		width = 24; height = 12;
		screenIndex = 0;
		isAlive = true;	
		images.clear();

		if(level < levels.length) {
			this.doLoad(levels[level]);
		} else {
			level = 0;
		}
	}
	//no more level or game over
	public void finishGame() {

		if (isWon) {
			isAlive = false;
			level++;
			UI.println("Yay");
			UI.println("Score: " + score);
			this.nextLevel();
		} else {
			UI.println("Sorry you lose");
			UI.println("Score: " + score);
			this.nextLevel();
		}
	}
	
	/**
	 * 
	 * File I/O section 
	 * 
	 */
	//Loading a ppm file and use it as a 'block'
	public void loadBlockWithName(String name) {
		try {
			String fileName = name;
			Scanner scanner = null;

			File myFile = new File (fileName);
			scanner = new Scanner(myFile);

			fileHeader = scanner.next();

			int w = scanner.nextInt();
			int h = scanner.nextInt();

			this.pixelArray = new float[w][h][3];

			colorDepth = scanner.nextFloat();

			Image im = new Image(fileName, fileHeader, w, h, colorDepth);

			for (int row = 0; row < h; row++ ) {
				for (int col = 0; col < w; col++) {
					float pix = scanner.nextFloat();
					pixelArray[col][row][0] = pix / colorDepth;
					pix = scanner.nextFloat();
					pixelArray[col][row][1] = pix / colorDepth;
					pix = scanner.nextFloat();
					pixelArray[col][row][2] = pix / colorDepth;
				}
			}
			
			scanner.close();
			
			im.setPixelArray(pixelArray);
			images.add(im);
			//im.drawImage(31 * size, 0, 1);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	//LOADING LEVEL file .level
	public void doLoad(String filename) {
		UI.clearGraphics();
		try {
			Scanner sc = new Scanner(new File(filename));
			
			int l = sc.nextInt();
			sc.nextLine();
			
			for(int i = 0; i < l; i++) {
				String fileName = sc.nextLine();
				this.loadBlockWithName(fileName);
			}
			
			this.width = sc.nextInt();
			sc.nextLine();
			
			this.height = sc.nextInt();
			sc.nextLine();
			
			playArea = new Element[width][height];
			screenW = height; screenH = height;
			for (int i = 0; i < height; i++) {
				
				for (int j = 0; j < width; j++) {
					if (sc.hasNext()) {
						
						String name = sc.nextLine();
						String blockName = sc.nextLine();
						
						if (name.equalsIgnoreCase("block")) {
							Element block = new Block(j * size, i * size, size, c);
							block.setBlockName(blockName);
							//UI.println(block.getName() + " "+block.getColor());
							//block.Draw();
							playArea [j][i] = block;
						} else if(name.equalsIgnoreCase("player")) {
							Element block = new Player(j * size, i * size, size, c);
							block.setBlockName(blockName);
							playerBody = block;
							playerX = j; playerY = i;
							//UI.println(block.getName() + " "+block.getColor());
							//block.Draw();
							playArea [j][i] = block;
						} else if(name .equalsIgnoreCase("apple")) {
							Element block = new Apple(j * size, i * size, size, c);
							block.setBlockName(blockName);
							//UI.println(block.getName() + " "+block.getColor());
							//block.Draw();
							playArea [j][i] = block;
						} else if(name.equalsIgnoreCase("finish")) {
							Element block = new Finish(j * size, i * size, size, c);
							block.setBlockName(blockName);
							//UI.println(block.getName() + " "+block.getColor());
							//block.Draw();
							playArea [j][i] = block;
						} else if(name.equalsIgnoreCase("empty")) {
							Element block = new EmptyBlock(j * size, i * size, size, c);
							block.setBlockName(blockName);
							//UI.println(block.getName() + " "+block.getColor());
							//block.Draw();
							playArea [j][i] = block;
						}
					}
				}
			}
			sc.close();
			this.drawArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.drawArray();
	}
	
	/**
	 * 
	 * Gameplay methods
	 * 
	 */
	
	//Read user movement into player's movement
	public void playerMove(String k) {
		
		if (k.equalsIgnoreCase("a") && !isEnemy) { // left	
			oldPlX = playerX; oldPlY = playerY;
			playerX--;
			if (playerX < 0 || playerX >= width) {
				playerX = oldPlX;
			}
			oldBlock = playArea[playerX][playerY];
			if(playerX >= screenW/2 && playerX < width - screenW/2) {
				screenIndex--;
				if (screenIndex < 0) { screenIndex = 0; }
			} else if (playerX < screenW/2) { 
				screenIndex = 0; 
			} 
			Element getBlock = playArea[playerX][playerY];
			if (getBlock.getName().equalsIgnoreCase("block")) {
				playerX = oldPlX;
				screenIndex++;
			} else if (getBlock.getName().equalsIgnoreCase("apple")) {
				Element em = new EmptyBlock(playerX * size, playerY * size, size, Color.white);
				em.setBlockName("grass.ppm");
				oldBlock = em;
				score++;
				UI.println(score);
			} else if (getBlock.getName().equalsIgnoreCase("finish")) {
				Element em = new EmptyBlock(playerX * size, playerY * size, size, Color.white);
				em.setBlockName("grass.ppm");
				oldBlock = em;
				score += 10;
				UI.println(score);
				isWon = true;
				this.finishGame();
			}
			this.updatePlayerPosition();
			
		} else if (k.equalsIgnoreCase("d") && !isEnemy) { // right
			oldPlX = playerX; oldPlY = playerY;
			playerX++;
			if (playerX < 0 || playerX >= width) {
				playerX = oldPlX;
			}
			oldBlock = playArea[playerX][playerY];
			if(playerX >= screenW/2 && playerX < width - screenW/2) {
				screenIndex++;
				if (screenIndex >= width - screenW) { screenIndex = width - screenW; }
			} else if (playerX >= width - screenW/2) {
				screenIndex = width - screenW;
			}
			Element getBlock = playArea[playerX][playerY];
			if (getBlock.getName().equalsIgnoreCase("block")) {
				playerX = oldPlX;
				screenIndex--;
				if (screenIndex < 0) { screenIndex = 0; }
			} else if (getBlock.getName().equalsIgnoreCase("apple")) {
				score++;
				Element em = new EmptyBlock(playerX * size, playerY * size, size, Color.white);
				em.setBlockName("grass.ppm");
				oldBlock = em;
				UI.println(score);
			} else if (getBlock.getName().equalsIgnoreCase("finish")) {
				score += 10;
				Element em = new EmptyBlock(playerX * size, playerY * size, size, Color.white);
				em.setBlockName("grass.ppm");
				oldBlock = em;
				UI.println(score);
				isWon = true;
				this.finishGame();
			}
			this.updatePlayerPosition();
			
		} else if (k.equalsIgnoreCase("w") && !isEnemy) { // up			
			oldPlX = playerX; oldPlY = playerY;
			playerY--;
			if (playerY < 0 || playerY >= height) {
				playerY = oldPlY;
			}
			oldBlock = playArea[playerX][playerY];
			Element getBlock = playArea[playerX][playerY];
			if (getBlock.getName().equalsIgnoreCase("block")) {
				playerY = oldPlY;
			} else if (getBlock.getName().equalsIgnoreCase("apple")) {
				score++;
				Element em = new EmptyBlock(playerX * size, playerY * size, size, Color.white);
				em.setBlockName("grass.ppm");
				oldBlock = em;
				UI.println(score);
			} else if (getBlock.getName().equalsIgnoreCase("finish")) {
				score += 10;
				Element em = new EmptyBlock(playerX * size, playerY * size, size, Color.white);
				em.setBlockName("grass.ppm");
				oldBlock = em;
				UI.println(score);
				isWon = true;
				this.finishGame();
			}
			this.updatePlayerPosition();
			
		} else if (k.equalsIgnoreCase("s") && !isEnemy) { // down			
			oldPlX = playerX; oldPlY = playerY;
			playerY++;
			if (playerY < 0 || playerY >= height) {
				playerY = oldPlY;
			}
			oldBlock = playArea[playerX][playerY];
			Element getBlock = playArea[playerX][playerY];
			if (getBlock.getName().equalsIgnoreCase("block")) {
				playerY = oldPlY;
			} else if (getBlock.getName().equalsIgnoreCase("apple")) {
				score++;
				Element em = new EmptyBlock(playerX * size, playerY * size, size, Color.white);
				em.setBlockName("grass.ppm");
				oldBlock = em;
				UI.println(score);
			} else if (getBlock.getName().equalsIgnoreCase("finish")) {
				score += 10;
				Element em = new EmptyBlock(playerX * size, playerY * size, size, Color.white);
				em.setBlockName("grass.ppm");
				oldBlock = em;
				UI.println(score);
				isWon = true;
				this.finishGame();
			}
			this.updatePlayerPosition();
		}
	}

	//Updating player movement in the array
	public void updatePlayerPosition() {
		//put a block to prev player pos
		playArea[oldPlX][oldPlY] = oldBlock;
		//put a player in the new pos
		playArea[playerX][playerY] = playerBody;
		
		//random enemy encounter
		int ran = (int)(Math.random() * 100);
		if(ran > 10 && ran < 15) {
			isEnemy = true;
			this.randomEnemy();
			/*
			UserInterface ui = new UserInterface();
			new RandomEnemy(ui);*/
		}
		
		//update the screen
		this.drawArray();
		
	}

	//Update the screen array with data from playArea array
		public void drawArray() {
			for(int row = 0; row < screenH; row++) {
				for(int col = 0; col < screenW; col++) {
				
					Element b = playArea[col + screenIndex][row];
					if (b!=null) {
						for (Image im : images) {
				
							if (b.getBlockName().contentEquals(im.getName())) {
								im.drawImage(col * size, row * size, 2);
							} 
						}
					}
				}
			}
		}
	
	/*public void doMouse(String action, double x, double y) {
		
	}*/
	
	/*private void checkRoutine() {
		while(isAlive) {
			UI.setKeyListener(this::playerMove);
		}
		this.finishGame();
	}*/
	
	public static void main(String[] args) {
		new UserInterface();
	}
	
	/**
	 * 
	 * additional method for creating random enemy every now and then
	 * have them in separate class suppose I can get it work that way
	 * 
	 */
	//create a new enemy
	private void randomEnemy() {

		UI.clearGraphics();

		this.pickRandomEnemy();
		String actionTaken = ""; int def = 0, damage = 0;
		while (activeEnemy.check() && activePlayer.check()) {
			//player's move
			actionTaken = UI.askString("[A]ttack! [D]efend [R]etreat");

			if (actionTaken.equalsIgnoreCase("a")) {
				UI.setColor(Color.red);
				UI.drawString("ATTACK!", 300, 50);
				damage = (int) (Math.random() * (activePlayer.getAttackPower())) + 1;
				UI.drawString(Integer.toString(damage), 300, 200);
				activeEnemy.takeHit(damage);
				UI.setColor(Color.black);
			}
			else if (actionTaken.equalsIgnoreCase("d")) {
				UI.setColor(Color.blue);
				UI.drawString("DEFEND", 300, 50);
				def = 2;
				UI.drawString(Integer.toString(def), 50, 180);
				UI.setColor(Color.black);
			}
			else if (actionTaken.equalsIgnoreCase("r")) {
				UI.setColor(Color.blue);
				UI.drawString("RUN...", 300, 50);
				int runChance = (int)(Math.random() * 100);
				if (runChance > 25 && runChance < 50) {
					
				} else {
					UI.setColor(Color.red);
					UI.drawString("FAILED", 300, 70);
				}
				UI.setColor(Color.black);
			} else {
				UI.setColor(Color.black);
				UI.drawString("wrong action", 300, 50);
			}

			try
			{
				Thread.sleep(1200);
			}
			catch(InterruptedException ex)
			{
				Thread.currentThread().interrupt();
			}

			//enemy's move
			int enemyMove = (int)(Math.random() * 100);
			if (enemyMove > 15 && enemyMove < 50) {
				UI.setColor(Color.red);
				UI.drawString("ATTACK!", 50, 400);
				damage = (int) (Math.random() * (activeEnemy.getAttackPower())) + 1;
				damage -=def;
				if(damage < 0) { damage = 0; }
				def = 0;
				UI.drawString(Integer.toString(damage), 50, 200);
				activePlayer.takeHit(damage);
				UI.setColor(Color.black);
			} else {
				UI.setColor(Color.blue);
				UI.drawString("DEFEND", 50, 400);
				def = activeEnemy.getAttackPower() / 3;
				UI.drawString(Integer.toString(def), 250, 400);
				UI.setColor(Color.black);
			}

			try
			{
				Thread.sleep(1200);
			}
			catch(InterruptedException ex)
			{
				Thread.currentThread().interrupt();
			}
			UI.clearPanes();
			this.updateStat();
		}
		if (!activeEnemy.check() && activePlayer.check()) { 
			score += activeEnemy.scoring();
			playerHealth = activePlayer.health();
		} else if (!activePlayer.check() && activeEnemy.check()) {
			isAlive = false;
			this.finishGame();
		}
		isEnemy = false;
		
	}
	
	//instantiate enemies objects method
	private void setupEnemies() {
		Enemy e = new Enemy("blob1.ppm", 10, 4);
		e.setScoring(20);
		enemies.add(e);
		e = new Enemy("angry blob.ppm", 15, 6);
		e.setScoring(35);
		enemies.add(e);
		e = new Enemy("skull.ppm", 20, 8);
		e.setScoring(50);
		enemies.add(e);
		e = new Enemy("player_d.ppm", playerHealth, playerMagic);
		activePlayer = e;
	}

	//pick a random enemy from enemies arraylist
	private void pickRandomEnemy() {

		int	ran = (int)(Math.random() * 100);

		if (ran > 10 && ran < 60) { activeEnemy = enemies.get(0); }
		else if (ran > 60 && ran < 90) { activeEnemy = enemies.get(1); }
		else if (ran > 90 && ran < 100) { activeEnemy = enemies.get(2); }
		else { activeEnemy = enemies.get(0); }

		UI.drawString("Oh ow.. ambushed!!", 275, 275);
		this.updateStat();
	}

	//updating player's and enemy's health bar on the screen
	private void updateStat() {
		activePlayer.drawImage(0, 0, 5);
		activePlayer.displayHBar(50, 155);
		activeEnemy.drawImage(50, 50, 5);
		activeEnemy.displayHBar(300, 400);
	}

}
