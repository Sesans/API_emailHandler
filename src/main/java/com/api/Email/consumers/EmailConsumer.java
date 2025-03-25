package com.api.Email.consumers;

import com.api.Email.domain.Email;
import com.api.Email.dto.EmailDTO;
import com.api.Email.service.impl.EmailServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
    @Autowired
    EmailServiceImpl emailService;

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailDTO emailDTO){
        Email email = new Email();
        BeanUtils.copyProperties(emailDTO, email);
        emailService.sendEmail(email);
    }
}