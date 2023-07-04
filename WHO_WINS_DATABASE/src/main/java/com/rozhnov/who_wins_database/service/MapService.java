package com.rozhnov.who_wins_database.service;

import com.rozhnov.who_wins_database.entity.Map;
import com.rozhnov.who_wins_database.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapService {

    @Autowired
    MapRepository repository;

    public void save(Map map) {
        if (!repository.existsById(map.getId())) {
            repository.save(map);
        }
    }

    public void saveAll(List<Map> maps) {
        List<Map> forSave = new ArrayList<>();
        for (Map map: maps) {
            if (!repository.existsById(map.getId())) {
                forSave.add(map);
            }
        }

        repository.saveAll(forSave);
    }
}
