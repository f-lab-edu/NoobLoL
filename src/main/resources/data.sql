INSERT INTO bbs_category(category_name, status, created_user_id, created_at, updated_user_id,
                         updated_at)
VALUES ('Test Active Category1', 1, 'test-admin-user', now(), 'test-admin-user', now());

INSERT INTO bbs (category_id, bbs_name, status, created_user_id, created_at, updated_user_id,
                 updated_at)
VALUES (1, 'Test Active BBS', 1, 'test-admin-user', now(), 'test-admin-user', now());

INSERT INTO bbs_articles(bbs_id, article_title, article_read_count, article_content, status,
                         created_user_id, created_at, updated_at)
VALUES (1, 'Test Article Title - 1', 0, '내용이웨요', 1, 'test-admin-user', now(), now());