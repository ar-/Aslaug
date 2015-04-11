package de.aslaug.mensch;

public class Field {
	private Pawn occupier;

	public Field() {
		super();
		occupier = null;
	}

	public Pawn getOccupier() {
		return occupier;
	}

	public void setOccupier(Pawn occupier) {
		this.occupier = occupier;
	}

	@Override
	public String toString() {
		if (occupier==null)
			return "_";
		return occupier.getColor().toString().substring(0, 1).toUpperCase();
		//return "Field [occupier=" + occupier + "]";
	}
	

}
