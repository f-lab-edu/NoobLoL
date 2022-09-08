/*password : abc*/
INSERT INTO users(user_id, user_email, user_name, user_password_hash, user_role, level, exp,
                  created_at, updated_at)
VALUES ('test', 'test@test.com', 'test',
        '3a81oZNherrMQXNJriBBMRLm+k6JqX6iCp7u5ktV05ohkpkqJ0/BqDa6PCOj/uu9RU1EI2Q86A4qmslPpUyknw==',
        1, 1, 0, now(), now());

INSERT INTO users(user_id, user_email, user_name, user_password_hash, user_role, level, exp,
                  created_at, updated_at)
VALUES ('test-admin-user', 'admin@test.com', 'admin',
        '3a81oZNherrMQXNJriBBMRLm+k6JqX6iCp7u5ktV05ohkpkqJ0/BqDa6PCOj/uu9RU1EI2Q86A4qmslPpUyknw==',
        9, 1, 0, now(), now());

INSERT INTO bbs_category(category_name, status, created_user_id, created_at, updated_user_id,
                         updated_at)
VALUES ('Test Active Category1', 1, 'test-admin-user', now(), 'test-admin-user', now());

INSERT INTO bbs (category_id, bbs_name, status, created_user_id, created_at, updated_user_id,
                 updated_at)
VALUES (1, 'Test Active BBS', 1, 'test-admin-user', now(), 'test-admin-user', now());

INSERT INTO bbs_articles(article_id, bbs_id, article_title, article_read_count, article_content,
                         status,
                         created_user_id, created_at, updated_at)
VALUES (1, 1, 'Test Article Title - 1', 0, '내용이웨요', 1, 'test-admin-user', now(), now());

INSERT INTO bbs_articles(article_id, bbs_id, article_title, article_read_count, article_content,
                         status,
                         created_user_id, created_at, updated_at)
VALUES (2, 1, 'Test Article Title - 22', 0, '내용이웨요22', 1, 'test', now(), now());