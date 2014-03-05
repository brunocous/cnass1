package javaSockets.threadedserver;

import java.io.*;
import java.net.*;

public class HTTPthreadedServer {
	
	public static void main(String argv[]) throws Exception {
		ServerSocket serverSocket = new ServerSocket(6789);
		while (true) {
			Socket connection = serverSocket.accept();
			if (connection != null) {
				Handler h = new Handler(connection);
				Thread thread = new Thread(h);
				thread.start();
			}
		}
	}
}
