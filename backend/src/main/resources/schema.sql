CREATE TABLE IF NOT EXISTS forum_user (
  id SERIAL PRIMARY KEY,
  username VARCHAR(255) NOT NULL UNIQUE,
  password CHAR(60) NOT NULL,
  image VARCHAR
);

CREATE TABLE IF NOT EXISTS user_authority (
  id SERIAL PRIMARY KEY,
  forum_user_id INTEGER REFERENCES forum_user(id),
  authority VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS topic (
  id SERIAL PRIMARY KEY,
  original_poster INTEGER REFERENCES forum_user(id),
  title TEXT UNIQUE
);

CREATE TABLE IF NOT EXISTS post (
  id SERIAL PRIMARY KEY,
  poster INTEGER REFERENCES forum_user(id),
  topic_id INTEGER REFERENCES topic(id),
  content TEXT NOT NULL
);