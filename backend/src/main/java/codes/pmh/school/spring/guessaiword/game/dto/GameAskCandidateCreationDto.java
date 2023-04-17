package codes.pmh.school.spring.guessaiword.game.dto;

import codes.pmh.school.spring.guessaiword.common.entity.Game;
import codes.pmh.school.spring.guessaiword.common.entity.GameRound;

import java.util.ArrayList;
import java.util.List;

public class GameAskCandidateCreationDto implements GameIdFetchableDto, GameFetchableDto, GameRoundFetchableDto {
    private String gameToken;

    private Game game;

    private GameRound gameRound;

    private int gameId;

    private List<GameAskCandidateDto> candidates = new ArrayList<>();

    private String candidateSecret;

    @Override
    public String getGameToken() {
        return this.gameToken;
    }

    @Override
    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    @Override
    public GameRound getGameRound() {
        return this.gameRound;
    }

    @Override
    public void setGameRound(GameRound gameRound) {
        this.gameRound = gameRound;
    }

    @Override
    public Game getGame() {
        return this.game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public int getGameId() {
        return this.gameId;
    }

    @Override
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

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
