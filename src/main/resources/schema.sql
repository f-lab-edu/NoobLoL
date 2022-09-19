drop
    all objects;
--account_id varchar변경
CREATE TABLE summoner_account
(
    id              varchar(255),
    account_id      varchar(255),
    PUUID           varchar(255),
    name            varchar(255),
    profile_icon_id varchar(255),
    revision_date   varchar(255),
    summoner_level  long
);

CREATE TABLE `summoner_simple_history`
(
    `summoner_id`   varchar(255),
    `queue_type`    varchar(255),
    `tier`          varchar(255),
    `rank`          varchar(255),
    `summoner_name` varchar(255),
    `league_points` int,
    `wins`          int,
    `losses`        int,
    `league_id`     varchar(255)
);

CREATE TABLE `match_gameinfo`
(
    `data_version`          varchar(255),
    `match_id`              varchar(255) PRIMARY KEY NOT NULL,
    `game_creation`         long,
    `game_duration`         long,
    `game_end_time_stamp`   long,
    `game_start_time_stamp` long,
    `game_id`               long,
    `game_mode`             varchar(255),
    `game_name`             varchar(255),
    `game_version`          varchar(255),
    `map_id`                int,
    `platform_id`           varchar(255),
    `queue_id`              int
);

CREATE TABLE `match_game_bans`
(
    `match_id`    varchar(255),
    `champion_id` int,
    `pick_turn`   int
);


CREATE TABLE `match_participants`
(
    `puuid`           varchar(255) NOT NULL,
    `match_id`        varchar(255) NOT NULL,
    `champion_name`   varchar(255),
    `champion_id`     int,
    `champion_level`  int,
    `summoner_id`     varchar(255),
    `summoner_name`   varchar(255),
    `kills`           int,
    `deaths`          int,
    `assists`         int,
    `role`            varchar(255),
    `lane`            varchar(255),
    `team_id`         int,
    `team_position`   varchar(255),
    `win`             tinyint,
    `summoner1_casts` int,
    `summoner1_id`    int,
    `summoner2_casts` int,
    `summoner2_id`    int,
    `item0`           int,
    `item1`           int,
    `item2`           int,
    `item3`           int,
    `item4`           int,
    `item5`           int,
    `item6`           int
);

CREATE TABLE `match_game_runes`
(
    `puuid`    varchar(255) NOT NULL COMMENT 'puuid',
    `match_id` varchar(255) NOT NULL,
    `type`     varchar(255),
    `sort_no`  int,
    `perk`     int
);

COMMIT;

CREATE TABLE `users`
(
    `user_id`            varchar(255) primary key not null,
    `user_email`         varchar(255) unique,
    `user_name`          varchar(255),
    `user_password_hash` varchar(255),
    `user_role`          int,
    `level`              int      DEFAULT 1,
    `exp`                int      DEFAULT 0,
    `created_at`         datetime DEFAULT (now()),
    `updated_at`         datetime
);

/*
 22. 09. 14 : to_status, from_status, to_read_status의 추가
    -> 쪽지를 삭제할 시 status값을 한쪽만 사용하게 되면, 둘다 동시에 삭제하게 되는 것이므로 해당 문제를 방지하기 위해 각각 status를 보관한다.
    -> 수신자의 읽음 처리를 구분하기 위하여 추가하였으며, true는 읽음, false는 읽지않음 이다.

 22. 09. 14 to, from status에서 이미 읽은 상태 구혀중이기에 to_read_status제거
 */
CREATE TABLE `users_letter`
(
    `letter_id`      int PRIMARY KEY AUTO_INCREMENT,
    `letter_title`   varchar(255) NOT NULL,
    `letter_content` text,
    `to_user_id`     varchar(255) NOT NULL,
    `from_user_id`   varchar(255) NOT NULL,
    `to_status`      int          NOT NULL,
    `from_status`    int          NOT NULL,
    `created_at`     datetime DEFAULT (now())
);

CREATE TABLE `bbs_category`
(
    `category_id`     int PRIMARY KEY AUTO_INCREMENT,
    `category_name`   varchar(255),
    `status`          int,
    `created_user_id` varchar(255),
    `created_at`      datetime DEFAULT (now()),
    `updated_user_id` varchar(255),
    `updated_at`      datetime
);

CREATE TABLE `bbs`
(
    `bbs_id`          int PRIMARY KEY AUTO_INCREMENT,
    `category_id`     int,
    `bbs_name`        varchar(255),
    `status`          int,
    `created_user_id` varchar(255),
    `created_at`      datetime DEFAULT (now()),
    `updated_user_id` varchar(255),
    `updated_at`      datetime
);

/* Upsert시 자동증가인 경우 Update도 값을 증기사키는 문제로 인한 수정*/
CREATE TABLE `bbs_articles`
(
    `article_id`         int PRIMARY KEY,
    `bbs_id`             int,
    `article_title`      varchar(255),
    `article_read_count` int,
    `article_content`    text,
    `status`             int,
    `created_user_id`    varchar(255),
    `created_at`         datetime DEFAULT (now()),
    `updated_at`         datetime
);

/*
  22. 09. 09 BBSID컬럼 삭제 : articleId로 추적이 가능하기 떄문에 해당 테이블에서는 꼭 필요하지 않다 판단.
 */
CREATE TABLE `bbs_articles_status`
(
    `article_id` int,
    `user_id`    varchar(255),
    `type`       tinyint(1),
    `created_at` datetime DEFAULT (now())
);

/*
  22. 09. 09 BBSID컬럼 삭제 : articleId로 추적이 가능하기 떄문에 해당 테이블에서는 꼭 필요하지 않다 판단.
  22. 09. 09 ReplyId Auto-Increment제거 : Upsert시 자동증가인 경우 Update도 값을 증기사키는 문제로 인한 수정
  22. 09. 09 테이블명 수정 : bbs_article_reply → bbs_articles_reply
 */
CREATE TABLE `bbs_articles_reply`
(
    `reply_id`        int PRIMARY KEY,
    `article_id`      int,
    `reply_content`   text,
    `status`          int,
    `sort_no`         int,
    `created_user_id` varchar(255),
    `created_at`      datetime DEFAULT (now())
);

CREATE TABLE `party_category`
(
    `category_id`     int PRIMARY KEY AUTO_INCREMENT,
    `category_name`   varchar(255),
    `status`          int,
    `created_user_id` int,
    `created_at`      datetime DEFAULT (now()),
    `updated_user_id` int,
    `updated_at`      datetime
);

//해당 글이 모집 완료가 되었는지, 구인구직중인지를 알기 위해서 Status 제작
CREATE TABLE `party_list`
(
    `id`            int PRIMARY KEY AUTO_INCREMENT,
    `category_id`   int,
    `title`         varchar(255),
    `content`       text,
    `status`        varchar(255),
    `character_id`  varchar(255),
    `password_hash` varchar(255),
    `created_at`    datetime DEFAULT (now())
);