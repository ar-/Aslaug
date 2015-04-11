package de.aslaug.mensch;

import java.util.Arrays;

/**
 * 
 * 
 OOO OOO OOO OOO OOOOOOOOOOO OOOOO OOOOO OOOOOOOOOOO OOO OOO OOO OOO
 * 
 * O8.09.10 OO.45.OO OO.OO.OO OO.OO.OO O0.OO.OO.OO.O4.48.14.OO.OO.OO.18
 * 39.41.OO.OO.44. .52.OO.OO.49.19 38.OO.OO.OO.34.56.24.OO.OO.OO.20 OO.OO.OO
 * OO.OO.OO OO.53.OO 30.29.28
 * 
 * 40 is unused 0 is first 56 is last (size)
	 * 	            O8.09.10
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
		            O8.09.10

 * colors from here:
 * http://upload.wikimedia.org/wikipedia/commons/thumb/9/91/Menschenaergern.svg/2000px-Menschenaergern.svg.png

 * @author yuckfou
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
		            O8.09.10

 * 
 */

public class MenschBoard implements Validatable {
	private final Field[] fields;
	private final Sidebag[] sidebags;

	public MenschBoard() {
		super();
		fields = new Field[64];
		sidebags = new Sidebag[4];

		for (int i = 0; i < fields.length; i++) {
			fields[i] = new Field();
		}

		// create one sidebag for each color
		for (int i = 0; i < sidebags.length; i++) {
			sidebags[i] = new Sidebag(Color.values()[i]);
		}
	}

	@Override
	public void validate() {
		Integer bagSize = null;
		for (int i = 0; i < sidebags.length; i++) {
			if (sidebags[i] == null)
				throw new ValidationException("Sidebag " + i
						+ "  not initialized");
			sidebags[i].validate();

			// check if they all have the same size
			if (bagSize != null) {
				if (bagSize != sidebags[i].getSize())
					throw new ValidationException(
							"Sidebags on Board dont have al lthe same size");
			}
			bagSize = sidebags[i].getSize();
		}

		final int pawnsPerColor = bagSize;
		final int numberOfBags = sidebags.length;
		final int expectedPawnCount = pawnsPerColor * numberOfBags;

		// count all pawns on the board (must be 4 times 4)
		// also count per color in a map (map is now assoc array)
		int countedPawns = 0;
		int[] pawnsColorCounters = new int[Color.values().length];

		// count in sidebags
		for (int i = 0; i < sidebags.length; i++) {
			pawnsColorCounters[sidebags[i].getColor().ordinal()] = sidebags[i]
					.getNumberOfPawns();
			countedPawns += sidebags[i].getNumberOfPawns();
		}

		// count on the field
		for (int i = 0; i < fields.length; i++) {
			Pawn occupier = fields[i].getOccupier();
			if (occupier != null) {
				pawnsColorCounters[occupier.getColor().ordinal()]++;
				countedPawns++;
			}
		}

		// VALIDATE everything
		for (int i = 0; i < pawnsColorCounters.length; i++) {
			if (pawnsColorCounters[i] != pawnsPerColor)
				throw new ValidationException("Wrong number of "
						+ Color.values()[i].toString() + " pawns on the board");
		}

		if (expectedPawnCount != countedPawns)
			throw new ValidationException("Wrong number of pawns on the board");
	}

