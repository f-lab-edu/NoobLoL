package com.nooblol.global.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RiotConfigurationTest {

    @Autowired
    private RiotConfiguration riotConfiguration;

    @Test
    void ApiKey획득_테스트() {
        String apiKey = riotConfiguration.getApiKey();

        System.out.println("API-KEY : " + apiKey);
        assertThat(apiKey).isNotNull();
    }
}