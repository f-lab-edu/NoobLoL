package com.nooblol.user.service.impl;

import com.nooblol.user.service.UserSendMailService;
import java.util.Map;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSendMailServiceImpl implements UserSendMailService {

  private final JavaMailSender javaMailSender;

  private final TemplateEngine templateEngine;

  @Override
  public boolean sendMail(String toUser, Map<String, String> mailContent) {
    if (validMailSendValue(toUser, mailContent)) {
      return false;
    }
    MimeMessage mailMessage = javaMailSender.createMimeMessage();
    try {
      //수신인 설정
      mailMessage.addRecipients(RecipientType.TO, toUser);

      //제목
      mailMessage.setSubject(mailContent.get("title"), CharEncoding.UTF_8);

      //내용
      Context context = getMailAuthContext(mailContent);
      String message = templateEngine.process(mailContent.get("context"), context);
      mailMessage.setText(message, CharEncoding.UTF_8, "html");
      javaMailSender.send(mailMessage);
    } catch (MailException mailEx) {
      log.warn("메일 발송 실패, 사용자메일 : " + toUser);
      log.warn("[UserSendMailServiceImpl MailException]", mailEx);
      return false;
    } catch (Exception e) {
      log.warn("메일 발송 실패, 사용자메일 : " + toUser);
      log.warn("[UserSendMailServiceImpl Exception]", e);
      return false;
    }
    return true;
  }

  private boolean validMailSendValue(String toUser, Map<String, String> map) {
    return ObjectUtils.isEmpty(map.get("title"))
        || ObjectUtils.isEmpty(map.get("content"))
        || StringUtils.isBlank(toUser);
  }

  private Context getMailAuthContext(Map<String, String> mailContent) {
    Context context = new Context();
    context.setVariable("title", mailContent.get("title"));
    context.setVariable("name", mailContent.get("name"));
    context.setVariable("content", mailContent.get("content"));
    return context;
  }
}
