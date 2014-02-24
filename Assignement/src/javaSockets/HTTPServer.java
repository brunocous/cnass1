package javaSockets;

import java.io.*;
import java.net.*;

public class HTTPServer {
	public static void main(String argv[]) throws Exception {
		ServerSocket welcomeSocket = new ServerSocket(6789);
		
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());
			String clientSentence = inFromClient.readLine();
			System.out.println("Received: " + clientSentence);
			
			String result = analyzeString(clientSentence.toUpperCase() + '\n');
			outToClient.writeBytes(result);
		}
	}
	public static String analyzeString(String clientSentence){
		String[] commandPieces = clientSentence.split(" ");
		switch(commandPieces[0]){
		case "GET": return executeGet(commandPieces);
		case "POST": return executePost(commandPieces);
		case "HEAD": return executeHead(commandPieces);
		case "PUT": return executePut(commandPieces);
		}
		
	}
	private static String executePut(String[] commandPieces) {
		// TODO Auto-generated method stub
		
	}
	private static String executeHead(String[] commandPieces) {
		// TODO Auto-generated method stub
		
	}
	private static String executePost(String[] commandPieces) {
		// TODO Auto-generated method stub
		
	}
	private static String executeGet(String[] commandPieces) {
		// TODO Auto-generated method stub
		
	}
	
}
