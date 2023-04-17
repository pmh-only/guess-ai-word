package codes.pmh.school.spring.guessaiword.common.repository;

import codes.pmh.school.spring.guessaiword.common.entity.GameAskCandidate;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameAskCandidateRepository extends JpaRepository<GameAskCandidate, Integer> {
    public Optional<GameAskCandidate> findByIdAndSecret (int id, String secret);

    @Transactional
    public void deleteBySecret(String secret);
}
