package com.is.uno.dao;

import com.is.uno.model.GameRoom;
import com.is.uno.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
   Optional<User> findByUsername(String username);

   boolean existsByUsername(String username);

    //@Query("SELECT COUNT(*) FROM Player p WHERE p.currentRoom.id = :currentRoom")
    long countByCurrentRoom(GameRoom currentRoom);
}
