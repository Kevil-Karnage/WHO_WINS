package com.rozhnov.who_wins_database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Player implements Serializable {
    @Id
    private Long id;

    private String nickname;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "player_id")
    List<PlayerStats> playerStats;
}
