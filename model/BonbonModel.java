package model;

public class BonbonModel implements Ibonbon {

	private int type;

	public BonbonModel(int type) {
		this.type = type;
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || obj.getClass() != this.getClass())
			return false;
		BonbonModel other = (BonbonModel) obj;
		return type == other.type;
	}

	@Override
	public String toString() {
		return "" + type;
	}

}
