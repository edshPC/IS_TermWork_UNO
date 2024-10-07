-- триггер на автоматическое хэширование пароля при регистрации пользователя
CREATE OR REPLACE FUNCTION hash_user_password()
    RETURNS TRIGGER AS $$
BEGIN
    IF NEW.password IS DISTINCT FROM OLD.password THEN
        NEW.password := md5(NEW.password);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER hash_password_trigger
    BEFORE INSERT OR UPDATE ON Users
    FOR EACH ROW
EXECUTE FUNCTION hash_user_password();


-- триггер на проверку уникальности имен в игровой комнате
CREATE OR REPLACE FUNCTION unique_in_game_name_in_room()
    RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM Player WHERE current_room_id = NEW.current_room_id AND in_game_name = NEW.in_game_name
    ) THEN
        RAISE EXCEPTION 'Player name % already exists in room %', NEW.in_game_name, NEW.current_room_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER unique_in_game_name_trigger
    BEFORE INSERT ON Player
    FOR EACH ROW
EXECUTE FUNCTION unique_in_game_name_in_room();


-- триггер на проверку максимального количества игроков в комнате
CREATE OR REPLACE FUNCTION check_player_count()
    RETURNS TRIGGER AS $$
BEGIN
    IF (SELECT COUNT(*) FROM Player WHERE current_room_id = NEW.current_room_id) >=
       (SELECT max_players FROM GameRoom WHERE room_id = NEW.current_room_id) THEN
        RAISE EXCEPTION 'Maximum number of players reached in room %', NEW.current_room_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER player_count_trigger
    BEFORE INSERT ON Player
    FOR EACH ROW
EXECUTE FUNCTION check_player_count();

