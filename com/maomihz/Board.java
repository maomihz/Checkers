package com.maomihz;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;

/*Board.java*/

/**
 * Represents a Board configuration of a game of Checkers61bl
 * 
 * @author
 */

public class Board {
	
	public static Color dynamicColor1;
	public static Color dynamicColor2;
	public static boolean increasing1 = false;
	public static boolean increasing2 = false;
	{
		dynamicColor1 = new Color(255,150,150);
		dynamicColor2 = new Color(255,150,150);
		new Thread() {
			public void run() {
				while (true) {
					int c = dynamicColor1.getBlue();
					if (increasing1) {
						c++;
						if (c >= 170)
							increasing1 = false;
					} else {
						c--;
						if (c <= 130)
							increasing1 = true;
					}
					dynamicColor1 = new Color(c, c, c);
					try {
						sleep(16);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		
		new Thread() {
			public void run() {
				while (true) {
					int d = dynamicColor2.getBlue();
					if (increasing2) {
						d++;
						if (d >= 170)
							increasing2 = false;
					} else {
						d--;
						if (d <= 110)
							increasing2 = true;
					}
					dynamicColor2 = new Color(255,d,d);
					try {
						sleep(16);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	private Piece[][] pieces;
	private int side;
	private Date startTime;
	private Date endTime;

	
	private double xOffset = 0.2, yOffset = 2.9;
	private String msg;
	
	private Piece selected;
	private int selectedX, selectedY;
	private Piece capturer;
	
	private boolean moved;
	private boolean captured;
	private boolean gameover;
	/**
	 * Define any variables associated with a Board object here. These variables
	 * MUST be private.
	 */

	/**
	 * Constructs a new Board
	 * 
	 * @param shouldBeEmpty
	 *            if true, add no pieces
	 */
	public Board() {
		this(false);
	}
	
	public Board(boolean shouldBeEmpty) {
		pieces = new Piece[8][8];
		msg = "New Game Started";
		startTime = new Date();
		side = Piece.SIDE_FIRE;
		moved = false;
		newGame();
	}
	
	private void drawBoard() {
		StdDrawPlus.clear(); // Clear the screen
		
		//Paint grid
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[0].length; j++) {
				if ((i + j) % 2 == 0) {
					if (selected != null && canMove(selectedX, selectedY, i, j)) {
						StdDrawPlus.setPenColor(dynamicColor2);
					} else if (selected == null && pieceAt(i, j) != null && pieceAt(i, j).side() == side && canMove(i, j)) {
						StdDrawPlus.setPenColor(dynamicColor1);
					}
					else {
						StdDrawPlus.setPenColor(StdDrawPlus.GRAY);
					}
				} else {
					StdDrawPlus.setPenColor(StdDrawPlus.CYAN);
				}
				
				StdDrawPlus.filledSquare(i + xOffset, j + yOffset, 0.5);
				
				// Picture
				String filename = "";
				if (pieces[i][j] != null) {
					switch (pieces[i][j].type()) {
					case Piece.TYPE_BOMB:
						filename += "bomb-";
						break;
					case Piece.TYPE_NORMAL:
						filename += "pawn-";
						break;
					case Piece.TYPE_SHIELD:
						filename += "shield-";
						break;
					}
					if (pieces[i][j].side() == Piece.SIDE_FIRE) {
						filename += "fire-";
					} else {
						filename += "water-";
					}
					if (pieces[i][j].isKing()) {
						filename += "crowned";
					} else {
						filename = filename.substring(0, filename.length() - 1);
					}
					StdDrawPlus.picture(i + xOffset, j + yOffset, "img/" + filename + ".png", 1, 1);
				}
				
				// Mouse
				int mx = (int)(StdDrawPlus.mouseX() - xOffset + 0.5);
				int my = (int)(StdDrawPlus.mouseY() - yOffset + 0.5);
				if(isValid(mx, my) && mx == i && my == j) {
					StdDrawPlus.picture(i + xOffset, j + yOffset, "img/Mouse.png", 1, 1);
				}
				
				// Selection
				if(pieceAt(i, j) != null && selected != null && pieceAt(i, j) == selected) {
					StdDrawPlus.picture(i + xOffset, j + yOffset, "img/selection.png", 1, 1);
				}

			}
		}
		
		// Indicator
		StdDrawPlus.setPenColor(StdDrawPlus.LIGHT_GRAY);
		StdDrawPlus.filledSquare(9, 1, 1);
		
		if (side == Piece.SIDE_FIRE) {
			StdDrawPlus.picture(9, 1, "img/pawn-fire.png", 2, 2);
		}
		if (side == Piece.SIDE_WATER) {
			StdDrawPlus.picture(9, 1, "img/pawn-water.png", 2, 2);
		}
		
		//Message Text
		StdDrawPlus.setPenColor(StdDrawPlus.BLACK);
		StdDrawPlus.text(4, 0, msg); // The Message
		if (gameover) {
			StdDrawPlus.text(9, 8, String.format("%02d:%02d", (endTime.getTime() - startTime.getTime()) / 1000 / 60 % 60, (endTime.getTime() - startTime.getTime()) / 1000 % 60));
		} else {
			StdDrawPlus.text(9, 8, String.format(new Date().getTime() % 1000 > 500 ? "%02d:%02d" : "%02d %02d", (new Date().getTime() - startTime.getTime()) / 1000 / 60 % 60, (new Date().getTime() - startTime.getTime()) / 1000 % 60));
		}
		
		if (!canMove()) {
			msg = "No Move Available, press SPACE";
		}
		StdDrawPlus.text(9, 8.5, "Time"); //Just a label
		StdDrawPlus.text(9.1, 9.5, "Checkers Game"); //Just a label

	}
	
	public void newGame() {
		for (int i=0;i<pieces.length;i++) {
			for (int j=0;j<pieces[i].length;j++) {
				if (pieces[i][j] != null)
				remove(i,j);
			}
		}
		
		place(new Piece(Piece.SIDE_WATER, this), 1, 7);
		place(new Piece(Piece.SIDE_WATER, this), 3, 7);
		place(new Piece(Piece.SIDE_WATER, this), 5, 7);
		place(new Piece(Piece.SIDE_WATER, this), 7, 7);

		place(new ShieldPiece(Piece.SIDE_WATER, this), 0, 6);
		place(new ShieldPiece(Piece.SIDE_WATER, this), 2, 6);
		place(new ShieldPiece(Piece.SIDE_WATER, this), 4, 6);
		place(new ShieldPiece(Piece.SIDE_WATER, this), 6, 6);

		place(new BombPiece(Piece.SIDE_WATER, this), 1, 5);
		place(new BombPiece(Piece.SIDE_WATER, this), 3, 5);
		place(new BombPiece(Piece.SIDE_WATER, this), 5, 5);
		place(new BombPiece(Piece.SIDE_WATER, this), 7, 5);

		place(new BombPiece(Piece.SIDE_FIRE, this), 0, 2);
		place(new BombPiece(Piece.SIDE_FIRE, this), 2, 2);
		place(new BombPiece(Piece.SIDE_FIRE, this), 4, 2);
		place(new BombPiece(Piece.SIDE_FIRE, this), 6, 2);

		place(new ShieldPiece(Piece.SIDE_FIRE, this), 1, 1);
		place(new ShieldPiece(Piece.SIDE_FIRE, this), 3, 1);
		place(new ShieldPiece(Piece.SIDE_FIRE, this), 5, 1);
		place(new ShieldPiece(Piece.SIDE_FIRE, this), 7, 1);

		place(new Piece(Piece.SIDE_FIRE, this), 0, 0);
		place(new Piece(Piece.SIDE_FIRE, this), 2, 0);
		place(new Piece(Piece.SIDE_FIRE, this), 4, 0);
		place(new Piece(Piece.SIDE_FIRE, this), 6, 0);
	}


	/**
	 * gets the Piece at coordinates (x, y)
	 * 
	 * @param x
	 *            X-coordinate of Piece to get
	 * @param y
	 *            Y-coordinate of Piece to get
	 * @return the Piece at (x, y)
	 */
	public Piece pieceAt(int x, int y) {
		try {
			return pieces[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IllegalArgumentException();
		}
	}
	
	public Piece pieceAt(Point x) {
		return pieceAt(x.x, x.y);
	}

	/**
	 * Places a Piece at coordinate (x, y)
	 * 
	 * @param p
	 *            Piece to place
	 * @param x
	 *            X coordinate of Piece to place
	 * @param y
	 *            Y coordinate of Piece to place
	 */
	public void place(Piece p, int x, int y) {
		if (x >= 0 && y >= 0 && x < 8 && y < 8)
			pieces[x][y] = p;
	}
	public void place(Piece p, Point x) {
		place(p, x.x, x.y);
	}

	/**
	 * Removes a Piece at coordinate (x, y)
	 * 
	 * @param x
	 *            X coordinate of Piece to remove
	 * @param y
	 *            Y coordinate of Piece to remove
	 * @return Piece that was removed
	 */
	public Piece remove(int x, int y) {
		Piece piece = pieces[x][y];
		pieces[x][y] = null;
		return piece;
	}
	public Piece remove(Point x) {
		return remove(x.x, x.y);
	}

	/**
	 * Determines if a Piece can be selected
	 * 
	 * @param x
	 *            X coordinate of Piece
	 * @param y
	 *            Y coordinate of Piece to select
	 * @return true if the Piece can be selected
	 */
	public boolean canSelect(int x, int y) {
		if (x >= 0 && y >= 0 && x < 8 && y < 8 && pieceAt(x, y) != null) {
			if (side == pieceAt(x, y).side() && canMove(x, y)) {
				return true;
			}
		}
		return false;
	}
	public boolean canSelect(Point x) {
		return canSelect(x.x, x.y);
	}

	/**
	 * Selects a square. If no Piece is active, selects the Piece and makes it
	 * active. If a Piece is active, performs a move if an empty place is
	 * selected. Else, allows you to reselect Pieces
	 * 
	 * @param x
	 *            X coordinate of place to select
	 * @param y
	 *            Y coordinate of place to select
	 */
	public void select(int x, int y) {
		if (canSelect(x, y)) {
			selected = null;
			selected = pieceAt(x, y);
			selectedX = x;
			selectedY = y;
		}
	}
	public void select(Point x) {
		select(x.x, x.y);
	}
	
	public boolean isValid(int x, int y) {
		if (x >= 0 && y >= 0 && x < 8 && y < 8) {
			return true;
		}
		return false;
	}
	public boolean isValid(Point x) {
		return isValid(x.x, x.y);
	}
	
	public Point[] getLocationAround(int x, int y) {
		Point[] p = new Point[8];
		
		//0, 1: water's move
		if (isValid(x + 1, y - 1)) {
			p[0] = new Point(x+1, y-1);
		}
		if (isValid(x - 1, y - 1)) {
			p[1] = new Point(x-1, y-1);
		}
		
		//2, 3: fire's move
		if (isValid(x + 1, y + 1)) {
			p[2] = new Point(x+1, y+1);
		}
		if (isValid(x - 1, y + 1)) {
			p[3] = new Point(x-1, y+1);
		}
		
		//4, 5: water's move
		if (isValid(x + 2, y - 2)) {
			p[4] = new Point(x+2, y-2);
		}
		if (isValid(x - 2, y - 2)) {
			p[5] = new Point(x-2, y-2);
		}
		
		//6, 7: fire's move
		if (isValid(x + 2, y + 2)) {
			p[6] = new Point(x+2, y+2);
		}
		if (isValid(x - 2, y + 2)) {
			p[7] = new Point(x-2, y+2);
		}
		
		return p;
	}
	
	public boolean canMove() {
		for (int i=0;i<pieces.length;i++) {
			for (int j=0;j<pieces[i].length;j++) {
				if (pieceAt(i, j) != null && pieceAt(i, j).side() == side && canMove(i, j)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean canMove(int x, int y) {
		Point[] plist = getLocationAround(x, y);
		for (Point p : plist) {
			if (p != null && canMove(x, y, p.x, p.y)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canMove(int x1, int y1, int x2, int y2) {
		Piece piece = pieceAt(x1, y1);
		Point[] loc = getLocationAround(x1, y1);
		Point target = new Point(x2, y2);
		
		if (pieceAt(target) != null) return false;
		
		if (moved && piece != capturer) return false;
		
		// Regular Move
		if (piece != capturer) {
			if (piece.isKing() || piece.side() == Piece.SIDE_WATER) {
				if ((loc[0] != null && target.equals(loc[0])) || (loc[1] != null && target.equals(loc[1]))) {
					return true;
				}
			}
			if (piece.isKing() || piece.side() == Piece.SIDE_FIRE) {
				if ((loc[2] != null && target.equals(loc[2])) || (loc[3] != null && target.equals(loc[3]))) {
					return true;
				}
			}
		}
		
		//Capture Move, Water's 
		if (piece.side() == Piece.SIDE_WATER) {
			if ((loc[4] != null && pieceAt(loc[0]) != null && pieceAt(loc[0]).side() != Piece.SIDE_WATER && target.equals(loc[4]))
					|| (loc[5] != null && pieceAt(loc[1]) != null && pieceAt(loc[1]).side() != Piece.SIDE_WATER
							&& target.equals(loc[5]))) {
				return true;
			}
			
			if(piece.isKing()) {
				if ((loc[6] != null && pieceAt(loc[2]) != null && pieceAt(loc[2]).side() != Piece.SIDE_WATER
						&& target.equals(loc[6]))
						|| (loc[7] != null && pieceAt(loc[3]) != null && pieceAt(loc[3]).side() != Piece.SIDE_WATER
								&& target.equals(loc[7]))) {
					return true;
				}
			}
		}
		
		//Capture Move(Fire)
		if (piece.side() == Piece.SIDE_FIRE) {
			if ((loc[6] != null && pieceAt(loc[2]) != null && pieceAt(loc[2]).side() != Piece.SIDE_FIRE
					&& target.equals(loc[6]))
					|| (loc[7] != null && pieceAt(loc[3]) != null && pieceAt(loc[3]).side() != Piece.SIDE_FIRE
							&& target.equals(loc[7]))) {
				return true;
			}
			if (piece.isKing()) {
				if ((loc[4] != null && pieceAt(loc[0]) != null && pieceAt(loc[0]).side() != Piece.SIDE_FIRE
						&& target.equals(loc[4]))
						|| (loc[5] != null && pieceAt(loc[1]) != null && pieceAt(loc[1]).side() != Piece.SIDE_FIRE
								&& target.equals(loc[5]))) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Moves the active piece to coordinate (x, y)
	 * 
	 * @param p
	 *            Piece to move
	 * @param x1
	 *            Original X coordinate of p
	 * @param y1
	 *            Origin Y coordinate of p
	 * @param x
	 *            X coordinate to move to
	 * @param y
	 *            Y coordinate to move to
	 */
	public void move(Piece p, int x1, int y1, int x2, int y2) {
		if (canMove(x1, y1, x2, y2)) {
			//move
			place(p, x2, y2);
			remove(x1, y1);
			if ((p.side() == Piece.SIDE_FIRE && y2 == 7) || (p.side() == Piece.SIDE_WATER && y2 == 0)) {
				p.becomeKing();
			}
			
			//reset capturer first
			captured = false;
			capturer = null;
			
			//capture
			if(Math.abs(x2 - x1) == 2 || Math.abs(y2 - y1) == 2) {
				remove(x1 + (x2 - x1) / 2, y1 + (y2 - y1) / 2);
				p.explode(x2, y2);
				captured = true;
				capturer = selected;
			}
			
			moved = true;
		}
		
		String winner = winner();
		if (winner.equals("Water")) {
			endTime = new Date();
			gameover = true;
			msg = "Water wins the game!!!";
		} else if (winner.equals("Fire")) {
			endTime = new Date();
			gameover = true;
			msg = "Fire wins the game!!!";
		} else if (winner.equals("Tie")) {
			endTime = new Date();
			gameover = true;
			msg = "It's a TIE!!!";
		}
		
		selected = null;
	}
	public void move(Piece p, Point p1, Point p2) {
		move(p, p1.x, p1.y, p2.x, p2.y);
	}

	/**
	 * Determines if the turn can end
	 * 
	 * @return true if the turn can end
	 */
	public boolean canEndTurn() {
		if (moved) 
			return true;
		return false;
	}

	/**
	 * Ends the current turn. Changes the player.
	 */
	public void endTurn() {
		if (canEndTurn()) {
			if (side == Piece.SIDE_FIRE){
				side = Piece.SIDE_WATER;
				msg = "Water's Move";
			}
			else {
				side = Piece.SIDE_FIRE;
				msg = "Fire's Move";
			}
			
			moved = false;
			selected = null;
			captured = false;
			capturer = null;
		} 
	}

	/**
	 * Returns the winner of the game
	 * 
	 * @return The winner of this game
	 */
	public String winner() {
		int fire = 0;
		int water = 0;
		for (int i=0;i<pieces.length;i++) {
			for (int j=0;j<pieces[i].length;j++) {
				if (pieceAt(i, j) != null) {
					if (pieceAt(i, j).side() == Piece.SIDE_WATER) {
						water++;
					} else if (pieceAt(i, j).side() == Piece.SIDE_FIRE) {
						fire++;
					}
				}
			}
		}
		if (fire == 0 && water == 0) {
			return "Tie";
		} else if (fire == 0) {
			return "Water";
		} else if (water == 0){
			return "Fire";
		} else {
			return "NO winner";
		}
	}
	
	public void mouseMove() {
		int x = (int)(StdDrawPlus.mouseX() - xOffset + 0.5);
		int y = (int)(StdDrawPlus.mouseY() - yOffset + 0.5);
		if (isValid(x, y)) {
			if (selected == null)
				select(x, y);
			else if (pieceAt(x, y) == null) {
				move(selected, selectedX, selectedY, x, y);
			} else if (pieceAt(x, y) != selected) {
				select(x, y);
			}
		}
	}

	
	
	
	
	/**
	 * Starts a game
	 */
	public static void main(String[] args) {
		StdDrawPlus.setCanvasSize(750, 750);
		StdDrawPlus.setYscale(0, 10);
		StdDrawPlus.setXscale(0, 10);
		Board b = new Board();
		while (true) {
			b.drawBoard();
			if (StdDrawPlus.mousePressed()) {
				b.mouseMove();
			}
			if (StdDrawPlus.isSpacePressed()) {
				b.endTurn();
			}
			StdDrawPlus.show(10);
		}

	}
}