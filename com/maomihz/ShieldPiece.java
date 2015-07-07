package com.maomihz;
/*ShieldPiece.java*/

/**
 * Represents a ShieldPiece in Checkers61bl
 * 
 * @author
 */

public class ShieldPiece extends Piece {

	/**
	 * Define any variables associated with a ShieldPiece object here. These
	 * variables MUST be private or package private.
	 */

	/**
	 * Constructs a new ShieldPiece
	 * 
	 * @param side
	 *            what side this ShieldPiece is on
	 * @param b
	 *            Board that this ShieldPiece belongs to
	 */

	public int type() {
		return Piece.TYPE_SHIELD;
	}
	public ShieldPiece(int side, Board b) {
		super(side,b);
	}
	
	public void blowUp(int x, int y) {
		//Do nothing
	}

}