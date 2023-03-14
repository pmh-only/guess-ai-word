package codes.pmh.school.spring.guessaime.game.datatype.api;

import codes.pmh.school.spring.guessaime.ai.datatype.AIAskQnAResult;

public class GameAPINextQnaResult {
    private AIAskQnAResult askQnAResult;

    public AIAskQnAResult getAskQnAResult() {
        return askQnAResult;
    }

    public void setAskQnAResult(AIAskQnAResult askQnAResult) {
        this.askQnAResult = askQnAResult;
    }
}
