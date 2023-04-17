package codes.pmh.school.spring.guessaiword.game.dto;

import codes.pmh.school.spring.guessaiword.common.entity.Game;

public interface GameFetchableDto {
    public int getGameId();

    public void setGameId(int gameId);

    public Game getGame();

    public void setGame(Game game);
}
