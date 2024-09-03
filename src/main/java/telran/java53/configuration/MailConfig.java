package telran.java53.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // Укажите ваш SMTP сервер
        mailSender.setPort(587); // или другой порт, если используется

        mailSender.setUsername("your-email@gmail.com"); // Ваш email
        mailSender.setPassword("your-email-password"); // Ваш пароль от почты

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Для включения логирования

        return mailSender;
    }
}