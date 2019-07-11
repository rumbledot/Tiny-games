package nz.ac.bram.blockDrop;

import java.awt.Color;
import java.util.ArrayList;
import ecs100.UI;

public class MainMenu {
	
	// game world vars
	private char[][] playField = new char[12][23];
	private int width = 12;
	private int height = 23;
	
	// block piece vars
	private ArrayList<Piece> pieces = new ArrayList<Piece>();
	private boolean aPieceIsActive = true;
	private Piece activePiece;
	private int activeW, activeH;
	private char[][] activeP;
	private Color activeC;
	
	// player vars
	private boolean isAlive = false;
	private int score;
	private int posX = 5, posY = 0;
	private int oldPosX = 0, oldPosY = 0;
	private int rot = 1;
	
	// gameplay vars
	private int timeToMove = 0;
	private int diffcultyLevel = 60;
	private int ranPiece;
	
	void setupGame()
	{
		String
		field = "*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"*..........*"+
				"************";
		int i = 0; char c;
		for (int row = 0; row < this.height; row++) 
		{
			for (int col = 0; col < this.width; col++) {
				c = field.charAt(i);
				playField[col][row] = c;
				i++;
			}
		}
		//piece #1
		Piece p = new Piece(4, 1, "oooo",
				Color.red);
		pieces.add(p);
		
		//piece #2
		p = new Piece(3, 2, ".o."
				+ 			"ooo",
				Color.blue);
		pieces.add(p);
		
		//piece #3
		p = new Piece(3, 2, "oo."
				+ 			".oo",
				Color.yellow);
		pieces.add(p);
		
		//piece #4
		p = new Piece(3, 2, ".oo"
				+ 			"oo.",
				Color.orange);
		pieces.add(p);
		
		//piece #5
		p = new Piece(3, 2, "..o"
				+ 			"ooo",
				Color.green);
		pieces.add(p);
		
		//piece #6
		p = new Piece(3, 2, "o.."
				+ 			"ooo",
				Color.cyan);
		pieces.add(p);
		
		//piece #7
		p = new Piece(2, 2, "oo"
				+ 			"oo",
				Color.pink);
		pieces.add(p);
	}
	
	void pickAPiece() {
		
		ranPiece = (int)(1 + Math.random() * pieces.size());
		switch (ranPiece) 
		{
		case 1:
			activePiece = pieces.get(0);
			break;
		case 2:
			activePiece = pieces.get(1);
			break;
		case 3:
			activePiece = pieces.get(2);
			break;
		case 4:
			activePiece = pieces.get(3);
			break;
		case 5:
			activePiece = pieces.get(4);
			break;
		case 6:
			activePiece = pieces.get(5);
			break;
		case 7:
			activePiece = pieces.get(6);
			break;
		default:
			activePiece = pieces.get(6);
			break;
		}
		activeW = activePiece.getWidth();
		activeH = activePiece.getHeight();
		activeP = activePiece.getPiece();
		activeC = activePiece.getColor();
		posX = 5 - (activeW / 2);
		posY = 0;
		
		this.placeAPiece();
		
	}
	
	void playerMove(String k) {
		
		if (k.equalsIgnoreCase("r"))
		{

			this.removeAPiece();
			
			rot++;
			if (rot > 4) {
				rot = 1;
			}
			this.activePiece.rotatePiece();
			this.activeH = this.activePiece.getHeight();
			this.activeW = this.activePiece.getWidth();
			this.activeP = this.activePiece.getPiece();
			this.placeAPiece();
		}
		
		else if (k.equalsIgnoreCase("a"))
		{
			this.removeAPiece();
			oldPosX = posX;
			posX--;
			if (posX < 1) {
				posX = oldPosX;
			}
			for (int i = 0; i < activeP[1].length; i++) {
				if (this.activeP[0][i] == 'o' && playField[posX - 1][posY + i] == '=') {
					posX = oldPosX;
				}
			}
			this.checkMove();
			this.placeAPiece();
		}
		else if (k.equalsIgnoreCase("d"))
		{
			this.removeAPiece();
			oldPosX = posX;
			posX++;
			if (posX + activeW > width - 1) {
				posX = width - 1 - activeW;
			}
			for (int i = 0; i < activeP[1].length; i++) {
				if (this.activeP[0][i] == 'o' && playField[posX + 1][posY + i] == '=') {
					posX = oldPosX;
				}
			}
			this.checkMove();
			this.placeAPiece();
		} else if (k.equalsIgnoreCase("s"))	{
			this.removeAPiece();			
			posY++;
			/*if (posY + activeH > height - 1) {
				posY = height - activeH - 1;
				for(int i = 0; i < this.activeH; i++) {
					for(int j = 0; j < this.activeW; j++) {
						if (this.activeP[j][i] == 'o') {
							playField[posX + j][posY + i] = '=';
						}
					}
				}
				this.aPieceIsActive = false;
			} else {
			this.placeAPiece(); }*/
			this.checkMove();
			this.placeAPiece();
		}
		
		else if (k.equalsIgnoreCase("q")) 
		{
			isAlive = false;
			this.endGame();
		}
		
		this.refreshField();
		
	}
	
