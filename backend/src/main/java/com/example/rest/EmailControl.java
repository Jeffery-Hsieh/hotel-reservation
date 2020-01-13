package com.example.rest;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

class EmailControl {
    static void sendEmail(String toEmail, Order order) throws Exception {
        String host = "smtp.gmail.com";
        int port = 587;
        final String username = "****";
        final String fromEmail = "****";
        final String password = "****";
      
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", port);
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
      
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("測試寄信.");
            message.setContent("Dear Customer,\n This is the email from hotel reservation service.\n Your OrderID is "+ order.commentId,"text/html; charset=UTF-8");
            
            System.out.println(toEmail);
            Transport.send(message);
        
            System.out.println("寄送email結束.");
        } catch (MessagingException e) {
            System.out.println(e);
        }
    }
}