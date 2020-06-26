package sb.project.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendConfirmationMail(String toEmail, String user, String link) throws Exception {

        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage);

        helper.setTo(toEmail);
        helper.setSubject("Регистрация на сайте \"Компьютерные комплектующие\"");
        helper.setText("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "\n" +
                "<head>\n" +
                "  <title>Подтверждение регистрации</title>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "  <p></p>\n" +
                "  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td style=\"padding: 10px 0 30px 0;\">\n" +
                "          <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border: 1px solid #cccccc; border-collapse: collapse;\">\n" +
                "            <tbody>\n" +
                "              <tr>\n" +
                "                <td align=\"center\" bgcolor=\"#5cb885\" style=\"padding: 40px 0 30px 0; color: #153643; font-size: 28px; font-weight: bold; font-family: Arial, sans-serif;\"><img src=\"https://i.ibb.co/X2kp1k0/login-2.png\" width=\"150px&quot;\" height=\"150px\" alt=\"Creating Email Magic\" style=\"display: block;\" /></td>\n" +
                "              </tr>\n" +
                "              <tr>\n" +
                "                <td bgcolor=\"#ffffff\" style=\"padding: 40px 30px 40px 30px;\">\n" +
                "                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"height: 171px;\">\n" +
                "                    <tbody>\n" +
                "                      <tr style=\"height: 79px;\">\n" +
                "                        <td style=\"color: #153643; font-family: Arial, sans-serif; font-size: 24px; height: 79px;\">\n" +
                "                          <p><b>Спасибо за регистрацию<br />на сайте \"Компьютерные комплектующие\"!</b></p>\n" +
                "                        </td>\n" +
                "                      </tr>\n" +
                "                      <tr style=\"height: 71px;\">\n" +
                "                        <td style=\"padding: 20px 0px 30px; color: #153643; font-family: Arial, sans-serif; font-size: 16px; line-height: 20px; height: 71px;\">\n" +
                "                          <p>Для активации аккаунта пожалуйста перейдите по ссылке:&nbsp</p>\n" +
                "                          <p><a href=\"" + link + "\">" + link + "</a></p>\n" +
                "                        </td>\n" +
                "                      </tr>\n" +
                "                      <tr style=\"height: 21px;\">\n" +
                "                        <td style=\"height: 21px;\"></td>\n" +
                "                      </tr>\n" +
                "                    </tbody>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              <tr>\n" +
                "                <td bgcolor=\"#ee4c50\" style=\"padding: 30px 30px 30px 30px;\">\n" +
                "                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "                    <tbody>\n" +
                "                      <tr>\n" +
                "                        <td style=\"color: #ffffff; font-family: Arial, sans-serif; font-size: 14px;\" width=\"75%\">&reg; Александр Кириллов</td>\n" +
                "                        <td align=\"right\" width=\"25%\">\n" +
                "                          <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "                            <tbody>\n" +
                "                              <tr>\n" +
                "                                <td style=\"font-family: Arial, sans-serif; font-size: 12px; font-weight: bold; width: 39px;\"></td>\n" +
                "                                <td style=\"font-size: 0px; line-height: 0; width: 20px;\" width=\"20\">&nbsp;</td>\n" +
                "                                <td style=\"font-family: Arial, sans-serif; font-size: 12px; font-weight: bold; width: 39px;\"></td>\n" +
                "                              </tr>\n" +
                "                            </tbody>\n" +
                "                          </table>\n" +
                "                        </td>\n" +
                "                      </tr>\n" +
                "                    </tbody>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "            </tbody>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "</body>\n" +
                "\n" +
                "</html>", true);

        mailMessage.setFrom("sb.spring.project@gmail.com");

        javaMailSender.send(mailMessage);
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