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

	/**
	 * Creates a new server with a fixed number of initial inactive handlers and
	 * a connection on port 6789.
	 * 
	 * @throws IOException
	 */
	public HTTPthreadedServer() throws IOException {
		for (int i = 0; i < getNumberofhandlersinpool(); i++) {
			this.threadPool.add(new Handler());
		}
		serverSocket = new ServerSocket(6789);
	}

	/**
	 * First a new server is created. Then the server waits for an incoming
	 * connection. If an connection is accepted, then that connection is
	 * assigned to an inactive handler from the thread pool of the server. That
	 * new active handler is then ran.
	 * 
	 * @param argv
	 * @throws Exception
	 */
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

	/**
	 * Assigns the given connection to an inactive handler. If this server does
	 * not have inactive handler, then a new handler is made and the given
	 * connection is assigned to the new handler.
	 * 
	 * 
	 * @param connection
	 * @return The handler the given connection is assigned to.
	 */
	private Runnable assignToHandler(Socket connection) {
		Handler newlyAssignedHandler = null;
		for (Handler handler : this.getThreadPool()) {
			if (handler.canAcceptConnection()) {
				handler.acceptConnection(connection);
				newlyAssignedHandler = handler;
			}
		}
		if (!this.hasInactiveHandler()) {
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
	public ArrayList<Handler> getThreadPool() {
		return this.threadPool;
	}

	/**
	 * Adds a new handler to the pool of this server and assigns the given
	 * connection to it.
	 */
	public Handler makeAndAssignHandler(Socket connection) {
		Handler newHandler = new Handler();
		newHandler.acceptConnection(connection);
		this.getThreadPool().add(newHandler);
		return newHandler;

	}

	/**
	 * Return the server socket of this server.
	 */
	public ServerSocket getServerSocket() {
		return this.serverSocket;
	}

	/**
	 * True if and only if this server has inactive handlers.
	 */
	public boolean hasInactiveHandler() {
		boolean result = false;
		for (Handler h : this.getThreadPool()) {
			if (h.canAcceptConnection())
				result = true;
		}
		return result;
	}
}
