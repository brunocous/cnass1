package javaSockets;

import java.io.*;
import java.net.*;

public class HTTPthreadedServer {
	
	public static void main(String argv[]) throws Exception {
		Socket clientSocket = new Socket("localhost", 6789);
		while (true) {
			Socket connectionSocket = clientSocket.accept();
			if (connectionSocket != null) {
				Handler h = new Handler(clientSocket);
				Thread thread = new Thread(request);
				thread.start();
			}
		}
	}
}
