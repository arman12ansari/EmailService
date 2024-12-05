package dev.arman.emailservice.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.arman.emailservice.dtos.SendEmailEventDto;
import dev.arman.emailservice.utils.EmailUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

/**
 * @author mdarmanansari
 */
@Service
public class SendEmailEventConsumer {
    private final ObjectMapper objectMapper;

    public SendEmailEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void handleSendEmailEvent(String message) throws JsonProcessingException {
        SendEmailEventDto sendEmailEventDto = objectMapper.readValue(message, SendEmailEventDto.class);

        String to = sendEmailEventDto.getTo();
        String from = sendEmailEventDto.getFrom();
        String subject = sendEmailEventDto.getSubject();
        String body = sendEmailEventDto.getBody();

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("infotostalin1234@gmail.com", "ujghmialzjwgymvb");
            }
        };

        Session session = Session.getInstance(props, authenticator);

        EmailUtil.sendEmail(session, to, subject, body);
    }
}
