package com.nooblol.account.service;

import com.nooblol.account.dto.match.MatchDto;
import com.nooblol.account.dto.match.MatchGameSimpleDto;
import com.nooblol.global.dto.ResponseDto;
import java.util.List;

/**
 * 최근 50게임에 대한 정보만 동기화를 진행한다.
 */
public interface MatchGameInfoService {

  /**
   * DB의 게임 전적에 대하여 바로 Return 을 진행 하 , 데이터가 존재하지 않는 경우에는 동기화를 한 이후에 조회를 진행함)
   *
   * @param puuid    사용자 ID, Account의 Puuid.
   * @param pageNum  불러올 페이지, 최초페이지 0부터 시작하며, DefaultValue는 0부터 시작한다.
   * @param limitNum 한번 조회할때 불러올 갯수로 DefaultValuesms 30이다.
   * @return
   * @throws Exception
   */
  ResponseDto getMatchInfoListByPuuid(String puuid, int pageNum, int limitNum) throws Exception;

  /**
   * 현재 DB에 있는 게임 매치 데이터를 반환한다.
   *
   * @param puuid    사용자 ID, Account의 Puuid.
   * @param pageNum  불러올 페이지, 최초페이지 0부터 시작하며, DefaultValue는 0부터 시작한다.
   * @param limitNum 한번 조회할때 불러올 갯수로 DefaultValuesms 30이다.
   * @return
   */
  List<MatchGameSimpleDto> selectMatchSimpleListByPuuidInDB(String puuid, int pageNum,
      int limitNum);

  /**
   * Riot서버에서 게임 매치 데이터를 받아와 DB에 Insert를 진행한다. 이후 DB에 Insert를 진행한 게임 매치 데이터를 조회하여 반환한다.
   *
   * @param puuid    사용자 ID, Account의 Puuid.
   * @param pageNum  불러올 페이지, 최초페이지 0부터 시작하며, DefaultValue는 0부터 시작한다.
   * @param limitNum 한번 조회할때 불러올 갯수로 DefaultValuesms 30이다.
   * @return
   * @throws Exception
   */
  ResponseDto syncRiotToDbByPuuidAfterGetMatchSimpleList(String puuid, int pageNum, int limitNum)
      throws Exception;

  ResponseDto syncRiotToDbDataProcess(String puuid) throws Exception;

  List<String> getNotExistMatchList(List<String> matchIdList);

  String getApiReplaceByMatchId(String puuid);

  String getMakeUri(String matchId);

  /**
   * MatchId를 기반으로 Riot과 통신, 받아온 데이터를 MatchDto르 변환
   *
   * @param matchId
   * @return
   */
  MatchDto getMatchDataByRiot(String matchId);

  /**
   * 해당 트랜잭션은 모두 같이 처리되거나, 혹은 한개라도 실패할 경우 모두 Rollback이 되고 다음으로 Match를 Insert하는 것으로 넘어가게 된다.
   *
   * @param matchDto
   * @return
   * @throws Exception
   */
  boolean insertMatchDataByDB(MatchDto matchDto) throws Exception;
}
