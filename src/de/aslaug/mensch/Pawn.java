package de.aslaug.mensch;

public class Pawn {
	private final Color color;

	public Pawn(Color color) {
		super();
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return "Pawn [color=" + color + "]";
	}
	
}
