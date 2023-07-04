package com.rozhnov.who_wins_database.service;

import com.rozhnov.who_wins_database.entity.Event;
import com.rozhnov.who_wins_database.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    @Autowired
    EventRepository repository;

    public Event getByName(String name) {
        return repository.findByName(name);
    }

    public void save(Event event) {
        if (!repository.existsById(event.getId())) {
            repository.save(event);
        }
    }

}
