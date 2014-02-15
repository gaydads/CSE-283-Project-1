/**
 * 
 */
package gaydadsProject1;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * @author David
 *
 */
public class BattleshipServer {

	static final int SERVER_PORT = 32100;
	static final int MAX_GUESSES = 25;
	Scanner consoleIn = new Scanner( System.in );
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
	int moveStatus = 0;
	int gameStatus = 0;
	int row = 0;
	int col = 0;
	Boolean isNewGame = true;
	Boolean isListening = true;



	public BattleshipServer() {
		createServerSocket();
		displayContactInfo();
		
		playGame();
		closeClientConnection();
		closeServer();

	}
	
	protected void createServerSocket() {

		try {
			serverSocket = new ServerSocket(SERVER_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
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
	
	protected void playGame() {
		do {
			if (isNewGame) {
				handleOneClient();
				isNewGame = false;
			}
			
			if (isListening) {
				System.out.println("\nListening...");
				receiveClientGuess();
				sendMoveandGameStatus();
			}
			
			
			//if (!isListening) {
				//isListening = false;
				//System.out.println("Server Closed");
			//}

		} while(isListening);


	}
	
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
	
	protected void receiveClientGuess() {
		try {

			row = dis.readInt();
			System.out.println("Row = " + row);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			col = dis.readInt();
			System.out.println("Column = " + col);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	protected void sendMoveandGameStatus() {
		System.out.print("\nEnter Move Status: ");
		moveStatus = consoleIn.nextInt();
		System.out.print("Enter Game Status: ");
		gameStatus = consoleIn.nextInt();
		
		try {
			dos.writeInt(moveStatus);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dos.writeInt(gameStatus);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (gameStatus == 40) {
			isNewGame = true;
			System.out.println("\nGame Over... Listening For New Client");
		}
	}
	
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