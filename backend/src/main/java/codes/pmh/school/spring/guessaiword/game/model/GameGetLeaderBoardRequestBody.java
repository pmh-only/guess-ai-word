package codes.pmh.school.spring.guessaiword.game.model;

import codes.pmh.school.spring.guessaiword.game.enums.GameType;
import jakarta.validation.constraints.NotNull;

public class GameGetLeaderBoardRequestBody {
    @NotNull
    private GameType gameType;

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }
}
