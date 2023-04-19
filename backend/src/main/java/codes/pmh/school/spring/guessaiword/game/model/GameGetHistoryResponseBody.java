package codes.pmh.school.spring.guessaiword.game.model;

import codes.pmh.school.spring.guessaiword.common.entity.Game;

import java.util.List;

public class GameGetHistoryResponseBody {
    private List<Game> games;

    private boolean isLast;

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }
}
