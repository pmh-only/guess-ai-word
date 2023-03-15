package codes.pmh.school.spring.guessaiword.game.datatype.api;

import codes.pmh.school.spring.guessaiword.ai.datatype.AIAskQnAResult;

public class GameAPINextQnaResult {
    private AIAskQnAResult askQnAResult;

    public AIAskQnAResult getAskQnAResult() {
        return askQnAResult;
    }

    public void setAskQnAResult(AIAskQnAResult askQnAResult) {
        this.askQnAResult = askQnAResult;
    }
}
