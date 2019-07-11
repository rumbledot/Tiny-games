package nz.ac.bram.snake;
import ecs100.UI;

import java.awt.Color;
import java.util.*;

import javax.swing.JTextField;

import java.awt.event.*;

public class SnakeGame {

	private ArrayList<String> gameBoard = new ArrayList<String>();
	private ArrayList<Snake> snake = new ArrayList<Snake>();
	
	private int row = 15, col = 15, snakeDir = 4;
	private int posAppleX = 0, posAppleY = 0;
	private boolean Alive = true, keyPressed = false, thereAnApple = false;
	private double counter = 0, score = 0, curCounter = 6000, scrCounter = 1000;
	
	public SnakeGame() {
		
		UI.initialise();
		UI.addButton("Start!", 				this :: startGame);
		UI.addSlider("Row", 10, 25, 15, 	this :: getRow);
		UI.addSlider("Col", 10, 25, 15, 	this :: getCol);
		UI.addButton("Exit", UI :: quit);
		
		UI.setKeyListener(this :: getUserKey);
		
	}
	
	/*
	 * USER ROW COL
	 * */
	
	public void getRow (double r) {
		this.row = (int)(r);
	}
	
	public void getCol (double c) {
		this.col = (int)(c);
	}
	
	/*
	 * SO ITS BEGIN
	 * */
	
	public void startGame() {
		
		/*
		do {
			row = UI.askInt("Rows (10 - 20) :");
			col = UI.askInt("Cols (10 - 20) :");
		} while (!(row >= 10 && row <= 20 && col >= 10 && col <= 20));
		*/
		// BOARD INITIALISE
		this.initialiseGameBoard();
		
		// MAIN LOOP
		while (Alive) {
	
			this.updateBoard();
			if (counter >= curCounter) {
				this.snakeMove();
				counter = 0;
				if (thereAnApple == false) {
					int ran = (int) (Math.random() * 10);
					if (ran > 4 && ran < 8) {
						this.putApple(); }
				}
			}
			counter++; }
	}
	
	/*
	 * BOARD INITIALISE METHOD
	 * */
	public void initialiseGameBoard() {

		UI.clearText();
		UI.clearGraphics();
		
		Alive = true; keyPressed = false; 
		snakeDir = 4;
		
		for (int r = 0; r <= row - 1; r++) {
			for (int c = 0; c <= col; c++) {
				
				if (r == 0 && c >=0 && c <= col - 1 ||
						r == row -1 && c >=0 && c <= col -1 ||
						c == 0 || c == col) {
					gameBoard.add("*");
					UI.setColor(Color.black);
					UI.fillRect(c * 30, r * 30, 30, 30); }
				else if (r == row /2 && c == col / 2 || 
							r == row /2 && c == col / 2 + 1 ||
								r == row /2 && c == col / 2 + 2) {
					Snake s = new Snake(c, r);
					snake.add(s);
					gameBoard.add("s");
					UI.setColor(Color.green);
					UI.fillOval(c * 30, r * 30, 30, 30); }
				else {
					gameBoard.add(".");
					UI.setColor(Color.gray);
					UI.fillRect(c * 30, r * 30, 30, 30); }
			}
		}
	}
	
	/*
	 * GAME LOOP MAIN METHODS
	 * */
	
	public void getUserKey(String v) {
		if (Alive && !keyPressed) {
		    if (v.equalsIgnoreCase("a")) {
					snakeDir--;
					if (snakeDir < 1) { snakeDir = 4; }
					keyPressed = true;
		    } else if (v.equalsIgnoreCase("d")) {
					snakeDir++;
					if (snakeDir > 4) { snakeDir = 1; }
					keyPressed = true; 
		    } else if (v.equalsIgnoreCase("q")) {
		    	this.exitGame();
		    }
		}
	}
	
	/*
	 * TO DO WITH SNAKE
	 * */
	
	public void snakeCheckHit() {
		
		Snake s = snake.get(0);
		int checkX = s.x, checkY = s.y;
		
		switch (snakeDir) {
		case 1:
			checkY = s.y - 1;
			checkX = s.x;
			if (checkY < 0) { checkY = row - 1; }
			break;
		case 3:
			checkY = s.y + 1;
			checkX = s.x;
			if (checkY >= row) { checkY = 0; }
			break;
		case 4:
			checkX = s.x - 1;
			checkY = s.y;
			if (checkX < 0) { checkX = col; }	
			break;
		case 2:
			checkX = s.x + 1;
			checkY = s.y;
			if (checkX > col) { checkX = 0; }
			break;
		}
		
		if (checkY == posAppleY && checkX == posAppleX) {
			this.eatApple(); }
		
		int i = (checkY * (col + 1) + checkX);
		if (gameBoard.get(i) == "s") {
			Alive = false;
			this.exitGame();
		}
		
	}
	
