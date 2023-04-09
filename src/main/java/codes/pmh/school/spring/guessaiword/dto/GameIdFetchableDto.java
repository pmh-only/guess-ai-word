package codes.pmh.school.spring.guessaiword.dto;

import codes.pmh.school.spring.guessaiword.entity.Game;

public interface GameIdFetchableDto {
    public String getGameToken();

    public void setGameToken(String gameToken);

    public int getGameId();

    public void setGameId(int gameId);
}
