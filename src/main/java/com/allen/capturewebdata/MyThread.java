package com.allen.capturewebdata;

import java.io.IOException;

import javax.swing.JTextArea;

class MyThread extends Thread {

	private String user;
	private String pwd;
	private String to;
	private String smtpServer;
	private JTextArea console;

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
		String url = "http://www.windeln.de/aptamil-milchnahrung.html?ids=63763I63765I63780I13992I13987I63767I63766I63762";
		while (true) {
			writeMessageToConsle("抓取数据中......");
			try {
				boolean flag = checkMilk.checkAptamilMilk(url,
						"Kinder-Milch 1 plus (600 g), 1 ");
				if (!flag) {
					writeMessageToConsle("现在没货哦,休息5分钟后重新检查");
					try {
						this.sleep(5*60*1000);
					} catch (InterruptedException e) {
						writeMessageToConsle(e.toString());
					}
				} else {
					writeMessageToConsle("有货了,快去抢购啊!");
					SendEmail sendEmail = new SendEmail(smtpServer, user, pwd,
							to);
					try {
						sendEmail.sendMail();
					} catch (Exception e) {
						writeMessageToConsle(e.toString());
					}
					try {
						writeMessageToConsle("休息60分钟后重新检查");
						this.sleep(60 * 60 * 1000);
					} catch (InterruptedException e) {
						writeMessageToConsle(e.toString());
					}
				}
			} catch (IOException e1) {
				writeMessageToConsle("网络出错了,请查看你是否用了代理!"+e1.toString());
				writeMessageToConsle("休息5分钟后重新检查");
				try {
					this.sleep(5*60*1000);
				} catch (InterruptedException e) {
					writeMessageToConsle(e.toString());
				}
			}
		}
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
