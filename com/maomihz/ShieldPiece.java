package com.maomihz;
/*ShieldPiece.java*/

/**
 * Represents a ShieldPiece in Checkers61bl
 * 
 * @author MaomiHz
 */

public class ShieldPiece extends Piece {
	

	
	/**
	 * Constructs a new ShieldPiece
	 * 
	 * @param side
	 *            what side this ShieldPiece is on
	 * @param b
	 *            Board that this ShieldPiece belongs to
	 */
	public ShieldPiece(int side, Board b) {
		super(side,b);
	}
	
	@Override
	public int type() {
		return Piece.TYPE_SHIELD;
	}
	
	@Override
	public String img() {
		return "shield-" + (side() == SIDE_FIRE ? "fire" : "water") + (isKing() ? "-crowned" : "") + ".png";
	}
	
	@Override
	public void blowUp(int x, int y) {
		
	}

}