	void removeAPiece() {
		for (int row = posY; row < posY + activeH; row++) {
			for (int col = posX; col < posX + activeW; col++) {
				if (activeP[col - posX][row - posY] == 'o' && 
						playField[posX][posY] != '=') {
				playField[col][row] = '.';
				}
			}
		}
	}
	
	void removeALine(int rowToBeRemoved, int rows) {
		char[][] tmpField = new char[width][height];
		for (int col = 1; col < width - 1; col++) {
			playField[col][rowToBeRemoved] = '.';
		}
		
		for (int row = rowToBeRemoved + 1; row >= 1; row--) {
			for (int col = 0; col < width; col++) {
				tmpField[col][row] = playField[col][row];
				playField[col][row + 1] = tmpField[col][row];
			}
		}
		
		for (int col = 1; col < width - 1; col++) {
			playField[col][0] = '.';
		}
		this.refreshField();
	}
	
	void checkMove() {
		boolean isCollided = false;
		
		//check collision with freezed blocks
		for(int i = 0; i < this.activeH; i++) {
			for(int j = 0; j < this.activeW; j++) {
				if (this.activeP[j][i] == 'o' && playField[posX + j][posY + i] == '=') {
					isCollided = true; 
					posY--;
					break;
				} else if (posY + activeH > height - 1) { //check bottom bound
					posY = height - activeH - 1;
					isCollided = true;
				}
			}
		}
		
		if (isCollided) {
			for(int i = 0; i < this.activeH; i++) {
				for(int j = 0; j < this.activeW; j++) {
					if (this.activeP[j][i] == 'o' &&
							playField[posX + j][posY + i] != '=') {
						playField[posX + j][posY + i] = '=';
					}
				}
			}
			this.checkLine();
			this.aPieceIsActive = false;
		}
	}
	
	void checkLine() {
		String oneLine = ""; int multiplier = 1;
		for(int row = 0; row < this.activeH; row++) {
			for(int j = 0; j < width; j++) {
				oneLine += Character.toString(playField[j][posY + row]);
			}
			if (oneLine.contentEquals("*==========*")) {
				for(int j = 0; j < width; j++) {
					playField[posX + j][posY + row] = '.';
					UI.setColor(Color.red);
					UI.fillOval(j * 20, (posY + row) * 20, 20, 20);
				}
				this.score += 20 * multiplier;
				multiplier++;
			}
		}
		UI.setColor(Color.black);
	}

	void placeAPiece() {
		if (posX + this.activeW > this.width - 1) {
			posX = this.width - this.activeW - 1;
		}
		for (int row = posY; row < posY + activeH; row++) {
			for (int col = posX; col < posX + activeW; col++) {
				if (this.activeP[col - posX][row - posY] == 'o' &&
						playField[col][row] != '=') {
					playField[col][row] = this.activeP[col - posX][row - posY];
				}
			}
		}
		
	}
	
