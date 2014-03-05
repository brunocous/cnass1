package javaSockets.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestKlasse {

	public static void main(String argv[]) {
		
		int i = 0;
		String response = "<img onerror=\"img_onerror(this);\" data-logit=\"true\" data-pid=\"MOBDDDBRHVWQZHYY\"\n"
				+ "   data-imagesize=\"thumb\"\n"
				+ "   data-error-url=\"http://img1a.flixcart.com/mob/thumb/mobile.jpg\"\n"
				+ "   src=\"http://img8a.flixcart.com/image/mobile/h/y/y/samsung-galaxy-s-duos-s7562-125x125-imadddczzr4qhqnc.jpeg\"\n"
				+ "   alt=\"Samsung Galaxy S Duos S7562: Mobile\"\n"
				+ "   title=\"Samsung Galaxy S Duos S7562: Mobile\"></img></a>";
		response += "<img onerror=\"img_onerror(this);\" data-logit=\"true\" data-pid=\"MOBDDDBRHVWQZHYY\"\n"
				+ "   data-imagesize=\"thumb\"\n"
				+ "   data-error-url=\"http://img1a.flixcart.com/mob/thumb/mobile.jpg\"\n"
				+ "   src=\"httpHHHHHHHHHHHHHHHHHHHHHHHHH://img8a.flixcart.com/image/mobile/h/y/y/samsung-galaxy-s-duos-s7562-125x125-imadddczzr4qhqnc.jpeg\"\n"
				+ "   alt=\"Samsung Galaxy S Duos S7562: Mobile\"\n"
				+ "   title=\"Samsung Galaxy S Duos S7562: Mobile\"></img></a>";
		Document doc = Jsoup.parse(response);
		Elements list = doc.getElementsByTag("img");
		String[] urls = new String[list.size()];
		for (Element element : list) {

			String url = element.attr("src");
			if(url !=  null){
			urls[i] = url; 
				i++;
			}

		}
		String resul = "";
		for (String el : urls) {
			resul +=el + "\n";
		}
		System.out.println(resul);
		System.out.println("tot hier");
	}
}
