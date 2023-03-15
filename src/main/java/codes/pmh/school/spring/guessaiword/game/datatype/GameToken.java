package codes.pmh.school.spring.guessaiword.game.datatype;

import codes.pmh.school.spring.guessaiword.ai.datatype.AIAskResult;

import java.util.Date;
import java.util.List;

public class GameToken {
    private Date gameCreatedAt = new Date();

    private List<AIAskResult> askResults;

    private int currentWordIndex = 0;

    private int currentQuestionIndex = 0;

    public GameToken () {}

    public GameToken (GameToken gameToken) {
        setAskResults(gameToken.getAskResults());
        setCurrentQuestionIndex(gameToken.getCurrentQuestionIndex());
        setCurrentWordIndex(gameToken.getCurrentWordIndex());
        setGameCreatedAt(gameToken.getGameCreatedAt());
    }

    public Date getGameCreatedAt() {
        return gameCreatedAt;
    }

    public void setGameCreatedAt(Date gameCreatedAt) {
        this.gameCreatedAt = gameCreatedAt;
    }

    public List<AIAskResult> getAskResults() {
        return askResults;
    }

    public void setAskResults(List<AIAskResult> askResults) {
        this.askResults = askResults;
    }

    public int getCurrentWordIndex() {
        return currentWordIndex;
    }

    public void setCurrentWordIndex(int currentWordIndex) {
        this.currentWordIndex = currentWordIndex;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }
}
