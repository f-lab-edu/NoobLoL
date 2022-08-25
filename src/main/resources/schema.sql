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
    `match_id`  varchar(255),
    `bans`      int,
    `pick_turn` int
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

CREATE TABLE `users_letter`
(
    `letter_id`      int PRIMARY KEY AUTO_INCREMENT,
    `letter_title`   varchar(255) NOT NULL,
    `letter_content` text,
    `to_user_id`     int          NOT NULL,
    `from_user_id`   int          NOT NULL,
    `created_at`     datetime DEFAULT (now())
);

CREATE TABLE `bbs_category`
(
    `category_id`     int PRIMARY KEY AUTO_INCREMENT,
    `category_name`   varchar(255),
    `status`          int,
    `created_user_id` int,
    `created_at`      datetime DEFAULT (now()),
    `updated_user_id` int,
    `updated_at`      datetime
);

CREATE TABLE `bbs`
(
    `bbs_id`          int PRIMARY KEY AUTO_INCREMENT,
    `category_id`     int,
    `bbs_name`        varchar(255),
    `status`          int,
    `created_user_id` int,
    `created_at`      datetime DEFAULT (now()),
    `updated_user_id` int,
    `updated_at`      datetime
);

CREATE TABLE `bbs_articles`
(
    `article_id`         int PRIMARY KEY AUTO_INCREMENT,
    `bbs_id`             int,
    `article_title`      varchar(255),
    `article_read_count` int,
    `article_content`    text,
    `status`             int,
    `created_user_id`    int,
    `created_at`         datetime DEFAULT (now()),
    `updated_at`         datetime
);

CREATE TABLE `bbs_articles_status`
(
    `article_id` int,
    `bbs_id`     int,
    `user_id`    int,
    `type`       tinyint,
    `created_at` datetime DEFAULT (now())
);

CREATE TABLE `bbs_article_reply`
(
    `reply_id`        int PRIMARY KEY AUTO_INCREMENT,
    `article_id`      int,
    `bbs_id`          int,
    `reply_content`   text,
    `status`          int,
    `sort_no`         int,
    `created_user_id` int,
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

CREATE TABLE `party_list`
(
    `party_id`      int PRIMARY KEY AUTO_INCREMENT,
    `category_id`   int,
    `party_title`   varchar(255),
    `party_content` text,
    `character_id`  varchar(255),
    `password_hash` varchar(255),
    `created_at`    datetime DEFAULT (now())
);