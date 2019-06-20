package com.mcw.football.repository;

import com.mcw.football.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findAllByTeamId(Long teamId);

    Player findByUserId(Long id);
}
