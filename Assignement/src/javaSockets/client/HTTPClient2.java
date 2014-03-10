package javaSockets.client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTTPClient2 {

	/**
	 * Executes the program
	 * 
	 * @param argv
	 * @throws Exception
	 */
	public static void main(String argv[]) throws Exception {
		while (true) {
			System.out.println("Welcome to the HTTP client, \n >");
			String userCommand = readUserCommand();
			if(userCommand.equals("")) {
				System.out.println("No command was given.");
			}
			else {
				String[] commandWords = parseCommand(userCommand);
				String uri = getUriFromCommand(commandWords);
				String host = uri.split("/")[0];
				int port = getPortFromCommand(commandWords);
				Socket clientSocket = createSocket(host, port);
				sendToServer(clientSocket, userCommand);
				processResponse(receiveResponse(clientSocket), clientSocket,
					commandWords, host);
			}
		}
	}

	/**
	 * Cuts the command in separate words
	 * 
	 * @param command
	 * @return Array with the words
	 */
	public static String[] parseCommand(String command) {
		String[] result = filterBlanks(command.split(" "));
		return result;
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
	 * Returns the URI from the command of the client
	 * 
	 * @param userCommand
	 * @return
	 */
	private static String getUriFromCommand(String[] userCommand) {
		return userCommand[1];
	}

	/**
	 * Returns the HTTP version from the command of the client
	 * 
	 * @param userCommand
	 * @return
	 */
	private static String getHttpFromCommand(String[] userCommand) {
		return userCommand[3];
	}

	/**
	 * Returns the port number of the command of the client
	 */
	public static int getPortFromCommand(String[] userCommand) {
		int value;
		try {
			value = Integer.parseInt(userCommand[2]);
			return value;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Reads the input form the user.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String readUserCommand() throws IOException {
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		return inFromUser.readLine();
	}

	/**
	 * Sends the given command to localhost through port 6789
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 * 
	 */
	public static void sendToServer(String command) throws UnknownHostException, IOException {
		Socket socket = createSocket("localhost", 6789);
		sendToServer(socket, command);
	}

	/**
	 * Sends the given command through the clientsocket to the server.
	 * 
	 * @throws IOException
	 */
	public static void sendToServer(Socket clientSocket, String command) throws IOException {
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		if(command.startsWith("post")||(command.startsWith("put"))) {
			System.out.println("What would you like to put/post? \n >");
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			String toPutOrPost = inFromUser.readLine();
			command += " " + toPutOrPost;
		}
		outToServer.writeBytes(command + '\n');
	}

	/**
	 * Method that creates and returns the socket
	 * 
	 * @param uri
	 *            The uri for the socket
	 * @param port
	 *            The port on which the socket is placed
	 * @return The socket for this client
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static Socket createSocket(String uri, int port)
			throws UnknownHostException, IOException {
		Socket clientSocket = new Socket(uri, port);
		return clientSocket;
	}

	/**
	 * Receives and returns the response from the server.
	 * 
	 * @param socket
	 *            The socket of the connection
	 * @return
	 * @throws IOException
	 */
	public static String receiveResponse(Socket socket) throws IOException {
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		String sentence = inFromServer.readLine();
		return sentence;
	}

	/**
	 * Returns the embedded objects that are requested.
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public static String receiveEmbeddedObjects(Socket socket) throws IOException {
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(dis));
		String sentence = inFromServer.readLine();
		return sentence;
	}
	
	/**
	 * Processes the response of the server and reacts appropriately
	 * 
	 * @param response
	 *            The string that the server responded
	 * @throws IOException
	 */
	public static void processResponse(String response, Socket socket, String[] commandWords, String host) throws IOException {
		System.out.println(response);
		if(response.startsWith("HTTP/1.0")) {
			int port = socket.getPort();
			socket.close();
			String[] urls = getUrls(response);
			int i = 0;
			for(String url : urls) {
				Socket newSocket = createSocket(host, port);
				String command = "GET " + url + " HTTP/1.0";
				sendToServer(newSocket, command);
				System.out.println("Image requested.");
				String image = receiveEmbeddedObjects(newSocket);
				PrintWriter out = new PrintWriter("image" + i + ".txt");
				i++;
				out.println(image);
				out.close();
				// System.out.println(image);
				newSocket.close();
			}
		}
		else if(response.startsWith("HTTP/1.1")) {
			String[] urls = getUrls(response);
			int j = 0;
			for(String url : urls) {
				String command = "GET " + url + " HTTP/1.1";
				sendToServer(socket, command);
				System.out.println("Image requested.");
				String imageString = receiveEmbeddedObjects(socket);
				PrintWriter out = new PrintWriter("image" + j + ".txt");
				out.println(imageString);
				out.close();
				// System.out.println(imageString);
			}
		}
	}

	/**
	 * Returns an array with the urls of the embedded images of the given response from the server.
	 * @param response
	 * @return
	 */
	private static String[] getUrls(String response) {
		int i = 0;
		Document doc = Jsoup.parse(response);
		Elements list = doc.getElementsByTag("img");
		int numberObjects = list.size();
		String[] urls = new String[numberObjects];
		for (Element element : list) {
			String url = element.attr("src");
			if (url != null) {
				urls[i] = url;
				i++;
			}
		}
		return urls;
	}
}
