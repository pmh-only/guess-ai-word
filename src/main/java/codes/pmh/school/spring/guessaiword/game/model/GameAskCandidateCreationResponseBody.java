package codes.pmh.school.spring.guessaiword.game.model;

import codes.pmh.school.spring.guessaiword.game.dto.GameAskCandidateDto;

import java.util.ArrayList;
import java.util.List;

public class GameAskCandidateCreationResponseBody {
    private List<GameAskCandidateDto> candidates = new ArrayList<>();

    private String candidateSecret;

    public List<GameAskCandidateDto> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<GameAskCandidateDto> candidates) {
        this.candidates = candidates;
    }

    public String getCandidateSecret() {
        return candidateSecret;
    }

    public void setCandidateSecret(String candidateSecret) {
        this.candidateSecret = candidateSecret;
    }
}
