package com.nooblol.account.service;

import com.nooblol.account.dto.SummonerDto;
import com.nooblol.account.dto.SummonerHistoryDto;
import com.nooblol.account.mapper.SummonerMapper;
import com.nooblol.global.dto.ResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SummonerServiceTest {

    @Autowired
    SummonerService summonerService;

    @Autowired
    SummonerMapper summonerMapper;

    String existSummoner = "눕는게 일상";
    String notExistSummoner = "존재하지않는소환사명";

    SummonerDto testDto;
    SummonerHistoryDto testHistoryDto;

    SummonerDto testDto2;
    ArrayList<SummonerHistoryDto> testHistoryList2;

    @BeforeEach
    void setUp() {
        testDto = new SummonerDto();
        testDto.setAccountId("-CZ1UdXj_30sT4ZOb1PDJfiCCZvYTRnYRQncqn8LUBE");
        testDto.setId("zJM0b_kEhZhHKhq6qsL8f4nusWE-IEegWdeqqK3LA3CrnA");
        testDto.setName("눕는게일상");
        testDto.setPuuid("KCSH-FOif2FOuoIFTkXclVK__08YQq8d4H7t96SNpLOVWUU8VDFA_2byLFMGlV_L3jZ0p_cRj-TYUg");
        testDto.setSummonerLevel(221);
        testDto.setProfileIconId(520);
        testDto.setRevisionDate(1655029488000L);

        testDto2 = new SummonerDto();
        testDto2.setAccountId("Ue0TIGyfK7pMIzq6kTQPoe5EcfWk-48yhg2dLNl9jbtk");
        testDto2.setId("GqGH7NQcZ8b-e9jQPi7dtohw9de1NT81JK7NsIgRCSi24g");
        testDto2.setName("윤재초이");
        testDto2.setPuuid("4DMbgWCQj_mGifdjDYCAGvms95eiwIyIYGefBjSEGJiG48-lGpkolswIsqU-5o7DqpogEQjqsjq8vA");
        testDto2.setSummonerLevel(467);
        testDto2.setProfileIconId(4570);
        testDto2.setRevisionDate(1658674111556L);


        SummonerHistoryDto sample1 = new SummonerHistoryDto();
        sample1.setLeagueId("5a509464-bba9-470c-b1f5-026349d00f0e");
        sample1.setSummonerId("GqGH7NQcZ8b-e9jQPi7dtohw9de1NT81JK7NsIgRCSi24g");
        sample1.setSummonerName("윤재초이");
        sample1.setQueueType("RANKED_FLEX_SR");
        sample1.setTier("PLATINUM");
        sample1.setRank("I");
        sample1.setLeaguePoints(21);
        sample1.setWins(13);
        sample1.setLosses(9);
        sample1.setHotStreak(false);
        sample1.setVeteran(false);
        sample1.setVeteran(false);
        sample1.setInactive(false);

        SummonerHistoryDto sample2 = new SummonerHistoryDto();
        sample2.setLeagueId("144a6d49-62c6-47ed-8406-fbaf7a7039b5");
        sample2.setSummonerId("GqGH7NQcZ8b-e9jQPi7dtohw9de1NT81JK7NsIgRCSi24g");
        sample2.setSummonerName("윤재초이");
        sample2.setQueueType("RANKED_SOLO_5x5");
        sample2.setTier("DIAMOND");
        sample2.setRank("II");
        sample2.setLeaguePoints(58);
        sample2.setWins(201);
        sample2.setLosses(165);
        sample2.setHotStreak(false);
        sample2.setVeteran(false);
        sample2.setVeteran(false);
        sample2.setInactive(false);

        testHistoryList2 = new ArrayList<>();
        testHistoryList2.add(sample1);
        testHistoryList2.add(sample2);
    }

    @Test
    void 소환사계정정보확인테스트_존재하는경우() {
        //given
        ResponseDto sampleDto = new ResponseDto(HttpStatus.OK.value(), this.testDto);

        //when
        ResponseDto resultDto = summonerService.getAccountInfo(existSummoner);

        //then
        SummonerDto sampleSummoner = (SummonerDto) sampleDto.getResult();
        SummonerDto resultSummoner = (SummonerDto) resultDto.getResult();

        //assertThat(testDto).isEqualToComparingFieldByField(dto); //해당 코드가 아래코드로 변경됨
        assertThat(resultDto.getResultCode()).isEqualTo(sampleDto.getResultCode());
        assertThat(resultDto.getResult()).usingRecursiveComparison().isEqualTo(sampleDto.getResult());
    }

    @Test
    void 소환사계정정보확인테스트_존재하지_않는_경우() {
        //given
        ResponseDto sampleDto = new ResponseDto(HttpStatus.NOT_FOUND.value(), "error");

        //when
        ResponseDto resultDto = summonerService.getAccountInfo(notExistSummoner);

        //then
        //애초에 오류기 떄문에 error로 들어감
        //assertThat(testDto).isEqualToComparingFieldByField(dto); //해당 코드가 아래코드로 변경됨
        assertThat(resultDto.getResultCode()).isEqualTo(sampleDto.getResultCode());
        assertThat(resultDto.getResult()).usingRecursiveComparison().isEqualTo(sampleDto.getResult());
    }

    @Test
    void 소환사_계정정보_등가성테스트_일치() {
        //given
        SummonerDto sampelDto = new SummonerDto();
        sampelDto.setAccountId("-CZ1UdXj_30sT4ZOb1PDJfiCCZvYTRnYRQncqn8LUBE");
        sampelDto.setId("zJM0b_kEhZhHKhq6qsL8f4nusWE-IEegWdeqqK3LA3CrnA");
        sampelDto.setName("눕는게일상");
        sampelDto.setPuuid("KCSH-FOif2FOuoIFTkXclVK__08YQq8d4H7t96SNpLOVWUU8VDFA_2byLFMGlV_L3jZ0p_cRj-TYUg");
        sampelDto.setSummonerLevel(221);
        sampelDto.setProfileIconId(520);
        sampelDto.setRevisionDate(1655029488000L);

        //when & then
        assertThat(summonerService.compareSummonerAccount(sampelDto, testDto)).isTrue();
    }

    @Test
    void 소환사_계정정보_등가성테스트_불일치() {
        //given
        SummonerDto sampelDto = new SummonerDto();
        sampelDto.setAccountId("-CZ1UdXj_30sT4ZOb1PDJfiCCZvYTRnYRQncqn8LUBE");
        sampelDto.setId("zJM0b_kEhZhHKhq6qsL8f4nusWE-IEegWdeqqK3LA3CrnA");
        sampelDto.setName("눕는게일상");
        sampelDto.setPuuid("KCSH-FOif2FOuoIFTkXclVK__08YQq8d4H7t96SNpLOVWUU8VDFA_2byLFMGlV_L3jZ0p_cRj-TYUee");
        sampelDto.setSummonerLevel(221);
        sampelDto.setProfileIconId(520);
        sampelDto.setRevisionDate(1655029488000L);

        //when & then
        assertThat(summonerService.compareSummonerAccount(sampelDto, testDto)).isFalse();
    }

    @Test
    @Transactional
    void 소환사_데이터_Insert() {
        //given
        summonerService.insertAccount(testDto);

        //when
        SummonerDto resultDto = summonerService.selectAccount(testDto);

        //then
        assertThat(resultDto).isNotNull();
    }

    @Test
    @Transactional
    void 소환사_데이터_Update() {
        SummonerDto updateDto = new SummonerDto();
        updateDto = new SummonerDto();
        updateDto.setAccountId("-CZ1UdXj_30sT4ZOb1PDJfiCCZvYTRnYRQncqn8LUBE");
        updateDto.setId("zJM0b_kEhZhHKhq6qsL8f4nusWE-IEegWdeqqK3LA3CrnA");
        updateDto.setName("눕는게일상유유유");
        updateDto.setPuuid("KCSH-FOif2FOuoIFTkXclVK__08YQq8d4H7t96SNpLOVWUU8VDFA_2byLFMGlV_L3jZ0p_cRj-TYUg");
        updateDto.setSummonerLevel(333);
        updateDto.setProfileIconId(521);
        updateDto.setRevisionDate(1655029488000L);

        summonerService.insertAccount(testDto);

        boolean compareResult = summonerService.compareSummonerAccount(testDto, updateDto);

        if(!compareResult)
            summonerService.updateAccount(compareResult, updateDto);

        SummonerDto resultDto = summonerService.selectAccount(updateDto);

        assertThat(summonerService.compareSummonerAccount(updateDto, resultDto)).isTrue();
    }

    @Test
    @Transactional
    void 랭크정보_DB_InsertAndSelect_Test() {
        //given
        summonerService.insertAccount(testDto2);
        testHistoryList2.forEach(dto -> summonerMapper.insertSummonerHistory(dto));

        //when
        List<SummonerHistoryDto> getList =  summonerService.selectSummonerLeagueListBySummonerId(testDto2.getId());

        //then
        assertThat(testHistoryList2).map(SummonerHistoryDto::getLeagueId)
                .usingRecursiveFieldByFieldElementComparator()
                .isEqualTo(getList.stream().map(o -> o.getLeagueId()).collect( Collectors.toList()));
    }

    @Test
    @Transactional
    void 랭크정보_DB_Update_Test() {
        //given
        summonerService.insertAccount(testDto2);
        testHistoryList2.forEach(dto -> summonerMapper.insertSummonerHistory(dto));

        SummonerHistoryDto sample2 = new SummonerHistoryDto();
        sample2.setLeagueId("144a6d49-62c6-47ed-8406-fbaf7a7039b5");
        sample2.setSummonerId("GqGH7NQcZ8b-e9jQPi7dtohw9de1NT81JK7NsIgRCSi24g");
        sample2.setSummonerName("윤재초이");
        sample2.setQueueType("RANKED_SOLO_5x5");
        sample2.setTier("DIAMOND");
        sample2.setRank("II");
        sample2.setLeaguePoints(58);
        sample2.setWins(300);
        sample2.setLosses(200);
        sample2.setHotStreak(false);
        sample2.setVeteran(false);
        sample2.setVeteran(false);
        sample2.setInactive(false);

        //when
        summonerMapper.updateSummonerHistory(sample2);

        //then
        List<SummonerHistoryDto> getList = summonerService.selectSummonerLeagueListBySummonerId(testDto2.getId());
        assertThat(getList).filteredOn(o -> o.getLeagueId().equals(sample2.getLeagueId()))
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList(sample2));
    }

    @Test
    void 소환사_리그정보_RIOT통신획득() {
        //when
        ResponseDto dto = summonerService.getSummonerHistoryRiotAPI(testDto2.getId());

        //when
        ArrayList<SummonerHistoryDto> getList = (ArrayList<SummonerHistoryDto>)dto.getResult();

        //then
        //통신케이스는 안바뀌는 값만 대조함.
        assertThat(testHistoryList2)
                .usingRecursiveComparison().ignoringFields("tier", "rank", "leaguePoints", "wins", "losses", "summonerName","hotStreak", "veteran", "freshBlood", "inactive")
                .isEqualTo(getList);
    }
}