package de.aslaug.mensch;

/**
 * Describes a move. 
 * from From-field-index to To-field-index
 * 
 * Move can be annotated by the player to mark
 * will-bring-home
 * will-beat
 * 
 * 
 * Special move is: -1 to 0 (or to 10,20,30)
 * 

	            O8.09.10
	            OO.45.OO
	            OO.OO.OO
	            OO.OO.OO
	O0.OO.OO.OO.O4.48.14.OO.OO.OO.18
	39.41.OO.OO.44.  .52.OO.OO.49.19
	38.OO.OO.OO.34.56.24.OO.OO.OO.20
	            OO.OO.OO
	            OO.OO.OO
	            OO.53.OO
	            30.29.28

 * @author yuckfou
 *
 */
public class Move implements Validatable
{
	public boolean willBringHome;
	public boolean willBeat;
	public Color willBeatColor;
	public boolean willBringInNewPawn;
	
	/**
	 * the field index on the board where to move starts
	 */
	public final int from;
	
	/**
	 * the field index on the board where to move ends
	 */
	public final int to;

	public Move(int from, int to) {
		super();
		this.from = from;
		this.to = to;
		
		this.willBringHome = false;
		this.willBeat = false;
		this.willBeatColor = null;
		this.willBringInNewPawn = false;
	}

	@Override
	public void validate() {
		if (willBringHome && willBeat)
			throw new ValidationException("Not possible: move will bring to goal (home) and beat. No beatable enemy can be in home.");
		
		if (willBringHome && willBringInNewPawn)
			throw new ValidationException("Not possible: no move can bring in new pawn and bring it into the goal area at the same time.");
		
	}

	@Override
	public String toString() {
		return "Move [from=" + from + ", to=" + to + "]";
	}
	
	
	
	
}
