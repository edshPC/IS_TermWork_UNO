CREATE TYPE type_of_card AS ENUM (
    'number',
    'plus_two',
    'plus_four',
    'change_direction',
    'skip',
    'choose_color'
    );

CREATE TYPE color_of_card AS ENUM (
    'red',
    'blue',
    'green',
    'yellow',
    'black'
    );

CREATE TABLE IF NOT EXISTS Users
(
    login             VARCHAR(32) PRIMARY KEY,
    user_name         VARCHAR(32),
    password          TEXT      NOT NULL,
    registration_date TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS GameRoom
(
    room_id     SERIAL PRIMARY KEY,
    room_name   VARCHAR(32) NOT NULL,
    password    TEXT,
    max_players INT         NOT NULL CHECK ( max_players >= 2 and max_players <= 8 ),
    owner       VARCHAR(32) NOT NULL REFERENCES Users (login) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Player
(
    player_id       SERIAL PRIMARY KEY,
    user_id         VARCHAR(32) NOT NULL REFERENCES Users (login) ON DELETE CASCADE,
    current_room_id INT         NOT NULL REFERENCES GameRoom (room_id) ON DELETE CASCADE,
    in_game_name    VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS Game
(
    game_id    SERIAL PRIMARY KEY,
    max_score  INT CHECK ( max_score >= 0 ),
    start_time TIMESTAMP NOT NULL DEFAULT now(),
    end_time   TIMESTAMP,
    winner_id  INT       REFERENCES Player (player_id) ON DELETE SET NULL,
    room_id    INT       NOT NULL REFERENCES GameRoom (room_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS GameScore
(
    score_id  SERIAL PRIMARY KEY,
    player_id INT NOT NULL REFERENCES Player (player_id) ON DELETE CASCADE,
    game_id   INT NOT NULL REFERENCES Game (game_id) ON DELETE CASCADE,
    score     INT NOT NULL DEFAULT 0 CHECK ( score >= 0 ),
    UNIQUE (player_id, game_id)
);

CREATE TABLE IF NOT EXISTS Card
(
    card_id       SERIAL PRIMARY KEY,
    type_of_card  type_of_card  NOT NULL,
    color_of_card color_of_card NOT NULL,
    value         INT CHECK ( value >= 0 and value <= 9 )
);

CREATE TABLE IF NOT EXISTS Deck
(
    entry_id SERIAL PRIMARY KEY,
    card_id  INT  NOT NULL REFERENCES Card (card_id) ON DELETE CASCADE,
    weight   REAL NOT NULL CHECK ( weight > 0 )
);

CREATE TABLE IF NOT EXISTS Statistics
(
    stat_id     SERIAL PRIMARY KEY,
    user_id     VARCHAR(32) NOT NULL UNIQUE REFERENCES Users (login) ON DELETE CASCADE,
    rating      REAL        NOT NULL DEFAULT 0 CHECK ( rating >= 0 ),
    play_count  INT         NOT NULL DEFAULT 0 CHECK ( play_count >= 0 ),
    win_count   INT         NOT NULL DEFAULT 0 CHECK ( win_count >= 0 ),
    time_played INTERVAL    NOT NULL DEFAULT '0' CHECK ( time_played >= '0' )
);

CREATE TABLE IF NOT EXISTS Achievement
(
    achievement_id SERIAL PRIMARY KEY,
    name           TEXT NOT NULL,
    description    TEXT
);

CREATE TABLE IF NOT EXISTS UserAchievement
(
    user_id        VARCHAR(32) NOT NULL REFERENCES Users (login) ON DELETE CASCADE,
    achievement_id INT         NOT NULL REFERENCES Achievement (achievement_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, achievement_id),
    time           TIMESTAMP   NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS Message
(
    message_id SERIAL PRIMARY KEY,
    text       TEXT      NOT NULL,
    time       TIMESTAMP NOT NULL DEFAULT now(),
    room_id    INT       NOT NULL REFERENCES GameRoom (room_id) ON DELETE CASCADE,
    sender_id  INT       NOT NULL REFERENCES Player (player_id) ON DELETE CASCADE
);


