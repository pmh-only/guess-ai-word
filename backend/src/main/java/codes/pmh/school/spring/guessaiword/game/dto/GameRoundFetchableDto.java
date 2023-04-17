package codes.pmh.school.spring.guessaiword.game.dto;

import codes.pmh.school.spring.guessaiword.common.entity.Game;
import codes.pmh.school.spring.guessaiword.common.entity.GameRound;

public interface GameRoundFetchableDto {
    public GameRound getGameRound ();

    public void setGameRound (GameRound gameRound);

    public Game getGame ();

    public void setGame (Game game);
}
