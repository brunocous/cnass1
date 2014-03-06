package javaSockets.threadedserver;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class HTTPthreadedServer {
	/**
	 * The number of handler that are initially in the thread pool.
	 */
	private static final int numberOfHandlersInPool = 20;
	/**
	 * The list of handlers, aka the thread pool.
	 */
	private ArrayList<Handler> threadPool;
	/**
	 * The socket of this server.
	 */
	private final ServerSocket serverSocket;
	
	public HTTPthreadedServer() throws IOException{
		for(int i = 0; i < getNumberofhandlersinpool(); i++){
			this.threadPool.add(new Handler());
		}
		serverSocket = new ServerSocket(6789);
	}
	
	public static void main(String argv[]) throws Exception {
		HTTPthreadedServer server = new HTTPthreadedServer();
		while (true) {
			Socket connection = server.getServerSocket().accept();
			if (connection != null) {
				Thread thread = new Thread(server.assignToHandler(connection));
				thread.start();
			}
		}
	}

	private Runnable assignToHandler(Socket connection) {
		Handler newlyAssignedHandler = null;
		for(Handler handler: this.getThreadPool()){
			if(handler.canAcceptConnection()){
				handler.acceptConnection(connection);
				newlyAssignedHandler = handler;
			}
		}
		if(!this.hasInactiveHandler()){
			newlyAssignedHandler = this.makeAndAssignHandler(connection);
		}
		return newlyAssignedHandler;
	}

	/**
	 * @return the numberofhandlersinpool
	 */
	public static int getNumberofhandlersinpool() {
		return numberOfHandlersInPool;
	}
	/**
	 * Returns the pool of threads of this server.
	 */
	public ArrayList<Handler> getThreadPool(){
		return this.threadPool;
	}
	/**
	 * Adds a new handler to the pool of this server and assigns the given connection to it.
	 */
	public Handler makeAndAssignHandler(Socket connection){
		
	}
	/**
	 * Return the server socket of this server.
	 */
	public ServerSocket getServerSocket(){
		return this.serverSocket;
	}
	/**
	 * True if and only if this server has inactive handlers.
	 */
	public boolean hasInactiveHandler(){
		boolean result = false;
		for(Handler h: this.getThreadPool()){
			if(h.canAcceptConnection())
				result = true;
		}
		return result;
	}
}
