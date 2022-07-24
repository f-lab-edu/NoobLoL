package com.nooblol.account.service;

import com.nooblol.account.dto.SummonerDto;
import com.nooblol.account.mapper.SummonerMapper;
import com.nooblol.global.dto.ResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

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

}