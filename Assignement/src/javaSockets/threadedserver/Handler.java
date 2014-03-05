package javaSockets.threadedserver;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import javaSockets.HTTPVersion;

class Handler implements Runnable {
	private Socket socket;
	private boolean isActive;

	public Handler(Socket socket) {
		this.setSocket(socket);
		this.setActive(false);
	}

	@Override
	public void run() {
		String[] commandPieces = parseCommand(getReader().readLine()
				.toUpperCase());
		processCommand(commandPieces);
		if (Arrays.asList(commandPieces).contains("HTTP/1.0"))
			quit();

		String clientSentence = inFromClient.readLine();
		System.out.println("Received: " + clientSentence);
		String capsSentence = clientSentence.toUpperCase() + '\n';
		outToClient.writeBytes(capsSentence);
	}

	/**
	 * The given command is first sliced after a blank character. Then this
	 * methods checks if the sliced command is a valid command. If the given
	 * command is not a valid command, this method returns the null-object.
	 * Otherwise the sliced command is returned.
	 * 
	 * @return
	 */
	public String[] parseCommand(String command) {
		String[] pieces = filterBlanks(command.split(" "));
		if (isValidCommand(pieces)) {
			return pieces;
		} else
			return null;
	}

	public void processCommand(String[] command) {

	}

	public void quit() {
		this.setSocket(null);
		this.setActive(false);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isValidSocket(Socket socket) {
		return false;
	}

	/**
	 * @return the isActive
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * Returns a buffered reader that reads the stream associated with the
	 * socket of this handler.
	 * 
	 * @return
	 * @throws IOException
	 */
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getSocket()
				.getInputStream()));
	}

	/**
	 * Returns a data output stream that is generated using the socket of this
	 * handler.
	 * 
	 * @return
	 * @throws IOException
	 */
	public DataOutputStream getOutputStream() throws IOException {
		return new DataOutputStream(this.getSocket().getOutputStream());
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
	 * Returns true if and only if the first part is a valid HTTP method, and
	 * the second part a valid uri and the third part a valid port and the last
	 * part a valid HTTP version.
	 */
	public static boolean isValidCommand(String[] command) {
		return isValidHttpCommand(command[0]) && isValidUri(command[1])
				&& isValidPort(command[2]) && isValidHttpVersion(command[3]);
	}

	private static boolean isValidHttpVersion(String string) {
		return (string.equals("HTTP/1.0") || string.equals("HTTP/1.1"));
	}

	private static boolean isValidPort(String string) {
		int port = Integer.parseInt(string);
		return (port >= 0) && (port <=63535);
	}

	private static boolean isValidUri(String string) {
		// TODO Auto-generated method stub
		return false;
	}

	private static boolean isValidHttpCommand(String string) {
		// TODO Auto-generated method stub
		return false;
	}

}