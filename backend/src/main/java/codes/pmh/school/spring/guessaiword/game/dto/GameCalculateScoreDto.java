package codes.pmh.school.spring.guessaiword.game.dto;

import codes.pmh.school.spring.guessaiword.common.entity.Game;

public class GameCalculateScoreDto implements GameIdFetchableDto, GameFetchableDto {
    private String gameToken;

    private int gameId;

    private Game game;

    private int gameScore;

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

    public int getGameScore() {
        return gameScore;
    }

    public void setGameScore(int gameScore) {
        this.gameScore = gameScore;
    }
}
