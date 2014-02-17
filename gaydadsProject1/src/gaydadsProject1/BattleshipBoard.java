/**
 * 
 */
package gaydadsProject1;

/**
 * @author David Gayda
 * Instructor: Prof. Bachman
 * CSE 283, Section B
 * 
 * This BattleshipBoard contains methods to create and maintain
 * a simple battleship game. This is to be used with the server
 * and client class of the Project 1 Program.
 * 
 * MAX_GUESSES can be changed in this class to limit the length of the game.
 * 
 * All aspects of this program with the exception of a few 
 * noted pieces provided by Prof. Bachman
 *
 */
public class BattleshipBoard {

	//These ints are used to keep track of the codes needed to be used often.
	static final int MAX_GUESSES = 25;
	static final int BLANK = 1;
	static final int MISS = 2;
	static final int DESTROYER = 3;
	static final int CRUISER = 4;
	static final int BATTLESHIP = 5;
	static final int HIT = 6;
	static final int MISS_CODE = 10;
	static final int HIT_CODE = 20;
	static final int SINK_CODE = 30;
	static final int ILLEGAL_MOVE_CODE = 40;
	static final int GAME_CONTINUES = 10;
	static final int CLIENT_WON = 20;
	static final int SERVER_WON = 30;
	static final int ILLEGAL_MOVE_GAME_OVER = 40;

	
	Boolean isIllegalMoveTriggered;
	//This int is used to track how many guesses the user has and compare with the max allowed.
	int guessesCount;
	//These ints are used to determine random ship placements
	int randomRow;
	int randomCol;
	int randomAxis;
	//This array is used to track information on the board.
	int [] [] boardArray;
	//These ints track how many hits each ship has taken on.
	int destroyerCount;
	int cruiserCount;
	int battleshipCount;

	/**
	 * This is the constructor for the BattleshipBoard Object
	 */
	public BattleshipBoard() {

		this.isIllegalMoveTriggered = false;
		this.destroyerCount = 2;
		this.cruiserCount = 3;
		this.battleshipCount = 4;
		this.boardArray = createBoard();
	}
	
	/**
	 * This method is a shortcut to place all ships with one method
	 */
	protected void placeShips() {
		placeDestroyer(this.boardArray);
		placeCruiser(this.boardArray);
		placeBattleship(this.boardArray);
		
	}

