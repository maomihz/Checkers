package com.maomihz;
/*Piece.java*/

/**
 * Represents a Normal Piece in Checkers61bl
 * 
 * @author MaomiHz
 */

public class Piece {
	
	// Side and Type constants
	public static final int SIDE_FIRE = 0;
	public static final int SIDE_WATER = 1;
	public static final int TYPE_NORMAL = 2;
	public static final int TYPE_BOMB = 3;
	public static final int TYPE_SHIELD = 4;
	
	private int mySide; //Side of the piece
	private boolean myIsKing; // is king piece or not
	Board board; //The board it's on
	
	
	/**
	 * Initializes a Piece
	 * 
	 * @param side
	 *            The side of the Piece
	 * @param b
	 *            The Board the Piece is on
	 */
	public Piece(int side, Board b) {
		mySide = side;
		board = b;
	}
	
	/**
	 * Returns the side that the piece is on
	 * 
	 * @return 0 if the piece is fire and 1 if the piece is water
	 */
	
	// Return the side of the piece
	public int side() {
		return mySide;
	}

	//Is a king piece
	public boolean isKing() {
		return myIsKing;
	}
	
	// Become a king piece(cannot go back)
	public void becomeKing() {
		myIsKing = true;
	}
	
	/**
	 *  Return the type of the piece
	 *  
	 * @return type
	 */
	public int type() {
		return Piece.TYPE_NORMAL;
	}
	
	public String img() {
		return "pawn-" + (side() == SIDE_FIRE ? "fire" : "water") + (isKing() ? "-crowned" : "") + ".png";
	}

	


	/**
	 * Destroys the piece at x, y. ShieldPieces do not blow up
	 * 
	 * @param x
	 *            The x position of Piece to destroy
	 * @param y
	 *            The y position of Piece to destroy
	 */
	public void blowUp(int x, int y) {
		board.remove(x, y);
	}

	/**
	 * Does nothing. For bombs, destroys pieces adjacent to it
	 * 
	 * @param x
	 *            The x position of the Piece that will explode
	 * @param y
	 *            The y position of the Piece that will explode
	 */
	public void explode(int x, int y) {
	}

}