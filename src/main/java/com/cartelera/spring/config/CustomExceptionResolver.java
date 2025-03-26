package com.cartelera.spring.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionResolver.class);

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${admin.email}") // Configurable en properties
    private String recipientEmail;

    @Autowired
    private JavaMailSender mailSender;

    @SuppressWarnings("null")
    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Object handler,
                                        Exception ex) {
        // 1. Log del error
        logger.error("Error no controlado: {}", ex.getMessage(), ex);
        
        // 2. Configurar respuesta HTTP
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        
        // 3. Enviar correo de forma asíncrona
        sendErrorEmailAsync(ex);
        
        // 4. Redirigir a vista de error
        return new ModelAndView("redirect:/error");
    }

    @Async
    protected void sendErrorEmailAsync(Exception ex) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            
            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject("[ERROR] " + ex.getClass().getSimpleName());
            
            String htmlContent = buildEmailContent(ex);
            helper.setText(htmlContent, true); // true = HTML
            
            mailSender.send(mimeMessage);
            
        } catch (MessagingException e) {
            logger.error("Error enviando correo de notificación: {}", e.getMessage());
        }
    }

    private String buildEmailContent(Exception ex) {
        return String.format(
            "<h2 style='color: #dc3545;'>Error en la aplicación</h2>" +
            "<p><strong>Tipo:</strong> %s</p>" +
            "<p><strong>Mensaje:</strong> %s</p>" +
            "<h3>Stack Trace:</h3>" +
            "<pre>%s</pre>" +
            "<hr><p style='color: #6c757d;'>Enviado automáticamente por el sistema</p>",
            ex.getClass().getName(),
            ex.getMessage(),
                ExceptionUtils.getStackTrace(ex) // Requiere Apache Commons Lang
        );
    }
}