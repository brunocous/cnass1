package javaSockets;

import java.io.*;
import java.net.*;

public class HTTPclient {
	/**
	 * A buffered reader for the textual input.
	 */
	private BufferedReader inFromUser;
	/**
	 * A client socket
	 */
	private Socket clientSocket;
	/**
	 * The output byte stream.
	 */
	private DataOutputStream outToServer;
	/**
	 * The input byte stream.
	 */
	private BufferedReader inFromServer;

	/**
	 * This constructor initiates an instance of HTTPclient. It creates a local
	 * connection on port 6789.
	 */
	public HTTPclient() throws Exception {
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
				System.in));
		Socket clientSocket = new Socket("localhost", 6789);
		DataOutputStream outToServer = new DataOutputStream(
				clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

	}

	public static void main(String argv[]) throws Exception {
		while (true) {
			HTTPclient client = new HTTPclient();
			client.runClient();
		}
	}

	public void runClient() {
		this.sendCommand(this.getUserCommand());

		this.getOutToServer().writeBytes(sentence + '\n');
		String modifiedSentence = this.getInFromServer().readLine();
		System.out.println("FROM SERVER: " + modifiedSentence);
		getClientSocket().close();

	}

	/**
	 * @return the inFromUser
	 */
	public BufferedReader getInFromUser() {
		return inFromUser;
	}

	/**
	 * @param inFromUser the inFromUser to set
	 */
	public void setInFromUser(BufferedReader inFromUser) {
		this.inFromUser = inFromUser;
	}
	/**
	 * @return the clientSocket
	 */
	public Socket getClientSocket() {
		return clientSocket;
	}

	/**
	 * @param clientSocket the clientSocket to set
	 */
	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * @return the outToServer
	 */
	public DataOutputStream getOutToServer() {
		return outToServer;
	}

	/**
	 * @param outToServer the outToServer to set
	 */
	public void setOutToServer(DataOutputStream outToServer) {
		this.outToServer = outToServer;
	}

	/**
	 * @return the inFromServer
	 */
	public BufferedReader getInFromServer() {
		return inFromServer;
	}

	/**
	 * @param inFromServer the inFromServer to set
	 */
	public void setInFromServer(BufferedReader inFromServer) {
		this.inFromServer = inFromServer;
	}
	/**
	 * Reads the user input using the buffered reader. Else return null.
	 * @return The user input in String type.
	 */
	public String getUserCommand(){
		try {
			return this.getInFromUser().readLine();
		} catch (IOException e) {
			System.out.println("Error. Something went wrong when reading user input.");
			return null;
		}
		
	}
	
	public void sendCommand(String userCommand){
		String HTTPversion = this.parseCommand(userCommand);
	}

}
