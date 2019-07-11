package nz.ac.bram.blockDrop;

import java.awt.Color;

public class Piece {
	private int w, h;
	private String piece;
	private Color c;
	private char[][] pieceArray;
	
	public Piece(int width, int height, String piece, Color c) {
		this.w = width;
		this.h = height;
		this.piece = piece;
		this.c = c;
		
		pieceArray = new char[this.w][this.h];
		int i = 0; char ch;
		for (int row = 0; row < this.h; row++) 
		{
			for (int col = 0; col < this.w; col++) {
				ch = piece.charAt(i);
				pieceArray[col][row] = ch;
				i++;
			}
		}
	}
	
	public int getWidth() {
		return this.w;
	}
	
	public int getHeight() {
		return this.h;
	}
	
	public String getPieceString() {
		return this.piece;
	}
	
	public Color getColor() {
		return this.c;
	}
	
	public void rotatePiece() {
		char[][] tmpPiece = new char[this.h][this.w];
		for (int y = 0; y < this.h; y++) {
			for (int x = this.w - 1; x >= 0; x--) {
				tmpPiece[y][this.w - 1 - x] = pieceArray[x][y];
			}
		}
		int tmp = this.w; this.w = this.h; this.h = tmp;
		pieceArray = new char[w][h];/*
		for (int r = 0; r < h; r++) {
			for (int c = 0; c < w; c++) {
				
				pieceArray[c][r] = tmpPiece[c][r];
				
			}
		}*/
		pieceArray = tmpPiece;
	}
	
	public char[][] getPiece() {
		return pieceArray;
	}
}