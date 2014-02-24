package javaSockets;

import java.io.*;
import java.net.*;

class Handler implements Runnable {
	Socket socket;

	public Handler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
				connectionSocket.getInputStream()));
		DataOutputStream outToClient = new DataOutputStream(
				connectionSocket.getOutputStream());
		String clientSentence = inFromClient.readLine();
		System.out.println("Received: " + clientSentence);
		String capsSentence = clientSentence.toUpperCase() + '\n';
		outToClient.writeBytes(capsSentence);
	}
}