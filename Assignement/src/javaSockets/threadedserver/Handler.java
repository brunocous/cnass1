package javaSockets.threadedserver;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javaSockets.HTTPVersion;

class Handler implements Runnable {
	private Socket socket;
	private boolean isActive;

	public Handler(Socket socket) {
		this.setSocket(socket);
		this.setActive(false);
	}

	/**
	 * Runs this handler. First the buffered reader reads the stream from the
	 * client over the socket of this handler. Then the gattered string is
	 * parsed. Then it is processed. As long as this handler "runs", this
	 * handler is active.
	 */
	@Override
	public void run() {
		this.setActive(true);
		String[] commandPieces = parseCommand(getReader().readLine()
				.toUpperCase());
		processCommand(commandPieces);
		if (Arrays.asList(commandPieces).contains("HTTP/1.0"))
			quit();
		this.setActive(false);
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

	/**
	 * PRECONDITION: the given command is a valid command.
	 * 
	 * Processes the given command and sends a response over the socket of this
	 * handler.
	 * 
	 * @param command
	 */
	public void processCommand(String[] command) {
		assert (isValidCommand(command));

		String response = command[3] + " ";
		switch (command[0]) {
		case "GET":
			response += processGet(command);
		case "POST":
			response += processPost(command);
		case "PUT":
			response += processPut(command);
		case "HEAD":
			response += processHead(command);
		case "QUIT":
			response += processQuit(command);
		default:
			response += 400;
		}
		

	}

	/**
	 * Processes the given command and produces response specifique for the
	 * given command. If the given command is not QUIT, then it returns 500.
	 * Otherwise the socket of this handler closes.
	 * 
	 * @param command
	 * @return
	 */
	private String processQuit(String[] command) {
		if (!command[0].equals("QUIT")) {
			return "" + 500 + getTimeAndDate();
		}
		try {
			this.getSocket().close();
			return ""+getTimeAndDate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "" + 500;
		}

	}

	private String processHead(String[] command) { 
	if (!command[0].equals("HEAD")) {
		return "" + 500 + getTimeAndDate();
	}
	if(!)
		Path path = FileSystems.getDefault().getPath(command[1]);
		//TODO verwijder system out!
		System.out.println("Dit is het pad in \"path\": "+path + "\nEn Deze de command[1]: "+command[1]);
		String contentType;
		
			contentType = "\nContent-Type: "+ Files.probeContentType(path);
		
		String contentLength = "\nContent-Length: " + readFile(command[1]).getBytes(Charset.defaultCharset().toString()).length;
		System.out.println("type = "+contentType+" length of content= "+contentLength);
		
		return 200+ getTimeAndDate() + contentType + contentLength;
		
		
	}

	private String processPut(String[] command) {
		// TODO Auto-generated method stub

	}

	private String processPost(String[] command) {
		// TODO Auto-generated method stub

	}

	private String processGet(String[] command) {
		// TODO Auto-generated method stub

	}

	/**
	 * Sets this handler to inactive.
	 */
	public void quit() {
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

	/**
	 * True if and only if the given http version is HTTP/1.0 or HTTP/1.1.
	 * 
	 * @param string
	 * @return
	 */
	private static boolean isValidHttpVersion(String string) {
		return (string.equals("HTTP/1.0") || string.equals("HTTP/1.1"));
	}

	/**
	 * True if and only the given port is greater than or equal 0, or is smaller
	 * than or equal to 65535.
	 * 
	 * @param string
	 * @return
	 */
	private static boolean isValidPort(String string) {
		int port = Integer.parseInt(string);
		return (port >= 0) && (port <= 63535);
	}

	/**
	 * True if and only if the given uri is not null.
	 * 
	 * @param string
	 * @return
	 */
	private static boolean isValidUri(String string) {
		return (string != null);
	}

	/**
	 * True if and only if the given command is GET, PUT, POST, HEAD, QUIT.
	 * 
	 * @param string
	 * @return
	 */
	private static boolean isValidHttpCommand(String string) {
		String[] validCommands = { "PUT", "GET", "POST", "HEAD", "QUIT" };
		return Arrays.asList(validCommands).contains(string);
	}

	/**
	 * Reads the file at the given path using the standard charset.
	 * 
	 * @param path
	 *            : The location on the disk of the file.
	 * @return The string representation of the file at the given path using the
	 *         standard charset.
	 * @throws IOException
	 */
	private static String readFile(String path)
			throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
	}
/**
 * Returns the current time and date in formatted as 'yyyy/MM/dd HH:mm:ss'.
 * @return
 */
	private static String getTimeAndDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		   Date date = new Date();
		   return "\n" + dateFormat.format(date);
	}
	/**
	 * Returns the bad request string and the current time and date.
	 */
	private static String getBadRequest(){
		return 400+getTimeAndDate();
	}
}