package com.maomihz;
/*BombPiece.java*/

/**
 * Represents a BombPiece ins Checkers61bl
 * 
 * @author
 */

public class BombPiece extends Piece {

	/**
	 * Define any variables associated with a BombPiece object here. These
	 * variables MUST be private or package private.
	 */

	/**
	 * Constructs a new BombPiece
	 * 
	 * @param side
	 *            what side this BombPiece is on
	 * @param b
	 *            Board that this BombPiece belongs to
	 */
	public BombPiece(int side, Board b) {
		super(side,b);
	}
	
	@Override
	public int type() {
		return Piece.TYPE_BOMB;
	}
	
	@Override
	public void explode(int x, int y) {
		for (int i=x-1; i<=x+1; i++) {
			for (int j=y-1; j<=y+1; j++) {
				if (board.isValid(i, j)) {
					if (board.pieceAt(i, j) != null) {
						board.pieceAt(i, j).blowUp(i, j);
					}
				}
			}
 		}
	}

}
