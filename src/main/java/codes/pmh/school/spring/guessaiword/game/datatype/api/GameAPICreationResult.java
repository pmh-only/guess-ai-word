package codes.pmh.school.spring.guessaiword.game.datatype.api;

import codes.pmh.school.spring.guessaiword.game.entity.GameAIResponse;

public class GameAPICreationResult {
    private int roundCount;

    private int aiResultCount;

    private GameAIResponse aiResponse;

    public int getRoundCount() {
        return roundCount;
    }

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public int getAiResultCount() {
        return aiResultCount;
    }

    public void setAiResultCount(int aiResultCount) {
        this.aiResultCount = aiResultCount;
    }

    public GameAIResponse getAiResponse() {
        return aiResponse;
    }

    public void setAiResponse(GameAIResponse aiResponse) {
        this.aiResponse = aiResponse;
    }
}
