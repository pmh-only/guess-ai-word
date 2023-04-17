package codes.pmh.school.spring.guessaiword.game.dto;

import codes.pmh.school.spring.guessaiword.ai.dto.AIAskDto;
import codes.pmh.school.spring.guessaiword.common.entity.Game;
import codes.pmh.school.spring.guessaiword.common.entity.GameAskCandidate;
import codes.pmh.school.spring.guessaiword.common.entity.GameRound;

public class GameAskToAIDto implements GameIdFetchableDto, GameFetchableDto, GameRoundFetchableDto, GameAskCandidateFetchableDto {
    private String gameToken;

    private int gameId;

    private Game game;

    private GameRound gameRound;

    private String candidateSecret;

    private int candidateId;

    private GameAskCandidate candidate;

    private AIAskDto aiDto;

    @Override
    public String getGameToken() {
        return gameToken;
    }

    @Override
    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    @Override
    public int getGameId() {
        return gameId;
    }

    @Override
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public GameRound getGameRound() {
        return gameRound;
    }

    @Override
    public void setGameRound(GameRound gameRound) {
        this.gameRound = gameRound;
    }

    public String getCandidateSecret() {
        return candidateSecret;
    }

    public void setCandidateSecret(String candidateSecret) {
        this.candidateSecret = candidateSecret;
    }

    public int getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(int candidateId) {
        this.candidateId = candidateId;
    }

    public GameAskCandidate getCandidate() {
        return candidate;
    }

    @Override
    public void setCandidate(GameAskCandidate candidate) {
        this.candidate = candidate;
    }

    public AIAskDto getAiDto() {
        return aiDto;
    }

    public void setAiDto(AIAskDto aiDto) {
        this.aiDto = aiDto;
    }
}