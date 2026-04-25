package com.doctorapointment.service;

import com.doctorapointment.util.EnvLoader;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class EmailService {
    private static Logger log = LoggerFactory.getLogger(EmailService.class);

    private static final String SMTP_HOST = EnvLoader.get("SMTP_HOST");
    private static final String SMTP_PORT = EnvLoader.get("SMTP_PORT");
    private static final String SMTP_USER = EnvLoader.get("SMTP_USER");
    private static final String SMTP_PASSWORD = EnvLoader.get("SMTP_PASSWORD");

    // send mail
    public boolean sendMail(String destinataire, String sujet, String contenuHtml) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER,SMTP_PASSWORD);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setContent(contenuHtml, "text/html; charset=utf-8");

            Transport.send(message);
            log.info("Email envoyé à '{}' ", destinataire);
            return  true;
        } catch (MessagingException e) {
            log.error("Error sending mail",e);
            return false;
        }
    }
}
