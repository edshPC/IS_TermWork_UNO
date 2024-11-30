package com.is.uno.dao;

import com.is.uno.model.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    //@Query("SELECT s FROM Statistics s WHERE s.user = :username")
    Optional<Statistics> findByUserUsername(String username);
}
