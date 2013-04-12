package com.allen.capturewebdata;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CheckMilk {

	private Document openURL(String destUrl) {
		try {
			return Jsoup.connect(destUrl).timeout(100000).get();
		} catch (IOException e) {
			System.err
					.println("Invalid URL Address, can't get data from the url. "
							+ e.toString());
		}
		return null;
	}

	public boolean checkAptamilMilk(String destUrl, String name) {
		Document doc = openURL(destUrl);
		if (doc == null)
			return false;
		Elements trElements = doc.select("tr[itemprop=offerDetails]");
		if (trElements != null) {
			for (Element trElement : trElements) {
				Elements tdElements = trElement.select("td[class=desc]");
				boolean flag = false;
				for (Element tdElement : tdElements) {
					if (tdElement.html().indexOf(name) >= 0) {
						flag = true;
					}
				}
				if (flag) {
					Elements inputElement = trElement.select("input");
					if (inputElement != null && inputElement.size() > 0) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}

}
