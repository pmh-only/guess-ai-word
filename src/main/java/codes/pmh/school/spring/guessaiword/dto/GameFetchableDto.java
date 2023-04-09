package codes.pmh.school.spring.guessaiword.dto;

import codes.pmh.school.spring.guessaiword.entity.Game;

public interface GameFetchableDto {
    public int getGameId();

    public void setGameId(int gameId);

    public Game getGame();

    public void setGame(Game game);
}
