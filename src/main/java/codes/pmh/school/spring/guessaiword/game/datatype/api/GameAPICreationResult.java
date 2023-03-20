package codes.pmh.school.spring.guessaiword.game.datatype.api;

import codes.pmh.school.spring.guessaiword.game.entity.GameAIResponse;

public class GameAPICreationResult {
    private int wordCount;

    private int qnaCount;

    private GameAIResponse aiResponse;

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getQnaCount() {
        return qnaCount;
    }

    public void setQnaCount(int qnaCount) {
        this.qnaCount = qnaCount;
    }

    public GameAIResponse getAiResponse() {
        return aiResponse;
    }

    public void setAiResponse(GameAIResponse aiResponse) {
        this.aiResponse = aiResponse;
    }
}
