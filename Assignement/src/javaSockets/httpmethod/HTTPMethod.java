package javaSockets.httpmethod;

import javaSockets.HTTPVersion;

public abstract class HTTPMethod {
	private final String uri;
	private final int port;
	private final HTTPVersion version;

	public HTTPMethod(String uri, int port, HTTPVersion version) {
		if (isValidUri(uri)) {
			this.uri = uri;
		} else
			this.uri = null;
		if (isValidPort(port))
			this.port = port;
		else
			this.port = 0;
		if (isValidHTTPVersion(version))
			this.version = version;
		else
			this.version = null;
	}

	/**
	 * True if and only if the given version is a valid enum.
	 * 
	 * @param version2
	 * @return
	 */
	public static boolean isValidHTTPVersion(HTTPVersion version2) {
		return (version2 == HTTPVersion.HTTP1point0)
				|| (version2 == HTTPVersion.HTTP1point1);
	}

	/**
	 * True if and only if the given port is greater than or equal to zero and
	 * less than or equal to 65535.
	 * 
	 * @param port2
	 * @return
	 */
	public static boolean isValidPort(int port2) {
		return (port2 >= 0 && port2 <= 65535);
	}

	/**
	 * True if and only if the piece of string before "/" starts with "www." or
	 * "http://www." and ends with ".com".
	 * 
	 * @param uri
	 * @return
	 */
	public static boolean isValidUri(String uri) {
//		if (uri == null) {
//			return false;
//		}
//		String[] splitted = uri.split("/");
//		if ((splitted[0].startsWith("www.") || splitted[0]
//				.startsWith("http://www.")) && splitted[0].endsWith(".com"))
//			return true;
//		else
			return false;

	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the version
	 */
	public HTTPVersion getVersion() {
		return version;
	}
	/**
	 * Returns a String representation of an instance.
	 */
	public String toString(){
		return this.getUri()+" "+this.getPort()+" "+this.getVersion();
	}
}
