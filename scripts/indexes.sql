CREATE INDEX rating_index ON Statistics(rating DESC);
EXPLAIN ANALYSE
SELECT * FROM Statistics ORDER BY rating DESC LIMIT 10;

CREATE INDEX player_user_index ON Player(user_id);
EXPLAIN ANALYSE
SELECT * FROM Player WHERE user_id = 'user3';

CREATE INDEX player_room_index ON Player(current_room_id);
EXPLAIN ANALYSE
SELECT * FROM Player WHERE current_room_id = 1;

CREATE INDEX message_room_index ON Message(room_id);
EXPLAIN ANALYSE
SELECT * FROM Message WHERE room_id = 1;

CREATE INDEX gamescore_game_index ON GameScore(game_id);
CREATE INDEX score_index ON GameScore(score ASC);
EXPLAIN ANALYSE
SELECT * FROM GameScore WHERE game_id = 2 ORDER BY score ASC;