	@Override
	/**
	 * 
	            O8.09.10
	            OO.44.OO
	            OO.OO.OO
	            OO.OO.OO
	O0.OO.OO.OO.O4.47.14.OO.OO.OO.18
	39.40.OO.OO.43.  .51.OO.OO.48.19
	38.OO.OO.OO.34.55.24.OO.OO.OO.20
	            OO.OO.OO
	            OO.OO.OO
	            OO.52.OO
	            30.29.28
	        
	        
	RR      _._.B      BB
	RR      _.B._      BB
	        _.B._
	        _.B._
	R._._._._.B._._._._._
	_.R.R.R.R   G.G.G.G._
	_._._._._.Y._._._._.G
	        _.Y._
	        _.Y._
	 YY     _.Y._      GG
	 YY     Y._._      GG
	 
	        _._._
	        _._._
	        _._._
	        _._._
	_._._._._._._._._._._
	_._._._._   _._._._._
	_._._._._._._._._._._
	        _._._
	        _._._
	        _._._
	        _._._
	        
	 */
	public String toString() {
		
		// TODO there is something wrong. GREEN is in the goal of blue, and red is always winning!!
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < sidebags.length; i++) {
			final Sidebag sidebag = sidebags[i];
			String s = sidebag.getColor()+ ": "+ sidebag.getNumberOfPawns();
			sb.append(s + "; ");
		}
/*
										            O8.09.10
										            OO.44.OO
										            OO.OO.OO
										            OO.OO.OO
										O0.OO.OO.OO.O4.47.14.OO.OO.OO.18
										39.40.OO.OO.43.  .51.OO.OO.48.19
										38.OO.OO.OO.34.55.24.OO.OO.OO.20
										            OO.OO.OO
										            OO.OO.OO
										            OO.52.OO
										            30.29.28
 */
		sb.append("\n");
		final Field[] f = fields;
		sb.append(String.format("	        %s.%s.%s\n",  f[8],  f[9], f[10]));
		sb.append(String.format("	        %s.%s.%s\n",  f[7], f[44], f[11]));
		sb.append(String.format("	        %s.%s.%s\n",  f[6], f[45], f[12]));
		sb.append(String.format("	        %s.%s.%s\n",  f[5], f[46], f[13]));
		sb.append(String.format("	%s.%s.%s.%s.%s.%s.%s.%s.%s.%s.%s\n",  f[0],  f[1],  f[2],  f[3],  f[4], f[47], f[14], f[15], f[16], f[17], f[18]));
		sb.append(String.format("	%s.%s.%s.%s.%s. .%s.%s.%s.%s.%s\n",  f[39], f[40], f[41], f[42], f[43],        f[51], f[50], f[49], f[48], f[19]));
		sb.append(String.format("	%s.%s.%s.%s.%s.%s.%s.%s.%s.%s.%s\n", f[38], f[37], f[36], f[35], f[34], f[55], f[24], f[23], f[22], f[21], f[20]));
		sb.append(String.format("	        %s.%s.%s\n", f[33], f[54], f[25]));
		sb.append(String.format("	        %s.%s.%s\n", f[32], f[53], f[26]));
		sb.append(String.format("	        %s.%s.%s\n", f[31], f[52], f[27]));
		sb.append(String.format("	        %s.%s.%s\n", f[30], f[29], f[28]));
		return sb.toString();
//		return "MenschBoard [fields=" + Arrays.toString(fields) + ", sidebags="
//				+ Arrays.toString(sidebags) + "]";
	}
	
	/**
	 * Returns the resulting index, if a diceRoll would be applied to the from-position
	 * for a certain color.
	 * The next index is determined by the color of the pawn.
	 * We can always ask the MenschBoard where a for a certain color would end.

	 * @param fromIndex the index of on the menschBoard, where the move is supposed to start
	 * @param diceRoll the current dice roll, so the amount of fields to move
	 * @param color the color of the moving pawn
	 * @return
	 * @throws MoveOutOfRangeException 
	 */
	public final int getResultingIndexOfMoveForColor(int fromIndex, int diceRoll, Color color) throws MoveOutOfRangeException {
		final int lastNonGoalFieldIndex=39;
		final int entryIndex = color.ordinal()*10;
		final int exitIndex = (entryIndex+lastNonGoalFieldIndex)%40;
		final int firstGoalIndexMinusOne = lastNonGoalFieldIndex + color.ordinal() * 4;
		final int lastGoalIndex = firstGoalIndexMinusOne + 4; // 4 goal fields per color
		
		
		// the code is more readable, if each move is done 1 by 1 (like a child)
		// it it can be seen clearly if the exitIndex is crossed
		int result = fromIndex;
		for (int i = 0; i < diceRoll; i++) {
			if (result==exitIndex)
			{
				result = firstGoalIndexMinusOne; // 4 is amount of goal fields per color
				
				// do an additional verification
				if (!isIndexGoalForColor(result+1,color))
					throw new ValidationException(String.format("Move for color %s from %d (rolled %d should end in Goal area but doesnt", color.toString(),fromIndex,diceRoll));
			}
			else if (result==lastNonGoalFieldIndex) // don't go in there on pos 39
			{
				result = -1; // -1 is here start position (0) minus one, will be incremented in next step
			}
			result++;
		}
		
		// check if in valid area and negate the expression
		if (!((result >=0 && result <=lastNonGoalFieldIndex) || (result > firstGoalIndexMinusOne && result <= lastGoalIndex )))
			throw new MoveOutOfRangeException();
		
		return result;
	}
	
	public final boolean isIndexGoalForColor(int index,Color color)
	{
		// doing the other way again is useless
		//final int lastNonGoalFieldIndex=39;
		//final int firstGoalIndexMinusOne = lastNonGoalFieldIndex + color.ordinal() * 4;
		//final int lastGoalIndex = firstGoalIndexMinusOne + 4;
		//return (index>firstGoalIndexMinusOne && index<=lastGoalIndex);

		// just do a different check
		if ((color==Color.red) && (index >= 40) && (index <=43))
			return true;
		if ((color==Color.blue) && (index >= 44) && (index <=47))
			return true;
		if ((color==Color.green) && (index >= 48) && (index <=51))
			return true;
		if ((color==Color.yellow) && (index >= 52) && (index <=55))
			return true;
		
		return false;
	}

	public Field[] getFields() {
		return fields;
	}

	public Sidebag[] getSidebags() {
		return sidebags;
	}
	
	

}
