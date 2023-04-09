package codes.pmh.school.spring.guessaiword.entity;

import jakarta.persistence.*;

@Entity
public class GameAskCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String candidateSecret;

    private String askPrompt;

    @ManyToOne
    private GameRound round;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCandidateSecret() {
        return candidateSecret;
    }

    public void setCandidateSecret(String candidateSecret) {
        this.candidateSecret = candidateSecret;
    }

    public String getAskPrompt() {
        return askPrompt;
    }

    public void setAskPrompt(String askPrompt) {
        this.askPrompt = askPrompt;
    }

    public GameRound getRound() {
        return round;
    }

    public void setRound(GameRound round) {
        this.round = round;
    }
}
