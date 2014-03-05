package javaSockets.client;
import java.io.*;
import java.net.*;


public class neKeerOpnieuwProberen {

	public static void main(String argv[]) throws Exception {
		while(true){
			String userCommand = readUserCommand();
			String uri = getUriFromCommand(userCommand);
			int port = getPortFromCommand(userCommand);
			
			Socket clientSocket = createSocket(uri, port);
			sendToServer(clientSocket, userCommand);
			
			processResponse(receiveResponse(clientSocket));
		}
		Socket clientSocket = new Socket("localhost", 6789);
		DataOutputStream outToServer = new DataOutputStream(
				clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		String sentence = inFromUser.readLine();
		outToServer.writeBytes(sentence + '\n');
		String modifiedSentence = inFromServer.readLine();
		System.out.println("FROM SERVER: " + modifiedSentence);
		clientSocket.close();
	}

	/**
	 * Reads the input form the user.
	 * @return 
	 */
	public static String readUserCommand(){
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.println(">");
		
		
	}
	
	/**
	 * Sends
	 * 
	 */
	public static void sendToServer(String command){
		
	}
	
	/**
	 * 
	 */
	public static void sendToServer(String uri, int port, String command){
		
	}
	
	public static Socket createSocket(String uri, int port){
		
	}
}


