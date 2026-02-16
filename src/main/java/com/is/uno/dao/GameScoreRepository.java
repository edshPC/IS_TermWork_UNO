package com.is.uno.dao;

import com.is.uno.model.GameScore;
import com.is.uno.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GameScoreRepository extends JpaRepository<GameScore, Long> {
    @Query("SELECT SUM(s.score) FROM GameScore s WHERE s.user = :user")
    Long calculatePlayerScore(User user);
}
