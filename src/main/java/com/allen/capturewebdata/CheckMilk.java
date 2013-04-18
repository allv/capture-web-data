package com.allen.capturewebdata;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CheckMilk {

	private String proxyName;
	private String proxyPass;
	private String proxyIP;
	private int proxyPort;

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyIP() {
		return proxyIP;
	}

	public void setProxyIP(String proxyIP) {
		this.proxyIP = proxyIP;
	}

	public String getProxyName() {
		return proxyName;
	}

	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}

	public String getProxyPass() {
		return proxyPass;
	}

	public void setProxyPass(String proxyPass) {
		this.proxyPass = proxyPass;
	}

	private Document openURL(String destUrl) throws IOException {
		HttpClient httpclient = new HttpClient();
		if (proxyIP != null && !proxyIP.trim().equals("")) {
			httpclient.getHostConfiguration().setProxy(proxyIP, proxyPort);
			if (proxyName != null && !proxyName.trim().equals("")) {
				UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
						proxyName, proxyPass);
				httpclient.getState().setProxyCredentials(AuthScope.ANY, creds);
			}
		}
		GetMethod method = new GetMethod(destUrl);
		try {
			int statusCode = httpclient.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				String html = new String(method.getResponseBody());
				return Jsoup.parse(html);
			} else {
				throw new IOException("连接出错!");
			}
		} catch (IOException e) {
			throw e;
		} finally {
			method.releaseConnection();
		}
	}

	public boolean checkAptamilMilk(String destUrl, String name)
			throws IOException {
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
