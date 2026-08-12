package com.info.tod;

import jakarta.mail.*;
import jakarta.mail.Authenticator;

import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    public static void sendMail(String toEmail, int points) {

        final String fromEmail = "dikshasonar1305@gmail.com";
        final String password = "truc ozoj yval blwu"; // app password

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );

            message.setSubject("Task Completed 🎉");

            message.setText(
                    "Congratulations!\n\n" +
                    "You completed a task and earned " + points + " points.\n\n" +
                    "Keep going 🚀"
            );

            Transport.send(message);

            System.out.println("✅ Email Sent Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}