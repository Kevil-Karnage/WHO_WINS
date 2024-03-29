package com.rozhnov.who_wins_database.repository;

import com.rozhnov.who_wins_database.entity.MapType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapTypeRepository extends CrudRepository<MapType, Long> {
    boolean existsByName(String name);

    MapType getByName(String name);
}
