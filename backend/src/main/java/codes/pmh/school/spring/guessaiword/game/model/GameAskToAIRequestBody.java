package codes.pmh.school.spring.guessaiword.game.model;

import jakarta.validation.constraints.NotNull;

public class GameAskToAIRequestBody {
    @NotNull
    private int candidateId;

    @NotNull
    private String candidateSecret;

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateSecret() {
        return candidateSecret;
    }

    public void setCandidateSecret(String candidateSecret) {
        this.candidateSecret = candidateSecret;
    }
}
