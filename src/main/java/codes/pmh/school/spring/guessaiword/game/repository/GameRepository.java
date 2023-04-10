package codes.pmh.school.spring.guessaiword.game.repository;

import codes.pmh.school.spring.guessaiword.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
}
