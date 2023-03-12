package codes.pmh.school.spring.guessaime.game;

import codes.pmh.school.spring.guessaime.ai.AIAskerResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Game {
    private Date createdAt;

    private String answer;

    private List<AIAskerResult> aiResults = new ArrayList<>();

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<AIAskerResult> getAiResults() {
        return aiResults;
    }

    public void setAiResults(List<AIAskerResult> aiResults) {
        this.aiResults = aiResults;
    }

    public void maskAiResults () {
        for (AIAskerResult aiAskerResult : this.aiResults)
            aiAskerResult.mask(this.answer);
    }
}
