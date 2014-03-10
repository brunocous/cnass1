package javaSockets.threadedserver;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.IOUtils;

class Handler implements Runnable {
	private Socket socket;
	private boolean isActive;

	/**
	 * Creates a new inactive handler with no socket.
	 */
	public Handler() {
		this.setSocket(null);
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
		BufferedReader reader = null;
		try {
			reader = getReader();
		} catch (IOException e3) {
			// TODO Auto-generated catch block
			System.out.println("Reader ni leesbaar.");
			e3.printStackTrace();
			return;
		}
		int i = 0;
		while(true){
			//TODO
			System.out.println("De " + i + "de keer da em doorloop in deze thread: " + this.toString());
			this.setActive(true);
			String[] commandPieces = null;
			try {
				commandPieces = parseCommand(reader.readLine()
						.toUpperCase());
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				System.out.println("Server kan de stream ni lezen");
				e2.printStackTrace();
			}
			try {
				processCommand(commandPieces);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (commandPieces != null
					&& Arrays.asList(commandPieces).contains("HTTP/1.0")) {
				try {
					this.getSocket().close();
					this.quit();
					break;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("probleem bij quiten van server");
				}
			}
			i++;
		}

	}

	/**
	 * The given command is first sliced after a blank character. Then this
	 * methods checks if the sliced command has a valid number of commands. If
	 * the given command is null, then null is returned.
	 * 
	 * Only accepted combinations: method uri port version || method uri version
	 * || method uri
	 * 
	 * @return
	 */
	public String[] parseCommand(String command) {
		if (command == null) {
			return null;
		}
		String[] pieces = filterBlanks(command.split(" "));
		if (howManyValidCommandPieces(pieces) != -1) {
			if (pieces[1].startsWith("LOCALHOST")) {
				pieces[1] = pieces[1].replaceFirst("LOCALHOST/", "");
				pieces[1] = pieces[1].replaceFirst("LOCALHOST", "");
			}
			return pieces;
		} else
			return null;

	}

	/**
	 * 
	 * Processes the given command and sends a response over the socket of this
	 * handler.
	 * 
	 * If the given command is null or if, and only if, the first command is not
	 * a valid http command, then a bad request response is send to the client.
	 * 
	 * @param command
	 * @throws IOException
	 *             : If an error occurs during processing the command, then an
	 *             internal error response is send to the client.
	 */
	public void processCommand(String[] command) throws IOException {
		String response = "";
		boolean isValidGet = false;
		// TODO
		// System.out.println("Volgende zal geprocessed worden: ");
		// for (String s : command) {
			
			//if(s.equals("")) System.out.println("------ gewoon niks");
			//else System.out.println(s);
		//}
		if (command == null) {
			response = getBadRequest();
		} else {
			int i = howManyValidCommandPieces(command);
			if (i == 2) {
				response = "HTTP/1.1" + " ";
			}
			if (i == 3) {
				response = command[2] + " ";
			}
			if (i == 4) {
				response = command[3] + " ";
			}
			try {
				if (command[0].equals("GET")) {
					if (command[1].equals("") || command[1].equals("\\")
							|| command[1].equals("/")) {
						command[1] = "INDEX.HTML";
					}
					String result = processGet(command);
					//TODO
					System.out.println("den URI: "+ command[1]);
					System.out.println("wa ik terugkrijg van get: " + result);
					response += result;
					if (result.startsWith("200")) {
						isValidGet = true;
					}
				} else if (command[0].equals("POST")) {
					response += processPost(command);
				} else if (command[0].equals("PUT")) {
					response += processPut(command);
				} else if (command[0].equals("HEAD")) {
					response += processHead(command);
				} else if (command[0].equals("QUIT")) {
					response += processQuit(command);
				} else {
					response += getBadRequest();
				}
			} catch (IOException e) {
				response += "500" + getTimeAndDate();
			}
		}

		DataOutputStream outToClient = new DataOutputStream(this.getSocket()
				.getOutputStream());
		if(isValidGet){
			//File file = new File(generatePath(command[1]).toString());
			//byte[] attachement = org.apache.commons.io.FileUtils.readFileToByteArray(file);
			System.out.println("TE STUREN: " + response +"\n"+ readFile(generatePath(command[1])) + "\n--------");
			try {
				outToClient.writeBytes(response +"\n"+ readFile(generatePath(command[1])));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(" ----   fout bij versturen van header en file");
				e.printStackTrace();
			}
		}else{
			outToClient.writeBytes(response);
		}

	}

	/**
	 * Processes the given command and produces response specific for the given
	 * command. If the given command is not QUIT, then it returns 500. Otherwise
	 * the socket of this handler closes.
	 * 
	 * @param command
	 * @return
	 */
	private String processQuit(String[] command) {
		if (!command[0].equals("QUIT")) {
			return "" + 500 + getTimeAndDate();
		}
		try {
			this.quit();
			return "" + getTimeAndDate();
		} catch (IOException e) {
			return "" + 500;
		}

	}

	/**
	 * Processes the given command. If the given command is not a HEAD or GET,
	 * then an internal error response is returned. Otherwise the date, content
	 * type and content length is returned.
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 */
	private static String processHead(String[] command) throws IOException {
		if (!(command[0].equals("HEAD") || command[0].equals("GET"))) {
			return "" + 500 + getTimeAndDate();
		}
		Path path = generatePath(command[1]);
		String contentType = "\nContent-Type: " + Files.probeContentType(path);
		String contentLength = "\nContent-Length: " + readFile(path).getBytes().length;
		return 200 + "\nHost: localhost:6789" + getTimeAndDate() + contentType + contentLength;

	}

	/**
	 * If the given command is not a PUT command, then an internal error
	 * response is returned. Otherwise it will store the remaining substrings of
	 * the given command in a file, that will be put in the path specified in
	 * the command. A response of success is then returned.
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 */
	private static String processPut(String[] command) throws IOException {
		if (!command[0].equals("PUT")) {
			return "" + 500 + getTimeAndDate();
		}
		File file = new File(generatePath(command[1]).toString());
		Writer output = new BufferedWriter(new FileWriter(file));
		for (int i = 4; i < command.length; i++) {
			output.write(command[i]);
		}
		output.close();

		return 200 + getTimeAndDate();
	}

	/**
	 * If the given command is not a POST command, then an internal error
	 * response is returned. Otherwise it will store the remaining substring of
	 * the given command in a file, that will be put in the path specified in
	 * the command. A response of success is then returned.
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 */
	private static String processPost(String[] command) throws IOException {
		if (!command[0].equals("POST")) {
			return "" + 500 + getTimeAndDate();
		}
		File file = new File(generatePath(command[1]).toString());
		Writer output = new BufferedWriter(new FileWriter(file));
		output.write(command[4]);
		output.close();

		return 200 + getTimeAndDate();

	}

	/**
	 * If the given command is not a POST command, then an internal error
	 * response is returned. Otherwise the header of the requested item on the
	 * requested path. If that fails, then a bad request response is returned.
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 */
	private static String processGet(String[] command) throws IOException {
		if (!command[0].equals("GET")) {
			return "" + 500 + getTimeAndDate();
		}
		String result = processHead(command);
		if (result.startsWith("200")) {
			return result;
		} else
			return "400" + getTimeAndDate();
	}

	/**
	 * Sets this handler to inactive.
	 * 
	 * @throws IOException
	 */
	public void quit() throws IOException {
		this.setActive(false);
		this.setSocket(null);
	}

	/**
	 * returns the socket of this handler.
	 * 
	 * @return
	 */
	public Socket getSocket() {
		return socket;
	}

	/**
	 * Sets the socket of this handler to the given socket.
	 * 
	 * @param socket
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
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
		return filtered.toArray(new String[filtered.size()]);
	}

	/**
	 * True if and only if the first part is a valid http command and the second
	 * part is a valid uri.
	 * 
	 * @param pieces
	 * @return
	 */
	private static boolean isValid2PiecedCommand(String[] pieces) {
		return isValidHttpCommand(pieces[0]) && isValidUri(pieces[1]);
	}

	/**
	 * True if and only if the first part is a valid http command and the second
	 * part is a valid uri and the third part is a valid http version.
	 * 
	 * @param pieces
	 * @return
	 */
	private static boolean isValid3PiecedCommand(String[] pieces) {
		return isValidHttpCommand(pieces[0]) && isValidUri(pieces[1])
				&& isValidHttpVersion(pieces[2]);
	}

	/**
	 * Returns true if and only if the first part is a valid HTTP method, and
	 * the second part a valid uri and the third part a valid port and the last
	 * part a valid HTTP version.
	 */
	private static boolean isValid4PiecedCommand(String[] command) {
		return isValidHttpCommand(command[0]) && isValidUri(command[1])
				&& isValidPort(command[2]) && isValidHttpVersion(command[3]);
	}

	/**
	 * Returns -1 if and only if the given command does not consists of 2 or 3
	 * or 4 pieces.
	 */
	private static int howManyValidCommandPieces(String[] command) {
		if (command.length >= 4 && isValid4PiecedCommand(command))
			return 4;
		if (command.length >= 3 && isValid3PiecedCommand(command))
			return 3;
		if (command.length >= 2 && isValid2PiecedCommand(command))
			return 2;
		else
			return -1;
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
	private static String readFile(Path path) throws IOException {
		return IOUtils.toString(Files.newInputStream(path));
	}

	/**
	 * Returns the current time and date in formatted as 'yyyy/MM/dd HH:mm:ss'.
	 * 
	 * @return
	 */
	private static String getTimeAndDate() {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
			Date date = new Date();
			return "\nDate: " + dateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Der zit een fout in time en date");
			return "";
		}
	}

	/**
	 * Returns the bad request string and the current time and date.
	 */
	private static String getBadRequest() {
		return 400 + getTimeAndDate();
	}

	/**
	 * Generates a Path object from the given path. A prefix is added.
	 */
	private static Path generatePath(String path) {
		String prefix = "C:\\Users\\Brun\\git\\cnass1\\Assignement\\src\\javaSockets\\threadedserver\\";
		return FileSystems.getDefault().getPath(prefix + path);
	}

	/**
	 * True if and only if this handler is not active and has no socket.
	 */
	public boolean canAcceptConnection() {
		return (this.getSocket() == null && !this.isActive());
	}

	/**
	 * Sets the given connection as its socket.
	 * 
	 * PRECONDITION: this handler can accept a connection.
	 */
	public void acceptConnection(Socket connection) {
		assert (this.canAcceptConnection());

		this.setSocket(connection);
	}
}