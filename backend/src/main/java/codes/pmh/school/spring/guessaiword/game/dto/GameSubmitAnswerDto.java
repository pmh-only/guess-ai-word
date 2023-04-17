package codes.pmh.school.spring.guessaiword.game.dto;

import codes.pmh.school.spring.guessaiword.common.entity.Game;
import codes.pmh.school.spring.guessaiword.common.entity.GameRound;

public class GameSubmitAnswerDto implements GameIdFetchableDto, GameFetchableDto, GameRoundFetchableDto {
    private String gameToken;

    private int gameId;

    private Game game;

    private GameRound gameRound;

    private String answer;

    private boolean isCorrect;

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
