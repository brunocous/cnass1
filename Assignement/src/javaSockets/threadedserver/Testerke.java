package javaSockets.threadedserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class Testerke {
	public static void main(String arg[])throws Exception{
String prefix = "C:\\Users\\Brun\\git\\cnass1\\Assignement\\src\\javaSockets\\threadedserver\\";
System.out.println(prefix);
		Path path = FileSystems.getDefault().getPath(prefix + "smiley.gif");
		//TODO verwijder system out!
		System.out.println("Dit is het pad in \"path\": "+path );
		String contentType;
		
			contentType = "\nContent-Type: "+ Files.probeContentType(path);
			System.out.println("contentype: "+ contentType);
		
		String contentLength = "\nContent-Length: " + readFile(path).getBytes(Charset.defaultCharset().toString()).length;
		System.out.println("type = "+contentType+" length of content= "+contentLength);
		
		System.out.println("De uiteindelijke file: " + readFile(path));
		
		
	}
	
	private static String readFile(Path path)
			throws IOException {
		return IOUtils.toString(Files.newInputStream(path));
	}

}
