/**
 * 
 */
package gaydadsProject1;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author David
 *
 */
public class BattleshipClient {

	static final int SERVER_PORT = 32100;
	static final int MAX_GUESSES = 25;
	Scanner readFromConsole = new Scanner( System.in ); 
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	InetAddress battleshipServerIp = null;
	int moveStatus = 0;
	int gameStatus = 0;
	int row = 0;
	int col = 0;
	Boolean isGameOver = false;
	Boolean isListening = true;


	public BattleshipClient() {

		askForIP();
		connectToServer();
		createOutputAndInputStream();
		
		playGame();
		
		closeSocketAndStreams();
		
	}

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
	
	protected void playGame() {
		
		while (isGameOver == false) {
		readInRow();
		readInColumn();
		sendRowGuess();
		sendColumnGuess();
		readMoveAndGameStatus();
		checkGameStatus();
		
		}
		System.out.println("\nGame Over");
		
	}
	protected void readInRow() {
		
		System.out.print("\nEnter Row: "); 
		row = readFromConsole.nextInt(); 
		
	}
	protected void readInColumn() {
		
		System.out.print("Enter Column: "); 
		col = readFromConsole.nextInt(); 
	}
	
	protected void sendRowGuess() {

		//Send RowGuess
		try {
			dos.writeInt(row);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void sendColumnGuess() {

		//Send ColGuess
		try {
			dos.writeInt(col);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
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
		
		System.out.println("\nMove Status: " + moveStatus);
		System.out.println("Game Status: " + gameStatus);
	}
	
	protected void checkGameStatus() {
		
		if (gameStatus == 40) {
			isGameOver = true;
		}
		
	}
	
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
	 * @param args
	 */
	public static void main(String[] args) {
		new BattleshipClient();
	}

}