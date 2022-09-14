package com.nooblol.user.utils;

public class LetterConstants {

  private LetterConstants() {
  }

  public static final String LETTER_ID_NULL = "쪽지의 ID값이 입력되지 않았습니다.";
  public static final String LETTER_TITLE_NULL = "제목이 입력되지 않았습니다.";
  public static final String LETTER_CONTENT_NULL = "내용이 입력되지 않았습니다.";
  public static final String LETTER_TO_USER_ID_NULL = "받는 사용자의 ID가 입력되지 않았습니다.";
  public static final String LETTER_TYPE_NULL = "쪽지의 타입이 발송자인지, 수신자인지가 입력되지 않았습니다.";

  public static final String LETTER_USER_ID_NULL = "사용자 ID가 입력되지 않았습니다.";

  //Delete Type
  public static final String LETTER_TYPE_FROM = "FROM"; //발송자
  public static final String LETTER_TYPE_TO = "TO"; //수신자
  public static final String[] LETTER_DELETE_TYPE_ARR = {
      LETTER_TYPE_FROM, LETTER_TYPE_TO
  };


  //ToStatus, FromStatus Value
  public static final int LETTER_STATUS_READ = 1;
  public static final int LETTER_STATUS_UNREAD = 2;
  public static final int LETTER_STATUS_DELETED = 9;

  public static final int[] LETTER_LIST_SEARCH_STATUS_ARR = {
      LETTER_STATUS_READ, LETTER_STATUS_UNREAD
  };

}
