package com.rozhnov.who_wins.repository;

import com.rozhnov.who_wins.entity.Map;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends CrudRepository<Map, Long> {
}
