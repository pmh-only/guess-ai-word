package codes.pmh.school.spring.guessaiword.game.dto;

import codes.pmh.school.spring.guessaiword.game.entity.GameAskCandidate;

public interface GameAskCandidateFetchableDto {
    public String getCandidateSecret();

    public int getCandidateId();

    public void setCandidate(GameAskCandidate candidate);
}
