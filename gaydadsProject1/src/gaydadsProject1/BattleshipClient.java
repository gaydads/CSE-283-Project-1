/**
 * 
 */
package gaydadsProject1;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author David Gayda
 * Instructor: Prof. Bachman
 * CSE 283, Section B
 * 
 * This Class is used to handle the client side of the battleship game.
 * 
 * MAX_GUESSES can be changed in the "BattleshipBoard" class to limit the length of the game.
 *
 */

/**
 * @author David Gayda
 * Instructor: Prof. Bachman
 * CSE 283, Section B
 * 
 * This class contains the functionality needed for the client in this
 * battleship game. It asks the user for an IP address, and moves to send the server.
 *
 */
public class BattleshipClient {

	//These ints are used to place clients guesses on their board.
	static final int BLANK = 1;
	static final int MISS = 2;
	static final int HIT = 6;
	static final int SERVER_PORT = 32100;
	
	//These are used to set up the connection between client and server later
	Scanner readFromConsole = new Scanner( System.in ); 
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	InetAddress battleshipServerIp = null;
	
	//These ints are used to set the statuses for the game to be checked.
	int moveStatus = 0;
	int gameStatus = 0;
	//These ints are be used to track the row and col guessed
	int row;
	int col;
	//These ints are used to track the user's previous moves
	int rowMovesTracker [] = new int [26];
	int colMovesTracker [] = new int [26];
	int movesCounter = 1;
	//These boolean's are used to track the status of the game.
	Boolean isGameOver = false;
	Boolean isListening = true;
	Boolean skipOnce = false;
	Boolean isFirstTime = true;
	//This is the client's battleship board for persistence.
	BattleshipBoard clientBoard;


	/**
	 * This is the constructor for a new battleship client.
	 */
	public BattleshipClient() {

		askForIP();
		connectToServer();
		createOutputAndInputStream();
		
		clientBoard = new BattleshipBoard();
		playGame();
		System.out.println("GAME OVER!!!!!!! CONNECTION CLOSED!!!!!!!");
		closeSocketAndStreams();
		
	}

	/**
	 * This method prompts the user for an IP, or assumes that it connects to itself.
	 */
	protected void askForIP() {

		Scanner consoleIn = new Scanner( System.in );
		System.out.print("Enter the IP address of the server or hit enter: ");
		String serverIpStrg = "";
		serverIpStrg = consoleIn.nextLine();
		if( serverIpStrg.isEmpty()) { 
			serverIpStrg = "127.0.0.1";
		}
		System.out.println("\nServer IP is " + serverIpStrg);

		try {
			battleshipServerIp = InetAddress.getByName(serverIpStrg);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method is used to connect to the server
	 */
	protected void connectToServer() {

		try {
			clientSocket = new Socket(battleshipServerIp, SERVER_PORT);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method creates the input and output streams needed.
	 */
	protected void createOutputAndInputStream() {
		//connectToServer();
		try {
			dos = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			dis = new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method loops the methods needed to play a battleship game with the server
	 */
	protected void playGame() {
		System.out.println("\nWelcome to Battleship!!!");
		System.out.println("You have " + clientBoard.MAX_GUESSES + " Guesses to sink our ships!\n");
		System.out.println("Good Luck! \n");
		
		while (isGameOver == false) {
		System.out.println("'M' indicates a 'miss'. 'H indicates a 'hit'. Enter your Move "
				+ "(enter negative number to quit). ");
		readInRow();
		readInColumn();
		sendRowGuess();
		sendColumnGuess();
		readMoveAndGameStatus();
		clientBoard.printBoard();
		}
		
		
	}
	
	/**
	 * This method reads in a row guess from the user.
	 */
	protected void readInRow() {
		System.out.print("\nEnter Row (0-9): "); 
		row = readFromConsole.nextInt(); 
	}
	/**
	 * This method reads in a column guess from the user.
	 */
	protected void readInColumn() {
		
		System.out.print("Enter Column (0-9): "); 
		col = readFromConsole.nextInt(); 
	}
	
	/**
	 * This method sends the row guess to the server
	 */
	protected void sendRowGuess() {

		
		//Send RowGuess
		try {
			dos.writeInt(row);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method sends the column guess to the server
	 */
	protected void sendColumnGuess() {

		//Send ColGuess
		try {
			dos.writeInt(col);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This Method reads the statuses back in from the server after their moves are sent.
	 * This method also prints out to the user the status of the game and handles statuses as needed.
	 */
	protected void readMoveAndGameStatus() {
		try {
			moveStatus = dis.readInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			gameStatus = dis.readInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//These keep track of the previous client moves
		rowMovesTracker[movesCounter] = row;
		colMovesTracker[movesCounter] = col;
		movesCounter++;
		
		/*This snippet compares previous moves with the current one in
		 * order to determine if a hit counts, or if it's a duplicate move.
		 * If it is the first move, I do not use this.
		 * */
			if (isFirstTime == false) {
			for (int i = 1; i < movesCounter - 1; i++) {
				if (row == rowMovesTracker[i] && colMovesTracker[i] == col) {
					skipOnce = true;
				}
			}
			}
			
		isFirstTime = false;

		//skipOnce is used if the move is a duplicate, then skip checking for hits or misses to place
		if (skipOnce) {
			System.out.println("\nYou've already fired here!");
			skipOnce = false;
		}
		else {
			if (moveStatus == 10) {
				//System.out.println("\nMove Status: " + moveStatus);
				System.out.println("\nMiss!");
				clientBoard.boardArray[row][col] = MISS;
			}
			if (moveStatus == 20) {
				//System.out.println("\nMove Status: " + moveStatus);
				System.out.println("\nHit!");
				clientBoard.boardArray[row][col] = HIT;
			}
			if (moveStatus == 30) {
				//System.out.println("\nMove Status: " + moveStatus);
				System.out.println("\nYou've Sunk my Battleship! (Or one of the other ones...)");
				clientBoard.boardArray[row][col] = HIT;
			}
		}
			
		//Prints a statement corresponding to the status code received from the server
		//If the status code should end the game, set bool isGameOver to true to end the client's game.
		if (moveStatus == 40) {
			//System.out.println("\nMove Status: " + moveStatus);
			System.out.println("Game Over. Illegal Move! \n");
			isGameOver = true;
		}
		if (gameStatus == 10) {
			//System.out.println("Game Status: " + gameStatus);
			System.out.println("Game Continues! \n");
		}
		if (gameStatus == 20) {
			//System.out.println("Game Status: " + gameStatus);
			System.out.println("Game Over. Client Wins! \n");
			isGameOver = true;
		}
		if (gameStatus == 30) {
			//System.out.println("Game Status: " + gameStatus);
			System.out.println("Game Over. Server Wins! \n");
			isGameOver = true;
		}
		if (gameStatus == 40) {
			//System.out.println("Game Status: " + gameStatus);
			System.out.println("Game Over. Illegal Move! \n");
			isGameOver = true;
		}
	}
	
	/**
	 * This method closes the client socket and streams
	 */
	protected void closeSocketAndStreams() {
		
		try {
			dis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Main Method
	 * @param args
	 */
	public static void main(String[] args) {
		new BattleshipClient();
	}

}