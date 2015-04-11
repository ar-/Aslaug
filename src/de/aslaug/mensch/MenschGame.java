package de.aslaug.mensch;

public class MenschGame implements Validatable
{
	private final MenschBoard board;
	private final Dice dice;
	private final MenschPlayer[] players;
	private Integer currentPlayerIndex;
	private boolean gameEnded;
	private int stepCounter;
	
	public MenschGame() {
		super();
		board = new MenschBoard();
		dice = new Dice();
		players = new MenschPlayer[4];

		players[0]= new MenschPlayer(Color.red, "RedPlayer0", this);
		players[1]= new MenschPlayer(Color.blue, "BluePlayer1", this);
		players[2]= new MenschPlayer(Color.green, "GreenPlayer2", this);
		players[3]= new MenschPlayer(Color.yellow, "YellowPlayer3", this);
		
		currentPlayerIndex = null; // first player no decided yet
		gameEnded = false;
		stepCounter = 0;
	}

	@Override
	public void validate() {
		board.validate();
		dice.roll(); // roll it for fun (and for initialisation)
		dice.validate();
		for (int i = 0; i < players.length; i++) {
			players[i].validate();
		}
		if (currentPlayerIndex==null)
			throw new ValidationException("MenschGame: first player not determined yet");
		if (currentPlayerIndex<0)
			throw new ValidationException("MenschGame: current player index is less than 0");
		if (currentPlayerIndex>players.length)
			throw new ValidationException("MenschGame: current player index higher than amount of players");
		if (getCurrentPlayer()==null)
			throw new ValidationException("MenschGame: CurrentPlayer is null");
		
	}

	public MenschBoard getBoard() {
		return board;
	}

	public MenschPlayer[] getPlayers() {
		return players;
	}

	public Dice getDice() {
		return dice;
	}

	public void determineFirstPlayer() {
		// simple for now: select one player randomly
		currentPlayerIndex = IntervalRandom.randInt(0,3);
		
		// TODO implement the first player determination correctly
		// put all players into a List
		// let them all roll
		// if highest number is held by many, let these many roll again, until highest number is unique
		
	}

	public MenschPlayer getCurrentPlayer() {
		return players[currentPlayerIndex];
	}
	
	public void letNextPlayerPlay() {
		currentPlayerIndex++;
		if (currentPlayerIndex>=players.length)
			currentPlayerIndex=0;
	}
	
	public void step()
	{
		stepCounter++;
		getCurrentPlayer().playAStep();
		letNextPlayerPlay();
	}
	
	/**
	 * just a quick hacky funtion to cancel the game at some stage
	 */
	private void cancelIfOnePlayerFinished() {
		for (int i = 0; i < players.length; i++) {
			if (players[i].hasFinished())
			{
				gameEnded=true;
				System.out.println(String.format("Player %s finished after %d steps in the game",players[i],stepCounter));
			}
		}
	}
	
	public void run()
	{
		determineFirstPlayer();
		validate();
		while (!gameEnded)
		{
			step();
			validate();
			System.out.println(board);
			cancelIfOnePlayerFinished();
			//if (stepCounter>10) break; // only one step (for initial testing)
		}
	}
	

}
