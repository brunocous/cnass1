package javaSockets.client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javaSockets.httpmethod.*;


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
		this.requests = new ArrayList<HTTPMethod>();
	}

	public static void main(String argv[]) throws Exception {
		while (true) {
			
			HTTPclient client = new HTTPclient();
			client.runClient();
		}
	}

	public void runClient() {
		try {
			String userCommand = this.getUserCommand();
			this.sendCommand(userCommand);
		} catch (IllegalCommandException | IOException e) {
			System.out
					.println("Oops, something went wrong when processing or trying to send the command.");
		}

		this.receiveResponse();

	}

	/**
	 * Sends the given command over the connection associated with this
	 * HTTPclient.
	 * 
	 * @param userCommand: The userCommand to send.
	 * @throws IOException
	 */
	public void sendCommand(String userCommand) throws 
			IOException {
		this.getOutToServer().writeBytes(userCommand + '\n');
	}

	private void addNewRequest(String[] userCommandPieces) {
		int port = Integer.parseInt(userCommandPieces[2]);
		HTTPVersion version;
		if (userCommandPieces[3].equals(HTTPVersion.HTTP1point0.toString())) {
			version = HTTPVersion.HTTP1point0;
		}
		if (userCommandPieces[3].equals(HTTPVersion.HTTP1point1.toString())) {
			version = HTTPVersion.HTTP1point0;
		} else
			version = null;
		switch (userCommandPieces[0]) {
		case "GET":
			this.addRequest(new Get(userCommandPieces[1], port, version));
		case "POST":
			this.addRequest(new Post(userCommandPieces[1], port, version));
		case "PUT":
			this.addRequest(new Put(userCommandPieces[1], port, version));
		case "HEAD":
			this.addRequest(new Head(userCommandPieces[1], port, version));
		}

	}

	/**
	 * Recieves and processes the response of the server.
	 */
	public void receiveResponse() {
		String modifiedSentence = this.getInFromServer().readLine();
		System.out.println("FROM SERVER: " + modifiedSentence);
		
		getClientSocket().close();
	}

	/**
	 * Cuts the given string command in pieces and filters the blank commands
	 * out of the resulting array of strings. Returns null if and only if the
	 * command is not valid.
	 * 
	 * @param command
	 *            to split
	 * @return An array of strings pieces without "blanks"
	 */
	public String[] parseCommand(String command) {
		String[] result = filterBlanks(command.split(" "));
		if (isValidCommand(result)) {
			return result;
		} else {
			return null;
		}

	}

	/**
	 * Returns true if and only if the command is in the correct order and if
	 * all the elements of the string array are valid.
	 * 
	 * @param command
	 * @return
	 */
	private boolean isValidCommand(String[] command) {
		if (isValidHTTPMethod(command[0]) && isValidURI(command[1])
				&& isValidPortNumber(command[2])
				&& isValidHTTPVersion(command[3])) {
			return true;
		} else
			return false;
	}

	/**
	 * Returns true if and only if the portNumber is a integer and if it is
	 * greater than zero.
	 * 
	 * @param string
	 * @return
	 */
	private boolean isValidPortNumber(String portNumber) {
		int parsedPortNumber = Integer.parseInt(portNumber);

		return (parsedPortNumber > 0);
	}

	/**
	 * Returns true if and only if the given version is "HTTP/1.0" or
	 * "HTTP/1.1".
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isValidHTTPVersion(String version) {
		return (version.equals("HTTP/1.0") || version.equals("HTTP/1.1"));
	}

	/**
	 * Returns true if and only if the given method is "GET" or "POST" or "HEAD"
	 * or "PUT".
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isValidHTTPMethod(String method) {
		return (method.equals("GET") || method.equals("POST")
				|| method.equals("HEAD") || method.equals("PUT"));
	}

	/**
	 * Return true if and only if the URI starts with www
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isValidURI(String uri) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Iterates over the string array and deletes array elements only containing
	 * a blank space.
	 */
	public static String[] filterBlanks(String[] arrayToFilter) {
		ArrayList<String> filtered = new ArrayList<String>();
		for (int i = 0; i < arrayToFilter.length; i++) {
			if (!arrayToFilter[i].equals(" "))
				filtered.add(arrayToFilter[i]);
		}
		return (String[]) filtered.toArray();
	}

	/**
	 * @return the inFromUser
	 */
	public BufferedReader getInFromUser() {
		return inFromUser;
	}

	/**
	 * @param inFromUser
	 *            the inFromUser to set
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
	 * @param clientSocket
	 *            the clientSocket to set
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
	 * @param outToServer
	 *            the outToServer to set
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
	 * @param inFromServer
	 *            the inFromServer to set
	 */
	public void setInFromServer(BufferedReader inFromServer) {
		this.inFromServer = inFromServer;
	}

	/**
	 * Reads the user input using the buffered reader. Else return null.
	 * 
	 * @return The user input in String type.
	 */
	public String getUserCommand() {
		try {
			return this.getInFromUser().readLine();
		} catch (IOException e) {
			System.out
					.println("Error. Something went wrong when reading user input.");
			return null;
		}

	}
}
