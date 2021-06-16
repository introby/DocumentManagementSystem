CREATE TABLE user_account (
    user_id SERIAL,
    username varchar(16) UNIQUE NOT NULL,
    password varchar(255) NOT NULL,
    first_name varchar(16) NOT NULL,
    last_name varchar(16) NOT NULL,
    role varchar(16) DEFAULT 'USER' NOT NULL,
    status varchar(16) DEFAULT 'ACTIVE' NOT NULL,
    created_at date,
    PRIMARY KEY (user_id)
);

INSERT INTO user_account (username, password, first_name, last_name, role, status, created_at)
VALUES ('potter', '$2y$12$VvFFkfZ4jtXlezd2sNjmgu/Yt/HxMwK4n3z5Cf7h7g57GFfTcGq2G', 'Harry', 'Potter', 'USER', 'INACTIVE', '2021-06-08'),
        ('admin', '$2y$12$0gLtM.XNwhOiUDqLmBHxgO7KGKxcF4ukiu.jPJOLmPJR8EaUZ0hiG', 'Admin', 'Admin', 'ADMIN', 'ACTIVE', '2021-06-09'),
        ('user', '$2y$12$oezo7GgzhpxJu2Om6iCQQOAu98CxUH7NQxkd.bM3U9Bl3WwMeZcnO', 'User', 'User', 'USER', 'ACTIVE', '2021-06-09');