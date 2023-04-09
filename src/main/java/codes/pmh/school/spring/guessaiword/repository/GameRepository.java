package codes.pmh.school.spring.guessaiword.repository;

import codes.pmh.school.spring.guessaiword.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
}
