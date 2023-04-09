package codes.pmh.school.spring.guessaiword.model;

import jakarta.validation.constraints.NotNull;

public class GameUpdatePlayerNameRequestBody {
    @NotNull
    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
