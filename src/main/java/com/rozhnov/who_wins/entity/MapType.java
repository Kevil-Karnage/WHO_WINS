package com.rozhnov.who_wins.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class MapType {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private boolean actual;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapTypeId")
    private List<Map> maps;
}
