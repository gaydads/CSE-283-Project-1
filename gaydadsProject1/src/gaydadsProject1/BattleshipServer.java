/**
 * 
 */
package gaydadsProject1;

import java.io.*;
import java.net.*;
import java.util.Scanner;


/**
 * @author David Gayda
 * Instructor: Prof. Bachman
 * CSE 283, Section B
 * 
 * This Class is used to handle the server side of the battleship game.
 * 
 * MAX_GUESSES can be changed in the "BattleshipBoard" class to limit the length of the game.
 *
 */

public class BattleshipServer {

	static final int SERVER_PORT = 32100;
	Scanner consoleIn = new Scanner( System.in );
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	int row = 0;
	int col = 0;
	Boolean isNewGame;
	Boolean isListening;
	BattleshipBoard serverBoard;


/**
 * Constructor for the server
 * Specifies if its a new game creates socket and displays contact info for client
 * then plays the game until client connection ends.
 */
	public BattleshipServer() {
		
		isNewGame = true;
		isListening = true;
		
		createServerSocket();
		displayContactInfo();
		
		playGame();
		closeServer();

	}
	
	/**
	 * Creates the server socket with specified server port.
	 */
	protected void createServerSocket() {

		try {
			serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Displays contact info for the client connection... Taken from Lab 2 instructions
	 */
	protected void displayContactInfo() {

		try { 
			 // Display contact information. 
			 System.out.println( 
			 "Number Server standing by to accept Clients:" 

			 + "\nIP : " + InetAddress.getLocalHost() 
			 + "\nPort: " + serverSocket.getLocalPort() 
			 + "\n\n" ); 

			 } catch (UnknownHostException e) { 
			 // NS lookup for host IP failed? 
			 // This should only happen if the host machine does 
			 // not have an IP address. 
			 e.printStackTrace(); 
			 }
	}
	
	/**
	 * This method includes the loop for the server to keep responding to one client's guesses
	 * If it's a new game, this method will print a new randomly created server battleship board
	 */
	protected void playGame() {
		while(isListening) {
			if (isNewGame) {
				
				handleOneClient();
				isNewGame = false;
				serverBoard = new BattleshipBoard();
				serverBoard.placeShips();
				System.out.println("\n");
				serverBoard.printBoard();
				System.out.println("Ships added randomly to board");
			}
				//System.out.println("\nListening for Client's Guess...");
				handleClientGuess();
			
			//No need to ever close server for this project
			//if (!isListening) {
				//isListening = false;
				//System.out.println("Server Closed");
			//}

		}


	}
	
	/**
	 * This accepts one client to the server
	 */
	protected void handleOneClient() {
		try {
			clientSocket = serverSocket.accept();
			System.out.println("\nConnected to Client" + clientSocket.getInetAddress() +" " + clientSocket.getLocalPort());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		createClientStreams();
		

	}

	/**
	 * This method creates input and output streams for client
	 */
	protected void createClientStreams() {

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
	 * This method handle's the client's guesses uses the send move and game status method.
	 */
	protected void handleClientGuess() {
		try {

			row = dis.readInt();
			//System.out.println("Row = " + row);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("Exception Caught");
			e1.printStackTrace();
		}
		try {
			col = dis.readInt();
			//System.out.println("Column = " + col);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception Caught");
			e.printStackTrace();
		}		
		
		sendMoveandGameStatus(serverBoard.checkMoveStatus(row,col), serverBoard.checkGameStatus());
	}

	/**
	 * This method takes in the move and game statuses and sends the status codes to the client
	 * @param moveStatus
	 * @param gameStatus
	 */
	protected void sendMoveandGameStatus(int moveStatus, int gameStatus) {
		if (gameStatus == 20 || gameStatus == 30 || gameStatus == 40 || moveStatus == 40) {
			
			isNewGame = true;
			System.out.println("\nGame Over... Listening For New Client");
		}
		
		try {
			dos.writeInt(moveStatus);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception Caught");
			e.printStackTrace();
		}
		try {
			dos.writeInt(gameStatus);
		} catch (IOException e) {
			System.out.println("Exception Caught");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method closes the client's connection by closing the client socket
	 * datainput and output streams
	 */
	protected void closeClientConnection() {
		try {
			dis.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			dos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method could be used to close the server. Never used in this project.
	 */
	protected void closeServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		new BattleshipServer();
		
	}

}