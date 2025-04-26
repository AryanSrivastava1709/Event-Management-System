package com.event.notification_service.service;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.event.notification_service.dto.EmailRequest;
import jakarta.mail.internet.MimeMessage;


@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public ResponseEntity<String> sendBookingConfirmationEmaik(EmailRequest emailRequest) throws Exception {
        
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            Context context = new Context();
            if (emailRequest.getTemplateData() != null) {
                emailRequest.getTemplateData().forEach(context::setVariable);
            }
            
            String html = templateEngine.process("booking-confirmation", context);

            helper.setTo(emailRequest.getTo());
            helper.setSubject(emailRequest.getSubject());
            helper.setText(html, true);

            mailSender.send(message);

            return new ResponseEntity<>("Email sent successfully",HttpStatus.OK);

        } catch (Exception ex) {
            throw ex;
        }
    }
    
}
