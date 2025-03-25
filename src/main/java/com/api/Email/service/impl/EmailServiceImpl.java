package com.api.Email.service.impl;

import com.api.Email.domain.Email;
import com.api.Email.repository.EmailRepository;
import com.api.Email.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    EmailRepository emailRepository;

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    @Override
    @Transactional
    public Email sendEmail(Email email) {
        try {
            email.setSubmissionDate(LocalDateTime.now());
            email.setEmailFrom(emailFrom);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(emailFrom);
            helper.setTo(email.getEmailTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText(), true);
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException(e);
        }
        return emailRepository.save(email);
    }
}
