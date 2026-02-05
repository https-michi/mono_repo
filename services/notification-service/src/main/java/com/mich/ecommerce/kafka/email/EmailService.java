package com.mich.ecommerce.kafka.email;

import com.mich.ecommerce.kafka.order.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendPaymentSuccessEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference
    ) {
        Map<String, Object> variables = Map.of(
                "customerName", customerName,
                "amount", amount,
                "orderReference", orderReference
        );

        sendEmail(destinationEmail, EmailTemplates.PAYMENT_CONFIRMATION, variables);
    }

    @Async
    public void sendOrderConfirmationEmail(
            String destinationEmail,
            String customerName,
            BigDecimal amount,
            String orderReference,
            List<Product> products
    ) {
        Map<String, Object> variables = Map.of(
                "customerName", customerName,
                "totalAmount", amount,
                "orderReference", orderReference,
                "products", products
        );

        sendEmail(destinationEmail, EmailTemplates.ORDER_CONFIRMATION, variables);
    }

    private void sendEmail(String to, EmailTemplates template, Map<String, Object> variables) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            Context context = new Context();
            context.setVariables(variables);

            String htmlTemplate = templateEngine.process(template.getTemplate(), context);

            helper.setFrom("mich@gmail.com");
            helper.setTo(to);
            helper.setSubject(template.getSubject());
            helper.setText(htmlTemplate, true);

            mailSender.send(mimeMessage);
            log.info("Email sent successfully to {} using template {}", to, template.getTemplate());
        } catch (MessagingException e) {
            log.error("CRITICAL - Error sending email to {}: {}", to, e.getMessage());
        }
    }
}
