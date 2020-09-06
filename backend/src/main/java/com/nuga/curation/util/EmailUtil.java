package com.nuga.curation.util;

import org.springframework.stereotype.Component;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


@Component
public class EmailUtil {
    static final String FROM = "happyssafyweb@gmail.com";
    static final String FROMNAME = "당신의 행복";

    static final String SMTP_USERNAME = "ssafyweb@gmail.com";
    static final String SMTP_PASSWORD = "3rltjdnf06";

    static final String HOST = "smtp.gmail.com";
    static final int PORT = 587;
    //static final int PORT = 465;
    public static void send(String subject, String body, String destEmail) throws Exception{
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(FROM, FROMNAME));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(destEmail));
        msg.setSubject(subject);
        msg.setContent(body, "text/html;charset=utf-8");

        Transport transport = session.getTransport();

        try {
            System.out.println("Sending...");

            transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Email sent!");
        } catch (Exception ex) {
           throw ex;
        } finally {
            transport.close();
        }
    }
}