package com.nooblol.community.service;

import java.util.List;
import java.util.Map;

public interface UserSendMailService {

  /**
   * MailContent는 Key로 Title과 Content를 발송한다. toUser는 수신받을 Email로, 단건만 발송한다
   *
   * @param toUser
   * @param mailContent
   */
  boolean sendMail(String toUser, Map<String, String> mailContent);
}
