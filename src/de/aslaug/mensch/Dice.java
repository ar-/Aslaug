package de.aslaug.mensch;


public class Dice implements Validatable {
	
	Integer lastRoll;
	
	
	public Dice() {
		super();
		lastRoll = null;
	}
	

	public int getLastRoll() throws Exception {
		if (lastRoll==null)
			throw new Exception("getLastRoll called before first roll() was done");
		return lastRoll;
	}

	public int roll() {
		lastRoll=IntervalRandom.randInt(1,6);
		
		//TODO is validatig the dice all the time a good idea?
		this.validate();
		return lastRoll;
	}

	

	@Override
	public String toString() {
		return "Dice [lastRoll=" + lastRoll + "]";
	}


	@Override
	public void validate() {
		if (lastRoll<1)
			throw new ValidationException("lastRoll to small");

		if (lastRoll>6)
			throw new ValidationException("lastRoll to big");
	}
}
