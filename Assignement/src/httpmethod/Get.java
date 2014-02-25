package httpmethod;

import javaSockets.HTTPVersion;

public class Get extends HTTPMethod {
private static final String STRINGREPRESENTATION = "GET";
	public Get(String uri, int port, HTTPVersion version){
		super(uri,port,version);
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
