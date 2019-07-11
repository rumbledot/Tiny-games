package userinterface;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JColorChooser;

import ecs100.UI;
import ecs100.UIFileChooser;

public class LevelEditor extends UserInterface {
/**
 * 
 * Level editor
 * 
 * Future refinement:
 * On load level.. check if one of the images that comes with the level file
 * already loaded so there will be no doubles
 *
 * 
 */
	//Editor vars
	private double size = 30;
	private Color c = Color.black;
	private float colorDepth = 255;
	private int tools = 1;
	private int activeIndex = 0;
	private String fileHeader = "";
	//Array vars
	private float[][][] pixelArray;
	private ArrayList<Image> images = new ArrayList<Image>();
	//Level completeness boolean
	private boolean isApple = false;
	private boolean isPlayer = false;
	private boolean isBlock = false;
	private boolean isFinish = false;
	
	public LevelEditor() {
		UI.initialise();
		
		UI.setMouseListener(  				this::doMouse);
		UI.addButton("New Level", 			this::newLevel);
		UI.addButton("Load blocks", 		this::loadBlock);
		UI.addButton("List blocks", 		this::listBlock);
		UI.addButton("Set active block", 	this::setActiveBlock);
		UI.addButton("Put Barrier", 		this::putBlock);
		UI.addButton("Put Player", 			this::putPlayer);
		UI.addButton("Put Apple", 			this::putApple);
		UI.addButton("Put Finish point", 	this::putFinish);
		UI.addButton("Put alkable", 		this::eraseBlock);
		UI.addButton("Load Level", 			this::doLoad);
		UI.addButton("Save Level", 			this::doSave);
		UI.addButton("Quit Editor", 		this::backToMain);
		//prepare vars and arrays for a new level creation
		this.newLevel();
	}
	//clear out old vars and arrays for a new level creation
	public void newLevel() {
		width = 24; height = 12;
		playArea = new Element[width][height];
		playAreaItems = new Element[width][height];
		
		images.clear();
		UI.clearGraphics();
		UI.setColor(Color.gray);
		UI.setLineWidth(1);
		this.defaultItems();
		//filled editor array with 'walkable' block with grass image
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {
				EmptyBlock em = new EmptyBlock(col * size, row * size, size, Color.white);
				em.setBlockName("grass.ppm");
				playArea[col][row] = em;
				playAreaItems[col][row] = new EmptyBlock(col * size, row * size, size, Color.white);
				UI.drawRect(col * size, row * size, size, size);
			}
		}
		this.drawArray();
	}
	//items needed to create a level
	public void defaultItems() {
		this.loadBlockWithName("apple.ppm");
		this.loadBlockWithName("health potion.ppm");
		this.loadBlockWithName("magic potion.ppm");
		this.loadBlockWithName("grass.ppm");
		this.loadBlockWithName("player_d.ppm");
		this.loadBlockWithName("stonewall.ppm");
		this.loadBlockWithName("finish.ppm");
		for(int i = 0; i < images.size(); i++) {
			Image im = images.get(i);
			im.drawImage(i * size, (height + 1) * 30, 1);
		}
	}
	//show all blocks that user loaded
	public void listBlock() {
		UI.clearText();
		UI.println("Loaded images :");
		int i = 1;
		for (Image im : images) {
			String shortName = im.getName().substring(53);
			UI.printf("%d. %s. %n",i ,shortName);
			i++;
		}
	}
	/**
	 * 
	 * File I/O methods
	 * 
	 */
	//Loading a compatible image file to be use as a block
	public void loadBlock() {
		try {
			String fileName = UIFileChooser.open("Pick a BLOCK file");
			Scanner scanner = null;

			File myFile = new File (fileName);
			scanner = new Scanner(myFile);

			fileHeader = scanner.next();

			int w = scanner.nextInt();
			int h = scanner.nextInt();

			pixelArray= new float[w][h][3];

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
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < images.size(); i++) {
			Image im = images.get(i);
			im.drawImage(i * size, (height + 1) * 30, 1);
		}
		
	}
	//another way of loading
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
	//use a block user selected in the editor
	public void setActiveBlock() {
		this.listBlock();
		int index = UI.askInt("Image index# to load :");
		if (index >= 1 && index <= images.size()) {
			Image im = images.get(index - 1);
			UI.println("Loading " + im.getName() + " image");
			this.fileHeader = im.getFileHeader();
			int w = im.getWidth();
			int h = im.getHeight();
			this.colorDepth = im.getColorDepth();
			this.pixelArray = new float[w][h][3];
			this.pixelArray = im.getPixelArray();
			
			this.activeIndex = index - 1;
			
			im.drawImage((width + 2) * size, 0, 1);
		} else {
			UI.println("Image not found");
		}
	}
	//check level completeness before saving .level file
	public void beforeSave() {
		if (isApple && isPlayer && isBlock && isFinish) {
			this.doSave();
		} else {
			UI.println("There are some elements missing");
		}
	}
	//save user created level into a file
	public void doSave() {
		Element b;
		try {
			String filename = UIFileChooser.save();
			File myfile = new File(filename);
			PrintStream ps;
			ps = new PrintStream(new FileOutputStream(myfile));

			ps.println(images.size());
			for(Image im : images) {
				ps.println(im.getName());
			}

			ps.println(width);
			ps.println(height);

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {

					b = playArea[j][i];

					ps.println(b.getName());
					ps.println(b.getBlockName());

				}	
			}

			ps.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	//load user created level into the editor
	public void doLoad() {
		UI.clearGraphics();
		try {
			Scanner sc = new Scanner(new File(UIFileChooser.open()));

			int l = sc.nextInt();
			sc.nextLine();

			for(int i = 0; i < l; i++) {
				String fileName = sc.nextLine();
				this.loadBlockWithName(fileName);
			}

			width = sc.nextInt();
			sc.nextLine();

			height = sc.nextInt();
			sc.nextLine();

			playArea = new Element[width][height];
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
		} catch (FileNotFoundException e) {
			// The line above should be marked as an error when you start out,
			// but it will go away once you fill in your code above.
			e.printStackTrace();
		}
		for(int i = 0; i < images.size(); i++) {
			Image im = images.get(i);
			im.drawImage(i * size, (height + 1) * 30, 1);
		}
		this.drawArray();
	}
	//update the screen with array data
	public void drawArray() {
		for(int row = 0; row < height; row++) {
			for(int col = 0; col < width; col++) {

				Element b = playArea[col][row];

				for (Image im : images) {

					if (b.getBlockName().contentEquals(im.getName())) {
						im.drawImage(col * size, row * size, 1);
					} else {
						UI.setColor(Color.gray);
						UI.setLineWidth(1);
						UI.drawRect(col * size, row * size, size, size);
					}
				}
			}
		}
	}

	public void doMouse(String action, double x, double y) {
		if(action.equals("released")) {
			//user can select a loaded block as an active block interactively
			if ((int)(y / size) == (height + 1)) {
				if ( images.get((int)(x/size)) != null ) {

					//refresh the icons list
					for(int i = 0; i < images.size(); i++) {
						Image im = images.get(i);
						im.drawImage(i * size, (height + 1) * 30, 1);
					}

					activeIndex = (int)(x/size); // put green box to user selection
					UI.setColor(Color.green);
					UI.setLineWidth(3);
					UI.drawRect((int)(x/size) * size, (height + 1) * size, size, size);
					UI.setColor(Color.black);
					UI.setLineWidth(1);
				}
			}

			switch (tools) {
			// depends on how the block behave
			// a solid block, player can't traverse this block
			case 1:
				int gridX = (int)(x / size);
				int gridY = (int)(y / size);

				if (gridX < width && gridY < height) {
					Element b = new Block(gridX * size, gridY * size, size, c);
					b.setBlockName(images.get(activeIndex).getName());
					playArea[gridX][gridY] = b;
					isBlock = true;
				}
				break;
				//put a player block in the field
			case 2:
				//only one player block allowed
				if(!isPlayer) {
					gridX = (int)(x / size);
					gridY = (int)(y / size);

					if (gridX < width && gridY < height) {

						//UI.fillRect(gridX * size, gridY * size, size, size);
						Element p = new Player(gridX * size, gridY * size, size, c);
						p.setBlockName(images.get(activeIndex).getName());
						playArea[gridX][gridY] = p;
						isPlayer = true;
					}
				}
				break;
				//this block will generate score if player walk thru it
			case 3:
				gridX = (int)(x / size);
				gridY = (int)(y / size);

				if (gridX < width && gridY < height) {

					//UI.fillRect(gridX * size, gridY * size, size, size);
					Element ap = new Apple(gridX * size, gridY * size, size, c);
					ap.setBlockName(images.get(activeIndex).getName());
					playArea[gridX][gridY] = ap;
					isApple = true;
				}
				break;
				//put a finish point block that will end a level if player reach it
			case 4:
				if (!isFinish) {
					gridX = (int)(x / size);
					gridY = (int)(y / size);

					if (gridX < width && gridY < height) {

						//UI.fillRect(gridX * size, gridY * size, size, size);
						Element f = new Finish(gridX * size, gridY * size, size, c);
						f.setBlockName(images.get(activeIndex).getName());
						playArea[gridX][gridY] = f;
						isFinish = true;
					}
				}
				break;
				//put a walkable block (as a ground or background)
			case 5:
				gridX = (int)(x / size);
				gridY = (int)(y / size);

				if (gridX < width && gridY < height) {

					//UI.fillRect(gridX * size, gridY * size, size, size);
					Element f = new EmptyBlock(gridX * size, gridY * size, size, c);
					f.setBlockName(images.get(activeIndex).getName());
					playArea[gridX][gridY] = f;
				}
				break;
			}
		}
		this.drawArray();
	}

	private void backToMain () {
		new UserInterface();
	}
	//TOOLS setter
	private void putBlock() {
		tools = 1;
	}
	
	private void putPlayer() {
		tools = 2;
	}
	
	private void putApple() {
		tools = 3;
	}
	
	private void putFinish() {
		tools = 4;
	}
	
	private void eraseBlock() {
		tools = 5;
	}
	
}
