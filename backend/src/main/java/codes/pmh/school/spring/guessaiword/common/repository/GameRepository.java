package codes.pmh.school.spring.guessaiword.common.repository;

import codes.pmh.school.spring.guessaiword.common.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
}
