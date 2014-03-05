package javaSockets.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestKlasse {
	
	
	public static void main(String argv[]) {
		String response = "<img onerror=\"img_onerror(this);\" data-logit=\"true\" data-pid=\"MOBDDDBRHVWQZHYY\"\n" + 
				   "   data-imagesize=\"thumb\"\n" + 
				   "   data-error-url=\"http://img1a.flixcart.com/mob/thumb/mobile.jpg\"\n" + 
				   "   src=\"http://img8a.flixcart.com/image/mobile/h/y/y/samsung-galaxy-s-duos-s7562-125x125-imadddczzr4qhqnc.jpeg\"\n" + 
				   "   alt=\"Samsung Galaxy S Duos S7562: Mobile\"\n" + 
				   "   title=\"Samsung Galaxy S Duos S7562: Mobile\"></img></a>";
		Document doc = Jsoup.parse(response);
		String resultaat = doc.getElementsByTag("img").attr("src");
		System.out.println(resultaat);
	}
}
