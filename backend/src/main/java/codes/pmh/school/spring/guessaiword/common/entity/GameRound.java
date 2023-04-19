package codes.pmh.school.spring.guessaiword.common.entity;

import codes.pmh.school.spring.guessaiword.dictionary.enums.DictionaryCategory;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class GameRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String answer;

    private DictionaryCategory answerCategory;

    private boolean isCorrectAnswer = false;

    private boolean isCorrectAnswerShowed = false;

    private boolean isChosungHintShowed = false;

    private Date lastAskedAt;

    private Date lastSubmittedAt;

    @CreationTimestamp
    private Date startedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Game game;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameAskRecord> asks = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public DictionaryCategory getAnswerCategory() {
        return answerCategory;
    }

    public void setAnswerCategory(DictionaryCategory answerCategory) {
        this.answerCategory = answerCategory;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public void setCorrectAnswer(boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
    }

    public boolean isCorrectAnswerShowed() {
        return isCorrectAnswerShowed;
    }

    public void setCorrectAnswerShowed(boolean correctAnswerShowed) {
        isCorrectAnswerShowed = correctAnswerShowed;
    }

    public boolean isChosungHintShowed() {
        return isChosungHintShowed;
    }

    public void setChosungHintShowed(boolean chosungHintShowed) {
        isChosungHintShowed = chosungHintShowed;
    }

    public Date getLastAskedAt() {
        return lastAskedAt;
    }

    public void setLastAskedAt(Date lastAskedAt) {
        this.lastAskedAt = lastAskedAt;
    }

    public Date getLastSubmittedAt() {
        return lastSubmittedAt;
    }

    public void setLastSubmittedAt(Date lastSubmittedAt) {
        this.lastSubmittedAt = lastSubmittedAt;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<GameAskRecord> getAsks() {
        return asks;
    }

    public void setAsks(List<GameAskRecord> asks) {
        this.asks = asks;
    }
}
