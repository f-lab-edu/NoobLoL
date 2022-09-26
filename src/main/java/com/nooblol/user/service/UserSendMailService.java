package com.nooblol.user.service;

import java.util.List;
import java.util.Map;

public interface UserSendMailService {

  /**
   * MailContent는 Key로 Title과 Content를 발송한다. toUser는 수신받을 Email로, 단건만 발송한다
   *
   * @param toUser      메일을 수신받을 이메일
   * @param mailContent 제목과 내용이 포함되어야 한다.
   */
  boolean sendMail(String toUser, Map<String, String> mailContent);
}