	public void refreshField()
	{
		//UI.clearText();
		//UI.setColor(Color.black);
		for (int row = 0; row < this.height; row++) {
			for (int col = 0; col < this.width; col++) {
				//UI.print(playField[col][row]);
				if (playField[col][row] == '*') {
					UI.setColor(Color.black);
					UI.fillRect(col * 20,  row * 20, 20, 20);
				} else if (playField[col][row] == '.') {
					UI.setColor(Color.lightGray);
					UI.fillRect(col * 20,  row * 20, 20, 20);
				} else if (playField[col][row] == 'o') {
					UI.setColor(activeC);
					UI.fillRect(col * 20,  row * 20, 20, 20);
				} else if (playField[col][row] == '=') {
					UI.setColor(Color.darkGray);
					UI.fillRect(col * 20,  row * 20, 20, 20);
				}
			}
			//UI.println();
		}
		//UI.setColor(Color.black);
		//UI.println("Score : " + score);
	}
	
	public void startGame() {
		if (!isAlive) {
			this.setupGame();
			this.pickAPiece();
			this.refreshField();
			isAlive = true;
			
			while (isAlive) {
				
				if (!aPieceIsActive) {
					this.pickAPiece();
					aPieceIsActive = true;
				}
				if (timeToMove > diffcultyLevel * 10000000 && aPieceIsActive) {
					this.removeAPiece();
					posY++;
					timeToMove = 0;
					this.checkMove();
					if (this.aPieceIsActive) {
						this.placeAPiece();
						this.refreshField();
					}
				}
				
				timeToMove++;
			}
		} else {
			UI.println("Please finish this game first!");
			UI.println("Press 'Q' anytime to quit this game");
		}
	}
	
	private void endGame() 
	{
		UI.clearPanes();
		UI.drawString("*********************", 50, 30);
		UI.drawString("THANK YOU FOR PLAYING", 50, 50);
		UI.drawString("*********************", 50, 80);
		UI.drawString("*TOTAL SCORE : " + score, 50, 110);
		UI.drawString("*********************", 50, 140);
		score = 0;
		isAlive = false;
	}
	
	public MainMenu ()
	{
		UI.initialise();
		UI.addButton("Start", 				this	::	startGame);
		//UI.addButton("put a piece", 		this	::	putAPiece);
		UI.setKeyListener(					this	::	playerMove);
		UI.addButton("Exit",				UI		::	quit);
	}
	
	public void putAPiece() {
		this.setupGame();
		//this.pickAPiece();
		activePiece = pieces.get(5);
		activeW = activePiece.getWidth();
		activeH = activePiece.getHeight();
		activeP = activePiece.getPiece();
		activeC = activePiece.getColor();
		posX = 5 - (activeW / 2);
		posY = 0;
		for(int i = 0; i < this.activeH; i++) {
			for(int j = 0; j < this.activeW; j++) {
				UI.print(this.activeP[j][i]);
			}
			UI.println();
		}
		this.placeAPiece();
		this.refreshField();
		
		activePiece.rotatePiece();
		activeW = activePiece.getWidth();
		activeH = activePiece.getHeight();
		activeP = activePiece.getPiece();
		activeC = activePiece.getColor();
		posX = 5 - (activeW / 2);
		posY = 3;
		for(int i = 0; i < this.activeH; i++) {
			for(int j = 0; j < this.activeW; j++) {
				UI.print(this.activeP[j][i]);
			}
			UI.println();
		}
		this.placeAPiece();
		this.refreshField();
		
		activePiece.rotatePiece();
		activeW = activePiece.getWidth();
		activeH = activePiece.getHeight();
		activeP = activePiece.getPiece();
		activeC = activePiece.getColor();
		posX = 5 - (activeW / 2);
		posY = 6;
		for(int i = 0; i < this.activeH; i++) {
			for(int j = 0; j < this.activeW; j++) {
				UI.print(this.activeP[j][i]);
			}
			UI.println();
		}
		this.placeAPiece();
		this.refreshField();
		
		activePiece.rotatePiece();
		activeW = activePiece.getWidth();
		activeH = activePiece.getHeight();
		activeP = activePiece.getPiece();
		activeC = activePiece.getColor();
		posX = 5 - (activeW / 2);
		posY = 6;
		for(int i = 0; i < this.activeH; i++) {
			for(int j = 0; j < this.activeW; j++) {
				UI.print(this.activeP[j][i]);
			}
			UI.println();
		}
		this.placeAPiece();
		this.refreshField();
	}
	
	public static void main(String[] args) {

		new MainMenu();
		
	}

}