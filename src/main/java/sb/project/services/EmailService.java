package sb.project.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import sb.project.domain.Order;
import sb.project.domain.ShoppingCart;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine htmlTemplateEngine;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendConfirmationMail(String toEmail, String token, final Locale locale, HttpServletRequest request) throws Exception {

        final Context ctx = new Context(locale);
        ctx.setVariable("token", token);
        ctx.setVariable("baseURL", request.getRequestURL().toString());

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Регистрация на сайте \"Компьютерные комплектующие\"");
        message.setFrom("sb.spring.project@gmail.com");
        message.setTo(toEmail);

        final String htmlContent = htmlTemplateEngine.process("email/email-registration", ctx);
        message.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    @Async
    public void sendPasswordResetMail(String toEmail, String token, final Locale locale, HttpServletRequest request, String userName) throws Exception {

        final Context ctx = new Context(locale);
        ctx.setVariable("token", token);
        ctx.setVariable("baseURL", request.getRequestURL().toString());
        ctx.setVariable("userName", userName);

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Сброс пароля на сайте \"Компьютерные комплектующие\"");
        message.setFrom("sb.spring.project@gmail.com");
        message.setTo(toEmail);

        final String htmlContent = htmlTemplateEngine.process("email/email-reset-password", ctx);
        message.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    @Async
    public void sendOrderStatusMail(String toEmail, Order order, ShoppingCart shoppingCart, final Locale locale) throws Exception {

        final Context ctx = new Context(locale);
        ctx.setVariable("order", order);
        ctx.setVariable("shoppingCartItems", shoppingCart.getCartItems());

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Статус заказа №" + order.getId() + " изменен!");
        message.setFrom("sb.spring.project@gmail.com");
        message.setTo(toEmail);

        final String htmlContent = htmlTemplateEngine.process("email/email-change-order-status", ctx);
        message.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    @Async
    public void sendOrderMail(String toEmail, Order order, ShoppingCart shoppingCart, final Locale locale) throws Exception {

        final Context ctx = new Context(locale);
        ctx.setVariable("order", order);
        ctx.setVariable("shoppingCartItems", shoppingCart.getCartItems());

        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Заказ №" + order.getId() + " принят!");
        message.setFrom("sb.spring.project@gmail.com");
        message.setTo(toEmail);

        final String htmlContent = htmlTemplateEngine.process("email/email-order", ctx);
        message.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    public String generateToken(String userName) {
        String datetime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        String randomUUID = UUID.randomUUID().toString();

        String originalToken = userName + datetime + randomUUID;

        String ch = ".-!@#$%^&*()_+!№;%:?*/\\\"~";
        for (char c : ch.toCharArray()) {
            originalToken = originalToken.replace(c, ' ');
        }
        originalToken = originalToken.replaceAll(" ", "");

        return DigestUtils.md5Hex(originalToken).toUpperCase();
    }
}