package codes.pmh.school.spring.guessaiword.game.dto;

import codes.pmh.school.spring.guessaiword.game.entity.Game;

public class GameRoundCreationDto implements GameIdFetchableDto, GameFetchableDto {
    private Game game;

    private int gameId;

    private String answerWord;

    private String gameToken;

    @Override
    public Game getGame() {
        return this.game;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    public String getAnswerWord() {
        return answerWord;
    }

    public void setAnswerWord(String answerWord) {
        this.answerWord = answerWord;
    }

    @Override
    public String getGameToken() {
        return this.gameToken;
    }

    @Override
    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }

    @Override
    public int getGameId() {
        return this.gameId;
    }

    @Override
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}