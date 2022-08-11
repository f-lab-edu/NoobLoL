package com.nooblol.account.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.account.service.MatchGameInfoService;
import com.nooblol.global.config.RiotConfiguration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MatchGameInfoServiceImpl implements MatchGameInfoService {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final RiotConfiguration riotConfiguration;
  private final ObjectMapper objectMapper;
  private final RestTemplate restTemplate;
}
