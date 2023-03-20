package codes.pmh.school.spring.guessaiword.game.datatype.api;

import codes.pmh.school.spring.guessaiword.game.entity.GameAIResponse;

public class GameAPIGetNextResult {
    private GameAIResponse aiResponse;

    public GameAIResponse getAiResponse() {
        return aiResponse;
    }

    public void setAiResponse(GameAIResponse aiResponse) {
        this.aiResponse = aiResponse;
    }
}
