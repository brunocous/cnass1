package httpmethod;

import javaSockets.HTTPVersion;

public class Post extends HTTPMethod {
	private static final String STRINGREPRESENTATION = "POST";
	
	public Post(String uri, int port, HTTPVersion version) {
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
