package httpmethod;

import javaSockets.HTTPVersion;

public class Head extends HTTPMethod {
	private static final String STRINGREPRESENTATION = "HEAD";
	
	public Head(String uri, int port, HTTPVersion version) {
		super(uri, port, version);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the stringrepresentation
	 */
	public static String getStringRepresentation() {
		return STRINGREPRESENTATION;
	}
	public String toString(){
		return getStringRepresentation()+" "+super.toString();
	}

}
