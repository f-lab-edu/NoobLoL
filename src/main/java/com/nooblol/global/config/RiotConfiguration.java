package com.nooblol.global.config;

import com.nooblol.global.utils.YamlLoadFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:constants.yml", factory = YamlLoadFactory.class)
@ConfigurationProperties(prefix = "riot")
@Getter
@Setter
public class RiotConfiguration {

  private String apiKey;

  private String summonerNameSearchApiUrl;

}
