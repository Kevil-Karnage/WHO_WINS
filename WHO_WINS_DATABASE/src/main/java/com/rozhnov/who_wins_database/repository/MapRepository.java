package com.rozhnov.who_wins_database.repository;

import com.rozhnov.who_wins_database.entity.Map;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends CrudRepository<Map, Long> {
}
