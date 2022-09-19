package com.nooblol.party.util;

public class PartyConstants {

  private PartyConstants() {
  }

  public static final int PARTY_STATUS_ACTIVE = 1;
  public static final int PARTY_STATUS_DEACTIVE = 2;
  public static final int PARTY_STATUS_FINISH = 3;
  public static final int PARTY_STATUS_DELETE = 9;


  /*
   * Status의 경우 시간이 만료되면 알아서 삭제되기 때문에 파티 인원이 구직중인지? 구직이 완료되었는지만 표시함.
   * 완료된 게시물도 시간이지나면 사라질 예정이기 때문에
   */
  public static final String PARTY_ARTICLE_STATUS_RECRUIT = "RECRUIT";
  public static final String PARTY_ARTICLE_STATUS_FINISH = "FINISH";
}
