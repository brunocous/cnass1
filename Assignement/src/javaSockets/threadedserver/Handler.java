package javaSockets.threadedserver;

import java.io.*;
import java.net.*;

class Handler implements Runnable {
	private Socket socket;
	private boolean isActive;

	public Handler(Socket socket) {
		this.setSocket(socket);
		this.setActive(false);
	}

	@Override
	public void run() {

		String clientSentence = inFromClient.readLine();
		System.out.println("Received: " + clientSentence);
		String capsSentence = clientSentence.toUpperCase() + '\n';
		outToClient.writeBytes(capsSentence);
	}

	/**
	 * First checks if the given command is a valid command. If and only if the
	 * given command is a valid command, it is sliced after a " ". 
	 * If the given command is not a valid command, this method returns the null-object.
	 * 
	 * @return
	 */
	public String[] parseCommand(String command) {

	}
	
	public void processCommand(String[] command){
		
	}
	
	public void quit(){
		this.setSocket(null);
		this.setActive(false);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public boolean isValidSocket(Socket socket){
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
	 * Returns a buffered reader that is generated using the socket of this
	 * handler.
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

}