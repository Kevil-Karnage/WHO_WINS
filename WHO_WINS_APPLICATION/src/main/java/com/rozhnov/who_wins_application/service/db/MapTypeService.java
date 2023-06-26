package com.rozhnov.who_wins_application.service.db;

import com.rozhnov.who_wins_application.entity.MapType;
import com.rozhnov.who_wins_application.repository.MapTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapTypeService {

    @Autowired
    MapTypeRepository repository;

    public MapType save(MapType mapType) {
        if (!repository.existsByName(mapType.getName())) {
            return repository.save(mapType);
        }
        return repository.getByName(mapType.getName());
    }

}
