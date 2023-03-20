package codes.pmh.school.spring.guessaiword.game.entity;

import jakarta.persistence.*;

import java.util.*;

@Entity
public class GameRound {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Game game;

    private String answer;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameAIResponse> aiResponses = new ArrayList<>();

    private int usedResponseCount = 0;

    private boolean isPlayerWin = false;

    private Date startedAt;

    private Date endedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<GameAIResponse> getAiResponses() {
        return aiResponses;
    }

    public void setAiResponses(List<GameAIResponse> aiResponses) {
        this.aiResponses = aiResponses;
    }

    public void appendAiResponse(GameAIResponse aiResponse) {
        aiResponse.setRound(this);
        this.aiResponses.add(aiResponse);
    }

    public int getUsedResponseCount() {
        return usedResponseCount;
    }

    public void setUsedResponseCount(int usedResponseCount) {
        this.usedResponseCount = usedResponseCount;
    }

    public boolean isPlayerWin() {
        return isPlayerWin;
    }

    public void setPlayerWin(boolean playerWin) {
        isPlayerWin = playerWin;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Date endedAt) {
        this.endedAt = endedAt;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