	public void snakeMove() {
		
		this.snakeCheckHit();
		score++;
		
		Snake s = snake.get(0);
		int lastX = s.x, lastY = s.y, tempX, tempY;
		
		switch (snakeDir) {
			case 1 :
				s.y = s.y - 1;
				if (s.y < 0) { s.y = row - 1; }
				break;
			case 3 :
				s.y = s.y + 1;
				if (s.y >= row) { s.y = 0; }
				break;
			case 4 :
				s.x = s.x - 1;
				if (s.x < 0 ) { s.x = col; }
				break;
			case 2 :
				s.x = s.x + 1;
				if (s.x > col) { s.x = 0; }
				break; }
		
		snake.set(0, new Snake(s.x, s.y));
		for (int i = 1; i < snake.size(); i++) {
			s = snake.get(i);
			tempX = s.x; tempY = s.y;
			s.x = lastX ; s.y = lastY;
			lastX = tempX; lastY = tempY;
			snake.set(i, new Snake(s.x, s.y));
		}
		UI.println("SCORE : " + score);
		keyPressed = false;
	}
	
	public void increaseLevel() {
		double counterOffset = Math.floor(snake.size() / 5);
		curCounter -= counterOffset * 250;
		if (curCounter <= 1000) { curCounter = 1000; }
	}
	
	/*
	 * TO DO WITH BOARD
	 * */
	
	public void updateBoard() {
		
		this.redrawBoard();
		
		// put the snake in the array
		int i = 0;
		for (Snake s : snake) {
			i = (s.y * (col + 1) + s.x);
			gameBoard.set(i, "s");
		}
		
		// put apple in the array
		if (thereAnApple) {
		i = (posAppleY * (col + 1) + posAppleX);
		gameBoard.set(i, "o");
		scrCounter-=10;
		if(scrCounter <= 10) { scrCounter = 10; }
		}
		
		// read the array and display it
		
		i = 0; String gb="";
		for (int r = 0; r <= row - 1; r++) {
			for (int c = 0; c <= col; c++) {
				gb = gameBoard.get(i);
				if (gb == "*") {
					UI.setColor(Color.black);
					UI.fillRect(c * 30, r * 30, 30, 30);
				} else if (gb == ".") {
					UI.setColor(Color.gray);
					UI.fillRect(c * 30, r * 30, 30, 30);
				} else if (gb == "s") {
					UI.setColor(Color.green);
					UI.fillOval(c * 30, r * 30, 30, 30); 
				}  else if (gb == "o") {
					UI.setColor(Color.red);
					UI.fillOval(c * 30, r * 30, 30, 30); 
				}
				i++;
			}
		}
		
	}
	
	public void redrawBoard() {
		gameBoard.clear();
		for (int r = 0; r <= row - 1; r++) {
			for (int c = 0; c <= col; c++) {
				
				if (r == 0 && c >=0 && c <= col - 1 ||
						r == row -1 && c >=0 && c <= col -1 ||
						c == 0 || c == col) {
					gameBoard.add("*"); 
				} else {
					gameBoard.add(".");
				}
			}
		}
	}
	
	/*
	 * TO DO WITH APPLE
	 * */
	
	public void putApple() {
			UI.println("Trying to put an apple");
			posAppleX = (int)(1 + Math.random() * (col - 1));
			posAppleY = (int)(1 + Math.random() * (row - 1));
			
			for (int i = 0; i < snake.size(); i++) {
				Snake s = snake.get(i);
				if (s.x == posAppleX && s.y == posAppleY) {
					UI.println("Not happened!! No apple");
					thereAnApple = false;
					break; } 
				else {
					scrCounter = 1000;
					thereAnApple = true; }
			}
	}
	
	public void eatApple() {
		snake.add(new Snake(posAppleX, posAppleY));
		thereAnApple = false;
		score += scrCounter;
		scrCounter = 1000;
		this.increaseLevel();
	}
	
	/*
	 * TERMINATION METHODS
	 * */
	
	public void exitGame() {
		UI.clearText();
		if (Alive) {
				UI.println("***********************");
				UI.println("*Thank you for playing*");
				UI.println("***********************");
			} else {
				UI.println("***********************");
				UI.println("*****  GAME OVER  *****");
				UI.println("***********************");
			}
		UI.printf		  ("TOTAL SCORE: %.1f", score);
		Alive = true; counter = 0;
		
		gameBoard.clear(); snake.clear();
	}
	
	public static void main(String[] args) {
		new SnakeGame();
	}

}
