package codes.pmh.school.spring.guessaiword.game.model;

import jakarta.validation.constraints.NotNull;

public class GameSubmitAnswerRequestBody {
    @NotNull
    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
