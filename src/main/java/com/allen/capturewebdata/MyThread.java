package com.allen.capturewebdata;

import java.io.IOException;

import javax.swing.JTextArea;

class MyThread extends Thread {

	private String user;
	private String pwd;
	private String to;
	private String smtpServer;
	private JTextArea console;
	private String proxyIP;
	private String proxyPort;
	private String proxyName;
	private String proxyPass;
	private String goods;

	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}

	public String getProxyIP() {
		return proxyIP;
	}

	public void setProxyIP(String proxyIP) {
		this.proxyIP = proxyIP;
	}

	public String getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}

	@Override
	public void run() {
		CheckMilk checkMilk = new CheckMilk();
		checkMilk.setProxyIP(proxyIP);
		if (proxyIP != null && !proxyIP.trim().equals("")) {
			int port = 80;
			if(proxyPort == null || proxyPort.trim().equals("")) {
				proxyPort = "80";
			}
			try {
				port = Integer.parseInt(proxyPort);
			} catch (NumberFormatException e) {
				writeMessageToConsle("端口号必须为数字,请重新输入后,开始!");
				return ;
			}
			checkMilk.setProxyPort(port);
		}
		checkMilk.setProxyName(proxyName);
		checkMilk.setProxyPass(proxyPass);
		String url = "http://www.windeln.de/aptamil-milchnahrung.html?ids=63763I63765I63780I13992I13987I63767I63766I63762";
		while (true) {
			writeMessageToConsle("抓取数据中......");
			try {
				String goodsName = "Kinder-Milch 1 plus (600 g), 1 ";
				if (goods != null && !goods.trim().equals("")) {
					goodsName = parseGoodsName(goods);
				}
				boolean flag = checkMilk.checkAptamilMilk(url, goodsName);
				if (!flag) {
					writeMessageToConsle("现在没货哦,休息5分钟后重新检查");
					try {
						Thread.sleep(5 * 60 * 1000);
					} catch (InterruptedException e) {
						break;
					}
				} else {
					writeMessageToConsle("有货了,快去抢购啊!");
					SendEmail sendEmail = new SendEmail(smtpServer, user, pwd,
							to);
					if (proxyIP != null && !proxyIP.trim().equals("")) {
						sendEmail.setProxyHost(proxyIP);
						sendEmail.setProxyPort(proxyPort);
					}
					try {
						sendEmail.sendMail();
					} catch (Exception e) {
						writeMessageToConsle("发送邮件出错: " + e.toString());
					}
					try {
						writeMessageToConsle("休息60分钟后重新检查");
						Thread.sleep(60 * 60 * 1000);
					} catch (InterruptedException e) {
						break;
					}
				}
			} catch (IOException e1) {
				writeMessageToConsle("网络出错了,请查看你是否用了代理!" + e1.toString());
				writeMessageToConsle("请重新输入后开始!");
				try {
					Thread.sleep(5 * 60 * 1000);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}

	private String parseGoodsName(String goods) {
		String result = goods.replace("ü", "&uuml;");
		return result;
	}

	public void setConsole(JTextArea jTextArea1) {
		this.console = jTextArea1;
	}

	private JTextArea getConsole() {
		return this.console;
	}

	private void writeMessageToConsle(String msg) {
		getConsole().append("\r\n" + msg);
	}

}
