package com.rozhnov.who_wins_application.repository;

import com.rozhnov.who_wins_application.entity.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {
    Event findByName(String name);
}
