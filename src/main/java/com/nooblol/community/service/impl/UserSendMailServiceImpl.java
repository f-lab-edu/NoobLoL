package com.nooblol.community.service.impl;

import com.nooblol.community.service.UserSendMailService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class UserSendMailServiceImpl implements UserSendMailService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JavaMailSender javaMailSender;

  @Override
  public boolean sendMail(String toUser, Map<String, String> mailContent) {
    if (validMailSendValue(toUser, mailContent)) {
      return false;
    }

    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

    try {
      //수신인 설정
      simpleMailMessage.setTo(toUser);

      //제목
      simpleMailMessage.setSubject(mailContent.get("title"));

      //내용
      simpleMailMessage.setText(mailContent.get("content"));
      javaMailSender.send(simpleMailMessage);
    } catch (MailException mailEx) {
      log.error("메일 발송 실패, 사용자메일 : " + toUser);
      log.error("Exception 내용 :" + mailEx.getMessage());
      return false;
    } catch (Exception e) {
      log.error("메일 발송 실패, 사용자메일 : " + toUser);
      log.error("Exception 내용 :" + e.getMessage());
      return false;
    }
    return true;
  }

  private boolean validMailSendValue(String toUser, Map<String, String> map) {
    return ObjectUtils.isEmpty(map.get("title"))
        || ObjectUtils.isEmpty(map.get("content"))
        || StringUtils.isBlank(toUser);
  }
}
