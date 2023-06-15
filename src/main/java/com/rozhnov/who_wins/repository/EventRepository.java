package com.rozhnov.who_wins.repository;

import com.rozhnov.who_wins.entity.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long> {
}
