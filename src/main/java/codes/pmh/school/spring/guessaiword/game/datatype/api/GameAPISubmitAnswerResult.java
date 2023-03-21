package codes.pmh.school.spring.guessaiword.game.datatype.api;

public class GameAPISubmitAnswerResult {
    private boolean isCorrect;

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}