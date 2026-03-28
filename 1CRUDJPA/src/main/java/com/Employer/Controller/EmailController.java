package com.Employer.Controller;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class EmailController {


    private  JavaMailSender mailSender;
    public EmailController(JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }
    @RequestMapping("/sendMail")
    public String sendMail()
    {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("rajpagare011@gmail.com");
            msg.setTo("rajpagare305@gmail.com");
            msg.setSubject("test java mail sender");
            msg.setText("Hello this is a test mail of java mail sender by simple mail msg");
            mailSender.send(msg);
            return "success";
        }
        catch (Exception e)
        {
           return e.getMessage();
        }

    }

    @RequestMapping("/sendMailA")
    public String sendMailA() {
        try {

            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true);

            helper.setFrom("rajpagare011@gmail.com");
            helper.setTo("rajpagare308@gmail.com");
            helper.setSubject("attachment file");
            helper.setText("Hello this is a test mail of java mail sender by simple mail msg");

            helper.addAttachment("im.jpg", new File("C:\\Users\\asus\\OneDrive\\Desktop\\Desktop 1\\Im.jpg"));
            mailSender.send(mime);
            return "success";
        } catch (Exception e) {
            return e.getMessage();
        }
    }


    }
