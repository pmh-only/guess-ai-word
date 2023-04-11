package codes.pmh.school.spring.guessaiword.game.entity;

import jakarta.persistence.*;

@Entity
public class GameAskCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String secret;

    private String askPrompt;

    @ManyToOne
    private GameRound round;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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
