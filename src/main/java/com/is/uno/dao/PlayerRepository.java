package com.is.uno.dao;

import com.is.uno.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByUserUsername(String name);

    @Query("SELECT p FROM Player p WHERE p.inGameName = :inGameName AND p.currentRoom.id = :roomId")
    Optional<Player> findByInGameNameAndRoomId(String inGameName, Long roomId);
}
