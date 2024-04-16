package com.example.newEcom.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    private static final String emailUsername = "YOUR_EMAIL";
    private static final String emailPassword = "YOUR_GENERATED_PASSWORD";

    private String subject;
    private String messageBody;
    private String recipientEmail;

    public EmailSender(String subject, String messageBody, String recipientEmail) {
        this.subject = subject;
        this.messageBody = messageBody;
        this.recipientEmail = recipientEmail;
    }

    public void sendEmail() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();

                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.socketFactory.port", "465");
                properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.port", "465");

                Session session = Session.getDefaultInstance(properties,
                        new javax.mail.Authenticator() {
                            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                                return new javax.mail.PasswordAuthentication(emailUsername, emailPassword);
                            }
                        });

                try {
                    Log.i("Email", recipientEmail);
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(emailUsername));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
                    message.setSubject(subject);
                    message.setText(messageBody);

                    Transport.send(message);
                }
                catch (MessagingException e) {
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();
    }
}