package codes.pmh.school.spring.guessaiword.repository;

import codes.pmh.school.spring.guessaiword.entity.GameAskRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameAskRecordRepository extends JpaRepository<GameAskRecord, Integer> {
}
