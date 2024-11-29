package com.is.uno.dao;

import com.is.uno.model.Player;
import com.is.uno.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUserIdUsername(String name);
}
