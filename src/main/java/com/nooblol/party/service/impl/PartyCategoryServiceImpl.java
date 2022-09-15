package com.nooblol.party.service.impl;

import com.nooblol.party.mapper.PartyCategoryMapper;
import com.nooblol.party.service.PartyCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartyCategoryServiceImpl implements PartyCategoryService {

  private final PartyCategoryMapper partyCategoryMapper;


}
