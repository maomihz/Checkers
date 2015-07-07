package com.maomihz;
/*DemoBoard.java*/

/**
 * Demo showing how StdDrawPlus works for the Checkers61BL project
 * 
 * Represents a simple Tic-Tac-Toe board
 * 
 * @author Daniel Nguyen
 */

public class DemoBoard {

	/**
	 * pieces is the internal representation of the board 0 is X, 1 is O, -1 is
	 * an empty space
	 */

	public static final int STATE_X = 0;
	public static final int STATE_O = 1;
	public static final int STATE_EMPTY = -1;
	public static final int STATE_NULL = 127;

	private int[][] pieces;
	private int side;
	private double xOffset = 0.5;
	private double yOffset = 0.5;
	private String msg = "New Game";

	/**
	 * Constructor for a DemoBoard
	 * 
	 * @param N
	 *            size of the board
	 */
	public DemoBoard(int N) {
		pieces = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				pieces[i][j] = -1;
			}
		}
	}

	// reset all to 1
	public void reset() {
		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[i].length; j++) {
				pieces[i][j] = -1;
			}
		}
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void switchSide() {
		side = 1 - side;
	}

	public int getSide() {
		return side;
	}

	public int getState(int x, int y) {
		try {
			return pieces[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return STATE_NULL;
		}
	}

	public void setState(int x, int y, int state) {
		try {
			pieces[x][y] = state;
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	/**
	 * Draws the board based on the configuration of Pieces
	 */
	private void drawBoard() {
		StdDrawPlus.clear(); // Clear the screen

		for (int i = 0; i < pieces.length; i++) {
			for (int j = 0; j < pieces[0].length; j++) {
				if ((i + j) % 2 == 0) {
					StdDrawPlus.setPenColor(StdDrawPlus.GRAY);
				} else {
					StdDrawPlus.setPenColor(StdDrawPlus.CYAN);
				}

				StdDrawPlus.filledSquare(i + yOffset, j + xOffset, .5);
				if (pieces[i][j] == 0) {
					StdDrawPlus.picture(i + yOffset, j + xOffset, "img/x.png", 1, 1);
				} else if (pieces[i][j] == 1) {
					StdDrawPlus.picture(i + yOffset, j + xOffset, "img/o.png", 1, 1);
				}
			}
		}

		StdDrawPlus.setPenColor(StdDrawPlus.BLACK);
		StdDrawPlus.text(2, .5, msg);

		StdDrawPlus.filledSquare(2, 2,
				StdDrawPlus.mouseY() - 2 < 0 ? -(StdDrawPlus.mouseY() - 2) : StdDrawPlus.mouseY() - 2);

		System.out.println("X:" + StdDrawPlus.mouseX() + ", Y:" + StdDrawPlus.mouseY());

	}

	/**
	 * Runs the Demo. Watches for mouse clicks and changes the Board state Does
	 * not prevent you from clicking on a square that has already been pressed
	 * Changes sides when space is pressed
	 */
	public static void main(String[] args) {
		DemoBoard b = new DemoBoard(3);
		StdDrawPlus.setScale(0, 4);
		b.side = 0;
		while (true) {
			b.drawBoard();
			if (StdDrawPlus.mousePressed()) {
				int x = (int) StdDrawPlus.mouseX();
				int y = (int) StdDrawPlus.mouseY();
				if (b.getState(x, y) == DemoBoard.STATE_EMPTY) {
					b.setState(x, y, b.getSide());
					b.switchSide();
					b.setMsg("It's " + b.getSide() + "'s move");
				}

			}

			if (StdDrawPlus.isSpacePressed()) {
				b.reset();
				b.setMsg("New Game");
			}
			StdDrawPlus.show(10);

		}

	}

}
