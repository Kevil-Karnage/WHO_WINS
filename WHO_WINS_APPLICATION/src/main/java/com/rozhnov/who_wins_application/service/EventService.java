package com.rozhnov.who_wins_application.service;

import com.rozhnov.who_wins_application.entity.Event;
import com.rozhnov.who_wins_application.repository.EventRepository;
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
