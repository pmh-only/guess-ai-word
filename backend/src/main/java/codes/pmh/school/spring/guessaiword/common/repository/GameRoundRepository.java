package codes.pmh.school.spring.guessaiword.common.repository;

import codes.pmh.school.spring.guessaiword.common.entity.GameRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRoundRepository extends JpaRepository<GameRound, Integer> {
}
