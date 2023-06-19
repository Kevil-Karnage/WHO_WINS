package com.rozhnov.who_wins.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Player {
    @Id
    private Long id;

    private String name;

    private String nickname;

    private String surname;

    @JsonIgnore
    @OneToMany(mappedBy = "player")
    List<PlayerStats> playerStats;
}
