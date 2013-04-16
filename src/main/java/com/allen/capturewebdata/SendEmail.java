package com.allen.capturewebdata;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

	Properties prop = null;
	private String smtpServer;

	public String getSmtpServer() {
		return smtpServer;
	}

	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
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

	private String user;
	private String pwd;
	private String to;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public SendEmail() {
		loadProperties();
	}

	private void loadProperties() {
		prop = new Properties();
		try {
			prop.load(this.getClass().getResourceAsStream(
					"/mailserver.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SendEmail(String smtpServer, String user, String pwd, String to) {
		this.smtpServer = smtpServer;
		this.user = user;
		this.pwd = pwd;
		this.to = to;
		loadProperties();
	}

	/**
	 * 创建Session对象，此时需要配置传输的协议，是否身份认证
	 */
	public Session createSession() {
		Session session = Session.getInstance(prop);
		// 启动JavaMail调试功能，可以返回与SMTP服务器交互的命令信息
		// 可以从控制台中看一下服务器的响应信息
		session.setDebug(true);
		return session;
	}

	/**
	 * 传入Session、MimeMessage对象，创建 Transport 对象发送邮件
	 */
	public void sendMail() throws Exception {
		Session session = createSession();
		MimeMessage msg = createDefaultMessage(session);
		// 设置发件人使用的SMTP服务器、用户名、密码

		// 由 Session 对象获得 Transport 对象
		Transport transport = session.getTransport();
		// 发送用户名、密码连接到指定的 smtp 服务器
		transport.connect(smtpServer, user, pwd);

		transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
		transport.close();
	}

	private MimeMessage createDefaultMessage(Session session)
			throws MessagingException {
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(to));
		msg.setRecipients(Message.RecipientType.TO, this.to);
		msg.setSubject("爱他美有货了,快去买吧!");
		msg.setSentDate(new Date());
		msg.setText("此信件来自吕军的奶粉侦查机器人,请勿回复!");

		return msg;
	}

}
