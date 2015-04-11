package de.aslaug.mensch;

/**
 * one side bad that contains between 0 and 4 reserve pawns
 * .
 * This doesn't use fields because the order doesn't matter. Only the number in the bag is important.
 * The color of the bag is here also important, to verify later, if all pawns are in the correct bags or on the field.
 * @author yuckfou
 *
 */
public class Sidebag implements Validatable {

	/**
	 * max number of pawns that can be in this bag
	 */
	private final int size;
	
	/**
	 * current number of pawns in this bag
	 */
	private int numberOfPawns;
	
	private final Color color;

	private Sidebag(int numberOfPawns, Color color) {
		super();
		size = 4;
		this.numberOfPawns = numberOfPawns;
		this.color = color;
	}

	public Sidebag(Color color) {
		this(4,color);
	}

	public void takeOut() {
		this.numberOfPawns--;
		if (numberOfPawns<0)
			throw new RuntimeException("numberOfPawns dropped under zero in Sidebag:"+this);
	}
	
	public void putIn() {
		this.numberOfPawns++;
	}
	
	/* (non-Javadoc)
	 * @see de.aslaug.mensch.Validatable#validate()
	 */
	@Override
	public void validate() {
		if (numberOfPawns<0)
			throw new ValidationException("numberOfPawns to small in Sidebag:"+this);

		if (numberOfPawns>size)
			throw new ValidationException("numberOfPawns to big in Sidebag:"+this);
	}

	public int getSize() {
		return size;
	}

	public int getNumberOfPawns() {
		return numberOfPawns;
	}

	public Color getColor() {
		return color;
	}

	@Override
	public String toString() {
		return "Sidebag [size=" + size + ", numberOfPawns=" + numberOfPawns
				+ ", color=" + color + "]";
	}
	
	

}
