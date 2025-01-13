package com.is.uno.dao;

import com.is.uno.model.GameScore;
import com.is.uno.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameScoreRepository extends JpaRepository<GameScore, Long> {
    List<GameScore> findByGameId(Long gameId);
    @Query("SELECT SUM(s.score) FROM GameScore s WHERE s.player = :player")
    Long calculatePlayerScore(Player player);
}
