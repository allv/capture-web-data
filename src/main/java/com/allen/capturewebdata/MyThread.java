package com.allen.capturewebdata;

class MyThread extends Thread {
	
	private String user;
	private String pwd;
	private String to;
	private String smtpServer;
	
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
		while(true){
			System.out.println("Capute Data...");
			boolean flag = checkMilk.checkAptamilMilk(url, "Kinder-Milch 1 plus (600 g), 1 ");
			if(!flag) {
				System.out.println("Not in Stock");
				try {
					this.sleep(300000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("In Stock, will Send Email");
				SendEmail sendEmail = new SendEmail(smtpServer,user,pwd,to);
				try {
					sendEmail.sendMail();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					this.sleep(60*60*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
