package codes.pmh.school.spring.guessaiword.game.model;

public class GameSubmitAnswerResponseBody {
    private boolean isCorrectAnswer;

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }
}
