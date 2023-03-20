package codes.pmh.school.spring.guessaiword.game.repository;

import codes.pmh.school.spring.guessaiword.game.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, String> {
}