	/**
	 * This method creates an array of arrays containing
	 * 1 (BLANK) the value for a blank space
	 * @return
	 */
	private int [][] createBoard() {

		int rows = 10;
		int cols = 10;
		int board[][] = new int[rows][cols];

		for (int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++) {

				board[i][j] = 1;

			}
		}
		return board;

	}

	/**
	 * This method takes in the board array and prints out
	 * the corresponding board to the console.
	 * @param board
	 */
	protected void printBoard() {

		//Taken from Example Code
		for (int r = 0; r < this.boardArray.length; r++) {

			System.out.print("|");

			for (int c = 0; c < this.boardArray[r].length; c++) {

				if (this.boardArray[r][c] == BLANK ) { 
					System.out.print(" _ |");
				}

				else if (this.boardArray[r][c] == MISS ) {
					System.out.print(" M |");
				}
				else if (this.boardArray[r][c] == DESTROYER ) {
					System.out.print(" D |");
				}
				else if (this.boardArray[r][c] == CRUISER ) {
					System.out.print(" C |");
				}
				else if (this.boardArray[r][c] == BATTLESHIP ) {
					System.out.print(" B |");
				}
				else {
					System.out.print(" H |");
				} 
			}
			System.out.println(); 
		}

	}
	
	/**
	 * This method checks compares the player's move and the current board.
	 * It returns an int specifying what has occurred (10, 20, 30, 40)
	 * @param row
	 * @param col
	 * @return
	 */
	protected int checkMoveStatus(int row, int col) {

		
		if (row > 9 || row < 0 || col > 9 || col < 0) {
			isIllegalMoveTriggered = true;
			return ILLEGAL_MOVE_CODE;
		}
		guessesCount++;
		if (this.boardArray[row][col] == DESTROYER ) {
			destroyerCount--;
			if (destroyerCount == 0) {
				return SINK_CODE;
			}
			else {
				return HIT_CODE;
			}
		}
		else if (this.boardArray[row][col] == CRUISER ) {
			cruiserCount--;
			if (cruiserCount == 0) {
				return SINK_CODE;
			}
			else {
				return HIT_CODE;
			}
		}
	
		else if (this.boardArray[row][col] == BATTLESHIP ) {
			battleshipCount--;
			if (battleshipCount == 0) {
				return SINK_CODE;
			}
			else {
				return HIT_CODE;
			}
		}
		
		else { 
			return MISS_CODE;
		}


	}

	/**
	 * This method checks the game status based on the current move and returns
	 * an int (10,20,30,40) determining if the game should end, who won, or if it continues.
	 * @return
	 */
	protected int checkGameStatus() {
		
		if (guessesCount == MAX_GUESSES ) {
			return SERVER_WON;
		}
		else if(destroyerCount == 0 && cruiserCount == 0 && battleshipCount == 0) {
			return CLIENT_WON;
		}
		else if (this.isIllegalMoveTriggered == true) {
			return ILLEGAL_MOVE_GAME_OVER;
		}
		else {
			return GAME_CONTINUES;
		}
		
		
	}

	/**
	 * This method places a "Destroyer" ship to the board.
	 * A "Destroyer" is a boat that is 2 spaces in length.
	 * No need to check for other ships on the board, only if out of bounds.
	 * @param board
	 */
	private void placeDestroyer(int board [][]) {

		//Choose Random Position and Axis between 1-9 to account for boundaries issues
		randomRow = (int) ((Math.random() * 9) + 1);
		randomCol = (int) ((Math.random() * 9) + 1);
		randomAxis = (int) (Math.random() * 2);



		//If randomAxis == 1 try to place Vertically
		if (randomAxis == 1) {
			board [randomRow][randomCol] = DESTROYER;
			board [randomRow - 1][randomCol] = DESTROYER;

		}
		else {
			board [randomRow][randomCol] = DESTROYER;
			board [randomRow][randomCol - 1] = DESTROYER;


		}
	}


	/**
	 *This method places a "Cruiser" ship to the board.
	 * A "Cruiser" is a boat that is 3 spaces in length.
	 * If the ship is overlapping another, I just choose another random number.
	 * Given that only 3 ships are on the board at any time, there is no need to check
	 * for a full board or lack of positions.
	 * @param board
	 */
	private void placeCruiser(int board [][]) {

		//Choose Random Position and Axis between 2-8 to account for boundaries issues
		randomRow = (int) ((Math.random() * 8) + 2);
		randomCol = (int) ((Math.random() * 8) + 2);
		randomAxis = (int) (Math.random() * 2);

		//If randomAxis == 1 try to place Vertically
		if (randomAxis == 1) {

			while (board [randomRow][randomCol] != BLANK
					|| board [randomRow - 1][randomCol] != BLANK
					|| board [randomRow - 2][randomCol] != BLANK) {

				randomRow = (int) ((Math.random() * 8) + 2);
				randomCol = (int) ((Math.random() * 8) + 2);

			}

			board [randomRow][randomCol] = CRUISER;
			board [randomRow - 1][randomCol] = CRUISER;
			board [randomRow - 2][randomCol] = CRUISER;


		}
		else {
			while (board [randomRow][randomCol] != BLANK
					|| board [randomRow - 1][randomCol] != BLANK
					|| board [randomRow - 2][randomCol] != BLANK) {

				randomRow = (int) ((Math.random() * 8) + 2);
				randomCol = (int) ((Math.random() * 8) + 2);
			}
			board [randomRow][randomCol] = CRUISER;
			board [randomRow][randomCol - 1] = CRUISER;
			board [randomRow][randomCol - 2] = CRUISER;

		}

	}

	/**
	 *This method places a "Battleship" ship to the board.
	 * A "Battleship" is a boat that is 4 spaces in length.
	 * Given that only 3 ships are on the board at any time, there is no need to check
	 * for a full board or lack of positions.
	 * @param board
	 */
	private void placeBattleship(int board [][]) {

		//Choose Random Position and Axis between 4-7 to account for boundaries issues
		randomRow = (int) ((Math.random() * 7) + 3);
		randomCol = (int) ((Math.random() * 7) + 3);
		randomAxis = (int) (Math.random() * 2);

		//If randomAxis == 1 try to place Vertically
		if (randomAxis == 1) {

			//If any part of the ship is overlapping, try new spot
			while (board [randomRow][randomCol] != BLANK
					|| board [randomRow - 1][randomCol] != BLANK
					|| board [randomRow - 2][randomCol] != BLANK
					|| board [randomRow - 3][randomCol] != BLANK) {

				randomRow = (int) ((Math.random() * 7) + 3);
				randomCol = (int) ((Math.random() * 7) + 3);

			}
			board [randomRow ][randomCol] = BATTLESHIP;
			board [randomRow - 1][randomCol] = BATTLESHIP;
			board [randomRow - 2][randomCol] = BATTLESHIP;
			board [randomRow - 3][randomCol] = BATTLESHIP;


		}
		else {

			//If any part of the ship is overlapping, try new spot
			while (board [randomRow][randomCol] != BLANK
					|| board [randomRow][randomCol - 1] != BLANK
					|| board [randomRow][randomCol - 2] != BLANK
					|| board [randomRow][randomCol - 3] != BLANK) {

				randomRow = (int) ((Math.random() * 7) + 3);
				randomCol = (int) ((Math.random() * 7) + 3);

			}
			board [randomRow ][randomCol] = BATTLESHIP;
			board [randomRow][randomCol - 1] = BATTLESHIP;
			board [randomRow][randomCol - 2] = BATTLESHIP;
			board [randomRow][randomCol - 3] = BATTLESHIP;


		}

	}

	/**
	 * This is the main method for this class,
	 * only used for testing and development purposes.
	 * @param args
	 */


}