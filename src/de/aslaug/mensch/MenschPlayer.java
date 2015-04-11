package de.aslaug.mensch;

import java.util.ArrayList;
import java.util.List;


public class MenschPlayer implements Validatable
{
	private final Color color;
	private final String name;
	
	/**
	 * every player has the ability to memorize ALL his recent dice rolls. This is cool for statistics.
	 */
	private final ArrayList<Integer> diceRolls;
	
	/**
	 * a player always belongs to exactly one game. the game must be set here
	 */
	private final MenschGame game;

	public MenschPlayer(Color color, String name, MenschGame game) {
		super();
		this.color = color;
		this.name = name;
		this.game = game;
		diceRolls = new ArrayList<>();
	}

	@Override
	public String toString() {
		return "MenschPlayer [color=" + color + ", name=" + name + "]";
	}
	
	/**
	 * this player takes the dice and rolls it.
	 * Usually this can be private, because the only way to roll a dice is initiated by the player himself
	 * in playAStep().
	 * Anyways, there might be situation in future, where 'others' make the player roll the dices
	 * for example, to determine the starting-player before the game starts, the game could trigger
	 * each player to roll the dice (maybe several times).
	 * @return
	 */
	public int rollDice() {
		int ret;
		ret = game.getDice().roll();
		diceRolls.add(ret);
		return ret;
	}
	
	private int getLastDiceRoll() {
		return diceRolls.get(diceRolls.size()-1);
	}

	@Override
	public void validate() {
		if (game == null)
			throw new ValidationException(this.toString()+" not assigned to a game");
		
		// validate number of own pawns
		final int expectedNumerOfPawns= getOwnSidebag().getSize();
		final int countedNumberOfPawns = getOwnSidebag().getNumberOfPawns() + getFieldPawnIndexes().size();
		if (expectedNumerOfPawns != countedNumberOfPawns)
			throw new ValidationException("Counted a wrong number of pawns ("+countedNumberOfPawns+") for player"+toString());
		
	}

	public void playAStep() {
		rollDice();
		List<Move> lm = getValidMoves();
		
		// TODO the STRATEGIES are implemented here: strategies determine which of the moves to go for
		
		// simple strategy: just use the first possible move (working but not useful in the simulation results)
		if (lm.size()>0)
		{
			executeMove (lm.get(0));
		}
	
	}
	
	/**
	 * ACID transaction move.
	 * @param m
	 */
	private void executeMove(Move m) {
		// the next index is determined by the color of the own pawn
		// we can always ask the MenschBoard where a Move for a certain color will end
		final Field[] f = getFields();
		final Pawn pawnToMove;
		if (m.from>=0)
			pawnToMove= f[m.from].getOccupier();
		else
			pawnToMove= new Pawn(color);
		
		final Pawn pawnAtDest = f[m.to].getOccupier();
		
		// if someone to beat at destination : beat it
		if (pawnAtDest != null)
		{
			getBoard().getSidebags()[pawnAtDest.getColor().ordinal()].putIn();
			f[m.to].setOccupier(null); // not needed - but compiler will remove this
		}
		f[m.to].setOccupier(pawnToMove);
		if (m.from>=0)
			f[m.from].setOccupier(null);
		else
			getBoard().getSidebags()[color.ordinal()].takeOut();
	}

	/**
	 * returns a list of all currently valid(legal) moves.
	 * player looks onto the board for that.
	 * 
	 * @return
	 */
	private List<Move> getValidMoves() {
		// TODO can there only be up to 4 possible moves?
		ArrayList<Move> ret = new ArrayList<Move>(4);
		
		// some shortcuts
		//final MenschBoard b = getBoard();
		final Sidebag sb = getOwnSidebag();
		final Field[] f = getFields();

		final int entryIndex = color.ordinal()*10; // 0,10,20 or 30
		
		// TODO the RULES are implemented here: rules determine valid (legal) moves
		
		int diceRoll = getLastDiceRoll();
		
		// if there is 1 in he side bag and first field is free
		// then the init-move (from -1) is possible
		if ((diceRoll ==6) && (sb.getNumberOfPawns()>0) && (f[entryIndex].getOccupier()==null ))
		{
			System.out.println("adding -1 move to sb: "+sb);
			Move m = new Move(-1, entryIndex);
			m.willBringInNewPawn=true;
			ret.add(m);
		}
		
		// iterate over all own pawns on the field, to check if they can move legally
		for (Integer fieldIndex : getFieldPawnIndexes()) {
			final Field fi = f[fieldIndex];
			// just verify quickly if we found the right one
			if (fi.getOccupier().getColor() != color)
				throw new RuntimeException("Something went wrong. Tried to move not owned pawn.");
			
			//fill the valid moves here
			try {
				int to = getBoard().getResultingIndexOfMoveForColor(fieldIndex,diceRoll,color);
				Move m = new Move(fieldIndex, to);
				
				if (getBoard().isIndexGoalForColor(to, color))
					m.willBringHome=true;
				// TODO make it illegal to jump over self in goal area (if rule for that is set)
				
				final Pawn occupierAtDestination = getFields()[to].getOccupier();
				if (occupierAtDestination!=null)
				{
					final Color colorAtDestination = occupierAtDestination.getColor();
					// TODO special rules for hitting itself can go in here
					if (colorAtDestination == color)
						continue; // will hit itself, continue the loop and don't add it
					else
					{
						// someone else is there
						m.willBeat = true;
						m.willBeatColor = colorAtDestination;
					}
				}
					
				ret.add(m);
			} catch (MoveOutOfRangeException e) {
				// expectable exception
				// ignore exception and simply don't add this one as a valid move
			}
		}
		
		
		return ret;
	}
	
	private MenschBoard getBoard() {
		return game.getBoard();
	}
	
	private Sidebag getOwnSidebag() {
		return getBoard().getSidebags()[color.ordinal()];
	}
	
	private Field[] getFields() {
		return getBoard().getFields();
	}
	
	/**
	 * finds all pawns owned by the player, on the fields (so not in a sidebag)
	 * and returns a list of their indexes
	 * @return
	 */
	private List<Integer> getFieldPawnIndexes() {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		
		// iterate all fields on board and add all own pawns to an indexed list
		for (int i = 0; i < getFields().length; i++) {
			final Field f = getFields()[i];
			if (f.getOccupier() !=null && 
					f.getOccupier().getColor() == color)
				ret.add(i);
		}
		
		return ret;
	}
	
	/**
	 * Player can decide by himself if he has finished. This could also be done by a referee instance or by the board,
	 * but with all the existing function is implements very well here.
	 * And it uses the board function to check if they are in goal.
	 * @return true if all pawns of this player are on a goal field
	 */
	public boolean hasFinished() {
		final List<Integer> fpis = getFieldPawnIndexes();
		if (fpis.size()<4)
			return false;
		
		for (Integer fpi : fpis) {
			if (!getBoard().isIndexGoalForColor(fpi, color))
				return false;
		}
		
		return true;
	}
}
