package model;

public class Case {

	private final int row;

	private final int col;

	private final Ibonbon ibonbon;

	private int previousRow;

	public Case(int row, int col, Ibonbon ibonbon) {
		this.row = row;
		this.col = col;
		this.ibonbon = ibonbon;
		this.previousRow = row;
	}

	public void setPreviousRow(int row) {
		previousRow = row;
	}

	public int getPreviousRow() {
		return previousRow;
	}

	public Ibonbon getIbonbon() {
		return ibonbon;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean Position(Case other) {
		return row == other.row && col == other.col;
	}

	@Override
	public String toString() {
		if (row == previousRow) {
			return String.format("[(%d,%d) %d]", row, col, ibonbon.getType());
		} else {
			return String.format("[(%d,%d) %d (%d)]", row, col,
					ibonbon.getType(), previousRow);
		}
	}

}
