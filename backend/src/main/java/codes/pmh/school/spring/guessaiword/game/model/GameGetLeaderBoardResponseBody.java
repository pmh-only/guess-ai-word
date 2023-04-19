package codes.pmh.school.spring.guessaiword.game.model;

import codes.pmh.school.spring.guessaiword.common.entity.Game;

import java.util.List;

public class GameGetLeaderBoardResponseBody {
    private List<Game> games;

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
}
