package com.maomihz;
/*Piece.java*/

/**
 * Represents a Normal Piece in Checkers61bl
 * 
 * @author MaomiHz
 */

public class Piece {
	public static final int SIDE_FIRE = 0;
	public static final int SIDE_WATER = 1;
	public static final int TYPE_NORMAL = 2;
	public static final int TYPE_BOMB = 3;
	public static final int TYPE_SHIELD = 4;
	
	private int mySide;
	private boolean myIsKing;
	Board board;
	
	

	/**
	 * Returns the side that the piece is on
	 * 
	 * @return 0 if the piece is fire and 1 if the piece is water
	 */
	
	public int side() {
		return mySide;
	}

	public boolean isKing() {
		return myIsKing;
	}
	
	public void becomeKing() {
		myIsKing = true;
	}
	
	public int type() {
		return TYPE_NORMAL;
	}

	
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
		// NO explosion
	}

}