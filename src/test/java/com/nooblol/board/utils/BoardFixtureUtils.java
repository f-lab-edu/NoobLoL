package com.nooblol.board.utils;

import com.nooblol.board.dto.BbsDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardFixtureUtils {

  public static List<BbsDto> BbsListFixtureByCategoryIdAndStatusAndUserID(
      int categoryId, int status, String adminUserId) {
    List<BbsDto> responseList = new ArrayList<BbsDto>();
    responseList.add(
        new BbsDto().builder()
            .bbsId(1).categoryId(categoryId).bbsName("Sample BbsName1").status(status)
            .createdUserId(adminUserId).createdAt(LocalDateTime.now())
            .updatedUserId(adminUserId).updatedAt(LocalDateTime.now())
            .build()
    );
    responseList.add(
        new BbsDto().builder()
            .bbsId(2).categoryId(categoryId).bbsName("Sample BbsName2").status(status)
            .createdUserId(adminUserId).createdAt(LocalDateTime.now())
            .updatedUserId(adminUserId).updatedAt(LocalDateTime.now())
            .build()
    );
    responseList.add(
        new BbsDto().builder()
            .bbsId(3).categoryId(categoryId).bbsName("Sample BbsName3").status(status)
            .createdUserId(adminUserId).createdAt(LocalDateTime.now())
            .updatedUserId(adminUserId).updatedAt(LocalDateTime.now())
            .build()
    );
    return responseList;
  }

  public static List<BbsDto> BbsAllCategoryListFixtureByAdminUserID(
      String adminUserId) {
    List<BbsDto> responseList = new ArrayList<BbsDto>();
    responseList.add(
        new BbsDto().builder()
            .bbsId(1).categoryId(1).bbsName("Sample BbsName1")
            .status(BoardStatusEnum.ACTIVE.getStatus())
            .createdUserId(adminUserId).createdAt(LocalDateTime.now())
            .updatedUserId(adminUserId).updatedAt(LocalDateTime.now())
            .build()
    );
    responseList.add(
        new BbsDto().builder()
            .bbsId(2).categoryId(2).bbsName("Sample BbsName2")
            .status(BoardStatusEnum.ACTIVE.getStatus())
            .createdUserId(adminUserId).createdAt(LocalDateTime.now())
            .updatedUserId(adminUserId).updatedAt(LocalDateTime.now())
            .build()
    );
    responseList.add(
        new BbsDto().builder()
            .bbsId(3).categoryId(3).bbsName("Sample BbsName3")
            .status(BoardStatusEnum.ACTIVE.getStatus())
            .createdUserId(adminUserId).createdAt(LocalDateTime.now())
            .updatedUserId(adminUserId).updatedAt(LocalDateTime.now())
            .build()
    );
    responseList.add(
        new BbsDto().builder()
            .bbsId(4).categoryId(3).bbsName("Sample BbsName4")
            .status(BoardStatusEnum.DELETE.getStatus())
            .createdUserId(adminUserId).createdAt(LocalDateTime.now())
            .updatedUserId(adminUserId).updatedAt(LocalDateTime.now())
            .build()
    );
    return responseList;
  }
}
