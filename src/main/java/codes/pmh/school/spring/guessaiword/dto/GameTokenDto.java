package codes.pmh.school.spring.guessaiword.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GameTokenDto {
    private int gameId;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public GameTokenDto () {}

    public GameTokenDto (String serialized) throws JsonProcessingException {
        this.gameId = new ObjectMapper()
                .readValue(serialized, GameTokenDto.class)
                .getGameId();
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}
