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
 * All aspects of this program with the exception of a few 
 * noted pieces provided by Prof. Bachman
 *
 */
public class BattleshipBoard {

	static final int BLANK = 1;
	static final int MISS = 2;
	static final int DESTROYER = 3;
	static final int CRUISER = 4;
	static final int BATTLESHIP = 5;

	/**
	 * This is the constructor for the BattleshipBoard Object
	 */
	public BattleshipBoard() {

		int [] [] board = createBoard();


		printBoard(board);


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
	private void printBoard(int board [][]) {

		//Taken from Example Code
		for (int r = 0; r < board.length; r++) {

			System.out.print("|");

			for (int c = 0; c < board[r].length; c++) {

				if (board[r][c] == BLANK ) { 
					System.out.print(" _ |");
				}

				else if (board[r][c] == MISS ) {
					System.out.print(" M |");
				}
				else if (board[r][c] == DESTROYER ) {
					System.out.print(" D |");
				}
				else if (board[r][c] == CRUISER ) {
					System.out.print(" C |");
				}
				else if (board[r][c] == BATTLESHIP ) {
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
	 * This method places a "Destroyer" ship to the board.
	 * A "Destroyer" is a boat that is 2 spaces in length.
	 * @param board
	 */
	private void placeDestroyer(int board [][]) {

		int randomRow = (int) (Math.random() * 10);
		int randomCol = (int) (Math.random() * 10);
		int randomAxis = (int) (Math.random() * 2);
		int randomDirection = (int) (Math.random() * 2);


			randomRow = (int) (Math.random() * 10);
			randomCol = (int) (Math.random() * 10);


			//TODO
			//Left off here.........
			// If randomAxis == 1 place vertically
			if (randomAxis == 1) {

				Boolean didRandomRowTryTen = false;
				//Try to face positively on the Y Axis
				//If out of bounds/taken, try the other direction
				while (randomRow + 1 == 0 || board[randomRow + 1] [randomCol] != BLANK
						|| board[randomRow] [randomCol] != BLANK) {
					
					if (didRandomRowTryTen)
					if (randomRow < 10 && didRandomRowTryTen == false) {
						randomRow++;
					}
					
					if (randomRow == 10) {
						didRandomRowTryTen = true;
						randomRow--;
					}
					
					randomRow = (int) (Math.random() * 10);
					randomCol = (int) (Math.random() * 10);
					
				}

			}

			//If randomAxis == 2 place horizontally
			else {
				//Try to face positively on the X Axis
				//If out of bounds, try the other direction
				if (randomRow + 1 == 0) {
					board[randomRow] [randomCol] = DESTROYER;
					board[randomRow] [randomCol - 1] = DESTROYER;
				}
				else {
					board[randomRow] [randomCol] = DESTROYER;
					board[randomRow] [randomCol - 1] = DESTROYER;
				}
			}
		}


	/**
	 *This method places a "Cruiser" ship to the board.
	 * A "Cruiser" is a boat that is 2 spaces in length.
	 * @param board
	 */
	private void placeCruiser(int board [][]) {



	}

	/**
	 *This method places a "Battleship" ship to the board.
	 * A "Battleship" is a boat that is 2 spaces in length.
	 * @param board
	 */
	private void placeBattleship(int board [][]) {


	}

	/**
	 * This is the main method for this class,
	 * only used for testing and development purposes.
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new BattleshipBoard();


	}
}
