package javaSockets;

/**
 * An enumeration of http protocol versions.
 * @author Brun
 *
 */
public enum HTTPVersion {

	HTTP1point0{
		public String toString(){
			return "HTTP/1.0";
		}
	},
	HTTP1point1{
		public String toString(){
			return "HTTP/1.1";
		}
	};
	public abstract String toString();
}
